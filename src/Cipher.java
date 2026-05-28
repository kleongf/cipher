import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Cipher {
    // things we need:
    // string -> string
    // string -> file
    // file -> string
    // file -> file
    String encrypt(String plainText);
    String decrypt(String cipherText);
//    void encryptFile(String fileName);
//    void decryptFile(String fileName);
    void save(String fileName) throws IOException;
//    Cipher load();

    // good to have byte[] versions and inputstream ones
    // since byte[] -> byte[] can be string -> string
    // and the inStream -> outStream calls bytes


    // AHHH idk anymore, i don't want to have 8 different functions

    // also, for example, rsa still needs to be chunked!

//    byte[] encrypt(byte[] plaintext) throws CryptoException;
//    byte[] decrypt(byte[] ciphertext) throws CryptoException;
//
//    void encrypt(InputStream input, OutputStream output);
//    voic decrypt()
//
//    byte[] encrypt()
//
//    // covers string -> string
//    // String convenience helpers
//    String encryptText(String plaintext) throws CryptoException;
//    String decryptText(String ciphertext) throws CryptoException;
//
//    void encryptStream(InputStream input, OutputStream output) throws CryptoException;
//    void decryptStream(InputStream input, OutputStream output) throws CryptoException;
}
