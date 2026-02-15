package dev.artixdev.practice.utils.other;

/**
 * ExpirableData
 * 
 * Represents data that expires after a certain amount of time.
 * 
 * @author RefineDev
 * @since 1.0
 */
public class ExpirableData {
    
    private final long expirationTime;
    private final long creationTime;
    private boolean expired;
    
    /**
     * Create a new ExpirableData with a duration in milliseconds.
     * 
     * @param durationMillis the duration in milliseconds until expiration
     */
    public ExpirableData(long durationMillis) {
        this.creationTime = System.currentTimeMillis();
        this.expirationTime = this.creationTime + durationMillis;
        this.expired = (durationMillis == 0);
    }
    
    /**
     * Get the expiration time in milliseconds.
     * 
     * @return the expiration time
     */
    public long getExpirationTime() {
        return expirationTime;
    }
    
    /**
     * Get the creation time in milliseconds.
     * 
     * @return the creation time
     */
    public long getCreationTime() {
        return creationTime;
    }
    
    /**
     * Check if this data has expired.
     * 
     * @return true if expired
     */
    public boolean hasExpired() {
        if (expired) {
            return true;
        }
        expired = System.currentTimeMillis() >= expirationTime;
        return expired;
    }
    
    /**
     * Get the remaining time until expiration in milliseconds.
     * 
     * @return the remaining time, or 0 if expired
     */
    public long getRemainingTime() {
        long remaining = expirationTime - System.currentTimeMillis();
        return remaining > 0 ? remaining : 0;
    }
    
    /**
     * Get the elapsed time since creation in milliseconds.
     * 
     * @return the elapsed time
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - creationTime;
    }
    
    /**
     * Set the expired flag.
     * 
     * @param expired true to mark as expired
     */
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
    
    /**
     * Check if this data is currently expired (without updating the flag).
     * 
     * @return true if expired
     */
    public boolean isExpired() {
        return expired;
    }
    
    /**
     * Get a formatted string representation of the remaining time.
     * 
     * @return formatted time string
     */
    public String getFormattedRemainingTime() {
        long remaining = getRemainingTime();
        if (remaining < 60000) {
            return TimeFormatUtils.formatMilliseconds(remaining);
        } else {
            return TimeFormatUtils.formatLong(remaining);
        }
    }
    
    @Override
    public String toString() {
        return "ExpirableData{expirationTime=" + expirationTime + 
               ", creationTime=" + creationTime + 
               ", expired=" + expired + "}";
    }
    
    @Override
    public int hashCode() {
        int result = (int) (expirationTime ^ (expirationTime >>> 32));
        result = 31 * result + (int) (creationTime ^ (creationTime >>> 32));
        result = 31 * result + (expired ? 1 : 0);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ExpirableData that = (ExpirableData) obj;
        return expirationTime == that.expirationTime &&
               creationTime == that.creationTime &&
               expired == that.expired;
    }
    
    protected boolean canEqual(Object other) {
        return other instanceof ExpirableData;
    }
}
