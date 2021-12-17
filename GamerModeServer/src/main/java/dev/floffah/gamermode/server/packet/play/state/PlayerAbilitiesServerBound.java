package dev.floffah.gamermode.server.packet.play.state;

import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

@Packet(
    name = "PlayerAbilities",
    id = 0x19,
    type = PacketType.SERVERBOUND,
    state = ConnectionState.PLAY
)
public class PlayerAbilitiesServerBound extends BasePacket {

   // TODO: Implement serverbound player abilities handler
}
