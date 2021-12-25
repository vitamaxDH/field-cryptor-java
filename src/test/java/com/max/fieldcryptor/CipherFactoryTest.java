package com.max.fieldcryptor;

import com.max.fieldcryptor.cipher.AbstractCipher;
import com.max.fieldcryptor.lang.FunctionWithException;
import com.max.fieldcryptor.type.CryptographicAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CipherFactoryTest {

    static String name;
    static String address;
    static Person person;
    static AbstractCipher cipher;

    @BeforeAll
    static void setUp() {
        for (CryptographicAlgorithm algorithm : CryptographicAlgorithm.values()) {
            String key = UUID.randomUUID().toString().substring(0, 32);
            String iv = UUID.randomUUID().toString().substring(0, 16);

            CipherFactory.addCipher(algorithm.toString(), algorithm, key, iv, true);
        }
        name = "Daehan";
        address = "Republic of Korea";
        person = new Person(name, address, 55);
        cipher = CipherFactory.getCipher(CryptographicAlgorithm.AES_CBC_PKCS5_PADDING.toString());
    }

    @Test
    @DisplayName("Check cbc pkcs5 padding cipher")
    public void getCipher() {
        String cipherKeyName = CryptographicAlgorithm.AES_CBC_PKCS5_PADDING.toString();
        AbstractCipher cipher = CipherFactory.getCipher(cipherKeyName);
        assertNotNull(cipher);
    }

    @Test
    public void encryptTest() {
        // given

        // when
        FieldCryptor fieldCryptor = FieldCryptor.from(cipher);
        Person encrypted = fieldCryptor.encrypt(person, Person::new);

        // then
        Assertions.assertNotEquals(name, encrypted.getName());
    }

    @Test
    public void decryptTest() throws Exception {
        // given

        // when
        person.setName(cipher.encrypt(name));
        FieldCryptor fieldCryptor = FieldCryptor.from(cipher);
        Person decrypted = fieldCryptor.decrypt(person, Person::new);

        // then
        Assertions.assertEquals(name, decrypted.getName());
    }

    @Test
    @DisplayName("커스텀 Cipher 를 이용한 암호화 테스트")
    public void customCipherEncryptTest() {
        // given

        // when
        Person encrypted = FieldCryptorUtils.encrypt(person, Person::new, cipherWrapper(cipher::encrypt));

        // then
        Assertions.assertNotEquals(name, encrypted.getName());
    }

    @Test
    @DisplayName("커스텀 Cipher 를 이용한 복호화 테스트")
    public void customCipherDecryptTest() throws Exception {
        // given

        // when
        person.setName(cipher.encrypt(name));
        Person decrypted = FieldCryptorUtils.decrypt(person, Person::new, cipherWrapper(cipher::decrypt));

        // then
        Assertions.assertEquals(name, decrypted.getName());
    }

    public <T, R, E extends Exception> Function<T, R> cipherWrapper(FunctionWithException<T, R, E> fe) {
        return data -> {
            try {
                return fe.apply(data);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Exception occurred.");
            }
        };
    }
}