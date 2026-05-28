void main() throws IOException {
    String test = "Hi's";
    IO.println(test.toLowerCase());
    IO.println(AbstractCipher.encode(test));

    char[] test2 = {'a', 'b', 'c', 'd'};
    IO.println(Arrays.toString(RandomSubstitutionCipher.shuffle(test2)));
    IO.println(Arrays.toString(RandomSubstitutionCipher.shuffle(test2)));
    IO.println(Arrays.toString(RandomSubstitutionCipher.shuffle(test2)));
    IO.println(Arrays.toString(RandomSubstitutionCipher.shuffle(test2)));

    RandomSubstitutionCipher cipher = new RandomSubstitutionCipher();
    String test3 = "hello world";
    IO.println(test3);
    String enc = cipher.encrypt(test3);
    IO.println(cipher.encrypt(test3));
    IO.println(cipher.decrypt(enc));

    cipher.save("random.txt");

    VigenereCipher cipher2 = new VigenereCipher("abc");
    String enc2 = cipher2.encrypt("tomorrow and tomorrow and tomorrow creeps in this petty pace from day to day");
    IO.println(enc2);
    IO.println(cipher2.decrypt(enc2));

    cipher2.save("vigenere.txt");

    IO.println(Chunker.read("/Users/kevin/IdeaProjects/Cipher/src/foo.txt", 126));
    RSACipher cipherRSA = new RSACipher();
    IO.println(cipherRSA.d);
    IO.println(cipherRSA.p);
    IO.println(cipherRSA.q);
    IO.println(cipherRSA.n);
    IO.println(cipherRSA.totient);
    ArrayList<byte[]> output = Chunker.read2("/Users/kevin/IdeaProjects/Cipher/src/foo.txt", 126);
    IO.println("RSA encrypted output");
    for (byte[] chunk: output) {
        BigInteger encrypted = cipherRSA.encrypt(chunk);
        IO.println(encrypted);
        IO.println(new String(cipherRSA.decrypt(encrypted.toByteArray()).toByteArray(), StandardCharsets.UTF_8));
        IO.println(encrypted.toString(16));
    }
    cipherRSA.encryptFile("/Users/kevin/IdeaProjects/Cipher/src/foo.txt", "/Users/kevin/IdeaProjects/Cipher/src/foo_encrypted.txt");
    cipherRSA.decryptFile("/Users/kevin/IdeaProjects/Cipher/src/foo_encrypted.txt", "/Users/kevin/IdeaProjects/Cipher/src/foo_decrypted.txt");

    cipherRSA.save("rsa.txt");

    CaesarCipher caesar = new CaesarCipher(3);
    caesar.save("caesar.txt");

//    • --caesar <shift_param>: Create a new Caesar cipher with the given integer shift parameter.
//• --random: Create a new monoalphabetic substitution cipher with a randomly chosen permutation of the
//    alphabet.
//• --vigenere <key>: Create a new Vigen` ere cipher with the given keyword. The keyword is given as a
//    string of maximum length 128 characters.
//• --rsa: Create a new RSA cipher.
//• --monoLoad <cipher_file>: Load a monoalphabetic substitution cipher (caesar or random) from the file
//    specified.
//• --vigenereLoad <cipher_file>: Load a Vigen` ere cipher from the file specified.
//• --rsaLoad <file>: Create an RSA encrypter/decrypter from the public/private key pair stored in the
//    file specified.
//    Cipher functions Next, at most one of the following options may also be specified by the user.
//• --em <message>: Encrypt the given string using the specified cipher scheme.
//• --ef <file>: Encrypt the contents of the specified file using the specified cipher scheme.
//• --dm <message>: Decrypt the given string using the specified cipher scheme.
//• --df <file>: Decrypt the contents of the specified file using the specified cipher scheme.
//    Output options Finally, the user may add as many of the following output flags as they wish.
//• --print: Print the result of applying the cipher (if any) to the console.
//• --out <file>: Print the result of applying the cipher (if any) to the specified file.
//• --save <file>: Save the current cipher to the specified file.
}


