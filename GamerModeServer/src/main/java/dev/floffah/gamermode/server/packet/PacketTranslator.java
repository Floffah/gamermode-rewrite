package dev.floffah.gamermode.server.packet;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.server.socket.ConnectionState;
import dev.floffah.gamermode.server.socket.SocketConnection;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class PacketTranslator {

    public static HashMap<PacketKey, Class<? extends BasePacket>> known = new HashMap<>();
    public static HashMap<Class<? extends BasePacket>, Packet> packetMeta = new HashMap<>();

    public static void storePackets() {
        Reflections reflections = new Reflections("dev.floffah.gamermode.server.packet");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Packet.class);

        for (Class<?> c : classes) {
            if (BasePacket.class.isAssignableFrom(c)) {
                Class<? extends BasePacket> packet = (Class<? extends BasePacket>) c;
                Packet meta = packet.getAnnotation(Packet.class);
                PacketKey key = new PacketKey(meta.id(), meta.state());

                System.out.println( meta.name() + ": " + meta.id() + " - " + meta.state() + " - " + meta.type());

                if(meta.type().equals(PacketType.SERVERBOUND)) known.put(key, packet);
                packetMeta.put(packet, meta);
            }
        }
    }

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
        BasePacket packet = new BasePacket();
        packet.name = "UNKNOWN";
        packet.id = 0x00;
        packet.type = PacketType.CLIENTBOUND;

        PacketKey key = new PacketKey(id, conn.getState());

        if (known.containsKey(key)) {
            packet = known.get(key).getConstructor().newInstance();
        }

        Packet meta = PacketTranslator.packetMeta.get(packet.getClass());
        if (meta != null) {
            packet.type = meta.type();
            packet.id = meta.id();
            packet.state = meta.state();
            packet.name = meta.name();
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PacketKey packetKey = (PacketKey) o;
            return id == packetKey.id && state == packetKey.state;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, state);
        }
    }
}
