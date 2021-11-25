package dev.floffah.gamermode.events.network;

import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.server.packet.BasePacket;

public class PacketSentEvent extends PacketEvent {
    public PacketSentEvent(BasePacket packet, ByteArrayDataOutput bytes) {
        super(packet, bytes);
    }
}
