package dev.floffah.gamermode.server.socket;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FlexibleInputStream extends InputStream {
    InputStream in;
    CipherInputStream cin;
    boolean decrypting = false;
    Cipher cipher;

    public FlexibleInputStream(InputStream in) {
        this.in = in;
    }

    public void enableDecryption(Cipher cipher) {
        this.cipher = cipher;
        this.cin = new CipherInputStream(in, cipher) {
            @Override
            public int available() throws IOException {
                return in.available();
            }
        };
        this.decrypting = true;
    }

    public void disableDecryption() {
        this.decrypting = false;
    }

    @Override
    public int read() throws IOException {
        if (decrypting) {
            return cin.read();
        } else {
            return in.read();
        }
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        if (decrypting) {
            return cin.read(b);
        } else {
            return in.read(b);
        }
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        if (decrypting) {
            return cin.read(b, off, len);
        } else {
            return in.read(b, off, len);
        }
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        if (decrypting) {
            return cin.readAllBytes();
        } else {
            return in.readAllBytes();
        }
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        if (decrypting) {
            return cin.readNBytes(len);
        } else {
            return in.readNBytes(len);
        }
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        if (decrypting) {
            return cin.read(b, off, len);
        } else {
            return in.read(b, off, len);
        }
    }

    @Override
    public int available() throws IOException {
        if (decrypting) {
            return cin.available();
        } else {
            return in.available();
        }
    }

    @Override
    public boolean markSupported() {
        if (decrypting) {
            return cin.markSupported();
        } else {
            return in.markSupported();
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        if (decrypting) {
            cin.mark(readlimit);
        } else {
            in.mark(readlimit);
        }
    }

    @Override
    public long skip(long n) throws IOException {
        if (decrypting) {
            return cin.skip(n);
        } else {
            return in.skip(n);
        }
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        if (decrypting) {
            return cin.transferTo(out);
        } else {
            return in.transferTo(out);
        }
    }

    @Override
    public void close() throws IOException {
        if (decrypting) {
            cin.close();
        }
        in.close();
    }

    @Override
    public synchronized void reset() throws IOException {
        if (decrypting) {
            cin.reset();
        } else {
            in.reset();
        }
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        if (decrypting) {
            cin.skipNBytes(n);
        } else {
            in.skipNBytes(n);
        }
    }
}
