package dev.floffah.gamermode.server.packet.serverlist;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.Strings;

import java.io.IOException;

public class Response extends BasePacket {

    public Response() {
        super("ServerListResponse", 0x00, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        String motd = conn.main.server.config.info.motd;
        if (conn.protocolVersion >= 0 && conn.protocolVersion < conn.main.server.protocolVersion) {
            motd = String.format("&cClient version out of date (%s@C %s@S)", conn.protocolVersion, conn.main.server.protocolVersion);
        }

        Strings.writeUTF(String.format("""
                {
                    "version": {
                        "name": "GamerMode 1.16.5",
                        "protocol": %s
                    },
                    "players": {
                        "max": %s,
                        "online": 5,
                        "sample": [
                            {
                                "name": "thinkofdeath",
                                "id": "4566e69f-c907-48ee-8d71-d7ba5aa00d20"
                            }
                        ]
                    },
                    "description": {
                        "text": "%s"
                    }
                }""", conn.main.server.protocolVersion, conn.main.server.config.players.max, motd.replaceAll("&", "§")), out);

        return out;
    }
}
