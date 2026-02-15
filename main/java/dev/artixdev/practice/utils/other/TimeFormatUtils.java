package dev.artixdev.practice.utils.other;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * TimeFormatUtils
 * 
 * Utility class for formatting time values.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class TimeFormatUtils {
    
    private static final String TIME_FORMAT_MINUTES = "%02d:%02d";
    private static final String TIME_FORMAT_HOURS = "%02d:%02d:%02d";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    
    /**
     * Normalize a timestamp to ensure it's within valid range (year <= 2037).
     * 
     * @param timestamp the timestamp to normalize
     * @return the normalized timestamp
     */
    public static Timestamp normalizeTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        
        if (timestamp.toLocalDateTime().getYear() > 2037) {
            timestamp.setYear(2037);
        }
        
        return timestamp;
    }
    
    /**
     * Format milliseconds to a readable string (e.g., "1.5s").
     * 
     * @param milliseconds the milliseconds to format
     * @return formatted string
     */
    public static String formatMilliseconds(long milliseconds) {
        if (milliseconds < 1000) {
            return milliseconds + "ms";
        }
        double seconds = milliseconds / 1000.0;
        return DECIMAL_FORMAT.format(seconds) + "s";
    }
    
    /**
     * Format a long timestamp to a readable string.
     * 
     * @param timestamp the timestamp to format
     * @return formatted string
     */
    public static String formatLong(Long timestamp) {
        if (timestamp == null) {
            return "0s";
        }
        
        long seconds = timestamp / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        seconds %= 60;
        minutes %= 60;
        
        if (hours > 0) {
            return String.format(TIME_FORMAT_HOURS, hours, minutes, seconds);
        } else {
            return String.format(TIME_FORMAT_MINUTES, minutes, seconds);
        }
    }
    
    /**
     * Parse a time string to milliseconds.
     * 
     * @param timeString the time string (e.g., "1h30m", "45s")
     * @return milliseconds, or 0 if parsing fails
     */
    public static long parseTimeString(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return 0;
        }
        
        long total = 0;
        String lower = timeString.toLowerCase().trim();
        
        // Parse hours
        int hIndex = lower.indexOf('h');
        if (hIndex > 0) {
            try {
                total += TimeUnit.HOURS.toMillis(Long.parseLong(lower.substring(0, hIndex)));
                lower = lower.substring(hIndex + 1);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        
        // Parse minutes
        int mIndex = lower.indexOf('m');
        if (mIndex > 0) {
            try {
                total += TimeUnit.MINUTES.toMillis(Long.parseLong(lower.substring(0, mIndex)));
                lower = lower.substring(mIndex + 1);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        
        // Parse seconds
        int sIndex = lower.indexOf('s');
        if (sIndex > 0) {
            try {
                total += TimeUnit.SECONDS.toMillis(Long.parseLong(lower.substring(0, sIndex)));
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        
        return total;
    }
    
    /**
     * Create a timestamp from milliseconds.
     * 
     * @param milliseconds the milliseconds
     * @return the timestamp
     */
    public static Timestamp createTimestamp(long milliseconds) {
        return new Timestamp(milliseconds);
    }
    
    /**
     * Create a timestamp from a duration added to current time.
     * 
     * @param timestamp the base timestamp
     * @return a new timestamp with duration added
     */
    public static Timestamp addDuration(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return normalizeTimestamp(new Timestamp(System.currentTimeMillis() + timestamp.getTime()));
    }
    
    /**
     * Format a date to string.
     * 
     * @param date the date to format
     * @return formatted string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toString();
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private TimeFormatUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
