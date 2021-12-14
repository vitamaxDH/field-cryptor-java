package com.max.fieldcryptor;

import com.max.fieldcryptor.type.CryptographicAlgorithm;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AESCipher {

    private static final Logger log = LoggerFactory.getLogger(AESCipher.class);

    private final CryptographicAlgorithm algorithm;
    private final String key;
    private final String iv;
    private final boolean enabled;

    private final static String AES = "AES";

    public AESCipher(CryptographicAlgorithm algorithm, String key, String iv, boolean enabled) {
        this.algorithm = algorithm;
        this.key = key;
        this.iv = iv;
        this.enabled = enabled;
        if (algorithm.needsIV() && iv == null) {
            throw new IllegalArgumentException("IV (initial vector) must not be null for padding algorithms");
        }
    }

    private SecretKeySpec getKey() throws Exception {
        byte[] keyBytes = new byte[algorithm.getBitSize() / 8]; // byte[32]:256bit,  byte[16]: 128bit
        byte[] bytes = key.getBytes(UTF_8);

        int len = bytes.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(bytes, 0, keyBytes, 0, len);

        return new SecretKeySpec(keyBytes, AES);
    }

    private Cipher getCipher(int cipherMode) throws Exception {
        Key keySpec = getKey();

        Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
        if (algorithm.needsIV()) {
            cipher.init(cipherMode, keySpec, new IvParameterSpec(iv.getBytes(UTF_8)));
        } else {
            cipher.init(cipherMode, keySpec);
        }
        return cipher;
    }

    // 암호화
    public String encrypt(String str) throws Exception {
        if (!this.enabled) {
            return str;
        }

        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(str.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 복호화
    public String decrypt(String enStr) throws Exception {
        if (!this.enabled) {
            return enStr;
        }

        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] decodedBytes = Base64.getDecoder().decode(enStr);
        return new String(cipher.doFinal(decodedBytes), UTF_8);
    }

}