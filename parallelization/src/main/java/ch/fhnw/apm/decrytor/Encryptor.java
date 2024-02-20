package ch.fhnw.apm.decrytor;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import static java.lang.System.arraycopy;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class Encryptor {

    private static final int SALT_SIZE = 16;
    private static final int IV_LENGTH = 128 / 8;

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        System.out.print("Enter password:        ");
        var password = scanner.nextLine();
        System.out.print("Enter text to encrypt: ");
        var text = scanner.nextLine();

        var encrypted = encrypt(text, password);
        System.out.println("\n" + encrypted);
    }

    public static String encrypt(String text, String password) {
        var random = new SecureRandom();
        var salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        try {
            var keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            var keySpec = new PBEKeySpec(password.toCharArray(), salt, 1, 256);
            var key = new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), "AES");

            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(ENCRYPT_MODE, key, random);
            var iv = cipher.getIV();
            assert iv.length == IV_LENGTH;

            var cipherText = cipher.doFinal(text.getBytes(UTF_8));

            var encrypted = new byte[SALT_SIZE + IV_LENGTH + cipherText.length];
            arraycopy(salt, 0, encrypted, 0, SALT_SIZE);
            arraycopy(iv, 0, encrypted, SALT_SIZE, IV_LENGTH);
            arraycopy(cipherText, 0, encrypted, SALT_SIZE + IV_LENGTH, cipherText.length);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
