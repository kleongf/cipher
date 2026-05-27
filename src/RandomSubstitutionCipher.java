import java.util.HashMap;
import java.util.Random;
import java.util.random.*;

public class RandomSubstitutionCipher extends AbstractCipher implements Cipher {
    private final HashMap<Character, Character> encryptMap;
    private final HashMap<Character, Character> decryptMap;

    public static char[] shuffle(char[] input) { // todo: should be private
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

    public RandomSubstitutionCipher() {
        this.encryptMap = new HashMap<>();
        this.decryptMap = new HashMap<>();
        buildMaps();
    }

    @Override
    public String encrypt(String plainText) {
        plainText = encode(plainText);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            if (Character.isAlphabetic(c)) {
                result.append(encryptMap.get(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    @Override
    public String decrypt(String cipherText) {
        IO.println(decryptMap);
        cipherText = encode(cipherText);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < cipherText.length(); i++) {
            char c = cipherText.charAt(i);
            if (Character.isAlphabetic(c)) {
                result.append(decryptMap.get(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
