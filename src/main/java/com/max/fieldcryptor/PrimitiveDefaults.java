package com.max.fieldcryptor;

public class PrimitiveDefaults {
    // These gets initialized to their default values
    private static boolean DEFAULT_BOOLEAN;
    private static byte DEFAULT_BYTE;
    private static short DEFAULT_SHORT;
    private static int DEFAULT_INT;
    private static long DEFAULT_LONG;
    private static float DEFAULT_FLOAT;
    private static double DEFAULT_DOUBLE;

    public static Object getDefaultValue(Class clazz) {
        if (clazz == boolean.class) {
            return DEFAULT_BOOLEAN;
        } else if (clazz == byte.class) {
            return DEFAULT_BYTE;
        } else if (clazz == short.class) {
            return DEFAULT_SHORT;
        } else if (clazz == int.class) {
            return DEFAULT_INT;
        } else if (clazz == long.class) {
            return DEFAULT_LONG;
        } else if (clazz == float.class) {
            return DEFAULT_FLOAT;
        } else if (clazz == double.class) {
            return DEFAULT_DOUBLE;
        } else {
            throw new IllegalArgumentException(
                "Class type " + clazz + " not supported");
        }
    }
}