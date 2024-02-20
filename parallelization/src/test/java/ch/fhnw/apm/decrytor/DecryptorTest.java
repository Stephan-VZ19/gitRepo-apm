package ch.fhnw.apm.decrytor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecryptorTest {

    @Test
    void tryDecryptFail() {
        var encrypted = Encryptor.encrypt("Hello, World!", "password");
        var decrypted = Decryptor.tryDecrypt(encrypted, "wrong password");
        assertNull(decrypted);

        decrypted = Decryptor.tryDecrypt(encrypted, "also wrong");
        assertNull(decrypted);

        decrypted = Decryptor.tryDecrypt(encrypted, "still wrong");
        assertNull(decrypted);
    }

    @Test
    void tryDecryptSuccess() {
        var encrypted = Encryptor.encrypt("Hello, World!", "password");
        var decrypted = Decryptor.tryDecrypt(encrypted, "password");
        assertEquals("Hello, World!", decrypted);
    }

    @Test
    void decrypt() {
        var encrypted = Encryptor.encrypt("Hello, World!", "pass");
        var decrypted = Decryptor.decrypt(encrypted);
        assertEquals("Hello, World!", decrypted);

        encrypted = Encryptor.encrypt("Execute Order 66!", "sith");
        decrypted = Decryptor.decrypt(encrypted);
        assertEquals("Execute Order 66!", decrypted);
    }
}
