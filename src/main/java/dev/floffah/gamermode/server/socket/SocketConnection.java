package dev.floffah.gamermode.server.socket;

import dev.floffah.gamermode.util.VarInt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.awaitility.core.ConditionTimeoutException;

import java.io.*;
import java.net.Socket;

import static org.awaitility.Awaitility.await;

public class SocketConnection {

    // parent stuff
    public SocketManager main;
    public Socket sock;

    // io
    public DataInputStream dataIn;
    public FlexibleInputStream in;
    public DataOutputStream dataOut;
    public FlexibleOutputStream out;

    // state
    public ConnectionState state;

    public boolean closed = false;

    // timeout prevention
    public long lastKeepaliveSent = 0;
    public long lastKeepaliveReceived = 0;
    public long currentKeepaliveID = 0;
    public long lastPacketReceived = System.currentTimeMillis();

    public SocketConnection(SocketManager main, Socket sock)
            throws IOException {
        this.main = main;
        this.sock = sock;

        this.dataIn = new DataInputStream(sock.getInputStream());
        this.in =
                new FlexibleInputStream(
                        new BufferedInputStream(sock.getInputStream())
                );
        this.dataOut = new DataOutputStream(sock.getOutputStream());
        this.out =
                new FlexibleOutputStream(
                        new BufferedOutputStream(sock.getOutputStream())
                );

        this.state = ConnectionState.HANDSHAKE;

        this.main.server.pool.execute(this::closedChecker);

        main.server.pool.execute(() -> {
            try {
                readPackets();
            } catch (IOException e) {
                main.server.logger.printStackTrace(e);
            }
        });
    }

    public boolean isTimedOut() {
        return this.state == ConnectionState.PLAY
                ? this.lastKeepaliveReceived <= (System.currentTimeMillis() - 30000)
                : lastPacketReceived <= (System.currentTimeMillis() - 10000);
    }

    public void closedChecker() {
        while (!this.closed) {
            try {
                await().until(() -> (this.isTimedOut() && this.in.available() <= 0) || this.sock.isClosed() || this.closed);

                if ((this.isTimedOut() && this.in.available() <= 0)) {
                    this.disconnect(Component.text("Keepalive timeout").color(NamedTextColor.RED));

                    this.closed = true;
                } else {
                    break;
                }
            } catch (IOException e) {
                this.main.server.logger.printStackTrace(e);
            } catch (ConditionTimeoutException ignored) {
            }
        }
    }

    public void disconnect(TextComponent reason) {
        String component = GsonComponentSerializer.gson().serialize(reason);
    }

    public void readPackets() throws IOException {
        while (true) {
            if (this.closed || this.sock.isClosed()) break;

            try {
                await().until(() -> this.in.available() > 0);
            } catch (ConditionTimeoutException e) {
                continue;
            }

            while (this.in.available() > 0) {
                int len = VarInt.readVarInt(this.in);
                int id = VarInt.readVarInt(this.in);

                byte[] data = new byte[len];

                for (int i = 0; i < len - 1; i++) {
                    data[i] = this.dataIn.readByte();
                }

                System.out.println("[Packet] " + id + " (" + len + " bytes)");
            }
        }
    }
}
