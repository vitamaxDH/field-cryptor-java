package com.max.fieldcryptor;

import java.lang.reflect.Field;
import java.util.*;

import static java.lang.reflect.Modifier.isFinal;

public class ReflectionUtils {

    private static final Map<Class<?>, List<Field>> fieldsCache = new HashMap<>();

    public static <T> List<Field> getAllFields(T t) {
        Objects.requireNonNull(t);

        Class<?> clazz = t.getClass();
        if (fieldsCache.containsKey(clazz)){
            return fieldsCache.get(clazz);
        }

        final List<Field> fields = new ArrayList<>();
        while (clazz != null) {    // 1. 상위 클래스가 null 이 아닐때까지 모든 필드를 list 에 담는다.
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        fieldsCache.put(clazz, fields);
        return fields;
    }

    public static <T> Field getFieldByName(T t, String fieldName) {
        Objects.requireNonNull(t);

        Field field = null;
        for (Field f : getAllFields(t)) {
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

    public static boolean isNotFinal(Field field) {
        return !isFinal(field.getModifiers());
    }

}