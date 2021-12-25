package com.max.fieldcryptor;

import com.max.fieldcryptor.annot.FieldCrypto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class ClassifiedFields {

    private final List<Field> cryptoFields = new ArrayList<>();
    private final List<Field> otherFields = new ArrayList<>();

    public ClassifiedFields(Object obj) {
        Class<?> sourceClass = obj.getClass();
        FieldCrypto fieldCryptoOnType = sourceClass.getAnnotation(FieldCrypto.class);
        boolean isTypeTarget = fieldCryptoOnType != null;

        for (Field field : ReflectionUtils.getFields(obj)) {
            field.setAccessible(true);

            FieldCrypto fieldCryptoOnField = field.getAnnotation(FieldCrypto.class);
            boolean isExcluded = fieldCryptoOnField != null && fieldCryptoOnField.exclude();
            boolean isNotExcluded = fieldCryptoOnField != null && !fieldCryptoOnField.exclude();
            boolean isString = field.getType() == String.class;

            // classify for other fields
            if (isTypeTarget){
                if (isExcluded || !isString) {
                    otherFields.add(field);
                } else {
                    cryptoFields.add(field);
                }
            // classify for crypto fields
            } else {
                if (isNotExcluded && isString) {
                    cryptoFields.add(field);
                } else {
                    otherFields.add(field);
                }
            }
        }
    }

    public static ClassifiedFields from(Object obj) {
        return new ClassifiedFields(obj);
    }

    public List<Field> getCryptoFields() {
        return new ArrayList<>(cryptoFields);
    }

    public List<Field> getOtherFields() {
        return new ArrayList<>(otherFields);
    }


}
