package cipher.impl;

import cipher.Cipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public abstract class SubstitutionCipher implements Cipher {
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

    @Override
    public void encryptString(String plainText, OutputStream out) throws IOException {
        byte[] encryptedBytes = encrypt(plainText.getBytes(StandardCharsets.UTF_8));
        out.write(encryptedBytes);
        out.flush();
        if (out != System.out) {
            out.close();
        }
    }

    @Override
    public void decryptString(String cipherText, OutputStream out) throws IOException {
        byte[] decryptedBytes = decrypt(cipherText.getBytes(StandardCharsets.UTF_8));
        out.write(decryptedBytes);
        out.flush();
        if (out != System.out) {
            out.close();
        }
    }

    @Override
    public void encryptFile(InputStream in, OutputStream out) throws IOException {
        try (in) {
            byte[] encryptedBytes = encrypt(in.readAllBytes());
            out.write(encryptedBytes);
        } finally {
            out.flush();
            if (out != System.out) {
                out.close();
            }
        }
    }

    @Override
    public void decryptFile(InputStream in, OutputStream out) throws IOException {
        try (in) {
            byte[] decryptedBytes = decrypt(in.readAllBytes());
            out.write(decryptedBytes);
        } finally {
            out.flush();
            if (out != System.out) {
                out.close();
            }
        }
    }

    public abstract byte[] encrypt(byte[] plainText);
    public abstract byte[] decrypt(byte[] cipherText);
}
