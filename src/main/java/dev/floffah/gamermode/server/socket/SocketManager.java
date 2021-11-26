package dev.floffah.gamermode.server.socket;

import dev.floffah.gamermode.server.Server;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class SocketManager {

    public ServerSocket sock;
    public List<SocketConnection> connections = new LinkedList<>();

    /**
     * Get the server instance
     * -- GETTER --
     * The server instance
     *
     * @return Server instance
     */
    @Getter
    protected Server server;

    public SocketManager(Server server) {
        this.server = server;
    }

    /**
     * Start accepting connections
     *
     * @throws IOException any exception thrown from initialising a java server socket
     */
    public void start() throws IOException {
        this.sock = new ServerSocket(this.server.getConfig().info.port);
        this.listen();
        this.server.getLogger()
            .info("Listening on " + sock.getLocalSocketAddress().toString());
    }

    /**
     * Listen for connections
     */
    public void listen() {
        Runnable listener = () -> {
            while (true) {
                try {
                    this.server.getLogger().debug("Waiting for connection...");
                    Socket csock = this.sock.accept();
                    this.server.getLogger()
                        .info(
                            "New connection from" +
                                csock.getRemoteSocketAddress().toString()
                        );
                    this.server.getTaskPool()
                        .execute(() -> {
                            try {
                                this.connections.add(
                                    new SocketConnection(this, csock)
                                );
                            } catch (IOException e) {
                                this.server.getLogger().printStackTrace(e);
                            }
                        });
                } catch (IOException e) {
                    this.server.getLogger().printStackTrace(e);
                }
            }
        };

        this.server.getPool().execute(listener);
    }

    /**
     * Util function to dispose of a connection
     *
     * @param conn The connection to dispose of
     * @throws IOException any exception thrown from closing the socket connection
     */
    public void disposeConnection(SocketConnection conn) throws IOException {
        this.connections.remove(conn);
        if (conn.sock != null && !conn.sock.isClosed()) conn.sock.close();
        conn = null;
    }

    /**
     * Stop the socket manager
     *
     * @throws IOException any exception thrown from stopping the socket
     */
    public void stop() throws IOException {
        for (SocketConnection conn : this.connections) {
            this.disposeConnection(conn);
        }
        this.sock.close();
    }
}
