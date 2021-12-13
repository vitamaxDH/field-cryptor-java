package com.max.fieldcryptor;

import java.util.HashMap;
import java.util.Map;


public class FieldCryptorFactory {

    private static final Map<String, FieldCipher> cipherMap = new HashMap<>();

    public String encrypt(String targetName, String plainText) throws Exception {
        return getCipher(targetName).encrypt(plainText);
    }

    public String decrypt(String targetName, String cipherText) throws Exception {
        return getCipher(targetName).decrypt(cipherText);
    }

    public static void addAESSpec(String name, String key, String iv, int bitSize, boolean enabled) {
        cipherMap.putIfAbsent(name, new FieldCipher(key, iv, bitSize, enabled));
    }

    public static FieldCipher getCipher(String cryptoTarget) {
        if (cipherMap.containsKey(cryptoTarget)) {
            return cipherMap.get(cryptoTarget);
        }
        throw new RuntimeException("Unknown Crypto Target: " + cryptoTarget);
    }

    public FieldCipher createCipher(String aesKey, String ivKey, int bitSize) {
        return new FieldCipher(aesKey, ivKey, bitSize, true);
    }


}