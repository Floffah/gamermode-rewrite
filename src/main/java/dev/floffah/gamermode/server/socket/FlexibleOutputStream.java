package dev.floffah.gamermode.server.socket;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FlexibleOutputStream extends OutputStream {
    OutputStream out;
    CipherOutputStream cout;
    boolean encrypted = false;
    Cipher cipher;

    public FlexibleOutputStream(OutputStream out) {
        this.out = out;
    }

    /**
     * Encrypts the stream.
     * @param cipher The cipher to use.
     */
    public void enableEncryption(Cipher cipher) {
        this.cipher = cipher;
        this.cout = new CipherOutputStream(out, cipher);
        this.encrypted = true;
    }

    /**
     * Disables encryption.
     */
    public void disableEncryption() {
        this.encrypted = false;
    }

    @Override
    public void write(int b) throws IOException {
        if (encrypted) {
            cout.write(b);
        } else {
            out.write(b);
        }
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        if (encrypted) {
            cout.write(b);
        } else {
            out.write(b);
        }
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        if (encrypted) {
            cout.write(b, off, len);
        } else {
            out.write(b, off, len);
        }
    }

    @Override
    public void close() throws IOException {
        if (encrypted) cout.close();
        out.close();
    }

    @Override
    public void flush() throws IOException {
        if (encrypted) {
            cout.flush();
        } else {
            out.flush();
        }
    }
}
