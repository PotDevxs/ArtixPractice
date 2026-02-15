package dev.artixdev.practice.utils.other;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * RandomEnumUtils
 * 
 * Utility class for getting random values from enums.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class RandomEnumUtils {
    
    /**
     * Get a random value from an enum.
     * 
     * @param enumClass the enum class
     * @param <T> the enum type
     * @return a random enum value
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getRandomValue(Class<T> enumClass) {
        List<T> values = Arrays.asList(enumClass.getEnumConstants());
        Collections.shuffle(values);
        return values.get(0);
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private RandomEnumUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
