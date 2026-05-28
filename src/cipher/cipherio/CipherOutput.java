package cipher.cipherio;

import java.io.IOException;
import java.io.OutputStream;

public interface CipherOutput {
    OutputStream openStream() throws IOException;
}
