package com.max.fieldcryptor;

import com.max.fieldcryptor.model.Person;

public class Main {

    public static void main(String[] args) {
        String name = "Field-Cryptor";
        String key = "sampleAESKeyForDemoItIsEasyToUse";
        String iv = "sampleCBCinitVal";

        FieldCryptorFactory.addAESSpec(name, key, iv, 128, true);
        FieldCipher cipher = FieldCryptorFactory.getCipher(name);

        Person person = new Person("VitaMax", "Suwon", 50);
        Person person2 = new Person("비타맥스", "수원", 30);

        long start = System.currentTimeMillis();
        Person encryptedObj = FieldCryptor.encrypt(person, cipher);
        System.out.println("first time elapsed " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        Person encryptedObj2 = FieldCryptor.encrypt(person2, cipher);
        System.out.println("second time elapsed " + (System.currentTimeMillis() - start));

        System.out.println("encryptedObj = " + encryptedObj);
        System.out.println("encryptedObj = " + encryptedObj2);
    }
}
