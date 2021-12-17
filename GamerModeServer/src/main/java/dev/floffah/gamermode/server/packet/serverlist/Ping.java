package dev.floffah.gamermode.server.packet.serverlist;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;

@Packet(
    name = "ServerListPing",
    id = 0x01,
    type = PacketType.SERVERBOUND,
    state = ConnectionState.STATUS
)
public class Ping extends BasePacket {

    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        long payload = in.readLong();
        conn.send(new Pong(payload));
    }
}
