package dev.floffah.gamermode.server.packet.serverlist;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import java.io.IOException;
import java.util.UUID;

import dev.floffah.gamermode.server.socket.ConnectionState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

@Packet(
    name = "ServerListResponse",
    id = 0x00,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.STATUS
)
public class Response extends BasePacket {

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        String motd = conn.getSocketManager().getServer().getConfig().info.motd;
        if (
            conn.getProtocolVersion() >= 0 &&
            conn.getProtocolVersion() <
            conn.getSocketManager().getServer().getProtocolVersion()
        ) {
            motd =
                String.format(
                    "&cClient version out of date (%s@C %s@S)",
                    conn.getProtocolVersion(),
                    conn.getSocketManager().getServer().getProtocolVersion()
                );
        }

        JSONObject json = new JSONObject();

        JSONObject version = new JSONObject()
            .put(
                "name",
                conn.getSocketManager().getServer().getServerBrand() + " 1.17.1"
            )
            .put(
                "protocol",
                conn.getSocketManager().getServer().getProtocolVersion()
            );
        json.put("version", version);

        JSONObject players = new JSONObject()
            .put(
                "max",
                conn.getSocketManager().getServer().getConfig().players.max
            )
            .put("online", 0);
        json.put("players", players);

        JSONArray sample = new JSONArray();
        sample.put(
            new JSONObject()
                .put(
                    "name",
                    LegacyComponentSerializer
                        .legacySection()
                        .serialize(
                            Component
                                .text(
                                    "GamerMode is still in beta. Please report any bugs you find!"
                                )
                                .color(NamedTextColor.RED)
                        )
                )
                .put("id", new UUID(0, 0).toString())
        );
        players.put("sample", sample);

        JSONObject description = new JSONObject(
            GsonComponentSerializer
                .gson()
                .serialize(
                    LegacyComponentSerializer
                        .legacyAmpersand()
                        .deserialize(
                            this.conn.getSocketManager()
                                .getServer()
                                .getConfig()
                                .info.motd
                        )
                )
        );
        json.put("description", description);

        StringUtil.writeUTF(json.toString(), out);

        return out;
    }
}
