package cipher.ciphers;

import cipher.impl.BinaryCipher;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;

public class RSACipher extends BinaryCipher {
    private static final BigInteger e = new BigInteger("3"); // or fermat's number, 65537
    private static final int certainty = 20;
    private static final int bitLength = 512;
    private static final int maxIterations = 1000;

    private final BigInteger n;
    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger d;
    private final BigInteger totient;

    public RSACipher() {
        for (int i = 0; i < maxIterations; i++) {
            BigInteger pTest = generatePrime();
            BigInteger qTest = generatePrime();
            if (e.gcd(pTest.subtract(BigInteger.ONE)).equals(BigInteger.ONE) &&
                    e.gcd(qTest.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
                this.p = pTest;
                this.q = qTest;
                this.n = this.p.multiply(this.q);
                this.totient = this.p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
                this.d = e.modInverse(this.totient);
                return;
            }
        }
        throw new IllegalStateException("Maximum of " + maxIterations + " iterations reached");
    }

    private static BigInteger generatePrime() {
        return new BigInteger(bitLength, certainty, new Random());
    }

    @Override
    public byte[] encrypt(byte[] plainText) {
        BigInteger convertedChunk = new BigInteger(1, plainText);
        return convertedChunk.modPow(new BigInteger(String.valueOf(e)), new BigInteger(String.valueOf(n))).toByteArray();
    }

    @Override
    public byte[] decrypt(byte[] cipherText) {
        BigInteger convertedChunk = new BigInteger(1, cipherText);
        return convertedChunk.modPow(new BigInteger(String.valueOf(d)), new BigInteger(String.valueOf(n))).toByteArray();
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
