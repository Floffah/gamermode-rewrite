package dev.floffah.gamermode.datatype.util;

import java.util.UUID;

public class UUIDUtil {

    public static int[] uuidToIntArray(UUID uuid) {
        long uuidMost = uuid.getMostSignificantBits();
        int uuidMostA = (int) (uuidMost >> 32);
        int uuidMostB = (int) uuidMost;
        long uuidLeast = uuid.getLeastSignificantBits();
        int uuidLeastA = (int) (uuidLeast >> 32);
        int uuidLeastB = (int) uuidLeast;

        return new int[] { uuidMostA, uuidMostB, uuidLeastA, uuidLeastB };
    }

    public static UUID intArrayToUUID(int[] ints) {
        long most = ((long) ints[0] << 32) | ((long) ints[1] & 0xFFFFFFFFL);
        long least = ((long) ints[2] << 32) | ((long) ints[3] & 0xFFFFFFFFL);
        return new UUID(most, least);
    }
}
