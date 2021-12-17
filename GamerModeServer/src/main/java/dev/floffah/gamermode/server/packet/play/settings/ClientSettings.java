package dev.floffah.gamermode.server.packet.play.settings;

import com.google.common.io.ByteArrayDataInput;
import dev.floffah.gamermode.datatype.VarInt;
import dev.floffah.gamermode.datatype.util.StringUtil;
import dev.floffah.gamermode.server.packet.BasePacket;
import dev.floffah.gamermode.server.packet.Packet;
import dev.floffah.gamermode.server.packet.PacketType;
import dev.floffah.gamermode.server.packet.play.state.inventory.HeldItemChange;
import dev.floffah.gamermode.server.socket.ConnectionState;

import java.io.IOException;

@Packet(
    name = "ClientSettings",
    id = 0x05,
    type = PacketType.SERVERBOUND,
    state = ConnectionState.PLAY
)
public class ClientSettings extends BasePacket {

    @Override
    public void process(int len, ByteArrayDataInput in) throws IOException {
        String locale = StringUtil.readUTF(in); // not used yet

        int viewDistance = in.readByte();

        VarInt chatMode = VarInt.from(in);
        boolean chatColors = in.readBoolean();
        int skinParts = in.readByte();
        VarInt mainHand = VarInt.from(in);
        boolean textFiltering = in.readBoolean();
        boolean allowServerListings = in.readBoolean();

        // TODO: make these values mean something
        // these will be applied as they are needed, e.g., skinParts will be applied once player skin broadcasting is implemented

        this.conn.send(new HeldItemChange());
    }
}
