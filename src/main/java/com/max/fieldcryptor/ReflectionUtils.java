package com.max.fieldcryptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.reflect.Modifier.isFinal;

public class ReflectionUtils {

    public static <T> List<Field> getFields(T t) {
        Objects.requireNonNull(t);

        Class<?> clazz = t.getClass();

        final List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static <T> Field getFieldByName(T t, String fieldName) {
        Objects.requireNonNull(t);

        return getFields(t).stream()
                .filter(f -> f.getName().equals(fieldName))
                .peek(f -> f.setAccessible(true))
                .findFirst()
                .orElse(null);
    }

    public static <T> T getFieldValue(Object obj, String fieldName) {
        Objects.requireNonNull(obj);

        try {
            Field field = getFieldByName(obj, fieldName);// 4. 해당 필드 조회 후
            T result = (T) field.get(obj);
            return result;    // 5. get 을 이용하여 field value 획득
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T map(Object source, T target) {
        final List<Field> sourceFields = getFields(source);
        final List<Field> targetFields =
                source.getClass() == target.getClass() ?
                        new ArrayList<>(sourceFields) :
                        getFields(target);

        sourceFields.forEach(sourceField ->
                targetFields.stream()
                        .filter(ReflectionUtils::isNotFinal)
                        .filter(targetField -> targetField.getName().equals(sourceField.getName()))
                        .forEach(targetField -> {
                            final Object sourceFieldValue = getFieldValue(source, sourceField.getName());
                            try {
                                targetField.setAccessible(true);
                                targetField.set(target, sourceFieldValue);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        })
        );
        return target;
    }

    public static boolean isNotFinal(Field field) {
        return !isFinal(field.getModifiers());
    }

    public static <T> void map(Field field, T source, T target) throws IllegalAccessException {
        Object fieldValue = field.get(source);
        field.set(target, fieldValue);
    }
}