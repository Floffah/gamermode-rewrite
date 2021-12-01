package dev.floffah.gamermode.server.packet.play;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.StringUtil;
import dev.floffah.gamermode.util.VarIntUtil;
import dev.floffah.gamermode.world.World;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

public class JoinGame extends BasePacket {

    public JoinGame() {
        super("JoinGame", 0x26, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeInt(this.conn.getPlayer().getEntityID()); // Entity ID
        output.writeByte(
            this.conn.getSocketManager()
                    .getServer()
                    .getConfig()
                    .worlds.isHardcore
                ? 1
                : 0
        ); // Is hardcore
        output.writeByte(this.conn.getPlayer().getGameMode()); // Gamemode
        output.writeByte(this.conn.getPlayer().getPreviousGameMode()); // Previous Gamemode

        VarIntUtil.writeVarInt(
            output,
            this.conn.getSocketManager()
                .getServer()
                .getWorldManager()
                .getWorlds()
                .size()
        ); // World Count
        for (World world : this.conn.getSocketManager()
            .getServer()
            .getWorldManager()
            .getWorlds()
            .values()) {
            StringUtil.writeUTF(world.getName(), output);
        } // World Names

        NBTSerializer serializer = new NBTSerializer(false);

        output.write(
            serializer.toBytes(
                new NamedTag(
                    null,
                    this.conn.getPlayer()
                        .getWorld()
                        .getWorldManager()
                        .buildDimensionCodec()
                )
            )
        ); // Dimension Codec
        output.write(
            serializer.toBytes(
                new NamedTag(
                    null,
                    this.conn.getPlayer().getWorld().buildDimType()
                )
            )
        ); // Dimension

        StringUtil.writeUTF(
            this.conn.getPlayer().getWorld().getType().getName(),
            output
        ); // World Name

        output.writeLong(
            ByteBuffer
                .wrap(
                    Hashing
                        .sha256()
                        .hashLong(this.conn.getPlayer().getWorld().getSeed())
                        .asBytes()
                )
                .getLong()
        ); // Hashed seed

        VarIntUtil.writeVarInt(
            output,
            this.conn.getSocketManager().getServer().getConfig().players.max
        ); // Max Players

        VarIntUtil.writeVarInt(
            output,
            this.conn.getSocketManager()
                .getServer()
                .getConfig()
                .worlds.renderDistance
        ); // View Distance

        output.writeByte(
            this.conn.getSocketManager().getServer().isDebugMode() ? 0 : 1
        ); // Reduced Debug Info

        // TODO: implement gamerules
        // even just reading and writing for now
        output.writeByte(1); // Enable respawn screen

        output.writeByte(0); // Is Debug - false because not necessary

        // TODO: implement world types
        // so the server can differentiate between superflat and normal worlds
        output.writeByte(0); // Is Flat

        return output;
    }
}
