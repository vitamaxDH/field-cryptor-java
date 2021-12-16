package com.max.fieldcryptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isFinal;

public class ReflectionUtils {

    private static final Map<Class<?>, List<Field>> fieldsCache = new HashMap<>();
    private static final Map<Class<?>, List<Field>> fieldsCacheForCryptor = new HashMap<>();
    private static final Map<Class<?>, Boolean> hasDefaultConstructorCache = new HashMap<>();
    private static final Map<Class<?>, Object[]> defaultValuesCache = new HashMap<>();

    public static <T> List<Field> getAllFields(T t) {
        Objects.requireNonNull(t);

        Class<?> clazz = t.getClass();
        if (fieldsCache.containsKey(clazz)) {
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

    public static <T> List<Field> getAllFieldsByTypes(T t, Class<?>... classes){
        Class<?> clazz = t.getClass();
        if (fieldsCacheForCryptor.containsKey(clazz)){
            return fieldsCacheForCryptor.get(clazz);
        }
        List<Field> fieldsByTypes = getAllFields(t).stream()
                .filter(field -> anyType(field, classes))
                .collect(Collectors.toList());
        fieldsCacheForCryptor.put(clazz, fieldsByTypes);
        return fieldsByTypes;
    }

    private static boolean anyType(Field field, Class<?>... classes){
        if (classes.length == 0){
            return false;
        }
        for (Class<?> clazz : classes){
            if (field.getType() == clazz){
                return true;
            }
        }
        return false;
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

    public static <T> T map(Object source, T target) {
        final List<Field> sourceFields = getAllFields(source);
        final List<Field> targetFields = getAllFields(target);

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

    public static Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return hasDefaultConstructor(clazz) ? clazz.newInstance() : newInstanceOfNoDefaultConstClass(clazz);
    }

    private static boolean hasDefaultConstructor(Class<?> clazz) {
        if (hasDefaultConstructorCache.containsKey(clazz)) {
            return hasDefaultConstructorCache.get(clazz);
        }
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                hasDefaultConstructorCache.put(clazz, true);
                return true;
            }
        }
        hasDefaultConstructorCache.putIfAbsent(clazz, false);
        return false;
    }

    private static Object newInstanceOfNoDefaultConstClass(Class<?> clazz) {
        final Constructor<?> constructor = clazz.getConstructors()[0];
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Object[] objs = new Object[parameterTypes.length];
        try {
            if (defaultValuesCache.containsKey(clazz)) {
                return constructor.newInstance(defaultValuesCache.get(clazz));
            }
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                objs[i] = parameterType.isPrimitive() ? PrimitiveDefaults.getDefaultValue(parameterType) : newInstance(parameterType);
            }
            defaultValuesCache.putIfAbsent(clazz, objs);
            return constructor.newInstance(objs);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate a new instance due to " + e.getMessage());
        }
    }

    public static boolean isNotFinal(Field field) {
        return !isFinal(field.getModifiers());
    }

}