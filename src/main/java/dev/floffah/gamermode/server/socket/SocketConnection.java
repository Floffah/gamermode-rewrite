package dev.floffah.gamermode.server.socket;

import java.io.*;
import java.net.Socket;

public class SocketConnection {
    // parent stuff
    public SocketManager main;
    public Socket sock;

    // io
    public DataInputStream dataIn;
    public FlexibleInputStream in;
    public DataOutputStream dataOut;
    public FlexibleOutputStream out;

    public SocketConnection(SocketManager main, Socket sock) throws IOException {
        this.main = main;
        this.sock = sock;

        this.dataIn = new DataInputStream(sock.getInputStream());
        this.in = new FlexibleInputStream(new BufferedInputStream(sock.getInputStream()));
        this.dataOut = new DataOutputStream(sock.getOutputStream());
        this.out = new FlexibleOutputStream(new BufferedOutputStream(sock.getOutputStream()));
    }
}
