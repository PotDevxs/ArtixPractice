package dev.artixdev.practice.models;

import java.util.Collection;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;
import dev.artixdev.practice.enums.PlayerState;
import dev.artixdev.practice.enums.MatchType;
import dev.artixdev.practice.enums.MatchStatus;
import dev.artixdev.practice.enums.KitType;

/**
 * Player Profile
 * Model for player profile data
 */
public final class PlayerProfile {
   
   @SerializedName("_id")
   private UUID uniqueId;
   
   @SerializedName("name")
   private String name;
   
   @SerializedName("state")
   private PlayerState state;
   
   @SerializedName("elo")
   private int elo;

   @SerializedName("level")
   private int level;
   
   @SerializedName("previousElo")
   private int previousElo;
   
   @SerializedName("kills")
   private int kills;

   @SerializedName("levelProgress")
   private int levelProgress;
   
   @SerializedName("experience")
   private int experience;

   @SerializedName("deaths")
   private int deaths;
   
   @SerializedName("wins")
   private int wins;

   @SerializedName("winsToNextLevel")
   private int winsToNextLevel;

   @SerializedName("winrate")
   private int winrate;
   
   @SerializedName("losses")
   private int losses;
   
   @SerializedName("winStreak")
   private int winStreak;
   
   @SerializedName("bestWinStreak")
   private int bestWinStreak;
   
   @SerializedName("playTime")
   private long playTime;
   
   @SerializedName("lastSeen")
   private long lastSeen;
   
   @SerializedName("peakElo")
   private int peakElo;
   
   @SerializedName("botProfile")
   private BotSettings botSettings;
   
   @SerializedName("cosmeticsProfile")
   private CosmeticSettings cosmeticSettings;
   
   @SerializedName("settings")
   private PlayerSettings playerSettings;
   
   @SerializedName("selectedKit")
   private KitType selectedKit;

   @SerializedName("unlockedAchievements")
   private java.util.Set<String> unlockedAchievements;

   @SerializedName("lastLoginDay")
   private long lastLoginDay;

   @SerializedName("loginStreak")
   private int loginStreak;

   @SerializedName("lastWeeklyClaim")
   private long lastWeeklyClaim;

   @SerializedName("selectedTitleId")
   private String selectedTitleId;

   @SerializedName("unlockedTitleIds")
   private java.util.Set<String> unlockedTitleIds;

   @SerializedName("ffaKills")
   private int ffaKills;

   @SerializedName("ffaDeaths")
   private int ffaDeaths;

   @SerializedName("coins")
   private int coins;

   @SerializedName("eventTokens")
   private int eventTokens;

   @SerializedName("rankedWins")
   private int rankedWins;

   @SerializedName("rankedLosses")
   private int rankedLosses;

   @SerializedName("tournamentsHosted")
   private int tournamentsHosted;

   @SerializedName("eventsHosted")
   private int eventsHosted;

   @SerializedName("eventWins")
   private int eventWins;

   @SerializedName("tournamentWins")
   private int tournamentWins;

   @SerializedName("requiredWinsForRanked")
   private int requiredWinsForRanked = 10;

   // Transient fields (not serialized)
   private transient Match currentMatch;
   private transient Queue currentQueue;
   private transient Object currentParty; // Placeholder for Party
   private transient boolean inMatch;
   private transient Player bukkitPlayer;
   private transient UUID currentOpponent;
   private transient MatchType currentMatchType;
   private transient MatchStatus currentMatchStatus;
   
   /**
    * Constructor
    * @param uniqueId the player UUID
    * @param name the player name
    */
   public PlayerProfile(UUID uniqueId, String name) {
      this.uniqueId = uniqueId;
      this.name = name;
      this.state = PlayerState.LOBBY;
      this.elo = 1000; // Default ELO
      this.level = 1;
      this.previousElo = 1000;
      this.kills = 0;
      this.deaths = 0;
      this.wins = 0;
      this.losses = 0;
      this.peakElo = 1000;
      this.botSettings = new BotSettings();
      this.cosmeticSettings = new CosmeticSettings();
      this.playerSettings = new PlayerSettings();
      this.inMatch = false;
      this.unlockedAchievements = new java.util.HashSet<>();
      this.loginStreak = 0;
      this.selectedTitleId = "none";
      this.unlockedTitleIds = new java.util.HashSet<>();
   }

   public java.util.Set<String> getUnlockedAchievements() {
      if (unlockedAchievements == null) unlockedAchievements = new java.util.HashSet<>();
      return unlockedAchievements;
   }

   public void setUnlockedAchievements(java.util.Set<String> unlockedAchievements) {
      this.unlockedAchievements = unlockedAchievements;
   }

   public boolean hasAchievement(String achievementId) {
      return getUnlockedAchievements().contains(achievementId);
   }

   public long getLastLoginDay() {
      return lastLoginDay;
   }

   public void setLastLoginDay(long lastLoginDay) {
      this.lastLoginDay = lastLoginDay;
   }

   public int getLoginStreak() {
      return loginStreak;
   }

   public void setLoginStreak(int loginStreak) {
      this.loginStreak = loginStreak;
   }

   public long getLastWeeklyClaim() {
      return lastWeeklyClaim;
   }

   public void setLastWeeklyClaim(long lastWeeklyClaim) {
      this.lastWeeklyClaim = lastWeeklyClaim;
   }

   public String getSelectedTitleId() {
      return selectedTitleId == null ? "none" : selectedTitleId;
   }

   public void setSelectedTitleId(String selectedTitleId) {
      this.selectedTitleId = selectedTitleId;
   }

   public java.util.Set<String> getUnlockedTitleIds() {
      if (unlockedTitleIds == null) unlockedTitleIds = new java.util.HashSet<>();
      return unlockedTitleIds;
   }

   public void setUnlockedTitleIds(java.util.Set<String> unlockedTitleIds) {
      this.unlockedTitleIds = unlockedTitleIds;
   }

   public boolean hasUnlockedTitleId(String titleId) {
      return getUnlockedTitleIds().contains(titleId != null ? titleId : "none");
   }

   public void addUnlockedTitleId(String titleId) {
      if (titleId != null && !titleId.isEmpty() && !"none".equalsIgnoreCase(titleId)) {
         getUnlockedTitleIds().add(titleId);
      }
   }

   public int getFfaKills() {
      return ffaKills;
   }

   public void setFfaKills(int ffaKills) {
      this.ffaKills = ffaKills;
   }

   public void addFfaKill() {
      this.ffaKills++;
   }

   public int getFfaDeaths() {
      return ffaDeaths;
   }

   public void setFfaDeaths(int ffaDeaths) {
      this.ffaDeaths = ffaDeaths;
   }

   public void addFfaDeath() {
      this.ffaDeaths++;
   }
   
   /**
    * Get unique ID
    * @return unique ID
    */
   public UUID getUniqueId() {
      return uniqueId;
   }
   
   /**
    * Set unique ID
    * @param uniqueId the unique ID
    */
   public void setUniqueId(UUID uniqueId) {
      this.uniqueId = uniqueId;
   }
   
   /**
    * Get name
    * @return name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Set name
    * @param name the name
    */
   public void setName(String name) {
      this.name = name;
   }
   
   /**
    * Get state
    * @return state
    */
   public PlayerState getState() {
      return state;
   }
   
   /**
    * Set state
    * @param state the state
    */
   public void setState(PlayerState state) {
      this.state = state;
   }
   
   /**
    * Get ELO
    * @return ELO
    */
   public int getElo() {
      return elo;
   }
   
   /**
    * Set ELO
    * @param elo the ELO
    */
   public void setElo(int elo) {
      this.previousElo = this.elo;
      this.elo = elo;
      
      // Update peak ELO
      if (elo > peakElo) {
         peakElo = elo;
      }
   }

      /**
    * Get LEVEL
    * @return LEVEL
    */
    public int getLevel() {
      return level;
   }

   /**
    * Set LEVEL
    * @param level the LEVEL
    */
   public void setLevel(int level) {
      this.level = level;
   }

      /**
    * Add level
    */
    public void addLevel() {
      this.level++;
   }

   /**
    * Get previous ELO
    * @return previous ELO
    */
   public int getPreviousElo() {
      return previousElo;
   }
   
   /**
    * Set previous ELO
    * @param previousElo the previous ELO
    */
   public void setPreviousElo(int previousElo) {
      this.previousElo = previousElo;
   }
   
   /**
    * Get kills
    * @return kills
    */
   public int getKills() {
      return kills;
   }
   
   /**
    * Set kills
    * @param kills the kills
    */
   public void setKills(int kills) {
      this.kills = kills;
   }
   
   /**
    * Add kill
    */
   public void addKill() {
      this.kills++;
   }
   
   /**
    * Get deaths
    * @return deaths
    */
   public int getDeaths() {
      return deaths;
   }
   
   /**
    * Set deaths
    * @param deaths the deaths
    */
   public void setDeaths(int deaths) {
      this.deaths = deaths;
   }
   
   /**
    * Add death
    */
   public void addDeath() {
      this.deaths++;
   }
   
   /**
    * Get wins
    * @return wins
    */
   public int getWins() {
      return wins;
   }
   
   /**
    * Set wins
    * @param wins the wins
    */
   public void setWins(int wins) {
      this.wins = wins;
   }
   
   /**
    * Add win
    */
   public void addWin() {
      this.wins++;
   }

   /**
    * Get winrate
    * @return winrate
    */
    public int getWinrate() {
      return winrate;
   }
   
   /**
    * Set winrate
    * @param winrate the winrate
    */
   public void setWinrate(int winrate) {
      this.winrate = winrate;
   }
   
   /**
    * Add winrate
    */
   public void addWinrate() {
      this.winrate++;
   }
   
   /**
    * Get losses
    * @return losses
    */
   public int getLosses() {
      return losses;
   }
   
   /**
    * Set losses
    * @param losses the losses
    */
   public void setLosses(int losses) {
      this.losses = losses;
   }
   
   /**
    * Add loss
    */
   public void addLoss() {
      this.losses++;
   }
   
   /**
    * Get peak ELO
    * @return peak ELO
    */
   public int getPeakElo() {
      return peakElo;
   }
   
   /**
    * Set peak ELO
    * @param peakElo the peak ELO
    */
   public void setPeakElo(int peakElo) {
      this.peakElo = peakElo;
   }
   
   /**
    * Get bot settings
    * @return bot settings
    */
   public BotSettings getBotSettings() {
      return botSettings;
   }
   
   /**
    * Set bot settings
    * @param botSettings the bot settings
    */
   public void setBotSettings(BotSettings botSettings) {
      this.botSettings = botSettings;
   }
   
   /**
    * Get cosmetic settings
    * @return cosmetic settings
    */
   public CosmeticSettings getCosmeticSettings() {
      return cosmeticSettings;
   }
   
   /**
    * Set cosmetic settings
    * @param cosmeticSettings the cosmetic settings
    */
   public void setCosmeticSettings(CosmeticSettings cosmeticSettings) {
      this.cosmeticSettings = cosmeticSettings;
   }
   
   /**
    * Get player settings
    * @return player settings
    */
   public PlayerSettings getPlayerSettings() {
      return playerSettings;
   }
   
   /**
    * Set player settings
    * @param playerSettings the player settings
    */
   public void setPlayerSettings(PlayerSettings playerSettings) {
      this.playerSettings = playerSettings;
   }
   
   /**
    * Check if player is in silent mode
    * @return true if silent mode is enabled
    */
   public boolean isSilentMode() {
      return playerSettings != null && playerSettings.isSilentMode();
   }
   
   /**
    * Set silent mode
    * @param silentMode true to enable silent mode
    */
   public void setSilentMode(boolean silentMode) {
      if (playerSettings != null) {
         playerSettings.setSilentMode(silentMode);
      }
   }
   
   /**
    * Check if duels are enabled
    * @return true if duel requests are enabled
    */
   public boolean isDuelsEnabled() {
      return playerSettings != null && playerSettings.isDuelRequests();
   }
   
   /**
    * Set duels enabled
    * @param enabled true to enable duel requests
    */
   public void setDuelsEnabled(boolean enabled) {
      if (playerSettings != null) {
         playerSettings.setDuelRequests(enabled);
      }
   }
   
   /**
    * Check if visibility is enabled
    * @return true if show players is enabled
    */
   public boolean isVisibilityEnabled() {
      return playerSettings != null && playerSettings.isShowPlayers();
   }
   
   /**
    * Set visibility enabled
    * @param enabled true to enable showing players
    */
   public void setVisibilityEnabled(boolean enabled) {
      if (playerSettings != null) {
         playerSettings.setShowPlayers(enabled);
      }
   }
   
   /**
    * Get current match
    * @return current match
    */
   public Match getCurrentMatch() {
      return currentMatch;
   }
   
   /**
    * Set current match
    * @param currentMatch the current match
    */
   public void setCurrentMatch(Match currentMatch) {
      this.currentMatch = currentMatch;
      this.inMatch = currentMatch != null;
   }
   
   /**
    * Get current queue
    * @return current queue
    */
   public Queue getCurrentQueue() {
      return currentQueue;
   }
   
   /**
    * Set current queue
    * @param currentQueue the current queue
    */
   public void setCurrentQueue(Queue currentQueue) {
      this.currentQueue = currentQueue;
   }
   
   /**
    * Get current party
    * @return current party
    */
   public Object getCurrentParty() {
      return currentParty;
   }
   
   /**
    * Set current party
    * @param currentParty the current party
    */
   public void setCurrentParty(Object currentParty) {
      this.currentParty = currentParty;
   }
   
   /**
    * Check if in match
    * @return true if in match
    */
   public boolean isInMatch() {
      return inMatch;
   }
   
   /**
    * Set in match
    * @param inMatch the in match status
    */
   public void setInMatch(boolean inMatch) {
      this.inMatch = inMatch;
   }
   
   /**
    * Get Bukkit player
    * @return Bukkit player
    */
   public Player getBukkitPlayer() {
      if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
         bukkitPlayer = Bukkit.getPlayer(uniqueId);
      }
      return bukkitPlayer;
   }
   
   /**
    * Set Bukkit player
    * @param bukkitPlayer the Bukkit player
    */
   public void setBukkitPlayer(Player bukkitPlayer) {
      this.bukkitPlayer = bukkitPlayer;
   }
   
   /**
    * Get current opponent
    * @return current opponent
    */
   public UUID getCurrentOpponent() {
      return currentOpponent;
   }
   
   /**
    * Set current opponent
    * @param currentOpponent the current opponent
    */
   public void setCurrentOpponent(UUID currentOpponent) {
      this.currentOpponent = currentOpponent;
   }
   
   /**
    * Get current match type
    * @return current match type
    */
   public MatchType getCurrentMatchType() {
      return currentMatchType;
   }
   
   /**
    * Set current match type
    * @param currentMatchType the current match type
    */
   public void setCurrentMatchType(MatchType currentMatchType) {
      this.currentMatchType = currentMatchType;
   }
   
   /**
    * Get current match status
    * @return current match status
    */
   public MatchStatus getCurrentMatchStatus() {
      return currentMatchStatus;
   }
   
   /**
    * Set current match status
    * @param currentMatchStatus the current match status
    */
   public void setCurrentMatchStatus(MatchStatus currentMatchStatus) {
      this.currentMatchStatus = currentMatchStatus;
   }
   
   /**
    * Get K/D ratio
    * @return K/D ratio
    */
   public double getKDRatio() {
      if (deaths == 0) {
         return kills;
      }
      return (double) kills / deaths;
   }
   
   /**
    * Get KDR (alias for getKDRatio)
    * @return KDR
    */
   public double getKDR() {
      return getKDRatio();
   }
   
   /**
    * Get W/L ratio
    * @return W/L ratio
    */
   public double getWLRatio() {
      if (losses == 0) {
         return wins;
      }
      return (double) wins / losses;
   }
   
   /**
    * Get total matches
    * @return total matches
    */
   public int getTotalMatches() {
      return wins + losses;
   }
   
   /**
    * Get win percentage
    * @return win percentage
    */
   public double getWinPercentage() {
      int total = getTotalMatches();
      if (total == 0) {
         return 0.0;
      }
      return (double) wins / total * 100;
   }
   
   /**
    * Check if player is online
    * @return true if online
    */
   public boolean isOnline() {
      return getBukkitPlayer() != null && getBukkitPlayer().isOnline();
   }
   
   /**
    * Reset profile
    */
   public void reset() {
      this.state = PlayerState.LOBBY;
      this.currentMatch = null;
      this.currentQueue = null;
      this.currentParty = null;
      this.inMatch = false;
      this.currentOpponent = null;
      this.currentMatchType = null;
      this.currentMatchStatus = null;
   }
   
   /**
    * Get profile summary
    * @return profile summary
    */
   public String getProfileSummary() {
      return String.format("Profile: %s, ELO: %d, K/D: %.2f, W/L: %.2f, State: %s", 
         name, elo, getKDRatio(), getWLRatio(), state.name());
   }
   
   @Override
   public String toString() {
      return getProfileSummary();
   }

   public Collection<PlayerProfile> getOnlineProfiles() {
    return java.util.Collections.emptyList();
   }

   public OfflinePlayer getPlayer() {
    return uniqueId != null ? Bukkit.getOfflinePlayer(uniqueId) : null;
   }

   public long getLastSaveTime() {
    return 0L;
   }

   public String getUsername() {
    return name != null ? name : (uniqueId != null ? uniqueId.toString() : "");
   }

   public void updateStatistics() {
    // No-op until statistics update logic is implemented.
   }

   public void updateCooldowns() {
    // No-op until cooldown storage is added; CooldownTask can still run.
   }

   /** Returns currently active cooldowns (e.g. name -> expiry time). Empty until cooldown storage is implemented. */
   public java.util.Map<String, Long> getActiveCooldowns() {
    return java.util.Collections.emptyMap();
   }

   public void updateLocation() {
    // No-op until location tracking is implemented.
   }

   public void updateInventory() {
    // No-op until inventory sync is implemented.
   }

   public void updateInventoryEffects() {
    // No-op until inventory effects are implemented.
   }

   public void updateInventoryCooldowns() {
    // No-op until inventory cooldown storage is implemented.
   }

   public void cleanupInventory() {
    // No-op until inventory cleanup logic is implemented.
   }

   public UUID getUuid() {
    return uniqueId;
   }

   public KitType getSelectedKit() {
      return selectedKit;
   }
   
   public void setSelectedKit(KitType selectedKit) {
      this.selectedKit = selectedKit;
   }

   public Object getInventory() {
    return null;
   }

   public Object getArmor() {
    return null;
   }

   public int getExperience() {
      return experience;
   }
   
   public void setExperience(int experience) {
      this.experience = experience;
   }

   public int getWinStreak() {
      return winStreak;
   }
   
   public void setWinStreak(int winStreak) {
      this.winStreak = winStreak;
      // Update best win streak if current streak is higher
      if (winStreak > bestWinStreak) {
         this.bestWinStreak = winStreak;
      }
   }

   public int getBestWinStreak() {
      return bestWinStreak;
   }
   
   public void setBestWinStreak(int bestWinStreak) {
      this.bestWinStreak = bestWinStreak;
   }

   public long getPlayTime() {
      return playTime;
   }
   
   public void setPlayTime(long playTime) {
      this.playTime = playTime;
   }

   public long getLastSeen() {
      return lastSeen;
   }
   
   public void setLastSeen(long lastSeen) {
      this.lastSeen = lastSeen;
   }

   public Object getStatistics() {
    return null;
   }

   public Object getSettings() {
    return playerSettings;
   }

   public int getCoins() {
      return coins;
   }

   public void setCoins(int coins) {
      this.coins = coins;
   }

   public int getEventTokens() {
      return eventTokens;
   }

   public void setEventTokens(int eventTokens) {
      this.eventTokens = eventTokens;
   }

   public int getRankedWins() {
      return rankedWins;
   }

   public void setRankedWins(int rankedWins) {
      this.rankedWins = rankedWins;
   }

   public int getRankedLosses() {
      return rankedLosses;
   }

   public void setRankedLosses(int rankedLosses) {
      this.rankedLosses = rankedLosses;
   }

   public int getUnrankedWins() {
      return Math.max(0, wins - rankedWins);
   }

   public int getUnrankedLosses() {
      return Math.max(0, losses - rankedLosses);
   }

   public int getTournamentsHosted() {
      return tournamentsHosted;
   }

   public void setTournamentsHosted(int tournamentsHosted) {
      this.tournamentsHosted = tournamentsHosted;
   }

   public int getEventsHosted() {
      return eventsHosted;
   }

   public void setEventsHosted(int eventsHosted) {
      this.eventsHosted = eventsHosted;
   }

   public int getEventWins() {
      return eventWins;
   }

   public void setEventWins(int eventWins) {
      this.eventWins = eventWins;
   }

   public int getTournamentWins() {
      return tournamentWins;
   }

   public void setTournamentWins(int tournamentWins) {
      this.tournamentWins = tournamentWins;
   }

   public int getRequiredWinsForRanked() {
      return requiredWinsForRanked;
   }

   public void setRequiredWinsForRanked(int requiredWinsForRanked) {
      this.requiredWinsForRanked = requiredWinsForRanked;
   }

   public Object getFirstJoin() {
    return null;
   }

   public Object getPreferences() {
    return null;
   }

   public String getWinstreak() {
    return String.valueOf(winStreak);
   }

   public Skin getSkin() {
    return null;
   }

}