package dev.floffah.gamermode.server.packet;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.events.network.PacketSentEvent;
import dev.floffah.gamermode.server.socket.SocketConnection;
import java.io.IOException;

public class BasePacket {

    public String name;
    public int id;
    public SocketConnection conn;
    public PacketType type;

    public BasePacket(String name, int id, PacketType type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    /**
     * Process the packet
     * @param len Packet length
     * @param in Byte array
     * @throws IOException Any exception thrown
     */
    public void process(int len, ByteArrayDataInput in) throws IOException {}

    /**
     * Packet's built outgoing byte array
     * @return
     * @throws IOException
     */
    public ByteArrayDataOutput buildOutput() throws IOException {
        return null;
    }

    /**
     * Executed after the packet is sent
     * @param e The sent event
     * @throws IOException Any exception thrown
     */
    public void postSend(PacketSentEvent e) throws IOException {}
}
