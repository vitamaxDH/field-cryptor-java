package com.max.fieldcryptor;

import com.max.fieldcryptor.type.CryptographicAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CipherFactoryTest {

    @BeforeAll
    private static void setUp() {
        for (CryptographicAlgorithm algorithm : CryptographicAlgorithm.values()) {
            String key = UUID.randomUUID().toString().substring(0, 32);
            String iv = UUID.randomUUID().toString().substring(0, 16);

            CipherFactory.addAESSpec(algorithm.toString(), algorithm, key, iv, true);
        }
    }

    @Test
    @DisplayName("Check cbc pkcs5 padding cipher")
    public void getCipher() {
        String cipherKeyName = CryptographicAlgorithm.AES_CBC_PKCS5_PADDING.toString();
        AESCipher cipher = CipherFactory.getCipher(cipherKeyName);
        assertNotNull(cipher);
    }

    @Test
    public void encryptTest() {
        // given
        String name = "Daehan";
        String address = "Republic of Korea";
        Person person = new Person(name, address, 55);
        AESCipher cipher = CipherFactory.getCipher(CryptographicAlgorithm.AES_CBC_PKCS5_PADDING.toString());

        // when
        Person encrypted = FieldCryptor.encryptFields(cipher, person, Person::new);

        // then
        Assertions.assertNotEquals(name, encrypted.getName());
    }

    @Test
    public void decryptTest() throws Exception {
        // given
        String name = "Daehan";
        String address = "Republic of Korea";
        Person person = new Person(name, address, 55);
        AESCipher cipher = CipherFactory.getCipher(CryptographicAlgorithm.AES_CBC_PKCS5_PADDING.toString());

        // when
        person.setName(cipher.encrypt(name));
        Person decrypted = FieldCryptor.decryptFields(cipher, person, Person::new);

        // then
        Assertions.assertEquals(name, decrypted.getName());
    }
}