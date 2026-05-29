package cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Cipher {
    void encryptString(String plainText, OutputStream out) throws IOException;
    void decryptString(String plainText, OutputStream out) throws IOException;

    void encryptFile(InputStream in, OutputStream out) throws IOException;
    void decryptFile(InputStream in, OutputStream out) throws IOException;

    void save(String fileName) throws IOException;
    // Cipher load(String fileName) throws IOException;
}
