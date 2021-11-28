package dev.floffah.gamermode.server.packet.login;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.StringUtil;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class LoginStart extends BasePacket {

    public LoginStart() {
        super("LoginStart", 0x00, PacketType.SERVERBOUND);
    }

    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        this.conn.setSessionCode(
                Long.toString(ThreadLocalRandom.current().nextLong()).trim()
            );

        this.conn.getPlayer().getProfile().startLogin(StringUtil.readUTF(in));

        this.conn.send(new EncryptionRequest());
    }
}
