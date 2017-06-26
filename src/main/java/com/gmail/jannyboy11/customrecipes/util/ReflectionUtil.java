package com.gmail.jannyboy11.customrecipes.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
        Field field = getDeclaredField(object, fieldName);
        try {
            return field.get(object);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setDeclaredFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);
        try {
            field.set(object, value);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    
    public static void setFinalFieldValue(Object object, String fieldName, Object value) {
    	Field field = getDeclaredField(object, fieldName);
        try {
        	Field modifiersField = Field.class.getDeclaredField("modifiers");
        	modifiersField.setAccessible(true);
        	modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        	
            field.set(object, value);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    
    public static void setStaticFinalFieldValue(Class<?> clazz, String fieldName, Object value) {
   		Field field = getDeclaredFieldRecursively(clazz, fieldName);
    	try {
    		Field modifiersField = Field.class.getDeclaredField("modifiers");
    		modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(null, value);
		} catch (Exception e) {
			e.printStackTrace();
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

    public static Object invokeInstanceMethod(Object instance, String methodName, Object ... args) {
    	Class<?>[] argTypes = new Class[args.length];
    	for (int i = 0; i < args.length; i++) {
    		argTypes[i] = args[i].getClass();
    	}
    	
        Method method = getDeclaredMethod(instance, methodName, argTypes);
        try {
            return method.invoke(instance, args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object ... args) {
    	Class<?>[] argTypes = new Class[args.length];
    	for (int i = 0; i < args.length; i++) {
    		argTypes[i] = args[i].getClass();
    	}
    	
		try {
			Method method = getDeclaredMethodRecursively(clazz, methodName, argTypes);
			return method.invoke(null, args);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

}
