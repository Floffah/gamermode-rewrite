package dev.floffah.gamermode.server.packet.connection;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.util.StringUtil;
import java.io.IOException;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

/**
 * Outgoing login packet for disconnecting the user
 */
public class LoginDisconnect extends BasePacket {

    /**
     * Reason for the disconnect
     */
    TextComponent chat;

    /**
     * Construct a LoginDisconnect packet with a reason ui
     * @param chat The reason
     */
    public LoginDisconnect(TextComponent chat) {
        super("LoginDisconnect", 0x00, PacketType.CLIENTBOUND);
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
