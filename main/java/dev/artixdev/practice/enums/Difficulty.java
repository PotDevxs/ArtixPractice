package dev.artixdev.practice.enums;

/**
 * Difficulty Enum
 * Defines different difficulty levels for practice
 */
public enum Difficulty {
   EASY(1, 10, "Easy"),
   NORMAL(2, 20, "Normal"),
   HARD(3, 30, "Hard"),
   EXPERT(4, 40, "Expert"),
   MASTER(5, 50, "Master");
   
   private final int level;
   private final int maxLevel;
   private final String displayName;
   
   Difficulty(int level, int maxLevel, String displayName) {
      this.level = level;
      this.maxLevel = maxLevel;
      this.displayName = displayName;
   }
   
   public int getLevel() {
      return level;
   }
   
   public int getMaxLevel() {
      return maxLevel;
   }
   
   public String getDisplayName() {
      return displayName;
   }
   
   /**
    * Check if difficulty is easy
    * @return true if easy
    */
   public boolean isEasy() {
      return this == EASY;
   }
   
   /**
    * Check if difficulty is normal
    * @return true if normal
    */
   public boolean isNormal() {
      return this == NORMAL;
   }
   
   /**
    * Check if difficulty is hard
    * @return true if hard
    */
   public boolean isHard() {
      return this == HARD;
   }
   
   /**
    * Check if difficulty is expert
    * @return true if expert
    */
   public boolean isExpert() {
      return this == EXPERT;
   }
   
   /**
    * Check if difficulty is master
    * @return true if master
    */
   public boolean isMaster() {
      return this == MASTER;
   }
   
   /**
    * Check if difficulty is beginner level
    * @return true if beginner
    */
   public boolean isBeginner() {
      return this == EASY || this == NORMAL;
   }
   
   /**
    * Check if difficulty is advanced level
    * @return true if advanced
    */
   public boolean isAdvanced() {
      return this == HARD || this == EXPERT || this == MASTER;
   }
   
   /**
    * Get difficulty from level
    * @param level the level
    * @return difficulty or null
    */
   public static Difficulty fromLevel(int level) {
      for (Difficulty difficulty : values()) {
         if (difficulty.getLevel() == level) {
            return difficulty;
         }
      }
      return null;
   }
   
   /**
    * Get difficulty from name
    * @param name the name
    * @return difficulty or null
    */
   public static Difficulty fromName(String name) {
      if (name == null) {
         return null;
      }
      
      for (Difficulty difficulty : values()) {
         if (difficulty.getDisplayName().equalsIgnoreCase(name)) {
            return difficulty;
         }
      }
      return null;
   }
   
   /**
    * Get all beginner difficulties
    * @return array of beginner difficulties
    */
   public static Difficulty[] getBeginnerDifficulties() {
      return new Difficulty[]{EASY, NORMAL};
   }
   
   /**
    * Get all advanced difficulties
    * @return array of advanced difficulties
    */
   public static Difficulty[] getAdvancedDifficulties() {
      return new Difficulty[]{HARD, EXPERT, MASTER};
   }
   
   /**
    * Get all difficulties
    * @return array of all difficulties
    */
   public static Difficulty[] getAll() {
      return values();
   }
   
   /**
    * Check if level is valid
    * @param level the level
    * @return true if valid
    */
   public static boolean isValidLevel(int level) {
      return level >= 1 && level <= 5;
   }
   
   /**
    * Get difficulty by index
    * @param index the index
    * @return difficulty or null
    */
   public static Difficulty getByIndex(int index) {
      Difficulty[] difficulties = values();
      if (index >= 0 && index < difficulties.length) {
         return difficulties[index];
      }
      return null;
   }
   
   /**
    * Get difficulty count
    * @return difficulty count
    */
   public static int getCount() {
      return values().length;
   }
   
   /**
    * Get difficulty description
    * @return description
    */
   public String getDescription() {
      switch (this) {
         case EASY:
            return "Easy difficulty for beginners";
         case NORMAL:
            return "Normal difficulty for average players";
         case HARD:
            return "Hard difficulty for experienced players";
         case EXPERT:
            return "Expert difficulty for skilled players";
         case MASTER:
            return "Master difficulty for elite players";
         default:
            return "Unknown difficulty";
      }
   }
   
   /**
    * Get difficulty color
    * @return color code
    */
   public String getColor() {
      switch (this) {
         case EASY:
            return "&a";
         case NORMAL:
            return "&e";
         case HARD:
            return "&c";
         case EXPERT:
            return "&5";
         case MASTER:
            return "&6";
         default:
            return "&f";
      }
   }
   
   /**
    * Get difficulty icon
    * @return icon name
    */
   public String getIcon() {
      switch (this) {
         case EASY:
            return "WOODEN_SWORD";
         case NORMAL:
            return "STONE_SWORD";
         case HARD:
            return "IRON_SWORD";
         case EXPERT:
            return "DIAMOND_SWORD";
         case MASTER:
            return "NETHERITE_SWORD";
         default:
            return "DIAMOND_SWORD";
      }
   }
}
