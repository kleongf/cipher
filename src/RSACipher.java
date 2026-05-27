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
    private int encryptedChunkSize = 128;

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
            if (e.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE) && e.gcd(q.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
                this.n = this.p.multiply(this.q);
                this.totient = this.p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
                this.d = e.modInverse(totient);
                break;
            }
        }
        // maybe throw some error???? unable to find x... in iterations. or put in while loop idk
        this.n = this.p.multiply(this.q);
        this.totient = this.p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
        this.d = e.modInverse(totient);
        // this.encryptedChunkSize = (n.bitLength() + 7) / 8;
//        for (int i = 0; i < maxIterations; i++) {
//            this.p = generatePrime();
//            this.q = generatePrime();
//            if (calculateGCD(e, p - 1) == 1 && calculateGCD(e, q - 1) == 1) {
//                this.n = this.p * this.q;
//                this.totient = (this.p - 1) * (this.q - 1);
//                this.d = calculateD(totient, e);
//                return;
//            }
//        }
//        this.n = this.p * this.q;
//        this.totient = (this.p - 1) * (this.q - 1);
//        this.d = calculateD(totient, e);
    }

//    private static int calculateGCD(int a, int b) {
//        while (b > 0) {
//            int next_b = a % b;
//            a = b;
//            b = next_b;
//        }
//        return a;
//    }

    // so the error is interesting: it's overflowing and wrapping around


    private static BigInteger generatePrime() {
//        BigInteger test = new BigInteger(bitLength, certainty, new Random());
//        test.g
//        test.modInverse() // bruh i couldve used this i guess bru
        return new BigInteger(bitLength, certainty, new Random());
    }

//    private static int calculateD(int totient, int e) {
//        int[] remainder = {totient, e};
//        int[] kTotient = {1, 0};
//        int[] kE = {0, 1};
//
//        while (remainder[1] > 0) {
//            int quotient = remainder[0] / remainder[1];
//
//            int tempR = remainder[1];
//            remainder[1] = remainder[0] % tempR;
//            remainder[0] = tempR;
//
//            int tempT = kTotient[0];
//            kTotient[0] = kTotient[1];
//            kTotient[1] = tempT - quotient * kTotient[1];
//
//            int tempE = kE[0];
//            kE[0] = kE[1];
//            kE[1] = tempE - quotient * kE[1];
//        }
//
//        if (kE[0] < 0) {
//            kE[0] += totient;
//        }
//        return kE[0];
//    }

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
}
