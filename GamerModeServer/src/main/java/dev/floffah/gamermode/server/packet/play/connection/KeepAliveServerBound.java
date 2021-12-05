package dev.floffah.gamermode.server.packet.play.connection;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import java.io.IOException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class KeepAliveServerBound extends BasePacket {

    public KeepAliveServerBound() {
        super("KeepAlive", 0x0F, PacketType.SERVERBOUND);
    }

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
