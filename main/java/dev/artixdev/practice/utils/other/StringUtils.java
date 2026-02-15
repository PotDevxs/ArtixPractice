package dev.artixdev.practice.utils.other;

import java.util.Iterator;

/**
 * StringUtils
 * 
 * Utility class for string operations.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class StringUtils {
    
    /**
     * Join an array of objects with a separator string.
     * 
     * @param array the array to join
     * @param separator the separator string
     * @return joined string, or null if array is null
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }
    
    /**
     * Join an array of objects with a separator string, starting from a specific index.
     * 
     * @param array the array to join
     * @param separator the separator string
     * @param startIndex the start index
     * @param endIndex the end index
     * @return joined string
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null || separator == null) {
            return null;
        }
        
        if (startIndex < 0 || endIndex > array.length || startIndex > endIndex) {
            return null;
        }
        
        if (startIndex == endIndex) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                sb.append(separator);
            }
            sb.append(array[i] != null ? array[i].toString() : "");
        }
        
        return sb.toString();
    }
    
    /**
     * Join an iterable collection with a separator string.
     * 
     * @param iterable the iterable to join
     * @param separator the separator string
     * @return joined string, or null if iterable is null
     */
    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }
    
    /**
     * Join an iterable collection with a separator character.
     * 
     * @param iterable the iterable to join
     * @param separator the separator character
     * @return joined string, or null if iterable is null
     */
    public static String join(Iterable<?> iterable, char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }
    
    /**
     * Join an iterator with a separator string.
     * 
     * @param iterator the iterator to join
     * @param separator the separator string
     * @return joined string
     */
    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null || separator == null) {
            return null;
        }
        
        if (!iterator.hasNext()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(iterator.next() != null ? iterator.next().toString() : "");
        
        while (iterator.hasNext()) {
            sb.append(separator);
            Object next = iterator.next();
            sb.append(next != null ? next.toString() : "");
        }
        
        return sb.toString();
    }
    
    /**
     * Join an iterator with a separator character.
     * 
     * @param iterator the iterator to join
     * @param separator the separator character
     * @return joined string
     */
    public static String join(Iterator<?> iterator, char separator) {
        if (iterator == null) {
            return null;
        }
        
        if (!iterator.hasNext()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        Object first = iterator.next();
        sb.append(first != null ? first.toString() : "");
        
        while (iterator.hasNext()) {
            sb.append(separator);
            Object next = iterator.next();
            sb.append(next != null ? next.toString() : "");
        }
        
        return sb.toString();
    }
    
    /**
     * Join variable arguments with a separator string.
     * 
     * @param elements the elements to join
     * @param <T> the element type
     * @return joined string
     */
    @SafeVarargs
    public static <T> String join(T... elements) {
        return join(elements, null);
    }
    
    /**
     * Convert an object to string, returning empty string if null.
     * 
     * @param obj the object to convert
     * @return string representation
     */
    public static String toString(Object obj) {
        return obj != null ? obj.toString() : "";
    }
    
    /**
     * Check if a string contains only letters.
     * 
     * @param str the string to check
     * @return true if string contains only letters
     */
    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }
        
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Hash a string (simple hash function).
     * 
     * @param str the string to hash
     * @return hash value as string
     */
    public static String hash(String str) {
        if (str == null) {
            return "0";
        }
        
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = 31 * hash + str.charAt(i);
        }
        
        return String.valueOf(Math.abs(hash));
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private StringUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
