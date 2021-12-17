package dev.floffah.gamermode.server.packet.play;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.datatype.VarInt;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.events.network.PacketSentEvent;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.packet.play.info.ServerDifficulty;
import dev.floffah.gamermode.server.packet.play.state.PlayerAbilitiesClientBound;
import dev.floffah.gamermode.server.socket.ConnectionState;
import dev.floffah.gamermode.world.World;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;

@Packet(
    name = "JoinGame",
    id = 0x26,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.PLAY
)
public class JoinGame extends BasePacket {

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeInt(this.conn.getPlayer().getEntityId()); // Entity ID
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

    @Override
    public void postSend(PacketSentEvent e) throws IOException {
        ByteArrayDataOutput brandOut = ByteStreams.newDataOutput();
        StringUtil.writeUTF("GamerMode", brandOut);

        this.conn.getPlayer()
            .sendPluginMessage(Identifier.from("minecraft", "brand"), brandOut);

        this.conn.send(new ServerDifficulty());

        this.conn.send(new PlayerAbilitiesClientBound());
    }
}
