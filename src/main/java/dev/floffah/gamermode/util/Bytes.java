package dev.floffah.gamermode.util;

public class Bytes {
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
