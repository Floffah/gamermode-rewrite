package dev.floffah.gamermode.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Strings {
    public static String readUTF(ByteArrayDataInput in) throws IOException {
        int len = VarInt.readVarInt(in);
        byte[] bytes = new byte[len];

        for (int i = 0; i < len; i++) {
            bytes[i] = in.readByte();
        }

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeUTF(String str, ByteArrayDataOutput out) throws IOException {
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        VarInt.writeVarInt(out, data.length);
        out.write(data);
    }
}
