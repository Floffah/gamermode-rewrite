package dev.floffah.gamermode.server.socket;

import dev.floffah.gamermode.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class SocketManager {
    public Server server;

    public ServerSocket sock;
    public List<SocketConnection> connections = new LinkedList<>();

    public SocketManager(Server server) {
        this.server = server;
    }

    /**
     * Start accepting connections
     * @throws IOException any exception thrown from initialising a java server socket
     */
    public void start() throws IOException {
        this.sock = new ServerSocket(this.server.config.info.port);
        this.listen();
        this.server.logger.info("Listening on " + sock.getLocalSocketAddress().toString());
    }

    /**
     * Listen for connections
     */
    public void listen() {
        Runnable listener = () -> {
            while (true) {
                try {
                    this.server.logger.debug("Waiting for connection...");
                    Socket csock = this.sock.accept();
                    this.server.logger.info("New connection from" + csock.getRemoteSocketAddress().toString());
                    this.server.taskPool.execute(() -> {
                        try {
                            this.connections.add(new SocketConnection(this, csock));
                        } catch (IOException e) {
                            this.server.logger.printStackTrace(e);
                        }
                    });
                } catch (IOException e) {
                    this.server.logger.printStackTrace(e);
                }
            }
        };

        this.server.pool.execute(listener);
    }

    /**
     * Util function to dispose of a connection
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
     * @throws IOException any exception thrown from stopping the socket
     */
    public void stop() throws IOException {
        for (SocketConnection conn : this.connections) {
            this.disposeConnection(conn);
        }
        this.sock.close();
    }
}
