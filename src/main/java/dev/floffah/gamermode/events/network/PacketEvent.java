package dev.floffah.gamermode.events.network;


import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.events.Event;
import dev.floffah.gamermode.server.packet.BasePacket;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * An event class that is only to be extended upon by other packet events
 */
public class PacketEvent extends Event {
    /**
     * The packet associated with the event
     * -- GETTER --
     * Get the packet associated with the event
     * @return The packet associated with the event
     * -- SETTER --
     * Set the packet associated with the event
     * @param packet The packet to set
     */
    @Getter
    @Setter
    BasePacket packet;
    ByteArrayDataOutput unencrypted;

    /**
     * Construct a new packet event
     * @param packet Packet associated with the event
     * @param bytes Packet bytes
     */
    public PacketEvent(BasePacket packet, @NonNull ByteArrayDataOutput bytes) {
        this.packet = packet;
        this.unencrypted = bytes;
    }

    /**
     * Get the byte array being sent or that was sent. This byte array is the unencrypted version.
     *
     * @return unencrypted byte array
     */
    public ByteArrayDataOutput getBytes() {
        return unencrypted;
    }
}
