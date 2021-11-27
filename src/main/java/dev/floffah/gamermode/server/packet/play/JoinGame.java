package dev.floffah.gamermode.server.packet.play;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.StringUtil;
import dev.floffah.gamermode.util.VarIntUtil;
import dev.floffah.gamermode.world.World;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

public class JoinGame extends BasePacket {

    public JoinGame() {
        super("JoinGame", 0x26, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput output = super.buildOutput();

        output.writeInt(this.conn.getPlayer().getEntityID());
        output.writeByte(
            this.conn.getSocketManager()
                    .getServer()
                    .getConfig()
                    .worlds.isHardcore
                ? 0x01
                : 0x00
        );
        output.writeByte(this.conn.getPlayer().getGameMode());
        output.writeByte(this.conn.getPlayer().getPreviousGameMode());

        VarIntUtil.writeVarInt(
            output,
            this.conn.getSocketManager()
                .getServer()
                .getWorldManager()
                .getWorlds()
                .size()
        );
        for (World world : this.conn.getSocketManager()
            .getServer()
            .getWorldManager()
            .getWorlds()) {
            StringUtil.writeUTF(world.getName(), output);
        }

        NBTSerializer serializer = new NBTSerializer();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        serializer.toStream(new NamedTag(null, new CompoundTag()), stream);
        byte[] tempDims = stream.toByteArray();

        output.write(tempDims);
        output.write(tempDims);

        StringUtil.writeUTF(
            this.conn.getPlayer().getWorld().getType().getName(),
            output
        );

        output.writeLong(
            ByteBuffer
                .wrap(
                    Hashing
                        .sha256()
                        .hashLong(this.conn.getPlayer().getWorld().getSeed())
                        .asBytes()
                )
                .getLong()
        );

        return output;
    }
}
