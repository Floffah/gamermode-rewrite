package dev.floffah.gamermode.server.packet.play.info;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;

@Packet(
    name = "ServerDifficulty",
    id = 0x0E,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.PLAY
)
public class ServerDifficulty extends BasePacket {

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
