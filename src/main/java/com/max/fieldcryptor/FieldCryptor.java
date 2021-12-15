package com.max.fieldcryptor;

import com.max.fieldcryptor.annot.FieldCrypto;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

import static com.max.fieldcryptor.FieldCryptoUtil.cipherWrapper;

public class FieldCryptor {

    private static final Logger log = LoggerFactory.getLogger(FieldCryptor.class);

    private FieldCryptor(){}

    public static AESCipher init(String key) {
        return CipherFactory.getCipher(key);
    }

    public static <T> T decryptFields(AESCipher cipher, T obj) {
        return taskTemplate(obj, cipherWrapper(cipher::decrypt));
    }

    public static <T> T encryptFields(AESCipher cipher, T obj) {
        return taskTemplate(obj, cipherWrapper(cipher::encrypt));
    }

    public static <T> T taskTemplate(T obj, Function<String, String> task) {
        final List<Field> allFields = ReflectionUtils.getAllFields(obj);

        T newObj = null;
        for (Field field : allFields) {
            if (field.getType() != String.class) {
                continue;
            }
            FieldCrypto fieldCrypto = field.getAnnotation(FieldCrypto.class);
            if (fieldCrypto == null || fieldCrypto.exclude()) {
                continue;
            }
            final String dataToWork = ReflectionUtils.getFieldValue(obj, field.getName());
            if (dataToWork == null) {
                continue;
            }

            try {
                final String data = task.apply(dataToWork);
                field.setAccessible(true);
                newObj = (T) ReflectionUtils.newInstance(obj.getClass());
                ReflectionUtils.map(obj, newObj);
                field.set(newObj, data);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("need default constructor");
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        return newObj;
    }
}
