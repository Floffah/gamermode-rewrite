package dev.floffah.gamermode.server.packet.play;

import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;

import java.io.IOException;

public class JoinGame extends BasePacket {
    public JoinGame() {
        super("JoinGame", 0x26, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput output = super.buildOutput();

        output.writeInt(this.conn.getPlayer().getEntityID());
        output.writeByte(this.conn.getSocketManager().getServer().getConfig().info.isHardcore ? 0x01 : 0x00);

        return output;
    }
}
