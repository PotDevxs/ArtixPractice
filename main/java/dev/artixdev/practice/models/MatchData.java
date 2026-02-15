package dev.artixdev.practice.models;

import java.util.UUID;
import dev.artixdev.practice.configs.SettingsConfig;

/**
 * Match Data
 * Model for storing match-related data
 */
public class MatchData {
   
   private int matchId;
   private final UUID playerId;
   private final UUID opponentId;
   private int playerKills;
   private int playerDeaths;
   private int opponentKills;
   private int opponentDeaths;
   private final long startTime;
   private boolean isRanked;
   private int playerElo;
   private int opponentElo;
   private boolean playerWon;
   
   /**
    * Constructor
    * @param playerId the player ID
    * @param opponentId the opponent ID
    */
   public MatchData(UUID playerId, UUID opponentId) {
      this.playerId = playerId;
      this.opponentId = opponentId;
      this.startTime = System.currentTimeMillis();
      this.playerKills = 0;
      this.playerDeaths = 0;
      this.opponentKills = 0;
      this.opponentDeaths = 0;
      this.isRanked = false;
      this.playerElo = 1000;
      this.opponentElo = 1000;
      this.playerWon = false;
   }
   
   // Getters and setters
   
   public int getMatchId() {
      return matchId;
   }
   
   public void setMatchId(int matchId) {
      this.matchId = matchId;
   }
   
   public UUID getPlayerId() {
      return playerId;
   }
   
   public UUID getOpponentId() {
      return opponentId;
   }
   
   public int getPlayerKills() {
      return playerKills;
   }
   
   public void setPlayerKills(int playerKills) {
      this.playerKills = playerKills;
   }
   
   public void addPlayerKill() {
      this.playerKills++;
   }
   
   public int getPlayerDeaths() {
      return playerDeaths;
   }
   
   public void setPlayerDeaths(int playerDeaths) {
      this.playerDeaths = playerDeaths;
   }
   
   public void addPlayerDeath() {
      this.playerDeaths++;
   }
   
   public int getOpponentKills() {
      return opponentKills;
   }
   
   public void setOpponentKills(int opponentKills) {
      this.opponentKills = opponentKills;
   }
   
   public void addOpponentKill() {
      this.opponentKills++;
   }
   
   public int getOpponentDeaths() {
      return opponentDeaths;
   }
   
   public void setOpponentDeaths(int opponentDeaths) {
      this.opponentDeaths = opponentDeaths;
   }
   
   public void addOpponentDeath() {
      this.opponentDeaths++;
   }
   
   public long getStartTime() {
      return startTime;
   }
   
   public long getDuration() {
      return System.currentTimeMillis() - startTime;
   }
   
   public boolean isRanked() {
      return isRanked;
   }
   
   public void setRanked(boolean ranked) {
      this.isRanked = ranked;
   }
   
   public int getPlayerElo() {
      return playerElo;
   }
   
   public void setPlayerElo(int playerElo) {
      this.playerElo = playerElo;
   }
   
   public int getOpponentElo() {
      return opponentElo;
   }
   
   public void setOpponentElo(int opponentElo) {
      this.opponentElo = opponentElo;
   }
   
   public boolean isPlayerWon() {
      return playerWon;
   }
   
   public void setPlayerWon(boolean playerWon) {
      this.playerWon = playerWon;
   }
   
   // Calculated methods
   
   public double getPlayerKDRatio() {
      if (playerDeaths == 0) {
         return playerKills;
      }
      return (double) playerKills / playerDeaths;
   }
   
   public double getOpponentKDRatio() {
      if (opponentDeaths == 0) {
         return opponentKills;
      }
      return (double) opponentKills / opponentDeaths;
   }
   
   public int getTotalKills() {
      return playerKills + opponentKills;
   }
   
   public int getTotalDeaths() {
      return playerDeaths + opponentDeaths;
   }
   
   public String getDurationString() {
      long duration = getDuration();
      long seconds = duration / 1000;
      long minutes = seconds / 60;
      seconds = seconds % 60;
      
      if (minutes > 0) {
         return String.format("%dm %ds", minutes, seconds);
      } else {
         return String.format("%ds", seconds);
      }
   }
   
   public String getMatchSummary() {
      return String.format("Match #%d: %s vs %s, Duration: %s, Winner: %s", 
         matchId, 
         playerId.toString().substring(0, 8), 
         opponentId.toString().substring(0, 8),
         getDurationString(),
         playerWon ? "Player" : "Opponent");
   }
   
   public String getDetailedSummary() {
      return String.format("Match #%d\n" +
         "Player: %s (K:%d D:%d K/D:%.2f ELO:%d)\n" +
         "Opponent: %s (K:%d D:%d K/D:%.2f ELO:%d)\n" +
         "Duration: %s\n" +
         "Ranked: %s\n" +
         "Winner: %s",
         matchId,
         playerId.toString().substring(0, 8), playerKills, playerDeaths, getPlayerKDRatio(), playerElo,
         opponentId.toString().substring(0, 8), opponentKills, opponentDeaths, getOpponentKDRatio(), opponentElo,
         getDurationString(),
         isRanked ? "Yes" : "No",
         playerWon ? "Player" : "Opponent");
   }
   
   @Override
   public String toString() {
      return getMatchSummary();
   }
}
