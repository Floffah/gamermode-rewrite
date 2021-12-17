package dev.floffah.gamermode.server.packet.play.message;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.events.message.PluginMessageReceivedEvent;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Incoming & outgoing play packet for telling either end some simple information (e.g. brand)
 */
@Packet(
    name = "PluginMessage",
    id = 0x18,
    type = PacketType.CLIENTBOUND,
    state = ConnectionState.PLAY
)
public class PluginMessageClientBound extends BasePacket {

    public ByteArrayDataOutput bytes;
    public byte[] bytesread;
    public Identifier channel;

    /**
     * Construct an outgoing plugin message packet
     *
     * @param channel Message channel
     * @param bytes   Byte array
     */
    public PluginMessageClientBound(Identifier channel, ByteArrayDataOutput bytes) {
        this.channel = channel;
        this.bytes = bytes;
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        StringUtil.writeUTF(channel.toString(), out);
        out.write(bytes.toByteArray());

        return out;
    }


}
