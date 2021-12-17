package dev.floffah.gamermode.server.packet.play.state.inventory;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;

@Packet(
    name = "HeldItemChange",
    state = ConnectionState.PLAY,
    type = PacketType.CLIENTBOUND,
    id = 0x47
)
public class HeldItemChange extends BasePacket {
    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeByte(this.conn.getPlayer().getInventory().getSelectedSlot());

        return out;
    }
}
