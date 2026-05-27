import java.util.stream.Collectors;

public abstract class AbstractCipher {
    public static String encode(String text) {
        return text.chars()
                .filter(c -> Character.isLetter(c) || Character.isWhitespace(c)) // only keep letters and whitespace
                .mapToObj(c -> String.valueOf((char) c).toLowerCase())
                .collect(Collectors.joining());
    }
}
