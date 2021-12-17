package dev.floffah.gamermode.server.packet;

import dev.floffah.gamermode.server.socket.ConnectionState;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Packet {
    String name();
    int id();
    PacketType type();
    ConnectionState state();
}
