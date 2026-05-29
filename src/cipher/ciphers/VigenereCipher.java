package cipher.ciphers;

import cipher.impl.SubstitutionCipher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class VigenereCipher extends SubstitutionCipher {
    private final String key;

    public VigenereCipher(String key) {
        this.key = key;
    }

    @Override
    public byte[] encrypt(byte[] plainBytes) {
        int numWhitespaceChars = 0;
        String plainText = encode(new String(plainBytes, StandardCharsets.UTF_8));
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
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decrypt(byte[] cipherBytes) {
        int numWhitespaceChars = 0;
        String cipherText = encode(new String(cipherBytes, StandardCharsets.UTF_8));
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
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void save(String fileName) throws IOException {
        String content = "VIGENERE" + System.lineSeparator() + key.toUpperCase();
        writeFile(fileName, content);
    }
}
