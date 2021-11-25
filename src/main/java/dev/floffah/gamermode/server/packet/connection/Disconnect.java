package dev.floffah.gamermode.server.packet.connection;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.Strings;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;

/**
 * Outgoing play packet for disconnecting the user
 */
public class Disconnect extends BasePacket {
    /**
     * Reason for the disconnect
     */
    TextComponent chat;

    /**
     * Construct a Disconnect packet with a reason component
     * @param chat The reason
     */
    public Disconnect(TextComponent chat) {
        super("Disconnect", 0x19, PacketType.CLIENTBOUND);
        this.chat = chat;
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        String message = GsonComponentSerializer.gson().serialize(chat);
        Strings.writeUTF(message, out);

        return out;
    }
}
