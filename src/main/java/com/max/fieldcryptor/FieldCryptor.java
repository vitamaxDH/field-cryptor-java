package com.max.fieldcryptor;

import com.max.fieldcryptor.annot.FieldCrypto;
import com.max.fieldcryptor.cipher.AbstractCipher;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.max.fieldcryptor.FieldCryptorUtils.cipherWrapper;

public class FieldCryptor {

    private static final Logger log = LoggerFactory.getLogger(FieldCryptor.class);

    private FieldCryptor() {
    }

    public static <T> T decryptFields(AbstractCipher cipher, T source, Supplier<T> supplier) {
        return taskTemplate(source, supplier.get(), cipherWrapper(cipher::decrypt));
    }

    public static <T> T decryptFields(AbstractCipher cipher, T source, T target) {
        return taskTemplate(source, target, cipherWrapper(cipher::decrypt));
    }

    public static <T> T encryptFields(AbstractCipher cipher, T source, Supplier<T> supplier) {
        return taskTemplate(source, supplier.get(), cipherWrapper(cipher::encrypt));
    }

    public static <T> T encryptFields(AbstractCipher cipher, T source, T target) {
        return taskTemplate(source, target, cipherWrapper(cipher::encrypt));
    }

    public static <T> T taskTemplate(T source, T target, Function<String, String> task) {
        final CryptorReflectionResult reflectionResult = FieldCryptorUtils.getReflectionResult(source);
        final List<Field> stringFields = reflectionResult.stringFields();

        for (Field field : stringFields) {
            FieldCrypto fieldCrypto = field.getAnnotation(FieldCrypto.class);
            if (fieldCrypto == null || fieldCrypto.exclude()) {
                continue;
            }
            final String dataToWork = ReflectionUtils.getFieldValue(source, field.getName());
            if (dataToWork == null) {
                continue;
            }

            try {
                final String data = task.apply(dataToWork);
                field.setAccessible(true);
                ReflectionUtils.map(source, target);
                field.set(target, data);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return target;
    }
}
