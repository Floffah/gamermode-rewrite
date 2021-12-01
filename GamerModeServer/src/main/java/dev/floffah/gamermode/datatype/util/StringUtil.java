package dev.floffah.gamermode.datatype.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StringUtil {

    public static String readUTF(ByteArrayDataInput in) throws IOException {
        int len = VarIntUtil.readVarInt(in);
        byte[] bytes = new byte[len];

        for (int i = 0; i < len; i++) {
            bytes[i] = in.readByte();
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeUTF(String str, ByteArrayDataOutput out)
        throws IOException {
        if (str == null) throw new NullPointerException();
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        VarIntUtil.writeVarInt(out, data.length);
        out.write(data);
    }
}
