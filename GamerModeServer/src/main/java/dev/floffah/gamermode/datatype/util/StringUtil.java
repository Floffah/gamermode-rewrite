package dev.floffah.gamermode.datatype.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.errorprone.annotations.Var;
import dev.floffah.gamermode.datatype.VarInt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StringUtil {

    public static String readUTF(ByteArrayDataInput in) throws IOException {
        int len = VarInt.from(in).intValue();
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
        new VarInt(data.length).writeTo(out);
        out.write(data);
    }
}
