package dev.floffah.gamermode.server.packet;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.server.socket.ConnectionState;
import dev.floffah.gamermode.server.socket.SocketConnection;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class PacketTranslator {

    public static HashMap<PacketKey, Class<? extends BasePacket>> known = new HashMap<>();

    public static void execute(
        int len,
        int id,
        ByteArrayDataInput in,
        SocketConnection conn
    )
        throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BasePacket p = identify(id, conn);
        p.process(len, in);
    }

    public static BasePacket identify(int id, SocketConnection conn)
        throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BasePacket packet = new BasePacket("UNKNOWN", 0x00, PacketType.UNKNOWN);
        PacketKey key = new PacketKey(id, conn.getState());

        if (known.containsKey(key)) {
            packet = known.get(key).getConstructor().newInstance();
        } else {
            for (AllServerBoundPackets serverBoundPacket : AllServerBoundPackets.values()) {
                if (
                    conn.getState() == serverBoundPacket.state &&
                    serverBoundPacket.id == id
                ) {
                    known.put(key, serverBoundPacket.packet);
                    packet =
                        serverBoundPacket.packet.getConstructor().newInstance();
                }
            }
        }

        packet.conn = conn;
        return packet;
    }

    public static class PacketKey {

        public int id;
        public ConnectionState state;

        public PacketKey(int id, ConnectionState state) {
            this.id = id;
            this.state = state;
        }
    }
}
