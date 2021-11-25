package dev.floffah.gamermode.server.packet;

public enum PacketType {
    /**
     * Packet type representing any packet that may be sent to the server.
     */
    SERVERBOUND,
    /**
     * Packet type representing any packet that may be sent to the client.
     */
    CLIENTBOUND,
    /**
     * Edge case packet type representing any packet that is ambiguous.
     */
    UNKNOWN,
}
