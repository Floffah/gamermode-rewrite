package dev.floffah.gamermode.server.socket;

import static org.awaitility.Awaitility.await;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.events.network.PacketSendingEvent;
import dev.floffah.gamermode.events.network.PacketSentEvent;
import dev.floffah.gamermode.player.Player;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketTranslator;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.packet.connection.Disconnect;
import dev.floffah.gamermode.server.packet.connection.LoginDisconnect;
import dev.floffah.gamermode.util.VarInt;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyPair;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.awaitility.core.ConditionTimeoutException;

public class SocketConnection {

    /**
     * The current state of the connection.
     * <p>
     * -- GETTER --
     * Get the connection state
     *
     * @return The current state of the connection
     */
    @Getter
    protected ConnectionState state;

    /**
     * The socket of the connection
     * -- GETTER --
     * Gets the socket of the connection
     *
     * @return The socket of the connection
     */
    @Getter
    protected SocketManager socketManager;

    /**
     * The connection's socket
     * -- GETTER --
     * Gets the connection's socket
     *
     * @return The connection's socket
     */
    @Getter
    protected Socket sock;

    /**
     * The input stream of the connection
     * -- GETTER --
     * Gets the input stream of the connection
     *
     * @return The input stream of the connection
     */
    @Getter
    protected DataInputStream in;

    /**
     * The output stream of the connection
     * -- GETTER --
     * Gets the output stream of the connection
     *
     * @return The output stream of the connection
     */
    @Getter
    protected DataOutputStream out;

    /**
     * The flexible data input stream of the connection
     * -- GETTER --
     * Gets the flexible data input stream of the connection
     *
     * @return The flexible data input stream of the connection
     */
    @Getter
    protected FlexibleInputStream baseIn;

    /**
     * The flexible data output stream of the connection
     * -- GETTER --
     * Gets the flexible data output stream of the connection
     *
     * @return The flexible data output stream of the connection
     */
    @Getter
    protected FlexibleOutputStream baseOut;

    /**
     * Whether or not the connection is closed
     * -- GETTER --
     * Gets whether or not the connection is closed
     *
     * @return Whether or not the connection is closed
     */
    @Getter
    protected boolean closed = false;

    /**
     * The last time in milliseconds that a keep alive packet was sent.
     * -- GETTER --
     * Gets the last time in milliseconds that a keep alive packet was sent.
     *
     * @return The last time in milliseconds that a keep alive packet was sent.
     */
    @Getter
    protected long lastKeepaliveSent = 0;

    /**
     * The last time in milliseconds that a keep alive packet was received.
     * -- GETTER --
     * Gets the last time in milliseconds that a keep alive packet was received.
     *
     * @return The last time in milliseconds that a keep alive packet was received.
     */
    @Getter
    protected long lastKeepaliveReceived = 0;

    /**
     * The current keep alive packet ID
     * -- GETTER --
     * Gets the current keep alive packet ID.
     *
     * @return The current keep alive packet ID.
     */
    @Getter
    protected long currentKeepaliveID = 0;

    /**
     * The last time in milliseconds that a packet was received.
     * -- GETTER --
     * Gets the last time in milliseconds that a packet was received.
     *
     * @return The last time in milliseconds that a packet was received.
     */
    @Getter
    @Setter
    protected long lastPacketReceived = System.currentTimeMillis();

    /**
     * The client's protocol version
     * -- GETTER --
     * Gets the client's protocol version.
     *
     * @return The client's protocol version.
     * -- SETTER --
     * Sets the client's protocol version.
     * @param protocolVersion The client's protocol version.
     */
    @Getter
    @Setter
    protected int protocolVersion;

    /**
     * The address provided by the client
     * -- GETTER --
     * Gets the address provided by the client.
     *
     * @return The address provided by the client.
     * -- SETTER --
     * Sets the address provided by the client.
     * @param address The address provided by the client.
     */
    @Getter
    @Setter
    protected String addressProvided;

    /**
     * The port provided by the client
     * -- GETTER --
     * Gets the port provided by the client.
     *
     * @return The port provided by the client.
     * -- SETTER --
     * Sets the port provided by the client.
     * @param port The port provided by the client.
     */
    @Getter
    @Setter
    protected int portProvided;

    /**
     * Whether or not the connection is encrypted
     * -- GETTER --
     * Gets whether or not the connection is encrypted.
     *
     * @return Whether or not the connection is encrypted.
     */
    @Getter
    @Setter
    protected boolean encrypted = false;

    /**
     * The connection's session code
     * -- GETTER --
     * Gets the connection's session code.
     *
     * @return The connection's session code.
     * -- SETTER --
     * Sets the connection's session code.
     * @param sessionCode The connection's session code.
     */
    @Getter
    @Setter
    protected String sessionCode;

    /**
     * The connection and server's shared secret
     * -- GETTER --
     * Gets the connection and server's shared secret.
     *
     * @return The connection and server's shared secret.
     * -- SETTER --
     * Sets the connection and server's shared secret.
     * @param sharedSecret The connection and server's shared secret.
     */
    @Getter
    @Setter
    protected SecretKey sharedSecret;

    /**
     * The connection's encryption key pair
     * -- GETTER --
     * Gets the connection's encryption key pair.
     *
     * @return The connection's encryption key pair.
     * -- SETTER --
     * Sets the connection's encryption key pair.
     * @param encryptionKeyPair The connection's encryption key pair.
     */
    @Getter
    @Setter
    protected KeyPair keyPair;

    /**
     * The connection's encryption cipher
     * -- GETTER --
     * Gets the connection's encryption cipher.
     *
     * @return The connection's encryption cipher.
     * -- SETTER --
     * Sets the connection's encryption cipher.
     * @param cipher The connection's encryption cipher.
     */
    @Getter
    @Setter
    protected Cipher encryptCipher;

    /**
     * The connection's decryption cipher
     * -- GETTER --
     * Gets the connection's decryption cipher.
     *
     * @return The connection's decryption cipher.
     * -- SETTER --
     * Sets the connection's decryption cipher.
     * @param cipher The connection's decryption cipher.
     */
    @Getter
    @Setter
    protected Cipher decryptCipher;

    /**
     * The connection's verification token
     * -- GETTER --
     * Gets the connection's verification token.
     *
     * @return The connection's verification token.
     * -- SETTER --
     * Sets the connection's verification token.
     * @param verificationToken The connection's verification token.
     */
    @Getter
    @Setter
    protected byte[] verifyToken;

    /**
     * The connection's session hash
     * -- GETTER --
     * Gets the connection's session hash.
     *
     * @return The connection's session hash.
     * -- SETTER --
     * Sets the connection's session hash.
     * @param sessionHash The connection's session hash.
     */
    @Getter
    @Setter
    protected String sessionHash;

    /**
     * The player
     * <p>
     * -- GETTER --
     * Get the player
     *
     * @return The player
     */
    @Getter
    protected Player player;

    public SocketConnection(SocketManager main, Socket sock)
        throws IOException {
        this.socketManager = main;
        this.sock = sock;

        this.baseIn = new FlexibleInputStream(sock.getInputStream());
        this.baseOut = new FlexibleOutputStream(sock.getOutputStream());
        this.in = new DataInputStream(this.baseIn);
        this.out = new DataOutputStream(this.baseOut);

        this.state = ConnectionState.HANDSHAKE;

        this.socketManager.server.getPool().execute(this::startClosedChecker);

        main.server
            .getPool()
            .execute(() -> {
                try {
                    startPacketReader();
                } catch (IOException e) {
                    main.server.getLogger().printStackTrace(e);
                }
            });
    }

    /**
     * Check if the connection has timed out or the player hasn't sent any data for too long.
     *
     * @return true if the connection has timed out or the player hasn't sent any data for too long.
     */
    public boolean isTimedOut() {
        return this.state == ConnectionState.PLAY
            ? this.lastKeepaliveReceived <= (System.currentTimeMillis() - 30000)
            : lastPacketReceived <= (System.currentTimeMillis() - 10000);
    }

    /**
     * Threadable method that checks if the connection is closed.
     */
    public void startClosedChecker() {
        while (!this.closed) {
            try {
                await()
                    .until(() ->
                        this.in == null ||
                        (this.isTimedOut() && this.in.available() <= 0) ||
                        this.out == null ||
                        this.sock == null ||
                        this.sock.isClosed() ||
                        this.closed
                    );
                if (this.closed) Thread.currentThread().interrupt();
                if ((this.isTimedOut() && this.in.available() <= 0)) {
                    this.disconnect(
                            Component
                                .text("Keepalive timeout")
                                .color(NamedTextColor.RED)
                        );
                } else {
                    break;
                }
            } catch (IOException e) {
                this.socketManager.server.getLogger().printStackTrace(e);
            } catch (ConditionTimeoutException ignored) {}
        }
        Thread.currentThread().interrupt();
    }

    /**
     * Disconnect the player with a text component reason.
     *
     * @param reason the reason for the disconnect.
     */
    public void disconnect(TextComponent reason) {
        String component = GsonComponentSerializer.gson().serialize(reason);
        try {
            if (state == ConnectionState.PLAY) {
                send(new Disconnect(reason));
            } else if (state == ConnectionState.LOGIN) {
                send(new LoginDisconnect(reason));
            }
            this.close();
        } catch (IOException e) {
            this.closed = true;
            this.socketManager.server.getLogger().printStackTrace(e);

            try {
                this.socketManager.disposeConnection(this);
            } catch (IOException e1) {
                this.socketManager.server.getLogger().printStackTrace(e1);
            }
        }
    }

    /**
     * Send a byte array to the player.
     *
     * @param out the byte array to send.
     * @throws IOException if an I/O error occurs.
     */
    public void send(ByteArrayDataOutput out) throws IOException {
        byte[] data = out.toByteArray();
        for (byte d : data) {
            this.out.writeByte(d);
        }
        this.out.flush();
    }

    public void send(BasePacket p) throws IOException {
        p.conn = this;

        ByteArrayDataOutput dataOutput = p.buildOutput();

        PacketSendingEvent sendingEvent = new PacketSendingEvent(p, dataOutput);
        socketManager.server.getEvents().execute(sendingEvent);
        if (sendingEvent.isCancelled()) return;

        ByteArrayDataOutput finalOutput = ByteStreams.newDataOutput();

        if (dataOutput != null) {
            byte[] loggedSent = dataOutput.toByteArray();
            socketManager.server
                .getLogger()
                .debug(
                    "SENDING",
                    this.encrypted ? "encrypted" : "not encrypted",
                    String.valueOf(this.state),
                    p.name,
                    Integer.toString(loggedSent.length + 1),
                    Integer.toString(p.id),
                    Arrays.toString(loggedSent)
                );

            VarInt.writeVarInt(
                finalOutput,
                dataOutput.toByteArray().length + 1
            );
            VarInt.writeVarInt(finalOutput, p.id);
            finalOutput.write(dataOutput.toByteArray());

            byte[] sent = finalOutput.toByteArray();

            out.write(sent);
            try {
                out.flush();
            } catch (SocketException e) {
                if (
                    e.getMessage().toLowerCase().contains("closed") ||
                    e.getMessage().toLowerCase().contains("aborted")
                ) this.close(); else this.socketManager.server.getLogger()
                    .printStackTrace(e);
            }

            socketManager.server
                .getLogger()
                .debug(
                    "SENT",
                    this.encrypted ? "encrypted" : "not encrypted",
                    String.valueOf(this.state),
                    p.name,
                    Integer.toString(sent.length - 1),
                    Integer.toString(p.id),
                    Arrays.toString(sent)
                );

            PacketSentEvent sentEvent = new PacketSentEvent(p, dataOutput);
            p.postSend(sentEvent);
            this.socketManager.server.getEvents().execute(sentEvent);
        }
    }

    /**
     * Close the connection.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        this.socketManager.server.getLogger().debug("Closing connection");

        if (this.in != null) this.in.close();
        if (this.out != null) this.out.close();
        if (this.sock != null && !this.sock.isClosed()) this.sock.close();

        this.in = null;
        this.out = null;
        this.sock = null;

        this.closed = true;

        this.socketManager.disposeConnection(this);
    }

    /**
     * Threadable method that reads packets from the player.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void startPacketReader() throws IOException {
        while (!this.closed) {
            try {
                await().until(() -> this.in == null || this.in.available() > 0);
            } catch (ConditionTimeoutException e) {
                continue;
            }
            if (this.in == null || this.closed || this.sock.isClosed()) Thread
                .currentThread()
                .interrupt();
            while (this.in != null && this.in.available() > 0) {
                // note that all bytes read here are automatically decrypted if needed as the underlying input stream used by the DataInputStream
                // is the custom FlexibleInputStream which may have encryption/decryption enabled.
                int len = VarInt.readVarInt(this.in);
                int id = VarInt.readVarInt(this.in);

                byte[] data = new byte[len - 1];

                for (int i = 0; i < len - 1; i++) {
                    if (this.closed) Thread.currentThread().interrupt();
                    try {
                        data[i] = this.in.readByte();
                    } catch (EOFException e) {
                        break;
                    }
                }
                if (this.closed) Thread.currentThread().interrupt();
                this.lastPacketReceived = System.currentTimeMillis();

                BasePacket packet;

                try {
                    packet = PacketTranslator.identify(id, this);
                } catch (
                    InvocationTargetException
                    | NoSuchMethodException
                    | IllegalAccessException
                    | InstantiationException e
                ) {
                    disconnect(
                        Component.text(e.getMessage()).color(NamedTextColor.RED)
                    );
                    return;
                }
                this.socketManager.server.getLogger()
                    .debug(
                        "RECEIVED",
                        this.encrypted ? "encrypted" : "not encrypted",
                        packet.name,
                        String.valueOf(state),
                        Integer.toString(len),
                        Integer.toString(id),
                        Arrays.toString(data)
                    );
                if (packet.type == PacketType.UNKNOWN) {
                    disconnect(
                        Component
                            .text("Unknown packet received")
                            .color(NamedTextColor.RED)
                    );
                    break;
                }

                ByteArrayDataInput input = ByteStreams.newDataInput(data);

                try {
                    packet.process(len, input);
                } catch (IOException e) {
                    disconnect(
                        Component.text(e.getMessage()).color(NamedTextColor.RED)
                    );
                    this.socketManager.server.getLogger().printStackTrace(e);
                    break;
                }
            }

            if (this.closed) Thread.currentThread().interrupt();
        }
        Thread.currentThread().interrupt();
    }

    /**
     * Set the state of the connection.
     *
     * @param state the new state.
     */
    public void setState(ConnectionState state) {
        if (
            this.state == ConnectionState.HANDSHAKE &&
            state == ConnectionState.LOGIN
        ) {
            this.player = new Player(this);
        }

        this.state = state;
    }
}
