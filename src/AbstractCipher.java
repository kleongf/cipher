import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public abstract class AbstractCipher {
    public static String encode(String text) {
        return text.chars()
                .filter(c -> Character.isLetter(c) || Character.isWhitespace(c)) // only keep letters and whitespace
                .mapToObj(c -> String.valueOf((char) c).toLowerCase())
                .collect(Collectors.joining());
    }

    public static void writeFile(String fileName, String content) throws IOException {
        Path filePath = Path.of(fileName);
        Files.writeString(filePath, content);
    }
}
