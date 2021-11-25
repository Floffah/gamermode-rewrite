package dev.floffah.gamermode.server.socket;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.events.network.PacketSendingEvent;
import dev.floffah.gamermode.events.network.PacketSentEvent;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketTranslator;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.VarInt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.awaitility.core.ConditionTimeoutException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import static org.awaitility.Awaitility.await;

public class SocketConnection {

    // parent stuff
    public SocketManager main;
    public Socket sock;

    // io
    public DataInputStream in;
    public DataOutputStream out;
    public FlexibleInputStream baseIn;
    public FlexibleOutputStream baseOut;

    // state
    public ConnectionState state;

    public boolean closed = false;

    // timeout prevention
    public long lastKeepaliveSent = 0;
    public long lastKeepaliveReceived = 0;
    public long currentKeepaliveID = 0;
    public long lastPacketReceived = System.currentTimeMillis();

    // encryption
    public boolean encrypted = false;

    // meta
    public int protocolVersion;

    // address data
    public String addressProvided;
    public int portProvided;

    public SocketConnection(SocketManager main, Socket sock)
            throws IOException {
        this.main = main;
        this.sock = sock;

//        this.dataIn = new DataInputStream(sock.getInputStream());
//        this.in =
//                new FlexibleInputStream(
//                        new BufferedInputStream(sock.getInputStream())
//                );
//        this.dataOut = new DataOutputStream(sock.getOutputStream());
//        this.out =
//                new FlexibleOutputStream(
//                        new BufferedOutputStream(sock.getOutputStream())
//                );

        this.baseIn = new FlexibleInputStream(sock.getInputStream());
        this.baseOut = new FlexibleOutputStream(sock.getOutputStream());
        this.in = new DataInputStream(this.baseIn);
        this.out = new DataOutputStream(this.baseOut);

        this.state = ConnectionState.HANDSHAKE;

        this.main.server.pool.execute(this::startClosedChecker);

        main.server.pool.execute(() -> {
            try {
                startPacketReader();
            } catch (IOException e) {
                main.server.logger.printStackTrace(e);
            }
        });
    }

    /**
     * Check if the connection has timed out or the client hasn't sent any data for too long.
     *
     * @return true if the connection has timed out or the client hasn't sent any data for too long.
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
        while (!this.closed && !this.sock.isClosed()) {
            try {
                await().until(() -> this.in == null || (this.isTimedOut() && this.in.available() <= 0) || this.out == null || this.sock == null || this.sock.isClosed() || this.closed);
                if (this.closed) break;

                if ((this.isTimedOut() && this.in.available() <= 0)) {
                    this.disconnect(Component.text("Keepalive timeout").color(NamedTextColor.RED));
                } else {
                    break;
                }
            } catch (IOException e) {
                this.main.server.logger.printStackTrace(e);
            } catch (ConditionTimeoutException ignored) {
            }
        }
    }

    /**
     * Disconnect the client with a text component reason.
     *
     * @param reason the reason for the disconnect.
     */
    public void disconnect(TextComponent reason) {
        String component = GsonComponentSerializer.gson().serialize(reason);
        try {
            this.close();
        } catch (IOException e) {
            this.closed = true;
            this.main.server.logger.printStackTrace(e);

            try {
                this.main.disposeConnection(this);
            } catch (IOException e1) {
                this.main.server.logger.printStackTrace(e1);
            }
        }
    }

    /**
     * Send a byte array to the client.
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
        main.server.events.execute(sendingEvent);
        if (sendingEvent.isCancelled()) return;

        ByteArrayDataOutput finalOutput = ByteStreams.newDataOutput();

        if (dataOutput != null) {
            byte[] loggedSent = dataOutput.toByteArray();
            main.server.logger.debug("SENDING", this.encrypted ? "encrypted" : "not encrypted", String.valueOf(this.state), p.name, Integer.toString(loggedSent.length + 1), Integer.toString(p.id), Arrays.toString(loggedSent));

            VarInt.writeVarInt(finalOutput, dataOutput.toByteArray().length + 1);
            VarInt.writeVarInt(finalOutput, p.id);
            finalOutput.write(dataOutput.toByteArray());

            byte[] sent = finalOutput.toByteArray();

            out.write(sent);
            try {
                out.flush();
            } catch (SocketException e) {
                if (e.getMessage().toLowerCase().contains("closed") || e.getMessage().toLowerCase().contains("aborted"))
                    this.close();
                else this.main.server.logger.printStackTrace(e);
            }

            main.server.logger.debug("SENT", this.encrypted ? "encrypted" : "not encrypted", String.valueOf(this.state), p.name, Integer.toString(sent.length - 1), Integer.toString(p.id), Arrays.toString(sent));

            PacketSentEvent sentEvent = new PacketSentEvent(p, dataOutput);
            p.postSend(sentEvent);
            this.main.server.events.execute(sentEvent);
        }
    }

    /**
     * Close the connection.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        this.main.server.logger.debug("Closing connection");

        if (this.in != null) this.in.close();
        if (this.out != null) this.out.close();
        if (this.sock != null && !this.sock.isClosed()) this.sock.close();

        this.in = null;
        this.out = null;
        this.sock = null;

        this.closed = true;

        this.main.disposeConnection(this);
    }

    /**
     * Threadable method that reads packets from the client.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void startPacketReader() throws IOException {
        while (!this.closed && !this.sock.isClosed()) {
            try {
                await().until(() -> this.in == null || this.in.available() > 0);
            } catch (ConditionTimeoutException e) {
                continue;
            }
            if (this.in == null || this.closed || this.sock.isClosed()) break;

            while (this.in.available() > 0) {
                // note that all bytes read here are automatically decrypted if needed as the underlying input stream used by the DataInputStream
                // is the custom FlexibleInputStream which may have encryption/decryption enabled.
                int len = VarInt.readVarInt(this.in);
                int id = VarInt.readVarInt(this.in);

                byte[] data = new byte[len - 1];

                for (int i = 0; i < len - 1; i++) {
                    if (this.in.available() < 0 || this.closed)
                        break;
                    try {
                        data[i] = this.in.readByte();
                    } catch (EOFException e) {
                        break;
                    }
                }
                if (this.in.available() < 0 || this.closed)
                    break;

                this.lastPacketReceived = System.currentTimeMillis();

                BasePacket packet;
                try {
                    packet = PacketTranslator.identify(id, this);
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                    disconnect(Component.text(e.getMessage()).color(NamedTextColor.RED));
                    return;
                }
                this.main.server.logger.debug("RECEIVED", this.encrypted ? "encrypted" : "not encrypted", packet.name, String.valueOf(state), Integer.toString(len), Integer.toString(id), Arrays.toString(data));

                if (packet.type == PacketType.UNKNOWN) {
                    disconnect(Component.text("Unknown packet received").color(NamedTextColor.RED));
                    break;
                }

                ByteArrayDataInput input = ByteStreams.newDataInput(data);

                try {
                    packet.process(len, input);
                } catch (IOException e) {
                    disconnect(Component.text(e.getMessage()).color(NamedTextColor.RED));
                    this.main.server.logger.printStackTrace(e);
                    break;
                }
            }

            if (this.closed) break;
        }
    }
}
