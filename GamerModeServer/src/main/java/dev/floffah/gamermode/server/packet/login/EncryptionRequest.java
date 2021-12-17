package dev.floffah.gamermode.server.packet.login;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.VarInt;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.datatype.util.VarIntUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import dev.floffah.gamermode.server.socket.ConnectionState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Packet(
    name = "LoginEncrytionRequest",
    id = 0x01,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.LOGIN
)
public class EncryptionRequest extends BasePacket {

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        StringUtil.writeUTF(
            this.conn.getSocketManager().getServer().getServerId(),
            out
        );

        try {
            conn.setKeyPair(
                this.conn.getSocketManager()
                    .getServer()
                    .getKeyPairGenerator()
                    .generateKeyPair()
            );
            byte[] publicKey = conn.getKeyPair().getPublic().getEncoded();
            new VarInt(publicKey.length).writeTo(out);
            out.write(publicKey);

            byte[] verifyToken = new byte[5];
            SecureRandom.getInstanceStrong().nextBytes(verifyToken);
            conn.setVerifyToken(verifyToken);

            new VarInt(verifyToken.length).writeTo(out);
            out.write(verifyToken);
        } catch (NoSuchAlgorithmException e) {
            conn
                .getSocketManager()
                .getServer()
                .getLogger()
                .error("Error occurred while randomizing integer", e);
            conn.disconnect(
                Component.text(e.getMessage()).color(NamedTextColor.RED)
            );
        }

        return out;
    }
}
