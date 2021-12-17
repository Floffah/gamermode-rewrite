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

@Packet(
    name = "PluginMessage",
    id = 0x0A,
    type = PacketType.SERVERBOUND,
    state = ConnectionState.PLAY
)
public class PluginMessageServerBound extends BasePacket {
    public ByteArrayDataOutput bytes;
    public byte[] bytesread;
    public Identifier channel;


    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        this.channel = Identifier.parse(StringUtil.readUTF(in));
        int restlen =
            len -
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
