package ch.fhnw.apm.decrytor;

import org.slf4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Scanner;

import static java.lang.System.arraycopy;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static org.slf4j.LoggerFactory.getLogger;

public class Decryptor {

    private static final Logger log = getLogger(Decryptor.class);

    private static final int SALT_SIZE = 16;
    private static final int IV_LENGTH = 128 / 8;

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        System.out.print("Enter text to decrypt:   ");
        var encrypted = scanner.nextLine();

        var start = System.nanoTime();
        var decrypted = decrypt(encrypted);
        var time = (System.nanoTime() - start) / 1_000_000_000.0;
        System.out.printf("\n%s (%.2f s)\n", decrypted, time);
    }

    public static String decrypt(String encrypted) {
        var i = 0L;
        String decrypted = null;
        while (decrypted == null) {
            var password = asPassword(i);
            if (i % 10_000 == 0) {
                log.info("Trying password: \"{}\"", password);
            }
            decrypted = tryDecrypt(encrypted, password);
            i++;
        }
        return decrypted;
    }

    public static String tryDecrypt(String encrypted, String password) {
        var bytes = Base64.getDecoder().decode(encrypted);
        var salt = new byte[SALT_SIZE];
        var iv = new byte[IV_LENGTH];
        var cipherText = new byte[bytes.length - SALT_SIZE - IV_LENGTH];
        arraycopy(bytes, 0, salt, 0, SALT_SIZE);
        arraycopy(bytes, SALT_SIZE, iv, 0, IV_LENGTH);
        arraycopy(bytes, SALT_SIZE + IV_LENGTH, cipherText, 0, cipherText.length);

        try {
            var keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            var keySpec = new PBEKeySpec(password.toCharArray(), salt, 1, 256);
            var key = new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), "AES");

            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(DECRYPT_MODE, key, new IvParameterSpec(iv));

            var decrypted = cipher.doFinal(cipherText);
            var decoded = UTF_8.newDecoder().decode(ByteBuffer.wrap(decrypted));
            return decoded.toString();
        } catch (BadPaddingException | CharacterCodingException e) {
            return null; // decryption failed
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private static String asPassword(long n) {
        var password = "";
        while (n > 0) {
            var c = (n - 1) % 26 + 'a';
            password = (char) c + password;
            n = (n - 1) / 26;
        }
        return password;
    }
}
