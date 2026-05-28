import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

    @Override
    public void save(String fileName) throws IOException {
        StringBuilder encryptedAlphabet = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            encryptedAlphabet.append((char) ((i + shiftParam) % 26 + 'A'));
        }

        String content = "MONO" + System.lineSeparator() + encryptedAlphabet;
        writeFile(fileName, content);
    }

    // testing: we need an encryptText, decryptText, encryptFile, decryptFile.
    // it is very simple to

    public void encryptText(String text, OutputStream out) throws IOException {
        // if (out instanceof FileOutputStream) todo: ooh very cool idea, check first before using (out) or somethign
        try {
            byte[] encryptedBytes = encrypt(text).getBytes(StandardCharsets.UTF_8);
            out.write(encryptedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // i can probabli take out the try catch
        if (out instanceof FileOutputStream) {
            out.close();
        }
    }

    public void decryptText(String text, OutputStream out) {
        try {
            byte[] decryptedBytes = decrypt(text).getBytes(StandardCharsets.UTF_8);
            out.write(decryptedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void encryptFile(InputStream in, OutputStream out) {
        try (in) {
            byte[] encryptedBytes = encrypt(new String(in.readAllBytes(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
            out.write(encryptedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void decryptFile(InputStream in, OutputStream out) {
        try (in) {
            byte[] encryptedBytes = decrypt(new String(in.readAllBytes(), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
            out.write(encryptedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
