package com.max.fieldcryptor.type;

public enum CryptographicAlgorithm {

    AES_CBC_NO_PADDING("AES/CBC/NoPadding", 128, true),
    AES_CBC_PKCS5_PADDING("AES/CBC/PKCS5Padding", 128, true),
    AES_ECB_NO_PADDING("AES/ECB/NoPadding", 128, false),
    AES_ECB_PKCS5_PADDING("AES/ECB/PKCS5Padding", 128, false),
    DES_CBC_NO_PADDING("DES/CBC/NoPadding", 56, true),
    DES_CBC_PKCS5_PADDING("DES/CBC/PKCS5Padding", 56, true),
    DES_ECB_NO_PADDING("DES/ECB/NoPadding", 56, false),
    DES_ECB_PKCS5_PADDING("DES/ECB/PKCS5Padding", 56, false),
    DES_EDE_CBC_NO_PADDING("DESede/CBC/NoPadding", 168, true),
    DES_EDE_CBC_PKCS5_PADDING("DESede/CBC/PKCS5Padding", 168, true),
    DES_EDE_ECB_NO_PADDING("DESede/ECB/NoPadding", 168, false),
    DES_EDE_ECB_PKCS5_PADDING("DESede/ECB/PKCS5Padding", 168, false),
    RSA_ECB_PKCS1_PADDING_1024("RSA/ECB/PKCS1Padding", 1024, false),
    RSA_ECB_PKCS1_PADDING_2048("RSA/ECB/PKCS1Padding", 2048, false),
    RSA_ECB_OAEP_SHA1_MGF1_PADDING_1024("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", 1024, false),
    RSA_ECB_OAEP_SHA1_MGF1_PADDING_2048("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", 2048, false),
    RSA_ECB_OAEP_SHA256_MGF1_PADDING_1024("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", 1024, false),
    RSA_ECB_OAEP_SHA256_MGF1_PADDING_2048("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", 2048, false),

    ;

    private final String algorithm;
    private final int bitSize;
    private final boolean needsIV;

    CryptographicAlgorithm(String algorithm, int bitSize, boolean needsIV) {
        this.algorithm = algorithm;
        this.bitSize = bitSize;
        this.needsIV = needsIV;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getBitSize() {
        return bitSize;
    }

    public boolean needsIV() {
        return needsIV;
    }
}
