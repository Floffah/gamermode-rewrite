package dev.floffah.gamermode.server.packet.serverlist;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;

import java.io.IOException;

public class Pong extends BasePacket {
    long payload;

    public Pong(long payload) {
        super("ServerListPong", 0x01, PacketType.CLIENTBOUND);
        this.payload = payload;
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeLong(payload);
        return out;
    }
}