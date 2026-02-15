package dev.artixdev.practice.models;

import java.util.UUID;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

/**
 * Kit Statistics
 * Model for tracking kit-specific player statistics
 */
public class KitStatistics {
   
   @SerializedName("_id")
   private UUID kitId;
   
   @SerializedName("rankedWins")
   private int rankedWins;
   
   @SerializedName("winstreak")
   private int winstreak;
   
   @SerializedName("kills")
   private int kills;
   
   @SerializedName("deaths")
   private int deaths;
   
   @SerializedName("tournamentWins")
   private int tournamentWins;
   
   @SerializedName("elo")
   private int elo;
   
   @SerializedName("highestWinstreak")
   private int highestWinstreak;
   
   @SerializedName("wins")
   private int wins;
   
   @SerializedName("rankedWinstreak")
   private int rankedWinstreak;
   
   @SerializedName("rankedHighestWinstreak")
   private int rankedHighestWinstreak;
   
   @SerializedName("losses")
   private int losses;
   
   @SerializedName("rankedLosses")
   private int rankedLosses;
   
   /**
    * Constructor
    */
   public KitStatistics() {
      this.kitId = UUID.randomUUID();
      this.rankedWins = 0;
      this.winstreak = 0;
      this.kills = 0;
      this.deaths = 0;
      this.tournamentWins = 0;
      this.elo = 1000;
      this.highestWinstreak = 0;
      this.wins = 0;
      this.rankedWinstreak = 0;
      this.rankedHighestWinstreak = 0;
      this.losses = 0;
      this.rankedLosses = 0;
   }
   
   /**
    * Constructor with kit ID
    * @param kitId the kit ID
    */
   public KitStatistics(UUID kitId) {
      this();
      this.kitId = kitId;
   }
   
   // Getters and setters
   
   public UUID getKitId() {
      return kitId;
   }
   
   public void setKitId(UUID kitId) {
      this.kitId = kitId;
   }
   
   public int getRankedWins() {
      return rankedWins;
   }
   
   public void setRankedWins(int rankedWins) {
      this.rankedWins = rankedWins;
   }
   
   public void addRankedWin() {
      this.rankedWins++;
   }
   
   public int getWinstreak() {
      return winstreak;
   }
   
   public void setWinstreak(int winstreak) {
      this.winstreak = winstreak;
      if (winstreak > highestWinstreak) {
         highestWinstreak = winstreak;
      }
   }
   
   public void incrementWinstreak() {
      this.winstreak++;
      if (winstreak > highestWinstreak) {
         highestWinstreak = winstreak;
      }
   }
   
   public void resetWinstreak() {
      this.winstreak = 0;
   }
   
   public int getKills() {
      return kills;
   }
   
   public void setKills(int kills) {
      this.kills = kills;
   }
   
   public void addKill() {
      this.kills++;
   }
   
   public int getDeaths() {
      return deaths;
   }
   
   public void setDeaths(int deaths) {
      this.deaths = deaths;
   }
   
   public void addDeath() {
      this.deaths++;
   }
   
   public int getTournamentWins() {
      return tournamentWins;
   }
   
   public void setTournamentWins(int tournamentWins) {
      this.tournamentWins = tournamentWins;
   }
   
   public void addTournamentWin() {
      this.tournamentWins++;
   }
   
   public int getElo() {
      return elo;
   }
   
   public void setElo(int elo) {
      this.elo = elo;
   }
   
   public int getHighestWinstreak() {
      return highestWinstreak;
   }
   
   public void setHighestWinstreak(int highestWinstreak) {
      this.highestWinstreak = highestWinstreak;
   }
   
   public int getWins() {
      return wins;
   }
   
   public void setWins(int wins) {
      this.wins = wins;
   }
   
   public void addWin() {
      this.wins++;
   }
   
   public int getRankedWinstreak() {
      return rankedWinstreak;
   }
   
   public void setRankedWinstreak(int rankedWinstreak) {
      this.rankedWinstreak = rankedWinstreak;
      if (rankedWinstreak > rankedHighestWinstreak) {
         rankedHighestWinstreak = rankedWinstreak;
      }
   }
   
   public void incrementRankedWinstreak() {
      this.rankedWinstreak++;
      if (rankedWinstreak > rankedHighestWinstreak) {
         rankedHighestWinstreak = rankedWinstreak;
      }
   }
   
   public void resetRankedWinstreak() {
      this.rankedWinstreak = 0;
   }
   
   public int getRankedHighestWinstreak() {
      return rankedHighestWinstreak;
   }
   
   public void setRankedHighestWinstreak(int rankedHighestWinstreak) {
      this.rankedHighestWinstreak = rankedHighestWinstreak;
   }
   
   public int getLosses() {
      return losses;
   }
   
   public void setLosses(int losses) {
      this.losses = losses;
   }
   
   public void addLoss() {
      this.losses++;
   }
   
   public int getRankedLosses() {
      return rankedLosses;
   }
   
   public void setRankedLosses(int rankedLosses) {
      this.rankedLosses = rankedLosses;
   }
   
   public void addRankedLoss() {
      this.rankedLosses++;
   }
   
   // Calculated statistics
   
   public double getKDRatio() {
      if (deaths == 0) {
         return kills;
      }
      return (double) kills / deaths;
   }
   
   public double getWLRatio() {
      if (losses == 0) {
         return wins;
      }
      return (double) wins / losses;
   }
   
   public double getRankedWLRatio() {
      if (rankedLosses == 0) {
         return rankedWins;
      }
      return (double) rankedWins / rankedLosses;
   }
   
   public int getTotalMatches() {
      return wins + losses;
   }
   
   public int getTotalRankedMatches() {
      return rankedWins + rankedLosses;
   }
   
   public double getWinPercentage() {
      int total = getTotalMatches();
      if (total == 0) {
         return 0.0;
      }
      return (double) wins / total * 100;
   }
   
   public double getRankedWinPercentage() {
      int total = getTotalRankedMatches();
      if (total == 0) {
         return 0.0;
      }
      return (double) rankedWins / total * 100;
   }
   
   public String getStatisticsSummary() {
      return String.format("Kit Stats - Wins: %d, Losses: %d, K/D: %.2f, ELO: %d, Winstreak: %d", 
         wins, losses, getKDRatio(), elo, winstreak);
   }
   
   @Override
   public String toString() {
      return getStatisticsSummary();
   }
}
