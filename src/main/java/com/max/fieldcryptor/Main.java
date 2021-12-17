package com.max.fieldcryptor;

import com.max.fieldcryptor.model.Person;
import com.max.fieldcryptor.type.CryptographicAlgorithm;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        String cipherKeyName = "Field-Cryptor";
        CryptographicAlgorithm algorithm = CryptographicAlgorithm.AES_CBC_PKCS5_PADDING;
        String key = UUID.randomUUID().toString().substring(0, 32);
        String iv = UUID.randomUUID().toString().substring(0, 16);

        CipherFactory.addAESSpec(cipherKeyName, algorithm, key, iv, true);

        AESCipher cipher = CipherFactory.getCipher(cipherKeyName);
        Person person = new Person("VitaMax", "Suwon", 50);

        long start = System.nanoTime();
        Person encryptedObj = FieldCryptor.encryptFields(cipher, person, Person::new);
        System.out.println("first time elapsed " + (System.nanoTime() - start));

        start = System.nanoTime();
        Person decryptedObj = FieldCryptor.decryptFields(cipher, encryptedObj, Person::new);
        System.out.println("second time elapsed " + (System.nanoTime() - start));

        System.out.println("encryptedObj = " + encryptedObj);
        System.out.println("decryptedObj = " + decryptedObj);
    }
}
