import java.io.IOException;

public class VigenereCipher extends AbstractCipher implements Cipher {
    private final String key;

    public VigenereCipher(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(String plainText) {
        int numWhitespaceChars = 0;
        plainText = encode(plainText);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            if (Character.isAlphabetic(c)) {
                int keyNum = key.charAt((i - numWhitespaceChars) % (key.length())) + 1;
                result.append((char) ((c + keyNum - 2 * 'a') % 26 + 'a'));
            } else {
                result.append(c);
                numWhitespaceChars++;
            }
        }
        return result.toString();
    }

    @Override
    public String decrypt(String cipherText) {
        int numWhitespaceChars = 0;
        cipherText = encode(cipherText);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < cipherText.length(); i++) {
            char c = cipherText.charAt(i);
            if (Character.isAlphabetic(c)) {
                int keyNum = key.charAt((i - numWhitespaceChars) % (key.length())) + 1;
                result.append((char) ((c - keyNum + 26) % 26 + 'a'));
            } else {
                result.append(c);
                numWhitespaceChars++;
            }
        }
        return result.toString();
    }

    @Override
    public void save(String fileName) throws IOException {
        String content = "VIGENERE" + System.lineSeparator() + key.toUpperCase();
        writeFile(fileName, content);
    }
}
