public abstract class AbstractCipher2 implements Cipher {
    // ooh this might be the way to do it...
    public void encryptFile() {
        decrypt("hi");
    }
}
// of course this kind of structure would not work on a cipher like RSA, since it needs buffered output and stuf
// we can make one abstract class TextCipher() and one NumericalCipher(), not sure what rsa is considered
// caesar, vingenre, and random will extend textcipher
