package dev.floffah.gamermode.datatype.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import net.querz.nbt.io.NBTOutput;
import net.querz.nbt.io.NBTOutputStream;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.Tag;

public class NBTUtility {

    public static byte[] toByteArray(NamedTag tag, boolean compressed)
        throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NBTSerializer serializer = new NBTSerializer(compressed);
        serializer.toStream(tag, baos);
        baos.flush();
        return baos.toByteArray();
    }

    public static byte[] toByteArray(Tag<?> tag, boolean compressed)
        throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        NBTUtility.toStream(tag, baos, compressed);
        baos.flush();
        return baos.toByteArray();
    }

    public static void toStream(
        Tag<?> object,
        OutputStream out,
        boolean compressed
    ) throws IOException {
        OutputStream output;
        if (compressed) {
            output = new GZIPOutputStream(out, true);
        } else {
            output = out;
        }

        NBTOutput nbtOut = new NBTOutputStream(output);

        nbtOut.writeTag(object, Tag.DEFAULT_MAX_DEPTH);
        nbtOut.flush();
    }
}
