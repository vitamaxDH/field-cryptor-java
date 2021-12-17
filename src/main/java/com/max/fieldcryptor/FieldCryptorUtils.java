package com.max.fieldcryptor;

import com.max.fieldcryptor.lang.FunctionWithException;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldCryptorUtils {

    private static final Logger log = LoggerFactory.getLogger(FieldCryptorUtils.class);

    private static final Map<Class<?>, CryptorReflectionResult> cacheMap = new HashMap<>();

    private String CIPHER_TRANSFORMATION;
    private String ALGORITHM;
    private int BUFFER_SIZE;
    private int KEY_SIZE;
    private int IV_SIZE;

    public FieldCryptorUtils() {
        this.CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
        this.ALGORITHM = "AES";
        this.BUFFER_SIZE = 1024;
        this.KEY_SIZE = 32;
        this.IV_SIZE = 16;
    }

    public static <T> CryptorReflectionResult getReflectionResult(T obj) {
        return cacheMap.getOrDefault(obj.getClass(), CryptorReflectionResult.from(obj));
    }

    public byte[] encrypt(final byte[] key, final byte[] iv, final byte[] msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = createCipher(key, iv);
        final byte[] encrypted = cipher.doFinal(msg);
        return encrypted;
    }

    private Cipher createCipher(byte[] key, byte[] iv) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        if (key.length != this.KEY_SIZE) {
            throw new InvalidKeyException("Illegal key size : The size of key must be 32bytes(256bits)");
        }
        final Cipher cipher = Cipher.getInstance(this.CIPHER_TRANSFORMATION);
        final IvParameterSpec ivSpec = new IvParameterSpec(iv);
        final SecretKeySpec keySpec = new SecretKeySpec(key, this.ALGORITHM);
        cipher.init(1, keySpec, ivSpec);
        return cipher;
    }

    public String encrypt(final byte[] key, final byte[] iv, final String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        final byte[] encrypted = this.encrypt(key, iv, msg.getBytes());
        return Base64.encodeBase64String(encrypted).trim();
    }

    public void encrypt(final byte[] key, final byte[] iv, final File srcFile, final File destFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = createCipher(key, iv);

        try (FileInputStream fis = new FileInputStream(srcFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {
            final byte[] in = new byte[this.BUFFER_SIZE];
            int read;
            while ((read = fis.read(in)) != -1) {
                final byte[] output = cipher.update(in, 0, read);
                if (output != null) {
                    fos.write(output);
                }
            }
            final byte[] output = cipher.doFinal();
            if (output != null) {
                fos.write(output);
            }
            fos.flush();
        } catch (IOException e) {
        }
    }

    public void encrypt(final byte[] key, final byte[] iv, final String srcPath, final String destPath) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
        this.encrypt(key, iv, new File(srcPath), new File(destPath));
    }

    public String encryptToHexString(final byte[] key, final byte[] iv, final String msg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        final byte[] encrypted = this.encrypt(key, iv, msg.getBytes());
        return Hex.encodeHexString(encrypted);
    }

    public byte[] decrypt(final byte[] key, final byte[] iv, final byte[] msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        if (key.length != this.KEY_SIZE) {
            throw new InvalidKeyException("Illegal key size : The size of key must be 32bytes(256bits)");
        }
        final Cipher cipher = Cipher.getInstance(this.CIPHER_TRANSFORMATION);
        final IvParameterSpec ivSpec = new IvParameterSpec(iv);
        final SecretKeySpec keySpec = new SecretKeySpec(key, this.ALGORITHM);
        cipher.init(2, keySpec, ivSpec);
        final byte[] encrypted = cipher.doFinal(msg);
        return encrypted;
    }

//    public String decrypt(final byte[] key, final byte[] iv, final String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        final byte[] decodeMsg = Base64.decodeBase64(msg);
//        return new String(this.decrypt(key, iv, decodeMsg));
//    }

    public void decrypt(final byte[] key, final byte[] iv, final File srcFile, final File destFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
        final Cipher cipher = Cipher.getInstance(this.CIPHER_TRANSFORMATION);
        final IvParameterSpec ivSpec = new IvParameterSpec(iv);
        final SecretKeySpec keySpec = new SecretKeySpec(key, this.ALGORITHM);
        cipher.init(2, keySpec, ivSpec);
        try (FileInputStream fis = new FileInputStream(srcFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {
            final byte[] in = new byte[this.BUFFER_SIZE];
            int read;
            while ((read = fis.read(in)) != -1) {
                final byte[] output = cipher.update(in, 0, read);
                if (output != null) {
                    fos.write(output);
                }
            }
            final byte[] output = cipher.doFinal();
            if (output != null) {
                fos.write(output);
            }
            fos.flush();
        } catch (IOException e) {
        }
    }

    public void decrypt(final byte[] key, final byte[] iv, final String srcPath, final String destPath) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
        this.decrypt(key, iv, new File(srcPath), new File(destPath));
    }

    public String decryptHexString(final byte[] key, final byte[] iv, final String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, DecoderException {
        final byte[] decodeMsg = Hex.decodeHex(msg.toCharArray());
        return new String(this.decrypt(key, iv, decodeMsg));
    }

    public byte[] generateRandomKey() throws NoSuchAlgorithmException {
        final KeyGenerator keygen = KeyGenerator.getInstance(this.ALGORITHM);
        keygen.init(new SecureRandom());
        keygen.init(this.KEY_SIZE * 8);
        final SecretKey secKey = keygen.generateKey();
        return secKey.getEncoded();
    }

    public byte[] generateRandomIV() {
        final SecureRandom secRand = new SecureRandom();
        final byte[] iv = new byte[this.IV_SIZE];
        secRand.nextBytes(iv);
        return iv;
    }

    public void setCipherTransformation(final String cipherTrnasformation) {
        this.CIPHER_TRANSFORMATION = cipherTrnasformation;
    }

    public static <T> List<Field> getStringFields(T t) {
        return ReflectionUtils.getFields(t).stream()
                .filter(field -> ReflectionUtils.anyType(field, String.class))
                .collect(Collectors.toList());
    }

    public static <T, R, E extends Exception> Function<T, R> cipherWrapper(FunctionWithException<T, R, E> fe) {
        return data -> {
            try {
                return fe.apply(data);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Exception occurred.");
            }
        };
    }

}