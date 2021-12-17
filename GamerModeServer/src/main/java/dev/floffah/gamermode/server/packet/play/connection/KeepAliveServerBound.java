package dev.floffah.gamermode.server.packet.play.connection;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import java.io.IOException;

import dev.floffah.gamermode.server.socket.ConnectionState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Packet(
    name = "KeepAlive",
    id = 0x0F,
    type = PacketType.SERVERBOUND,
    state = ConnectionState.PLAY
)
public class KeepAliveServerBound extends BasePacket {

    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        long id = in.readLong();

        if (id != this.conn.getCurrentKeepaliveID()) {
            this.conn.disconnect(
                    Component
                        .text("Invalid KeepAlive ID")
                        .color(NamedTextColor.RED)
                );
            return;
        }

        this.conn.setLastKeepaliveReceived(System.currentTimeMillis());
    }
}
