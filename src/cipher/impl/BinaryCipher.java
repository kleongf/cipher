package cipher.impl;

import cipher.Cipher;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.ArrayList;

public abstract class BinaryCipher implements Cipher {
    private int encryptChunkSize; // 126
    private int decryptChunkSize; // 128
    
    public static ArrayList<byte[]> chunkString(String input, int bufferSize) {
        ArrayList<byte[]> chunks = new ArrayList<>();
        if (input == null || input.isEmpty() || bufferSize <= 0) {
            return chunks;
        }

        byte[] allBytes = input.getBytes(StandardCharsets.UTF_8);
        int offset = 0;

        while (offset < allBytes.length) {
            int chunkSize = Math.min(allBytes.length - offset, bufferSize);
            byte[] chunk = Arrays.copyOfRange(allBytes, offset, offset + chunkSize);
            chunks.add(chunk);
            offset += chunkSize;
        }
        return chunks;
    }

    public static void writeFile(String fileName, String content) throws IOException {
        Path filePath = Path.of(fileName);
        Files.writeString(filePath, content);
    }

    @Override
    public void encryptString(String plainText, OutputStream out) throws IOException {
        ArrayList<byte[]> chunkBytes = chunkString(plainText, encryptChunkSize);
        
        for (byte[] chunk: chunkBytes) {
            // add another byte containing the number of encrypted bytes
            byte[] processedBytes = new byte[encryptChunkSize + 1];
            processedBytes[0] = (byte) chunk.length;
            System.arraycopy(chunk, 0, processedBytes, 1, encryptChunkSize);

            // encrypt the processed bytes
            byte[] encryptedBytes = encrypt(processedBytes);
            if (encryptedBytes[0] == 0)
                encryptedBytes = Arrays.copyOfRange(encryptedBytes, 1, encryptedBytes.length);

            // pad processed bytes with zeroes
            byte[] fixedChunk = new byte[decryptChunkSize];
            int paddingAmount = decryptChunkSize - encryptedBytes.length;
            System.arraycopy(encryptedBytes, 0, fixedChunk, paddingAmount, encryptedBytes.length);

            out.write(fixedChunk);
        }
        out.flush();
        if (out != System.out) {
            out.close();
        }
    }

    @Override
    public void decryptString(String cipherText, OutputStream out) throws IOException {
        ArrayList<byte[]> chunkBytes = chunkString(cipherText, decryptChunkSize);

        for (byte[] chunk: chunkBytes) {
            byte[] decryptedBytes = decrypt(chunk);

            // strip leading 0x00 sign byte if present
            if (decryptedBytes[0] == 0)
                decryptedBytes = Arrays.copyOfRange(decryptedBytes, 1, decryptedBytes.length);

            int numBytes = decryptedBytes[0] & 0xFF;
            byte[] actualBytes = Arrays.copyOfRange(decryptedBytes, 1, numBytes + 1);
            out.write(actualBytes);
        }
        out.flush();
        if (out != System.out) {
            out.close();
        }
    }

    @Override
    public void encryptFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[encryptChunkSize];
        BufferedInputStream bufferedIn = new BufferedInputStream(in);
        BufferedOutputStream bufferedOut = new BufferedOutputStream(out);

        try {
            int bytesRead;
            while ((bytesRead = bufferedIn.read(buffer)) != -1) {
                if (bytesRead < encryptChunkSize)
                    Arrays.fill(buffer, bytesRead, encryptChunkSize, (byte) 0);

                // add another byte containing the number of encrypted bytes
                byte[] processedBytes = new byte[encryptChunkSize + 1];
                processedBytes[0] = (byte) bytesRead;
                System.arraycopy(buffer, 0, processedBytes, 1, encryptChunkSize);

                // encrypt the processed bytes
                byte[] encryptedBytes = encrypt(processedBytes);
                if (encryptedBytes[0] == 0)
                    encryptedBytes = Arrays.copyOfRange(encryptedBytes, 1, encryptedBytes.length);

                // pad processed bytes with zeroes
                byte[] fixedChunk = new byte[decryptChunkSize];
                int paddingAmount = decryptChunkSize - encryptedBytes.length;
                System.arraycopy(encryptedBytes, 0, fixedChunk, paddingAmount, encryptedBytes.length);

                out.write(fixedChunk);
            }
        } finally {
            bufferedOut.flush(); // Ensure data is written
            bufferedIn.close();
            if (out != System.out) {
                bufferedOut.close();
            }
        }
    }

    @Override
    public void decryptFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[decryptChunkSize];
        BufferedInputStream bufferedIn = new BufferedInputStream(in);
        BufferedOutputStream bufferedOut = new BufferedOutputStream(out);

        try {
            while (in.read(buffer) != -1) {
                byte[] decryptedBytes = decrypt(buffer);

                // strip leading 0x00 sign byte if present
                if (decryptedBytes[0] == 0)
                    decryptedBytes = Arrays.copyOfRange(decryptedBytes, 1, decryptedBytes.length);

                int numBytes = decryptedBytes[0] & 0xFF;
                byte[] actualBytes = Arrays.copyOfRange(decryptedBytes, 1, numBytes + 1);
                out.write(actualBytes);
            }
        } finally {
            bufferedOut.flush(); // Ensure data is written
            bufferedIn.close();
            if (out != System.out) {
                bufferedOut.close();
            }
        }
    }

    public abstract byte[] encrypt(byte[] plainText);
    public abstract byte[] decrypt(byte[] cipherText);
}

