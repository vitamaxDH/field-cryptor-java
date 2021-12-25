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

    static String name = "Daehan";
    static String address = "Republic of Korea";
    static Person person = new Person(name, address, 55);
    static AbstractCipher cipher;

    static String vendor = "HYUNDAI";
    static String designer = "Daehan";
    static String madeIn = "Korea";
    static int numberOfWheels = 4;
    Car plainCar = new Car(vendor, designer, madeIn, numberOfWheels);

    @BeforeAll
    static void setUp() {
        for (CryptographicAlgorithm algorithm : CryptographicAlgorithm.values()) {
            String key = UUID.randomUUID().toString().substring(0, 32);
            String iv = UUID.randomUUID().toString().substring(0, 16);

            CipherFactory.addCipher(algorithm.toString(), algorithm, key, iv, true);
        }
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
        FieldCryptor fieldCryptor = FieldCryptor.from(cipher);
        Person encrypted = fieldCryptor.encrypt(person, Person::new);
        Person decrypted = fieldCryptor.decrypt(encrypted, Person::new);

        // then
        System.out.println("person : " + person);
        System.out.println("encrypted : " + encrypted);
        System.out.println("decrypted : " + decrypted);
        Assertions.assertEquals(person, decrypted);
    }

    @Test
    @DisplayName("커스텀 Cipher 를 이용한 암호화 테스트")
    public void customCipherEncryptTest() {
        // given

        // when
        Person encrypted = FieldCryptorUtils.encrypt(person, Person::new, cipherWrapper(cipher::encrypt));

        // then
        System.out.println(encrypted);
        Assertions.assertNotEquals(name, encrypted.getName());
    }

    @Test
    @DisplayName("커스텀 Cipher 를 이용한 복호화 테스트")
    public void customCipherDecryptTest() throws Exception {
        // given

        // when
        Person encrypted = FieldCryptorUtils.encrypt(person, Person::new, cipherWrapper(cipher::encrypt));
        Person decrypted = FieldCryptorUtils.decrypt(encrypted, Person::new, cipherWrapper(cipher::decrypt));

        Car encryptedCar = FieldCryptorUtils.encrypt(plainCar, Car::new, cipherWrapper(cipher::encrypt));
        Car decryptedCar = FieldCryptorUtils.decrypt(encryptedCar, Car::new, cipherWrapper(cipher::decrypt));

        // then
        System.out.println("person : " + person);
        System.out.println("encrypted : " + encrypted);
        System.out.println("decrypted : " + decrypted);

        System.out.println("plainCar : " + plainCar);
        System.out.println("encrypted : " + encryptedCar);
        System.out.println("decrypted : " + decryptedCar);

        Assertions.assertEquals(person, decrypted);
        Assertions.assertEquals(plainCar, decryptedCar);
    }

    @Test
    @DisplayName("타입의 암/복호화 테스트")
    public void fieldCryptoOnTypeTest() throws Exception {
        // given
        String vendor = "HYUNDAI";
        String designer = "Daehan";
        String madeIn = "Korea";
        int numberOfWheels = 4;

        // when
        FieldCryptor fc = FieldCryptor.from(cipher);
        Car encrypted = fc.encrypt(plainCar, Car::new);

        // then
        System.out.println(plainCar);
        System.out.println(encrypted);

        Assertions.assertEquals(encrypted.getVendor(), cipher.encrypt(vendor));
        Assertions.assertEquals(encrypted.getDesigner(), cipher.encrypt(designer));
        Assertions.assertEquals(plainCar.getMadeIn(), encrypted.getMadeIn());
        Assertions.assertEquals(encrypted.getNumberOfWheels(), numberOfWheels);
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