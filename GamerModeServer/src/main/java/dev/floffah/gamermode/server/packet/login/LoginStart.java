package dev.floffah.gamermode.server.packet.login;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@Packet(
    name = "LoginStart",
    id = 0x00,
    type = PacketType.SERVERBOUND,
    state = ConnectionState.LOGIN
)
public class LoginStart extends BasePacket {

    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        this.conn.setSessionCode(
                Long.toString(ThreadLocalRandom.current().nextLong()).trim()
            );

        this.conn.getPlayer().getProfile().startLogin(StringUtil.readUTF(in));

        this.conn.send(new EncryptionRequest());
    }
}
