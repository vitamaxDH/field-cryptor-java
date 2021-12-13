package com.max.fieldcryptor;

import com.max.fieldcryptor.model.Person;

public class Main {

    public static void main(String[] args) {
        String name = "Field-Cryptor";
        String key = "sampleAESKeyForDemoItIsEasyToUse";
        String iv = "sampleCBCinitVal";

        FieldCryptorFactory.addAESSpec(name, key, iv, 128, true);
        FieldCipher cipher = FieldCryptorFactory.getCipher(name);

        Person person = new Person("VitaMax", 50);
        Person encryptedObj = FieldCryptor.encrypt(person, cipher);

        System.out.println("encryptedObj = " + encryptedObj);

    }
}
