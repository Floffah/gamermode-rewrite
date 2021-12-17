package dev.floffah.gamermode.server.packet.play.connection;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;

@Packet(
    name = "KeepAlive",
    id = 0x21,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.PLAY
)
public class KeepAliveClientBound extends BasePacket {

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        this.conn.setCurrentKeepaliveID(
                this.conn.getKeepaliveIdGenerator().nextLong()
            );
        out.writeLong(this.conn.getCurrentKeepaliveID());

        return out;
    }
}
