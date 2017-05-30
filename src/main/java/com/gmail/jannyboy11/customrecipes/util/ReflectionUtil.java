package com.gmail.jannyboy11.customrecipes.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public final class ReflectionUtil {
	
	private ReflectionUtil() {}
	
	public static Field getDeclaredField(Object object, String fieldName) {
        return getDeclaredFieldRecursively(object.getClass(), fieldName);
    }

    public static Field getDeclaredFieldRecursively(Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException e) {
            return getDeclaredFieldRecursively(clazz.getSuperclass(), fieldName);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getDeclaredFieldValue(Object object, String fieldName) {
        Field field = ReflectionUtil.getDeclaredField(object, fieldName);
        try {
            return field.get(object);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setDeclaredFieldValue(Object object, String fieldName, Object value) {
        Field field = ReflectionUtil.getDeclaredField(object, fieldName);
        try {
            field.set(object, value);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static Method getDeclaredMethod(Object object, String methodName, Class<?> ... argTypes) {
        return getDeclaredMethodRecursively(object.getClass(), methodName, argTypes);
    }

    public static Method getDeclaredMethodRecursively(Class<?> clazz, String methodName, Class<?> ... argTypes) {
        if (clazz == null) {
            return null;
        }
        try {
            Method method = clazz.getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            return method;
        }
        catch (NoSuchMethodException e) {
            return getDeclaredMethodRecursively(clazz.getSuperclass(), methodName, argTypes);
        }
        catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Object object, String methodName, Object ... args) {
        Method method = getDeclaredMethod(object, methodName, (Class[]) Arrays.stream(args).map(Object::getClass).toArray());
        try {
            return method.invoke(object, args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
