package cipher.cipherio;

import java.io.InputStream;

public class StdInput implements CipherInput {
    @Override
    public InputStream openStream() { return System.in; }
}
