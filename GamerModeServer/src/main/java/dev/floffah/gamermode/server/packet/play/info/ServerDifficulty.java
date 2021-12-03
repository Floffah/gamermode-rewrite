package dev.floffah.gamermode.server.packet.play.info;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import java.io.IOException;

public class ServerDifficulty extends BasePacket {

    public ServerDifficulty() {
        super("ServerDifficulty", 0x0E, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.write(
            Byte.toUnsignedInt(
                this.conn.getSocketManager()
                    .getServer()
                    .getConfig()
                    .worlds.difficulty
            )
        );
        out.writeBoolean(true);

        return out;
    }
}
