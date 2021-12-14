package com.max.fieldcryptor;

import com.max.fieldcryptor.type.CryptographicAlgorithm;

import java.util.HashMap;
import java.util.Map;


public class CipherFactory {

    private static final Map<String, AESCipher> cipherMap = new HashMap<>();

    public String encrypt(String cipherKeyName, String plainText) throws Exception {
        return getCipher(cipherKeyName).encrypt(plainText);
    }

    public String decrypt(String cipherKeyName, String cipherText) throws Exception {
        return getCipher(cipherKeyName).decrypt(cipherText);
    }

    public static void addAESSpec(String cipherKeyName, CryptographicAlgorithm algorithm, String key, String iv, boolean enabled) {
        cipherMap.putIfAbsent(cipherKeyName, new AESCipher(algorithm, key, iv, enabled));
    }

    public static AESCipher getCipher(String cryptoTarget) {
        if (cipherMap.containsKey(cryptoTarget)) {
            return cipherMap.get(cryptoTarget);
        }
        throw new RuntimeException("Unknown Crypto Target: " + cryptoTarget);
    }

    public AESCipher createCipher(CryptographicAlgorithm algorithm, String key, String iv) {
        return new AESCipher(algorithm, key, iv, true);
    }


}