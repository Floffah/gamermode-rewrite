package dev.floffah.gamermode.datatype.util;

public class ByteUtil {

    public static byte[] byteToArray(byte b) {
        byte[] ba = new byte[1];
        ba[0] = b;
        return ba;
    }

    public static byte bool(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }
}
