public class CaesarCipher extends AbstractCipher implements Cipher {
    private final int shiftParam;

    public CaesarCipher(int shiftParam) {
        this.shiftParam = shiftParam;
    }

    @Override
    public String encrypt(String plainText) {
        plainText = encode(plainText);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            if (Character.isAlphabetic(c)) {
                result.append((char) ((c - 'a' + shiftParam) % 26 + 'a'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    @Override
    public String decrypt(String cipherText) {
        cipherText = encode(cipherText);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < cipherText.length(); i++) {
            char c = cipherText.charAt(i);
            if (Character.isAlphabetic(c)) {
                result.append((char) ((c - 'a' - shiftParam) % 26 + 'a'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
