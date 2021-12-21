package com.max.fieldcryptor;

import com.max.fieldcryptor.cipher.AESCipher;
import com.max.fieldcryptor.cipher.AbstractCipher;
import com.max.fieldcryptor.type.CryptographicAlgorithm;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class CipherFactory {

    private static final Logger log = LoggerFactory.getLogger(CipherFactory.class);

    private static final Map<String, AbstractCipher> cipherMap = new HashMap<>();

    public String encrypt(String cipherKeyName, String plainText) throws Exception {
        return getCipher(cipherKeyName).encrypt(plainText);
    }

    public String decrypt(String cipherKeyName, String cipherText) throws Exception {
        return getCipher(cipherKeyName).decrypt(cipherText);
    }

    public static void addCipher(String cipherKeyName, CryptographicAlgorithm algorithm, String key, String iv, boolean enabled) {
        AbstractCipher cipher = null;
        switch (algorithm){
            case AES_ECB_NO_PADDING:
            case AES_ECB_PKCS5_PADDING:
            case AES_CBC_NO_PADDING:
            case AES_CBC_PKCS5_PADDING:
                cipher = new AESCipher(algorithm, key, iv, enabled);
                break;
            default:
                log.debug(algorithm + " has not been implemented yet");
        }


        cipherMap.putIfAbsent(cipherKeyName, cipher);
    }

    public static AbstractCipher getCipher(String cryptoTarget) {
        if (cipherMap.containsKey(cryptoTarget)) {
            return cipherMap.get(cryptoTarget);
        }
        throw new RuntimeException("Unknown Crypto Target: " + cryptoTarget);
    }

    public AESCipher createCipher(CryptographicAlgorithm algorithm, String key, String iv) {
        return new AESCipher(algorithm, key, iv, true);
    }


}