package dev.floffah.gamermode.server.packet.play.state;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;

@Packet(
    name = "PlayerAbilities",
    id = 0x32,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.PLAY
)
public class PlayerAbilitiesClientBound extends BasePacket {

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeByte(this.conn.getPlayer().getAbilities().toBitField());
        out.writeFloat(this.conn.getPlayer().getMovement().getFlightSpeed());
        out.writeFloat(
            this.conn.getPlayer().getMovement().getFieldOfViewModifier()
        );

        return out;
    }
}
