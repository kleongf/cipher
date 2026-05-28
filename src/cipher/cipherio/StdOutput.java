package cipher.cipherio;

import java.io.OutputStream;

public class StdOutput implements CipherOutput {
    @Override
    public OutputStream openStream() { return System.out; }
}
