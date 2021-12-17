package dev.floffah.gamermode.server.packet.connection;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;

/**
 * Outgoing login packet for disconnecting the user
 */
@Packet(
    name = "LoginDisconnect",
    id = 0x00,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.LOGIN
)
public class LoginDisconnect extends BasePacket {

    /**
     * Reason for the disconnect
     */
    TextComponent chat;

    /**
     * Construct a LoginDisconnect packet with a reason ui
     *
     * @param chat The reason
     */
    public LoginDisconnect(TextComponent chat) {
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
