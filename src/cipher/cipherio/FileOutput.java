package cipher.cipherio;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOutput implements CipherOutput {
    private final Path path;
    public FileOutput(Path path) { this.path = path; }

    @Override
    public OutputStream openStream() throws IOException {
        // idk if this is correct
        // return new BufferedOutputStream(new FileOutputStream(path.toFile()));
        return Files.newOutputStream(path);
    }
}
