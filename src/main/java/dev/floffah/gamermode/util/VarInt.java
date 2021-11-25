package dev.floffah.gamermode.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import dev.floffah.gamermode.server.socket.FlexibleInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VarInt {
    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
    public static int readVarInt(DataInputStream in) throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = (byte) in.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
    public static int readVarInt(ByteArrayDataInput in) throws IOException {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = in.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    // this is no longer needed as the base input stream automatically encrypts and decrypts its data
    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
//    public static int readEncryptedVarInt(DataInputStream in, Cipher decrypter) throws IOException {
//        int numRead = 0;
//        int result = 0;
//        byte read;
//        do {
//            try {
//                read = decrypter.doFinal(Bytes.byteToArray(in.readByte()))[0];
//            } catch (IllegalBlockSizeException | BadPaddingException e) {
//                e.printStackTrace();
//                return -1;
//            }
//            int value = (read & 0b01111111);
//            result |= (value << (7 * numRead));
//
//            numRead++;
//            if (numRead > 5) {
//                throw new RuntimeException("VarInt is too big");
//            }
//        } while ((read & 0b10000000) != 0);
//
//        return result;
//    }

    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
    public static void writeVarInt(DataOutputStream out, int value) throws IOException {
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            out.writeByte(temp);
        } while (value != 0);
    }

    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
    public static void writeVarInt(ByteArrayDataOutput out, int value) throws IOException {
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            out.writeByte(temp);
        } while (value != 0);
    }

    // this is no longer needed as the base input stream automatically encrypts and decrypts its data
//    public static void writeEncryptedVarInt(DataOutputStream out, int value, Cipher encrypter) throws IOException {
//        do {
//            byte temp = (byte) (value & 0b01111111);
//            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
//            value >>>= 7;
//            if (value != 0) {
//                temp |= 0b10000000;
//            }
//            byte enc = 0;
//            try {
//                enc = encrypter.doFinal(Bytes.byteToArray(temp))[0];
//            } catch (IllegalBlockSizeException | BadPaddingException e) {
//                e.printStackTrace();
//            }
//            out.writeByte(enc);
//        } while (value != 0);
//    }
}
