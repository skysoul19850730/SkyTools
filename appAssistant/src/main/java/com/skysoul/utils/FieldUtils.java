package com.skysoul.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by liuxiang on 2020/3/25
 */
public class FieldUtils {

    public static Field getDeclaredField(final Class<?> cls, final String fieldName) {
        try {
            // only consider the specified class by using getDeclaredField()
            final Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (final NoSuchFieldException e) { // NOPMD
            // ignore
        }
        return null;
    }

    public static boolean setFieldValue(final Object obj, final Field field, final Object newFieldValue) {
        try {
            int accessFlags = field.getModifiers();
            if (Modifier.isFinal(accessFlags)) {
                Field modifiersField = Field.class.getDeclaredField("accessFlags");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(obj, newFieldValue);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
