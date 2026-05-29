package cipher;

import cipher.ciphers.CaesarCipher;
import cipher.ciphers.RSACipher;

public class CipherFactory {
    public Cipher createCipher(String type) {
        if (type == null || type.isEmpty()) return null;
        return switch (type) {
            case "caesar" -> new CaesarCipher(0); // todo: constructor with no shift so u can set it later?
            case "rsa" -> new RSACipher();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}
