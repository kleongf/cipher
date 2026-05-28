import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RSACipher extends AbstractCipher implements Cipher {
    private static final BigInteger e = new BigInteger("3"); // or fermat's number, 65537
    private static final int certainty = 20;
    private static final int bitLength = 512;
    private static final int chunkSize = 126;
    private static final int maxIterations = 1000;
    private static final int encryptedChunkSize = 128;

    public BigInteger n;
    public BigInteger p;
    public BigInteger q;
    public BigInteger d;
    public BigInteger totient;

    // ohhhh... so fried
    public RSACipher() {
        for (int i = 0; i < maxIterations; i++) {
            this.p = generatePrime();
            this.q = generatePrime();
            if (e.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE) &&
                    e.gcd(q.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
                this.n = this.p.multiply(this.q);
                this.totient = this.p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
                this.d = e.modInverse(totient);
                return;
            }
        }
        throw new IllegalStateException("Maximum of " + maxIterations + " iterations reached");
    }


    private static BigInteger generatePrime() {
        return new BigInteger(bitLength, certainty, new Random());
    }

    public BigInteger encrypt(byte[] plainChunk) {
        BigInteger convertedChunk = new BigInteger(1, plainChunk);
        return convertedChunk.modPow(new BigInteger(String.valueOf(e)), new BigInteger(String.valueOf(n)));
    }

    public BigInteger decrypt(byte[] cipherChunk) {
        BigInteger convertedChunk = new BigInteger(1, cipherChunk);
        return convertedChunk.modPow(new BigInteger(String.valueOf(d)), new BigInteger(String.valueOf(n)));
    }

    // new approach:
    // extract 126 bytes
    // get length of bytesRead
        // note that the first bit of this is the sign bit and it is unsigned and 0 since its less than 128
    // store in the first byte
    // encrypt the whole thing
    // pad the encrypted text with zeroes in the front to not change its value

    public void encryptFile(String inFile, String outFile) {
        byte[] buffer = new byte[chunkSize];
        try (InputStream in = new BufferedInputStream(new FileInputStream(inFile));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile))) {

            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                if (bytesRead < chunkSize)
                    Arrays.fill(buffer, bytesRead, chunkSize, (byte) 0);

                // add another byte containing the number of encrypted bytes
                byte[] processedBytes = new byte[chunkSize + 1];
                processedBytes[0] = (byte) bytesRead;
                System.arraycopy(buffer, 0, processedBytes, 1, chunkSize);

                // encrypt the processed bytes
                byte[] encryptedBytes = encrypt(processedBytes).toByteArray();
                if (encryptedBytes[0] == 0)
                    encryptedBytes = Arrays.copyOfRange(encryptedBytes, 1, encryptedBytes.length);

                // pad processed bytes with zeroes
                byte[] fixedChunk = new byte[encryptedChunkSize];
                int paddingAmount = encryptedChunkSize - encryptedBytes.length;
                System.arraycopy(encryptedBytes, 0, fixedChunk, paddingAmount, encryptedBytes.length);

                out.write(fixedChunk);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // the only exception is the RSA cipher. if the cipher type is rsa, then the inputsteams and outputstreams are buffered.
    // not sure what the best way to do this is...

    public void decryptFile(InputStream in, OutputStream out) throws IOException {
        // ooh this looks like a smart way to do it, actually no its not since it has out either way
        // todo: the actual smartest way is probably at the end, check if it's a buffered output stream, and if so, call close()
        // wait but here i created a new one? so it might be okay to do it like this?
        try (InputStream bufferedIn = new BufferedInputStream(in);
             OutputStream bufferedOut = new BufferedOutputStream(out)) {
            // wait but
        }

    }
    public void decryptFile(String inFile, String outFile) {
        byte[] buffer = new byte[encryptedChunkSize];
        try (InputStream in = new BufferedInputStream(new FileInputStream(inFile));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile))) {

            while (in.read(buffer) != -1) {
                byte[] decryptedBytes = decrypt(buffer).toByteArray();

                // Strip leading 0x00 sign byte if present
                if (decryptedBytes[0] == 0)
                    decryptedBytes = Arrays.copyOfRange(decryptedBytes, 1, decryptedBytes.length);

                int numBytes = decryptedBytes[0] & 0xFF;
                byte[] actualBytes = Arrays.copyOfRange(decryptedBytes, 1, numBytes + 1);
                out.write(actualBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encrypt(String plainText) {
        plainText = encode(plainText);
        // ooh but it might be better to just use binary???
        return "";
    }

    @Override
    public String decrypt(String cipherText) {
        return "";
    }

    @Override
    public void save(String fileName) throws IOException {
        String content =
                "RSA" +
                System.lineSeparator() +
                        d + System.lineSeparator() +
                        e + System.lineSeparator() +
                        n;
        writeFile(fileName, content);
    }
}
