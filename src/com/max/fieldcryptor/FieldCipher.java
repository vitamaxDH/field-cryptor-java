package com.max.fieldcryptor;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FieldCipher {
    private final String key;
    private final String iv;
    private final int bitSize;

    private final boolean enabled;
    private final static String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public FieldCipher(String key, String iv, int bitSize, boolean enabled) {
        this.key = key;
        this.iv = iv;
        this.bitSize = bitSize;  // 128 or 256
        this.enabled = enabled;
    }

    private SecretKeySpec getKey() throws Exception {
        byte[] keyBytes = new byte[bitSize / 8]; // byte[32]:256bit,  byte[16]: 128bit
        byte[] bytes = key.getBytes(UTF_8);

        int len = bytes.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(bytes, 0, keyBytes, 0, len);
        return new SecretKeySpec(keyBytes, "AES");
    }

    // 암호화
    public String encrypt(String str) throws Exception {

        if (!this.enabled) {
            return str;
        }

        Key keySpec = getKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(UTF_8)));
        byte[] encrypted = cipher.doFinal(str.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // 복호화
    public String decrypt(String enStr) throws Exception {
        if (!this.enabled) {
            return enStr;
        }

        Key keySpec = getKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes(UTF_8)));
        byte[] decodedBytes = Base64.getDecoder().decode(enStr);
        return new String(cipher.doFinal(decodedBytes), UTF_8);
    }

}