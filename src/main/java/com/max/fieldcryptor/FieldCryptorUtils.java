package com.max.fieldcryptor;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class FieldCryptorUtils {

    private static final Logger log = LoggerFactory.getLogger(FieldCryptorUtils.class);

    private static final Map<Class<?>, ClassifiedFields> cacheMap = new HashMap<>();

    /**
     * get string fields of an object's class in parameter.
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> ClassifiedFields getClassifiedFields(T obj) {
        return cacheMap.getOrDefault(obj.getClass(), ClassifiedFields.from(obj));
    }

    public static <T> T encrypt(T source, Supplier<T> supplier, Function<String, String> customEncrypt) {
        return FieldCryptorUtils.encrypt(source, supplier.get(), customEncrypt);
    }

    public static <T> T encrypt(T source, T target, Function<String, String> customEncrypt) {
        return FieldCryptor.taskTemplate(source, target, customEncrypt);
    }

    public static <T> T decrypt(T source, Supplier<T> supplier, Function<String, String> customDecrypt) {
        return FieldCryptorUtils.decrypt(source, supplier.get(), customDecrypt);
    }

    public static <T> T decrypt(T source, T target, Function<String, String> customDecrypt) {
        return FieldCryptor.taskTemplate(source, target, customDecrypt);
    }

//    public byte[] encrypt(final byte[] key, final byte[] iv, final byte[] msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        Cipher cipher = createCipher(key, iv);
//        final byte[] encrypted = cipher.doFinal(msg);
//        return encrypted;
//    }
//
//    private Cipher createCipher(byte[] key, byte[] iv) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
//        if (key.length != this.KEY_SIZE) {
//            throw new InvalidKeyException("Illegal key size : The size of key must be 32bytes(256bits)");
//        }
//        final Cipher cipher = Cipher.getInstance(this.CIPHER_TRANSFORMATION);
//        final IvParameterSpec ivSpec = new IvParameterSpec(iv);
//        final SecretKeySpec keySpec = new SecretKeySpec(key, this.ALGORITHM);
//        cipher.init(1, keySpec, ivSpec);
//        return cipher;
//    }
//
//    public String encrypt(final byte[] key, final byte[] iv, final String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        final byte[] encrypted = this.encrypt(key, iv, msg.getBytes());
//        return Base64.encodeBase64String(encrypted).trim();
//    }
//
//    public void encrypt(final byte[] key, final byte[] iv, final File srcFile, final File destFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
//
//        Cipher cipher = createCipher(key, iv);
//
//        try (FileInputStream fis = new FileInputStream(srcFile);
//             FileOutputStream fos = new FileOutputStream(destFile)) {
//            final byte[] in = new byte[this.BUFFER_SIZE];
//            int read;
//            while ((read = fis.read(in)) != -1) {
//                final byte[] output = cipher.update(in, 0, read);
//                if (output != null) {
//                    fos.write(output);
//                }
//            }
//            final byte[] output = cipher.doFinal();
//            if (output != null) {
//                fos.write(output);
//            }
//            fos.flush();
//        } catch (IOException e) {
//        }
//    }
//
//    public void encrypt(final byte[] key, final byte[] iv, final String srcPath, final String destPath) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
//        this.encrypt(key, iv, new File(srcPath), new File(destPath));
//    }
//
//    public String encryptToHexString(final byte[] key, final byte[] iv, final String msg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        final byte[] encrypted = this.encrypt(key, iv, msg.getBytes());
//        return Hex.encodeHexString(encrypted);
//    }
//
//    public byte[] decrypt(final byte[] key, final byte[] iv, final byte[] msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
//        if (key.length != this.KEY_SIZE) {
//            throw new InvalidKeyException("Illegal key size : The size of key must be 32bytes(256bits)");
//        }
//        final Cipher cipher = Cipher.getInstance(this.CIPHER_TRANSFORMATION);
//        final IvParameterSpec ivSpec = new IvParameterSpec(iv);
//        final SecretKeySpec keySpec = new SecretKeySpec(key, this.ALGORITHM);
//        cipher.init(2, keySpec, ivSpec);
//        final byte[] encrypted = cipher.doFinal(msg);
//        return encrypted;
//    }
//
////    public String decrypt(final byte[] key, final byte[] iv, final String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
////        final byte[] decodeMsg = Base64.decodeBase64(msg);
////        return new String(this.decrypt(key, iv, decodeMsg));
////    }
//
//    public void decrypt(final byte[] key, final byte[] iv, final File srcFile, final File destFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
//        final Cipher cipher = Cipher.getInstance(this.CIPHER_TRANSFORMATION);
//        final IvParameterSpec ivSpec = new IvParameterSpec(iv);
//        final SecretKeySpec keySpec = new SecretKeySpec(key, this.ALGORITHM);
//        cipher.init(2, keySpec, ivSpec);
//        try (FileInputStream fis = new FileInputStream(srcFile);
//             FileOutputStream fos = new FileOutputStream(destFile)) {
//            final byte[] in = new byte[this.BUFFER_SIZE];
//            int read;
//            while ((read = fis.read(in)) != -1) {
//                final byte[] output = cipher.update(in, 0, read);
//                if (output != null) {
//                    fos.write(output);
//                }
//            }
//            final byte[] output = cipher.doFinal();
//            if (output != null) {
//                fos.write(output);
//            }
//            fos.flush();
//        } catch (IOException e) {
//        }
//    }
//
//    public void decrypt(final byte[] key, final byte[] iv, final String srcPath, final String destPath) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
//        this.decrypt(key, iv, new File(srcPath), new File(destPath));
//    }
//
//    public String decryptHexString(final byte[] key, final byte[] iv, final String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, DecoderException {
//        final byte[] decodeMsg = Hex.decodeHex(msg.toCharArray());
//        return new String(this.decrypt(key, iv, decodeMsg));
//    }
//
//    public byte[] generateRandomKey() throws NoSuchAlgorithmException {
//        final KeyGenerator keygen = KeyGenerator.getInstance(this.ALGORITHM);
//        keygen.init(new SecureRandom());
//        keygen.init(this.KEY_SIZE * 8);
//        final SecretKey secKey = keygen.generateKey();
//        return secKey.getEncoded();
//    }
//
//    public byte[] generateRandomIV() {
//        final SecureRandom secRand = new SecureRandom();
//        final byte[] iv = new byte[this.IV_SIZE];
//        secRand.nextBytes(iv);
//        return iv;
//    }
//
//    public void setCipherTransformation(final String cipherTrnasformation) {
//        this.CIPHER_TRANSFORMATION = cipherTrnasformation;
//    }
}