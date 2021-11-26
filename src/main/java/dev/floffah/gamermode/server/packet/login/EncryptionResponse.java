package dev.floffah.gamermode.server.packet.login;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.error.UUIDMismatchException;
import dev.floffah.gamermode.events.network.PacketSentEvent;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.VarInt;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Incoming login packet for processing the client's encryption response
 */
public class EncryptionResponse extends BasePacket {

    /**
     * Construct an EncryptionResponse
     */
    public EncryptionResponse() {
        super("LoginEncryptionResponse", 0x01, PacketType.SERVERBOUND);
    }

    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        // temporary cipher
        Cipher tempCipher;

        // read the shared secret
        int sharedSecretLength = VarInt.readVarInt(in);
        byte[] sharedSecret = new byte[sharedSecretLength];
        in.readFully(sharedSecret, 0, sharedSecretLength);

        // read the verify token
        int verifyTokenLength = VarInt.readVarInt(in);
        byte[] verifyToken = new byte[verifyTokenLength];
        in.readFully(verifyToken, 0, verifyTokenLength);

        // initialise the temporary cipher
        try {
            tempCipher = Cipher.getInstance("RSA");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            this.conn.getSocketManager()
                .getServer()
                .getLogger()
                .printStackTrace(e);
            this.conn.disconnect(
                    Component.text(e.getMessage()).color(NamedTextColor.RED)
                );
            return;
        }

        // decrypt the shared secret
        try {
            tempCipher.init(
                Cipher.DECRYPT_MODE,
                this.conn.getKeyPair().getPrivate()
            );
            this.conn.setSharedSecret(
                    new SecretKeySpec(tempCipher.doFinal(sharedSecret), "AES")
                );
        } catch (
            IllegalBlockSizeException
            | BadPaddingException
            | InvalidKeyException e
        ) {
            this.conn.getSocketManager()
                .getServer()
                .getLogger()
                .printStackTrace(e);
            this.conn.disconnect(
                    Component.text(e.getMessage()).color(NamedTextColor.RED)
                );
            return;
        }

        // decrypt the verify token
        byte[] clientVerify;
        try {
            tempCipher.init(
                Cipher.DECRYPT_MODE,
                this.conn.getKeyPair().getPrivate()
            );
            clientVerify = tempCipher.doFinal(verifyToken);
        } catch (
            IllegalBlockSizeException
            | BadPaddingException
            | InvalidKeyException e
        ) {
            this.conn.getSocketManager()
                .getServer()
                .getLogger()
                .printStackTrace(e);
            this.conn.disconnect(
                    Component.text(e.getMessage()).color(NamedTextColor.RED)
                );
            return;
        }

        // make sure its the same as the server sent
        if (!Arrays.equals(this.conn.getVerifyToken(), clientVerify)) {
            this.conn.disconnect(
                    Component
                        .text("Invalid verify token")
                        .color(NamedTextColor.RED)
                );
            return;
        }

        // initialise the cipher used for the rest of communication
        try {
            Cipher encryptCypher = Cipher.getInstance("AES/CFB8/NoPadding");
            encryptCypher.init(
                Cipher.ENCRYPT_MODE,
                this.conn.getSharedSecret(),
                new IvParameterSpec(this.conn.getSharedSecret().getEncoded())
            );
            this.conn.setEncryptCipher(encryptCypher);
            Cipher decryptCypher = Cipher.getInstance("AES/CFB8/NoPadding");
            decryptCypher.init(
                Cipher.DECRYPT_MODE,
                this.conn.getSharedSecret(),
                new IvParameterSpec(this.conn.getSharedSecret().getEncoded())
            );
            this.conn.setDecryptCipher(decryptCypher);
        } catch (
            InvalidAlgorithmParameterException
            | NoSuchPaddingException
            | InvalidKeyException
            | NoSuchAlgorithmException e
        ) {
            this.conn.getSocketManager()
                .getServer()
                .getLogger()
                .printStackTrace(e);
            this.conn.disconnect(
                    Component.text(e.getMessage()).color(NamedTextColor.RED)
                );
            return;
        }

        // hash the server id, shared secret, and public key
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(
                this.conn.getSocketManager()
                    .getServer()
                    .getServerId()
                    .getBytes(StandardCharsets.US_ASCII)
            );
            digest.update(this.conn.getSharedSecret().getEncoded());
            digest.update(this.conn.getKeyPair().getPublic().getEncoded());

            BigInteger hashIntForm = new BigInteger(digest.digest());
            String hash = hashIntForm.toString(16);
            if (!hash.startsWith("-") && hashIntForm.signum() == -1) hash =
                "-" + hash;
            this.conn.setSessionHash(hash);
        } catch (NoSuchAlgorithmException e) {
            this.conn.getSocketManager()
                .getServer()
                .getLogger()
                .printStackTrace(e);
            this.conn.disconnect(
                    Component.text(e.getMessage()).color(NamedTextColor.RED)
                );
            return;
        }

        // enable encryption
        this.conn.setEncrypted(true);
        this.conn.getBaseIn().enableDecryption(this.conn.getDecryptCipher());
        this.conn.getBaseOut().enableEncryption(this.conn.getEncryptCipher());

        try {
            this.conn.getPlayer().getProfile().doHasJoined();
        } catch (UUIDMismatchException e) {
            this.conn.getSocketManager()
                .getServer()
                .getLogger()
                .printStackTrace(e);
            this.conn.disconnect(
                Component.text(e.getMessage()).color(NamedTextColor.RED)
            );
            return;
        }

        // sent new packets
        this.conn.send(new LoginSuccess());
    }
}
