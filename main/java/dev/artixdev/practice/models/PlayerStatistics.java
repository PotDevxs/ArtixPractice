package dev.artixdev.practice.models;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

/**
 * Player Statistics
 * Model for storing player statistics
 */
public final class PlayerStatistics {
   
   private transient boolean isLoaded;
   private final List<MatchHistory> matchHistory;
   private int totalMatches;
   private int totalWins;
   private int totalLosses;
   private int totalKills;
   private int totalDeaths;
   private int currentWinstreak;
   private int highestWinstreak;
   private int currentRankedWinstreak;
   private int highestRankedWinstreak;
   private final Map<String, KitStatistics> kitStatistics;
   @SerializedName("_id")
   private final UUID playerId;
   private int currentElo;
   private int highestElo;
   private int currentRankedElo;
   private int highestRankedElo;
   private long totalPlayTime;
   private long lastLogin;
   private long firstLogin;
   private boolean isOnline;
   private String playerName;
   private int level;
   private int experience;
   private int coins;
   private int gems;
   private int tokens;
   private int prestige;
   private String rank;
   private String prefix;
   private String suffix;
   private List<String> permissions;
   private List<String> achievements;
   private Map<String, Object> customData;
   
   /**
    * Constructor
    * @param playerId the player ID
    */
   public PlayerStatistics(UUID playerId) {
      this.playerId = playerId;
      this.matchHistory = new LinkedList<>();
      this.kitStatistics = new ConcurrentHashMap<>();
      this.permissions = new LinkedList<>();
      this.achievements = new LinkedList<>();
      this.customData = new ConcurrentHashMap<>();
      
      // Initialize default values
      this.totalMatches = 0;
      this.totalWins = 0;
      this.totalLosses = 0;
      this.totalKills = 0;
      this.totalDeaths = 0;
      this.currentWinstreak = 0;
      this.highestWinstreak = 0;
      this.currentRankedWinstreak = 0;
      this.highestRankedWinstreak = 0;
      this.currentElo = 1000;
      this.highestElo = 1000;
      this.currentRankedElo = 1000;
      this.highestRankedElo = 1000;
      this.totalPlayTime = 0;
      this.lastLogin = System.currentTimeMillis();
      this.firstLogin = System.currentTimeMillis();
      this.isOnline = false;
      this.playerName = "Unknown";
      this.level = 1;
      this.experience = 0;
      this.coins = 0;
      this.gems = 0;
      this.tokens = 0;
      this.prestige = 0;
      this.rank = "Default";
      this.prefix = "";
      this.suffix = "";
      this.isLoaded = false;
   }
   
   // Getters and setters
   
   public UUID getPlayerId() {
      return playerId;
   }
   
   public String getPlayerName() {
      return playerName;
   }
   
   public void setPlayerName(String playerName) {
      this.playerName = playerName;
   }
   
   public boolean isLoaded() {
      return isLoaded;
   }
   
   public void setLoaded(boolean loaded) {
      this.isLoaded = loaded;
   }
   
   public int getTotalMatches() {
      return totalMatches;
   }
   
   public void setTotalMatches(int totalMatches) {
      this.totalMatches = totalMatches;
   }
   
   public int getTotalWins() {
      return totalWins;
   }
   
   public void setTotalWins(int totalWins) {
      this.totalWins = totalWins;
   }
   
   public int getTotalLosses() {
      return totalLosses;
   }
   
   public void setTotalLosses(int totalLosses) {
      this.totalLosses = totalLosses;
   }
   
   public int getTotalKills() {
      return totalKills;
   }
   
   public void setTotalKills(int totalKills) {
      this.totalKills = totalKills;
   }
   
   public int getTotalDeaths() {
      return totalDeaths;
   }
   
   public void setTotalDeaths(int totalDeaths) {
      this.totalDeaths = totalDeaths;
   }
   
   public int getCurrentWinstreak() {
      return currentWinstreak;
   }
   
   public void setCurrentWinstreak(int currentWinstreak) {
      this.currentWinstreak = currentWinstreak;
      if (currentWinstreak > highestWinstreak) {
         this.highestWinstreak = currentWinstreak;
      }
   }
   
   public int getHighestWinstreak() {
      return highestWinstreak;
   }
   
   public int getCurrentRankedWinstreak() {
      return currentRankedWinstreak;
   }
   
   public void setCurrentRankedWinstreak(int currentRankedWinstreak) {
      this.currentRankedWinstreak = currentRankedWinstreak;
      if (currentRankedWinstreak > highestRankedWinstreak) {
         this.highestRankedWinstreak = currentRankedWinstreak;
      }
   }
   
   public int getHighestRankedWinstreak() {
      return highestRankedWinstreak;
   }
   
   public int getCurrentElo() {
      return currentElo;
   }
   
   public void setCurrentElo(int currentElo) {
      this.currentElo = currentElo;
      if (currentElo > highestElo) {
         this.highestElo = currentElo;
      }
   }
   
   public int getHighestElo() {
      return highestElo;
   }
   
   public int getCurrentRankedElo() {
      return currentRankedElo;
   }
   
   public void setCurrentRankedElo(int currentRankedElo) {
      this.currentRankedElo = currentRankedElo;
      if (currentRankedElo > highestRankedElo) {
         this.highestRankedElo = currentRankedElo;
      }
   }
   
   public int getHighestRankedElo() {
      return highestRankedElo;
   }
   
   public long getTotalPlayTime() {
      return totalPlayTime;
   }
   
   public void setTotalPlayTime(long totalPlayTime) {
      this.totalPlayTime = totalPlayTime;
   }
   
   public long getLastLogin() {
      return lastLogin;
   }
   
   public void setLastLogin(long lastLogin) {
      this.lastLogin = lastLogin;
   }
   
   public long getFirstLogin() {
      return firstLogin;
   }
   
   public void setFirstLogin(long firstLogin) {
      this.firstLogin = firstLogin;
   }
   
   public boolean isOnline() {
      return isOnline;
   }
   
   public void setOnline(boolean online) {
      this.isOnline = online;
   }
   
   public int getLevel() {
      return level;
   }
   
   public void setLevel(int level) {
      this.level = level;
   }
   
   public int getExperience() {
      return experience;
   }
   
   public void setExperience(int experience) {
      this.experience = experience;
   }
   
   public int getCoins() {
      return coins;
   }
   
   public void setCoins(int coins) {
      this.coins = coins;
   }
   
   public int getGems() {
      return gems;
   }
   
   public void setGems(int gems) {
      this.gems = gems;
   }
   
   public int getTokens() {
      return tokens;
   }
   
   public void setTokens(int tokens) {
      this.tokens = tokens;
   }
   
   public int getPrestige() {
      return prestige;
   }
   
   public void setPrestige(int prestige) {
      this.prestige = prestige;
   }
   
   public String getRank() {
      return rank;
   }
   
   public void setRank(String rank) {
      this.rank = rank;
   }
   
   public String getPrefix() {
      return prefix;
   }
   
   public void setPrefix(String prefix) {
      this.prefix = prefix;
   }
   
   public String getSuffix() {
      return suffix;
   }
   
   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }
   
   public List<String> getPermissions() {
      return permissions;
   }
   
   public void setPermissions(List<String> permissions) {
      this.permissions = permissions;
   }
   
   public List<String> getAchievements() {
      return achievements;
   }
   
   public void setAchievements(List<String> achievements) {
      this.achievements = achievements;
   }
   
   public Map<String, Object> getCustomData() {
      return customData;
   }
   
   public void setCustomData(Map<String, Object> customData) {
      this.customData = customData;
   }
   
   public List<MatchHistory> getMatchHistory() {
      return matchHistory;
   }
   
   public Map<String, KitStatistics> getKitStatistics() {
      return kitStatistics;
   }
   
   // Calculated methods
   
   public double getWinRate() {
      if (totalMatches == 0) {
         return 0.0;
      }
      return (double) totalWins / totalMatches * 100.0;
   }
   
   public double getKillDeathRatio() {
      if (totalDeaths == 0) {
         return totalKills;
      }
      return (double) totalKills / totalDeaths;
   }
   
   public int getWinLossDifference() {
      return totalWins - totalLosses;
   }
   
   public String getFormattedPlayTime() {
      long hours = totalPlayTime / 3600000;
      long minutes = (totalPlayTime % 3600000) / 60000;
      long seconds = (totalPlayTime % 60000) / 1000;
      
      if (hours > 0) {
         return String.format("%dh %dm %ds", hours, minutes, seconds);
      } else if (minutes > 0) {
         return String.format("%dm %ds", minutes, seconds);
      } else {
         return String.format("%ds", seconds);
      }
   }
   
   public String getFormattedLastLogin() {
      long timeSince = System.currentTimeMillis() - lastLogin;
      long days = timeSince / 86400000;
      long hours = (timeSince % 86400000) / 3600000;
      long minutes = (timeSince % 3600000) / 60000;
      
      if (days > 0) {
         return String.format("%dd %dh ago", days, hours);
      } else if (hours > 0) {
         return String.format("%dh %dm ago", hours, minutes);
      } else if (minutes > 0) {
         return String.format("%dm ago", minutes);
      } else {
         return "Just now";
      }
   }
   
   public String getStatisticsSummary() {
      return String.format("Stats for %s: %d matches, %.1f%% win rate, %.2f K/D, %d ELO", 
         playerName, totalMatches, getWinRate(), getKillDeathRatio(), currentElo);
   }
   
   @Override
   public String toString() {
      return getStatisticsSummary();
   }
}
