package dev.floffah.gamermode.server.packet.connection;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import java.io.IOException;

import dev.floffah.gamermode.server.socket.ConnectionState;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

/**
 * Outgoing play packet for disconnecting the user
 */
@Packet(
    name = "Disconnect",
    id = 0x1A,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.PLAY
)
public class Disconnect extends BasePacket {

    /**
     * Reason for the disconnect
     */
    TextComponent chat;

    /**
     * Construct a Disconnect packet with a reason ui
     * @param chat The reason
     */
    public Disconnect(TextComponent chat) {
        this.chat = chat;
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        String message = GsonComponentSerializer.gson().serialize(chat);
        StringUtil.writeUTF(message, out);

        return out;
    }
}
