package com.max.fieldcryptor;

import com.max.fieldcryptor.type.CryptographicAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CipherFactoryTest {

    static String cipherKeyName = "Field-Cryptor";

    @BeforeAll
    public static void setUp(){
        CryptographicAlgorithm algorithm = CryptographicAlgorithm.AES_CBC_PKCS5_PADDING;
        String key = UUID.randomUUID().toString().substring(0, 32);
        String iv = UUID.randomUUID().toString().substring(0, 16);

        CipherFactory.addAESSpec(cipherKeyName, algorithm, key, iv, true);

    }

    @Test
    public void getCipher(){

        AESCipher cipher = CipherFactory.getCipher(cipherKeyName);
        Assertions.assertNotNull(cipher);
    }

}