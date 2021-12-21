package com.max.fieldcryptor.cipher;

import com.max.fieldcryptor.type.CryptographicAlgorithm;

public abstract class AbstractCipher {

    protected final String ALGORITHM;
    protected final CryptographicAlgorithm algorithm;
    protected final String key;
    protected final String iv;
    protected final boolean enabled;

    public AbstractCipher(CryptographicAlgorithm algorithm, String key, String iv, boolean enabled) {
        this.ALGORITHM = algorithm.getCryptAlgorithmString();
        this.algorithm = algorithm;
        this.key = key;
        this.iv = iv;
        this.enabled = enabled;
        if (algorithm.needsIV() && iv == null) {
            throw new IllegalArgumentException("IV (initial vector) must not be null for padding algorithms");
        }
    }

    public abstract String encrypt(String plainStr) throws Exception;

    public abstract String decrypt(String encryptedStr) throws Exception;
}
