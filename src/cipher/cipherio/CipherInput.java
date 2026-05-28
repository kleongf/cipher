package cipher.cipherio;

import java.io.IOException;
import java.io.InputStream;

public interface CipherInput {
    InputStream openStream() throws IOException;
}
