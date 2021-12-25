package com.max.fieldcryptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class StringFields {

    private final List<Field> stringFields;

    public StringFields(Object obj) {
        this.stringFields = getStringFields(obj);
    }

    public static StringFields from(Object obj) {
        return new StringFields(obj);
    }

    public List<Field> stringFields() {
        return new ArrayList<>(stringFields);
    }

    private static <T> List<Field> getStringFields(T t) {
        return ReflectionUtils.getFields(t).stream()
                .filter(field -> ReflectionUtils.anyType(field, String.class))
                .collect(Collectors.toList());
    }

}
