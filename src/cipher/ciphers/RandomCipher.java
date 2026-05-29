package cipher.ciphers;

import cipher.impl.SubstitutionCipher;

import java.io.IOException;

public class RandomCipher extends SubstitutionCipher {
    @Override
    public byte[] encrypt(byte[] plainText) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] cipherText) {
        return new byte[0];
    }

    @Override
    public void save(String fileName) throws IOException {

    }
}
