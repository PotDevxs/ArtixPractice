package dev.artixdev.practice.models;

import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

/**
 * Bot Settings Model
 * Represents configuration settings for practice bots
 */
public class BotSettings {
   
   @SerializedName("wTap")
   private boolean wTap = true;
   
   @SerializedName("range")
   private double range = 3.0D;
   
   @SerializedName("tryHard")
   private boolean tryHard = false;
   
   @SerializedName("ping")
   private int ping = 0;
   
   @SerializedName("cps")
   private int cps = 10;
   
   @SerializedName("sidePearl")
   private boolean sidePearl = false;
   
   @SerializedName("blockHit")
   private boolean blockHit = false;
   
   @SerializedName("strafe")
   private boolean strafe = false;
   
   /**
    * Check if w-tap is enabled
    * @return true if enabled
    */
   public boolean isWTap() {
      return wTap;
   }
   
   /**
    * Set w-tap status
    * @param wTap the w-tap status
    */
   public void setWTap(boolean wTap) {
      this.wTap = wTap;
   }
   
   /**
    * Get range
    * @return range
    */
   public double getRange() {
      return range;
   }
   
   /**
    * Set range
    * @param range the range
    */
   public void setRange(double range) {
      this.range = range;
   }
   
   /**
    * Check if try hard mode is enabled
    * @return true if enabled
    */
   public boolean isTryHard() {
      return tryHard;
   }
   
   /**
    * Set try hard mode
    * @param tryHard the try hard status
    */
   public void setTryHard(boolean tryHard) {
      this.tryHard = tryHard;
   }
   
   /**
    * Get ping
    * @return ping
    */
   public int getPing() {
      return ping;
   }
   
   /**
    * Set ping
    * @param ping the ping
    */
   public void setPing(int ping) {
      this.ping = ping;
   }
   
   /**
    * Get CPS (Clicks Per Second)
    * @return CPS
    */
   public int getCps() {
      return cps;
   }
   
   /**
    * Set CPS
    * @param cps the CPS
    */
   public void setCps(int cps) {
      this.cps = cps;
   }
   
   /**
    * Check if side pearl is enabled
    * @return true if enabled
    */
   public boolean isSidePearl() {
      return sidePearl;
   }
   
   /**
    * Set side pearl status
    * @param sidePearl the side pearl status
    */
   public void setSidePearl(boolean sidePearl) {
      this.sidePearl = sidePearl;
   }
   
   /**
    * Check if block hit is enabled
    * @return true if enabled
    */
   public boolean isBlockHit() {
      return blockHit;
   }
   
   /**
    * Set block hit status
    * @param blockHit the block hit status
    */
   public void setBlockHit(boolean blockHit) {
      this.blockHit = blockHit;
   }
   
   /**
    * Check if strafe is enabled
    * @return true if enabled
    */
   public boolean isStrafe() {
      return strafe;
   }
   
   /**
    * Set strafe status
    * @param strafe the strafe status
    */
   public void setStrafe(boolean strafe) {
      this.strafe = strafe;
   }
   
   /**
    * Get difficulty level based on settings
    * @return difficulty level (1-5)
    */
   public int getDifficultyLevel() {
      int difficulty = 1;
      
      if (tryHard) difficulty += 2;
      if (wTap) difficulty += 1;
      if (strafe) difficulty += 1;
      if (blockHit) difficulty += 1;
      if (sidePearl) difficulty += 1;
      
      return Math.min(difficulty, 5);
   }
   
   /**
    * Get difficulty name
    * @return difficulty name
    */
   public String getDifficultyName() {
      int level = getDifficultyLevel();
      
      switch (level) {
         case 1:
            return "Easy";
         case 2:
            return "Normal";
         case 3:
            return "Hard";
         case 4:
            return "Expert";
         case 5:
            return "Master";
         default:
            return "Unknown";
      }
   }
   
   /**
    * Check if settings are valid
    * @return true if valid
    */
   public boolean isValid() {
      return range > 0 && range <= 10 && 
             ping >= 0 && ping <= 1000 && 
             cps > 0 && cps <= 20;
   }
   
   /**
    * Reset to default settings
    */
   public void resetToDefaults() {
      this.wTap = true;
      this.range = 3.0D;
      this.tryHard = false;
      this.ping = 0;
      this.cps = 10;
      this.sidePearl = false;
      this.blockHit = false;
      this.strafe = false;
   }
   
   /**
    * Clone settings
    * @return cloned settings
    */
   public BotSettings clone() {
      BotSettings clone = new BotSettings();
      clone.setWTap(this.wTap);
      clone.setRange(this.range);
      clone.setTryHard(this.tryHard);
      clone.setPing(this.ping);
      clone.setCps(this.cps);
      clone.setSidePearl(this.sidePearl);
      clone.setBlockHit(this.blockHit);
      clone.setStrafe(this.strafe);
      return clone;
   }
   
   @Override
   public String toString() {
      return String.format("BotSettings{wTap=%s, range=%.1f, tryHard=%s, ping=%d, cps=%d, sidePearl=%s, blockHit=%s, strafe=%s}", 
         wTap, range, tryHard, ping, cps, sidePearl, blockHit, strafe);
   }
}
