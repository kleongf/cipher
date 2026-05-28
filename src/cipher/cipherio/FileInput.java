package cipher.cipherio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Files;

public class FileInput implements CipherInput {
    private final Path path;
    public FileInput(Path path) { this.path = path; }

    @Override
    public InputStream openStream() throws IOException {
        return Files.newInputStream(path);
    }
}
