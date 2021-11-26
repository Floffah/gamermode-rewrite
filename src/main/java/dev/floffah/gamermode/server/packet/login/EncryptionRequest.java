package dev.floffah.gamermode.server.packet.login;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.Strings;
import dev.floffah.gamermode.util.VarInt;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class EncryptionRequest extends BasePacket {

    public EncryptionRequest() {
        super("LoginEncryptionRequest", 0x01, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        Strings.writeUTF(
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
            VarInt.writeVarInt(out, publicKey.length);
            out.write(publicKey);

            byte[] verifyToken = new byte[5];
            SecureRandom.getInstanceStrong().nextBytes(verifyToken);
            conn.setVerifyToken(verifyToken);

            VarInt.writeVarInt(out, verifyToken.length);
            out.write(verifyToken);
        } catch (NoSuchAlgorithmException e) {
            conn.getSocketManager().getServer().getLogger().printStackTrace(e);
            conn.disconnect(
                Component.text(e.getMessage()).color(NamedTextColor.RED)
            );
        }

        return out;
    }
}
