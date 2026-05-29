package cipher.ciphers;

import cipher.impl.SubstitutionCipher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CaesarCipher extends SubstitutionCipher {
    private final int shiftParam;

    public CaesarCipher(int shiftParam) {
        this.shiftParam = shiftParam;
    }

    @Override
    public byte[] encrypt(byte[] plainBytes) {
        String plainText = encode(new String(plainBytes, StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            if (Character.isAlphabetic(c)) {
                result.append((char) ((c - 'a' + shiftParam) % 26 + 'a'));
            } else {
                result.append(c);
            }
        }
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decrypt(byte[] cipherBytes) {
        String cipherText = encode(new String(cipherBytes, StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < cipherText.length(); i++) {
            char c = cipherText.charAt(i);
            if (Character.isAlphabetic(c)) {
                result.append((char) ((c - 'a' - shiftParam) % 26 + 'a'));
            } else {
                result.append(c);
            }
        }
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void save(String fileName) throws IOException {
        StringBuilder encryptedAlphabet = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            encryptedAlphabet.append((char) ((i + shiftParam) % 26 + 'A'));
        }

        String content = "MONO" + System.lineSeparator() + encryptedAlphabet;
        writeFile(fileName, content);
    }
}
