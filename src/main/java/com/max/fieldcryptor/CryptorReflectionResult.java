package com.max.fieldcryptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class CryptorReflectionResult {

    private final List<Field> stringFields;

    public CryptorReflectionResult(Object obj) {
        this.stringFields = FieldCryptorUtils.getStringFields(obj);
    }

    public static CryptorReflectionResult from(Object obj) {
        return new CryptorReflectionResult(obj);
    }

    public List<Field> stringFields() {
        return new ArrayList<>(stringFields);
    }

}
