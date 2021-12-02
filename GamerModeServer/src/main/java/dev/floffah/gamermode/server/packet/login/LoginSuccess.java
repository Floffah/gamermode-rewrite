package dev.floffah.gamermode.server.packet.login;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;
import java.io.IOException;

/**
 * Outgoing login packet for notifying the client that the login was a success
 */
public class LoginSuccess extends BasePacket {

    /**
     * Construct a LoginSuccess packet
     */
    public LoginSuccess() {
        super("LoginSuccess", 0x02, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeLong(conn.getPlayer().getUniqueId().getMostSignificantBits());
        out.writeLong(conn.getPlayer().getUniqueId().getLeastSignificantBits());

        StringUtil.writeUTF(conn.getPlayer().getUsername(), out);

        this.conn.setLastKeepaliveReceived(System.currentTimeMillis());
        conn.setState(ConnectionState.PLAY);

        return out;
    }
}
