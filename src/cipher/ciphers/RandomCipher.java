package cipher.ciphers;

import cipher.impl.SubstitutionCipher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

public class RandomCipher extends SubstitutionCipher {
    private final HashMap<Character, Character> encryptMap;
    private final HashMap<Character, Character> decryptMap;

    protected static char[] shuffle(char[] input) {
        if (input.length == 0 || input.length == 1) {
            return input;
        }

        Random randomGenerator = new Random();
        char[] output = input.clone();

        for (int i = input.length - 1; i >= 0; i--) {
            int j = randomGenerator.nextInt(0, i+1);
            char temp = output[i];
            output[i] = output[j];
            output[j] = temp;
        }

        return output;
    }

    private void buildMaps() {
        char[] original = new char[26];
        char[] shuffled;

        for (int i = 0; i < 26; i++) {
            original[i] = (char) ('a' + i);
        }
        shuffled = shuffle(original);

        for (int i = 0; i < 26; i++) {
            encryptMap.put(original[i], shuffled[i]);
            decryptMap.put(shuffled[i], original[i]);
        }
    }

    public RandomCipher() {
        this.encryptMap = new HashMap<>();
        this.decryptMap = new HashMap<>();
        buildMaps();
    }

    @Override
    public byte[] encrypt(byte[] plainBytes) {
        String plainText = encode(new String(plainBytes, StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            if (Character.isAlphabetic(c)) {
                result.append(encryptMap.get(c));
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
                result.append(decryptMap.get(c));
            } else {
                result.append(c);
            }
        }
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void save(String fileName) throws IOException {
        StringBuilder encryptedAlphabet = new StringBuilder();
        for (Character c: encryptMap.keySet()) {
            encryptedAlphabet.append(decryptMap.get(c));
        }
        String uppercaseEncryptedAlphabet = encryptedAlphabet.toString().toUpperCase();
        String content = "RANDOM" + System.lineSeparator() + uppercaseEncryptedAlphabet;
        writeFile(fileName, content);
    }
}
