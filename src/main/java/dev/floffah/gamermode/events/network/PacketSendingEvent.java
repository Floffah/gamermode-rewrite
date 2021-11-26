package dev.floffah.gamermode.events.network;

import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.events.types.ICancellable;
import dev.floffah.gamermode.server.packet.BasePacket;
import lombok.Getter;
import lombok.Setter;

/**
 * An event fired right before a packet is sent
 */
public class PacketSendingEvent extends PacketEvent implements ICancellable {

    @Getter
    @Setter
    boolean cancelled = false;

    /**
     * Event fired right before a packet is sent
     *
     * @param packet the packet
     */
    public PacketSendingEvent(BasePacket packet, ByteArrayDataOutput bytes) {
        super(packet, bytes);
    }
}
