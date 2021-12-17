package dev.floffah.gamermode.server.packet.serverlist;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.events.network.PacketSentEvent;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;

@Packet(
    name = "ServerListPong",
    id = 0x01,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.STATUS
)
public class Pong extends BasePacket {

    long payload;

    public Pong(long payload) {
        this.payload = payload;
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeLong(payload);
        return out;
    }

    @Override
    public void postSend(PacketSentEvent e) throws IOException {
        this.conn.close();
    }
}
