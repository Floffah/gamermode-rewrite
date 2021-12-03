package dev.floffah.gamermode.server.packet.play;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.VarInt;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.world.World;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;

public class JoinGame extends BasePacket {

    public JoinGame() {
        super("JoinGame", 0x26, PacketType.CLIENTBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeInt(this.conn.getPlayer().getEntityID()); // Entity ID
        output.writeBoolean(
            this.conn.getSocketManager()
                .getServer()
                .getConfig()
                .worlds.isHardcore
        ); // Is hardcore
        output.writeByte(this.conn.getPlayer().getGameMode()); // Gamemode
        output.writeByte(this.conn.getPlayer().getPreviousGameMode()); // Previous Gamemode

        new VarInt(
            this.conn.getSocketManager()
                .getServer()
                .getWorldManager()
                .getWorlds()
                .size()
        )
            .writeTo(output); // World Count
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
        );
        output.write(
            serializer.toBytes(
                new NamedTag(
                    null,
                    this.conn.getPlayer().getWorld().buildDimType()
                )
            )
        );

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

        new VarInt(
            this.conn.getSocketManager().getServer().getConfig().players.max
        )
            .writeTo(output); // Max Players

        new VarInt(
            this.conn.getSocketManager()
                .getServer()
                .getConfig()
                .worlds.renderDistance
        )
            .writeTo(output); // View Distance

        new VarInt(
            this.conn.getSocketManager()
                .getServer()
                .getConfig()
                .worlds.simulationDistance
        )
            .writeTo(output); // Simulation Distance

        output.writeBoolean(
            this.conn.getSocketManager().getServer().isDebugMode()
        ); // Reduced Debug Info

        // TODO: implement gamerules
        // even just reading and writing for now
        output.writeBoolean(true); // Enable respawn screen

        output.writeBoolean(false); // Is Debug - false because not necessary

        // TODO: implement world types
        // so the server can differentiate between superflat and normal worlds
        output.writeBoolean(false); // Is Flat

        return output;
    }
}
