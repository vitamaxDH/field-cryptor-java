package com.max.fieldcryptor.cipher;

import com.max.fieldcryptor.type.CryptographicAlgorithm;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AESCipher extends AbstractCipher {

    private static final Logger log = LoggerFactory.getLogger(AESCipher.class);

    public AESCipher(CryptographicAlgorithm algorithm, String key, String iv, boolean enabled) {
        super(algorithm, key, iv, enabled);
    }

    private SecretKeySpec getKey() throws Exception {
        byte[] keyBytes = new byte[algorithm.getBitSize() / 8]; // byte[32]:256bit,  byte[16]: 128bit
        byte[] bytes = key.getBytes(UTF_8);

        int len = bytes.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(bytes, 0, keyBytes, 0, len);

        return new SecretKeySpec(keyBytes, ALGORITHM);
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

    public String encrypt(String plainStr) throws Exception {
        if (!this.enabled) {
            return plainStr;
        }

        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(plainStr.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encryptedStr) throws Exception {
        if (!this.enabled) {
            return encryptedStr;
        }

        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedStr);
        return new String(cipher.doFinal(decodedBytes), UTF_8);
    }

}