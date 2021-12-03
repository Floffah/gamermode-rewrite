package dev.floffah.gamermode.datatype;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.Native;

public class VarInt
    extends Number
    implements /*Constable, ConstantDesc,*/Comparable<VarInt> {

    @Native
    public static final VarInt MIN_VALUE = new VarInt(Integer.MIN_VALUE);

    @Native
    public static final VarInt MAX_VALUE = new VarInt(Integer.MAX_VALUE);

    private final Integer value;

    public VarInt(int value) {
        this.value = value;
    }

    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
    public static VarInt from(DataInputStream in) throws IOException {
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

        return new VarInt(result);
    }

    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
    public static VarInt from(ByteArrayDataInput in) throws IOException {
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

        return new VarInt(result);
    }

    public static VarInt sum(VarInt a, VarInt b) {
        return new VarInt(a.value + b.value);
    }

    public static VarInt sum(VarInt a, int b) {
        return new VarInt(a.value + b);
    }

    public static VarInt sum(int a, VarInt b) {
        return new VarInt(a + b.value);
    }

    @Override
    public int compareTo(@NotNull VarInt o) {
        return Integer.compare(o.value, value);
    }

    @Override
    public int intValue() {
        return this.value;
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
    public void writeTo(DataOutputStream out) throws IOException {
        int tempValue = Integer.parseInt(this.value.toString()); // clone
        do {
            byte temp = (byte) (tempValue & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            tempValue >>>= 7;
            if (tempValue != 0) {
                temp |= 0b10000000;
            }
            out.writeByte(temp);
        } while (tempValue != 0);
    }

    // modified from https://wiki.vg/Protocol#VarInt_and_VarLong
    public void writeTo(ByteArrayDataOutput out) throws IOException {
        int tempValue = Integer.parseInt(this.value.toString()); // clone
        do {
            byte temp = (byte) (tempValue & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            tempValue >>>= 7;
            if (tempValue != 0) {
                temp |= 0b10000000;
            }
            out.writeByte(temp);
        } while (tempValue != 0);
    }
    //    @Override
    //    public Optional<? extends ConstantDesc> describeConstable() {
    //        return Optional.of(this);
    //    }
    //
    //    @Override
    //    public Object resolveConstantDesc(MethodHandles.Lookup lookup)
    //        throws ReflectiveOperationException {
    //        return this;
    //    }
}
