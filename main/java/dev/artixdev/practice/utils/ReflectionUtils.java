package dev.artixdev.practice.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ReflectionUtils {
    
    private static final Logger logger = LogManager.getLogger(ReflectionUtils.class);
    private static Field modifiersField;
    private static Field lookupField;

    static {
        try {
            // For Java 8 and below
            if (getJavaVersion() <= 8) {
                modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
            } else {
                // For Java 9 and above
                lookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                lookupField.setAccessible(true);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize reflection fields", e);
        }
    }

    private ReflectionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Set a field value using reflection
     */
    public static void setFieldValue(Object object, Field field, Object value) {
        if (object == null || field == null) {
            return;
        }
        
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            logger.error("Failed to set field value", e);
        }
    }

    /**
     * Get a field value using reflection
     */
    public static Object getFieldValue(Field field, Object object) {
        if (field == null) {
            return null;
        }
        
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            logger.error("Failed to get field value", e);
            return null;
        }
    }

    /**
     * Make a field accessible and remove final modifier if needed
     */
    public static void makeFieldAccessible(Field field) {
        if (field == null) {
            return;
        }
        
        field.setAccessible(true);
        
        try {
            int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers)) {
                if (getJavaVersion() <= 8) {
                    // For Java 8 and below
                    if (modifiersField != null) {
                        int newModifiers = (~modifiers | -17) - ~modifiers;
                        modifiersField.setInt(field, newModifiers);
                    }
                } else {
                    // For Java 9 and above
                    if (lookupField != null) {
                        try {
                            MethodHandles.Lookup lookup = (MethodHandles.Lookup) lookupField.get(null);
                            MethodHandle setter = lookup.findSetter(Field.class, "modifiers", int.class);
                            int newModifiers = (~modifiers | -17) - ~modifiers;
                            setter.invokeExact(field, newModifiers);
                        } catch (Throwable e) {
                            logger.error("Failed to remove final modifier", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to make field accessible", e);
        }
    }

    /**
     * Get the Java version
     */
    private static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }

    /**
     * Get a field from a class
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            logger.error("Field not found: " + fieldName, e);
            return null;
        }
    }

    /**
     * Get a field from a class (including inherited fields)
     */
    public static Field getFieldIncludingInherited(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        logger.error("Field not found in class hierarchy: " + fieldName);
        return null;
    }

    /**
     * Get all fields from a class
     */
    public static Field[] getFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }

    /**
     * Check if a field exists
     */
    public static boolean hasField(Class<?> clazz, String fieldName) {
        try {
            clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    /**
     * Get a method from a class
     */
    public static java.lang.reflect.Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            logger.error("Method not found: " + methodName, e);
            return null;
        }
    }

    /**
     * Invoke a method using reflection
     */
    public static Object invokeMethod(Object object, java.lang.reflect.Method method, Object... args) {
        if (method == null) {
            return null;
        }
        
        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            logger.error("Failed to invoke method", e);
            return null;
        }
    }

    /**
     * Create a new instance of a class
     */
    public static <T> T newInstance(Class<T> clazz, Object... args) {
        try {
            Class<?>[] parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
            
            java.lang.reflect.Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            logger.error("Failed to create new instance", e);
            return null;
        }
    }

    /**
     * Check if a class exists
     */
    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Get a class by name
     */
    public static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("Class not found: " + className, e);
            return null;
        }
    }
}