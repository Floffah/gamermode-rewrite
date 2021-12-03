package dev.floffah.gamermode.datatype.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.datatype.VarInt;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VarIntUtil {

    /**
     * @deprecated Use {@link VarInt#from(ByteArrayDataInput)} instead
     */
    public static int readVarInt(DataInputStream in) throws IOException {
        return VarInt.from(in).intValue();
    }

    /**
     * @deprecated Use {@link VarInt#from(ByteArrayDataInput)} instead
     */
    public static int readVarInt(ByteArrayDataInput in) throws IOException {
        return VarInt.from(in).intValue();
    }

    /**
     * @deprecated Use {@link VarInt#writeTo(DataOutputStream)} instead
     */
    public static void writeVarInt(DataOutputStream out, int value)
        throws IOException {
        new VarInt(value).writeTo(out);
    }

    /**
     * @deprecated Use {@link VarInt#writeTo(ByteArrayDataOutput)} instead
     */
    public static void writeVarInt(ByteArrayDataOutput out, int value)
        throws IOException {
        new VarInt(value).writeTo(out);
    }
}
