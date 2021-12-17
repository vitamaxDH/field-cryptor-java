package com.max.fieldcryptor;

import java.lang.reflect.Field;
import java.util.*;

import static java.lang.reflect.Modifier.isFinal;

public class ReflectionUtils {

    // TODO : Needs an object for caching
    private static final Map<Class<?>, Boolean> hasDefaultConstructorCache = new HashMap<>();

    public static <T> List<Field> getFields(T t) {
        Objects.requireNonNull(t);

        Class<?> clazz = t.getClass();

        final List<Field> fields = new ArrayList<>();
        while (clazz != null) {    // 1. 상위 클래스가 null 이 아닐때까지 모든 필드를 list 에 담는다.
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
//        fieldsCache.put(clazz, fields);
        return fields;
    }

    public static boolean anyType(Field field, Class<?>... classes) {
        if (classes.length == 0) {
            return false;
        }
        for (Class<?> clazz : classes) {
            if (field.getType() == clazz) {
                return true;
            }
        }
        return false;
    }

    public static <T> Field getFieldByName(T t, String fieldName) {
        Objects.requireNonNull(t);

        Field field = null;
        for (Field f : getFields(t)) {
            if (f.getName().equals(fieldName)) {
                field = f;    // 2. 모든 필드들로부터 fieldName이 일치하는 필드 추출
                break;
            }
        }
        if (field != null) {
            field.setAccessible(true);    // 3. 접근 제어자가 private 일 경우
        }

        return field;
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

}