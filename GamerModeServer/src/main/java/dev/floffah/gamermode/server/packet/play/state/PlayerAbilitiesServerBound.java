package dev.floffah.gamermode.server.packet.play.state;

import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;

public class PlayerAbilitiesServerBound extends BasePacket {
    public PlayerAbilitiesServerBound() {
        super("PlayerAbilities", 0x19, PacketType.SERVERBOUND);
    }

    // TODO: Implement serverbound player abilities handler
}
