package dev.floffah.gamermode.server.packet.serverlist;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.datatype.VarInt;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.datatype.util.VarIntUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;
import java.io.IOException;

@Packet(
    name = "ServerListHandshake",
    id = 0x00,
    type = PacketType.SERVERBOUND,
    state = ConnectionState.HANDSHAKE
)
public class Handshake extends BasePacket {

    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        int protocol = VarInt.from(in).intValue();
        String addr = StringUtil.readUTF(in);
        int port = in.readUnsignedShort();
        int next = VarInt.from(in).intValue();

        conn.setProtocolVersion(protocol);
        conn.setAddressProvided(addr);
        conn.setPortProvided(port);
        if (next == 1) {
            conn.setState(ConnectionState.STATUS);
        } else {
            conn.setState(ConnectionState.LOGIN);
        }
    }
}
