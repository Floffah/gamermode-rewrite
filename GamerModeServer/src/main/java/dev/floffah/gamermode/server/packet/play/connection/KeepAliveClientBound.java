package dev.floffah.gamermode.server.packet.play.connection;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;

import java.io.IOException;

public class KeepAliveClientBound extends BasePacket {
    public KeepAliveClientBound() {
        super("KeepAlive", 0x21, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        this.conn.setCurrentKeepaliveID(this.conn.getKeepaliveIdGenerator().nextLong());
        out.writeLong(this.conn.getCurrentKeepaliveID());

        return out;
    }
}
