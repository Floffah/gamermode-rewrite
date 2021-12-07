package dev.floffah.gamermode.server.packet.play.message;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.floffah.gamermode.datatype.Identifier;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.events.message.PluginMessageReceivedEvent;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.PacketType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Incoming & outgoing play packet for telling either end some simple information (e.g. brand)
 */
public class PluginMessage extends BasePacket {

    public ByteArrayDataOutput bytes;
    public byte[] bytesread;
    public Identifier channel;

    /**
     * Construct an outgoing plugin message packet
     *
     * @param channel Message channel
     * @param bytes   Byte array
     */
    public PluginMessage(Identifier channel, ByteArrayDataOutput bytes) {
        super("PluginMessageClientBound", 0x18, PacketType.CLIENTBOUND);
        this.channel = channel;
        this.bytes = bytes;
    }

    /**
     * Construct an incoming plugin message packet
     */
    public PluginMessage() {
        super("PluginMessageServerBound", 0x0A, PacketType.SERVERBOUND);
    }

    @Override
    public ByteArrayDataOutput buildOutput() throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        StringUtil.writeUTF(channel.toString(), out);
        out.write(bytes.toByteArray());

        return out;
    }

    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        this.channel = Identifier.parse(StringUtil.readUTF(in));
        int restlen =
            len -
            1 -
            this.channel.toString().getBytes(StandardCharsets.UTF_8).length;
        byte[] bread = new byte[restlen];
        for (int i = 0; i < restlen; i++) {
            try {
                bread[i] = in.readByte();
            } catch (Exception e) {
                conn
                    .getSocketManager()
                    .getServer()
                    .getLogger()
                    .error("Error occurred while reading plugin message", e);
                break;
            }
        }
        this.bytesread = bread;
        if (this.channel.equals("minecraft:brand")) {
            ByteArrayDataInput dat = ByteStreams.newDataInput(bread);
            String brand = StringUtil.readUTF(dat);
            conn.getPlayer().setBrand(brand);
            System.out.println(channel + " " + brand);
        }
        conn
            .getSocketManager()
            .getServer()
            .getEvents()
            .execute(new PluginMessageReceivedEvent(this));
    }
}
