package com.max.fieldcryptor;

import com.max.fieldcryptor.annot.FieldCrypto;
import com.max.fieldcryptor.cipher.AbstractCipher;
import com.max.fieldcryptor.lang.FunctionWithException;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FieldCryptor {

    private static final Logger log = LoggerFactory.getLogger(FieldCryptor.class);

    private final AbstractCipher cipher;

    private FieldCryptor(AbstractCipher cipher) {
        this.cipher = cipher;
    }

    public static FieldCryptor from(AbstractCipher cipher) {
        return new FieldCryptor(cipher);
    }

    public <T> T encrypt(T source, Supplier<T> supplier) {
        return encrypt(source, supplier.get());
    }

    public <T> T encrypt(T source, T target) {
        return taskTemplate(source, target, cipherWrapper(cipher::encrypt));
    }

    public <T> T decrypt(T source, Supplier<T> supplier) {
        return decrypt(source, supplier.get());
    }

    public <T> T decrypt(T source, T target) {
        return taskTemplate(source, target, cipherWrapper(cipher::decrypt));
    }

    public <T, R, E extends Exception> Function<T, R> cipherWrapper(FunctionWithException<T, R, E> fe) {
        return data -> {
            try {
                return fe.apply(data);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Exception occurred.");
            }
        };
    }

    public static <T> T taskTemplate(T source, T target, Function<String, String> task) {
        final StringFields reflectionResult = FieldCryptorUtils.getStringFields(source);
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
