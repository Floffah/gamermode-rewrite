package dev.floffah.gamermode.server.packet.play.state;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;

import java.io.IOException;

public class PlayerAbilitiesClientBound extends BasePacket {
    public PlayerAbilitiesClientBound() {
        super("PlayerAbilities", 0x32, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeByte(this.conn.getPlayer().getAbilities().toBitField());
        out.writeFloat(this.conn.getPlayer().getMovement().getFlightSpeed());
        out.writeFloat(this.conn.getPlayer().getMovement().getFieldOfViewModifier());

        return out;
    }
}
