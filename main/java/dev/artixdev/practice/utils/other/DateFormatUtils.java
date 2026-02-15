package dev.artixdev.practice.utils.other;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateFormatUtils
 * 
 * Utility class for formatting dates.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class DateFormatUtils {
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    
    /**
     * Format a date to a string.
     * 
     * @param date the date to format
     * @return the formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMAT.format(date);
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private DateFormatUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
