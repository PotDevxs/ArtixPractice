package dev.artixdev.practice.models;

import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

/**
 * Player Settings
 * Model for storing player settings and preferences
 */
public class PlayerSettings {
   
   @SerializedName("statsChangeMsg")
   private boolean statsChangeMsg = true;
   
   @SerializedName("showSpectator")
   private boolean showSpectator = true;
   
   @SerializedName("autoSilent")
   private boolean autoSilent = false;
   
   @SerializedName("silentMode")
   private boolean silentMode = false;
   
   @SerializedName("showPlayers")
   private boolean showPlayers = true;
   
   @SerializedName("tournamentMessages")
   private boolean tournamentMessages = true;
   
   @SerializedName("playAgainRematch")
   private boolean playAgainRematch = true;
   
   @SerializedName("vanillaTab")
   private boolean vanillaTab = false;
   
   @SerializedName("duelSounds")
   private boolean duelSounds = true;
   
   @SerializedName("sidebarEnabled")
   private boolean sidebarEnabled = true;
   
   @SerializedName("duelRequests")
   private boolean duelRequests = true;
   
   @SerializedName("deathAnimation")
   private boolean deathAnimation = true;
   
   @SerializedName("scoreboardEnabled")
   private boolean scoreboardEnabled = true;
   
   @SerializedName("tablistEnabled")
   private boolean tablistEnabled = true;
   
   @SerializedName("nametagEnabled")
   private boolean nametagEnabled = true;
   
   @SerializedName("chatEnabled")
   private boolean chatEnabled = true;
   
   @SerializedName("privateMessages")
   private boolean privateMessages = true;
   
   @SerializedName("friendRequests")
   private boolean friendRequests = true;
   
   @SerializedName("partyInvites")
   private boolean partyInvites = true;
   
   @SerializedName("guildInvites")
   private boolean guildInvites = true;
   
   @SerializedName("announcements")
   private boolean announcements = true;
   
   @SerializedName("notifications")
   private boolean notifications = true;
   
   @SerializedName("sounds")
   private boolean sounds = true;
   
   @SerializedName("music")
   private boolean music = true;
   
   @SerializedName("particles")
   private boolean particles = true;
   
   @SerializedName("effects")
   private boolean effects = true;
   
   @SerializedName("animations")
   private boolean animations = true;
   
   @SerializedName("blood")
   private boolean blood = true;
   
   @SerializedName("damageIndicators")
   private boolean damageIndicators = true;
   
   @SerializedName("healthBar")
   private boolean healthBar = true;
   
   @SerializedName("hungerBar")
   private boolean hungerBar = true;
   
   @SerializedName("experienceBar")
   private boolean experienceBar = true;
   
   @SerializedName("hotbar")
   private boolean hotbar = true;
   
   @SerializedName("inventory")
   private boolean inventory = true;
   
   @SerializedName("armor")
   private boolean armor = true;
   
   @SerializedName("offhand")
   private boolean offhand = true;
   
   @SerializedName("helmet")
   private boolean helmet = true;
   
   @SerializedName("chestplate")
   private boolean chestplate = true;
   
   @SerializedName("leggings")
   private boolean leggings = true;
   
   @SerializedName("boots")
   private boolean boots = true;
   
   @SerializedName("sword")
   private boolean sword = true;
   
   @SerializedName("bow")
   private boolean bow = true;
   
   @SerializedName("arrow")
   private boolean arrow = true;
   
   @SerializedName("potion")
   private boolean potion = true;
   
   @SerializedName("food")
   private boolean food = true;
   
   @SerializedName("block")
   private boolean block = true;
   
   @SerializedName("item")
   private boolean item = true;
   
   @SerializedName("entity")
   private boolean entity = true;
   
   @SerializedName("mob")
   private boolean mob = true;
   
   @SerializedName("animal")
   private boolean animal = true;
   
   @SerializedName("monster")
   private boolean monster = true;
   
   @SerializedName("npc")
   private boolean npc = true;
   
   @SerializedName("player")
   private boolean player = true;
   
   @SerializedName("spectator")
   private boolean spectator = true;
   
   @SerializedName("admin")
   private boolean admin = true;
   
   @SerializedName("moderator")
   private boolean moderator = true;
   
   @SerializedName("helper")
   private boolean helper = true;
   
   @SerializedName("vip")
   private boolean vip = true;
   
   @SerializedName("premium")
   private boolean premium = true;
   
   @SerializedName("donator")
   private boolean donator = true;
   
   @SerializedName("youtuber")
   private boolean youtuber = true;
   
   @SerializedName("streamer")
   private boolean streamer = true;
   
   @SerializedName("developer")
   private boolean developer = true;
   
   @SerializedName("owner")
   private boolean owner = true;
   
   @SerializedName("staff")
   private boolean staff = true;
   
   @SerializedName("member")
   private boolean member = true;
   
   @SerializedName("guest")
   private boolean guest = true;
   
   @SerializedName("banned")
   private boolean banned = false;
   
   @SerializedName("muted")
   private boolean muted = false;
   
   @SerializedName("kicked")
   private boolean kicked = false;
   
   @SerializedName("warned")
   private boolean warned = false;
   
   @SerializedName("jailed")
   private boolean jailed = false;
   
   @SerializedName("frozen")
   private boolean frozen = false;
   
   @SerializedName("teleported")
   private boolean teleported = false;
   
   @SerializedName("invisible")
   private boolean invisible = false;
   
   @SerializedName("flying")
   private boolean flying = false;
   
   @SerializedName("god")
   private boolean god = false;
   
   @SerializedName("vanish")
   private boolean vanish = false;
   
   @SerializedName("noclip")
   private boolean noclip = false;
   
   @SerializedName("speed")
   private boolean speed = false;
   
   @SerializedName("jump")
   private boolean jump = false;
   
   @SerializedName("strength")
   private boolean strength = false;
   
   @SerializedName("resistance")
   private boolean resistance = false;
   
   @SerializedName("fireResistance")
   private boolean fireResistance = false;
   
   @SerializedName("waterBreathing")
   private boolean waterBreathing = false;
   
   @SerializedName("nightVision")
   private boolean nightVision = false;
   
   @SerializedName("invisibility")
   private boolean invisibility = false;
   
   @SerializedName("regeneration")
   private boolean regeneration = false;
   
   @SerializedName("absorption")
   private boolean absorption = false;
   
   @SerializedName("healthBoost")
   private boolean healthBoost = false;
   
   @SerializedName("saturation")
   private boolean saturation = false;
   
   @SerializedName("luck")
   private boolean luck = false;
   
   @SerializedName("unluck")
   private boolean unluck = false;
   
   @SerializedName("levitation")
   private boolean levitation = false;
   
   @SerializedName("slowFalling")
   private boolean slowFalling = false;
   
   @SerializedName("conduitPower")
   private boolean conduitPower = false;
   
   @SerializedName("dolphinsGrace")
   private boolean dolphinsGrace = false;
   
   @SerializedName("badOmen")
   private boolean badOmen = false;
   
   @SerializedName("heroOfTheVillage")
   private boolean heroOfTheVillage = false;
   
   @SerializedName("darkness")
   private boolean darkness = false;
   
   @SerializedName("windCharged")
   private boolean windCharged = false;
   
   @SerializedName("weaving")
   private boolean weaving = false;
   
   @SerializedName("omen")
   private boolean omen = false;
   
   @SerializedName("infested")
   private boolean infested = false;
   
   @SerializedName("density")
   private boolean density = false;
   
   @SerializedName("breach")
   private boolean breach = false;
   
   @SerializedName("trialOmen")
   private boolean trialOmen = false;
   
   @SerializedName("raidOmen")
   private boolean raidOmen = false;
   
   // Getters and setters for all fields
   
   public boolean isStatsChangeMsg() {
      return statsChangeMsg;
   }
   
   public void setStatsChangeMsg(boolean statsChangeMsg) {
      this.statsChangeMsg = statsChangeMsg;
   }
   
   public boolean isShowSpectator() {
      return showSpectator;
   }
   
   public void setShowSpectator(boolean showSpectator) {
      this.showSpectator = showSpectator;
   }
   
   public boolean isAutoSilent() {
      return autoSilent;
   }
   
   public void setAutoSilent(boolean autoSilent) {
      this.autoSilent = autoSilent;
   }
   
   public boolean isSilentMode() {
      return silentMode;
   }
   
   public void setSilentMode(boolean silentMode) {
      this.silentMode = silentMode;
   }
   
   public boolean isShowPlayers() {
      return showPlayers;
   }
   
   public void setShowPlayers(boolean showPlayers) {
      this.showPlayers = showPlayers;
   }
   
   public boolean isTournamentMessages() {
      return tournamentMessages;
   }
   
   public void setTournamentMessages(boolean tournamentMessages) {
      this.tournamentMessages = tournamentMessages;
   }
   
   public boolean isPlayAgainRematch() {
      return playAgainRematch;
   }
   
   public void setPlayAgainRematch(boolean playAgainRematch) {
      this.playAgainRematch = playAgainRematch;
   }
   
   public boolean isVanillaTab() {
      return vanillaTab;
   }
   
   public void setVanillaTab(boolean vanillaTab) {
      this.vanillaTab = vanillaTab;
   }
   
   public boolean isDuelSounds() {
      return duelSounds;
   }
   
   public void setDuelSounds(boolean duelSounds) {
      this.duelSounds = duelSounds;
   }
   
   public boolean isSidebarEnabled() {
      return sidebarEnabled;
   }
   
   public void setSidebarEnabled(boolean sidebarEnabled) {
      this.sidebarEnabled = sidebarEnabled;
   }
   
   public boolean isDuelRequests() {
      return duelRequests;
   }
   
   public void setDuelRequests(boolean duelRequests) {
      this.duelRequests = duelRequests;
   }
   
   public boolean isDeathAnimation() {
      return deathAnimation;
   }
   
   public void setDeathAnimation(boolean deathAnimation) {
      this.deathAnimation = deathAnimation;
   }
   
   public boolean isScoreboardEnabled() {
      return scoreboardEnabled;
   }
   
   public void setScoreboardEnabled(boolean scoreboardEnabled) {
      this.scoreboardEnabled = scoreboardEnabled;
   }
   
   public boolean isTablistEnabled() {
      return tablistEnabled;
   }
   
   public void setTablistEnabled(boolean tablistEnabled) {
      this.tablistEnabled = tablistEnabled;
   }
   
   public boolean isNametagEnabled() {
      return nametagEnabled;
   }
   
   public void setNametagEnabled(boolean nametagEnabled) {
      this.nametagEnabled = nametagEnabled;
   }
   
   public boolean isChatEnabled() {
      return chatEnabled;
   }
   
   public void setChatEnabled(boolean chatEnabled) {
      this.chatEnabled = chatEnabled;
   }
   
   public boolean isPrivateMessages() {
      return privateMessages;
   }
   
   public void setPrivateMessages(boolean privateMessages) {
      this.privateMessages = privateMessages;
   }
   
   public boolean isFriendRequests() {
      return friendRequests;
   }
   
   public void setFriendRequests(boolean friendRequests) {
      this.friendRequests = friendRequests;
   }
   
   public boolean isPartyInvites() {
      return partyInvites;
   }
   
   public void setPartyInvites(boolean partyInvites) {
      this.partyInvites = partyInvites;
   }
   
   public boolean isGuildInvites() {
      return guildInvites;
   }
   
   public void setGuildInvites(boolean guildInvites) {
      this.guildInvites = guildInvites;
   }
   
   public boolean isAnnouncements() {
      return announcements;
   }
   
   public void setAnnouncements(boolean announcements) {
      this.announcements = announcements;
   }
   
   public boolean isNotifications() {
      return notifications;
   }
   
   public void setNotifications(boolean notifications) {
      this.notifications = notifications;
   }
   
   public boolean isSounds() {
      return sounds;
   }
   
   public void setSounds(boolean sounds) {
      this.sounds = sounds;
   }
   
   public boolean isMusic() {
      return music;
   }
   
   public void setMusic(boolean music) {
      this.music = music;
   }
   
   public boolean isParticles() {
      return particles;
   }
   
   public void setParticles(boolean particles) {
      this.particles = particles;
   }
   
   public boolean isEffects() {
      return effects;
   }
   
   public void setEffects(boolean effects) {
      this.effects = effects;
   }
   
   public boolean isAnimations() {
      return animations;
   }
   
   public void setAnimations(boolean animations) {
      this.animations = animations;
   }
   
   public boolean isBlood() {
      return blood;
   }
   
   public void setBlood(boolean blood) {
      this.blood = blood;
   }
   
   public boolean isDamageIndicators() {
      return damageIndicators;
   }
   
   public void setDamageIndicators(boolean damageIndicators) {
      this.damageIndicators = damageIndicators;
   }
   
   public boolean isHealthBar() {
      return healthBar;
   }
   
   public void setHealthBar(boolean healthBar) {
      this.healthBar = healthBar;
   }
   
   public boolean isHungerBar() {
      return hungerBar;
   }
   
   public void setHungerBar(boolean hungerBar) {
      this.hungerBar = hungerBar;
   }
   
   public boolean isExperienceBar() {
      return experienceBar;
   }
   
   public void setExperienceBar(boolean experienceBar) {
      this.experienceBar = experienceBar;
   }
   
   public boolean isHotbar() {
      return hotbar;
   }
   
   public void setHotbar(boolean hotbar) {
      this.hotbar = hotbar;
   }
   
   public boolean isInventory() {
      return inventory;
   }
   
   public void setInventory(boolean inventory) {
      this.inventory = inventory;
   }
   
   public boolean isArmor() {
      return armor;
   }
   
   public void setArmor(boolean armor) {
      this.armor = armor;
   }
   
   public boolean isOffhand() {
      return offhand;
   }
   
   public void setOffhand(boolean offhand) {
      this.offhand = offhand;
   }
   
   public boolean isHelmet() {
      return helmet;
   }
   
   public void setHelmet(boolean helmet) {
      this.helmet = helmet;
   }
   
   public boolean isChestplate() {
      return chestplate;
   }
   
   public void setChestplate(boolean chestplate) {
      this.chestplate = chestplate;
   }
   
   public boolean isLeggings() {
      return leggings;
   }
   
   public void setLeggings(boolean leggings) {
      this.leggings = leggings;
   }
   
   public boolean isBoots() {
      return boots;
   }
   
   public void setBoots(boolean boots) {
      this.boots = boots;
   }
   
   public boolean isSword() {
      return sword;
   }
   
   public void setSword(boolean sword) {
      this.sword = sword;
   }
   
   public boolean isBow() {
      return bow;
   }
   
   public void setBow(boolean bow) {
      this.bow = bow;
   }
   
   public boolean isArrow() {
      return arrow;
   }
   
   public void setArrow(boolean arrow) {
      this.arrow = arrow;
   }
   
   public boolean isPotion() {
      return potion;
   }
   
   public void setPotion(boolean potion) {
      this.potion = potion;
   }
   
   public boolean isFood() {
      return food;
   }
   
   public void setFood(boolean food) {
      this.food = food;
   }
   
   public boolean isBlock() {
      return block;
   }
   
   public void setBlock(boolean block) {
      this.block = block;
   }
   
   public boolean isItem() {
      return item;
   }
   
   public void setItem(boolean item) {
      this.item = item;
   }
   
   public boolean isEntity() {
      return entity;
   }
   
   public void setEntity(boolean entity) {
      this.entity = entity;
   }
   
   public boolean isMob() {
      return mob;
   }
   
   public void setMob(boolean mob) {
      this.mob = mob;
   }
   
   public boolean isAnimal() {
      return animal;
   }
   
   public void setAnimal(boolean animal) {
      this.animal = animal;
   }
   
   public boolean isMonster() {
      return monster;
   }
   
   public void setMonster(boolean monster) {
      this.monster = monster;
   }
   
   public boolean isNpc() {
      return npc;
   }
   
   public void setNpc(boolean npc) {
      this.npc = npc;
   }
   
   public boolean isPlayer() {
      return player;
   }
   
   public void setPlayer(boolean player) {
      this.player = player;
   }
   
   public boolean isSpectator() {
      return spectator;
   }
   
   public void setSpectator(boolean spectator) {
      this.spectator = spectator;
   }
   
   public boolean isAdmin() {
      return admin;
   }
   
   public void setAdmin(boolean admin) {
      this.admin = admin;
   }
   
   public boolean isModerator() {
      return moderator;
   }
   
   public void setModerator(boolean moderator) {
      this.moderator = moderator;
   }
   
   public boolean isHelper() {
      return helper;
   }
   
   public void setHelper(boolean helper) {
      this.helper = helper;
   }
   
   public boolean isVip() {
      return vip;
   }
   
   public void setVip(boolean vip) {
      this.vip = vip;
   }
   
   public boolean isPremium() {
      return premium;
   }
   
   public void setPremium(boolean premium) {
      this.premium = premium;
   }
   
   public boolean isDonator() {
      return donator;
   }
   
   public void setDonator(boolean donator) {
      this.donator = donator;
   }
   
   public boolean isYoutuber() {
      return youtuber;
   }
   
   public void setYoutuber(boolean youtuber) {
      this.youtuber = youtuber;
   }
   
   public boolean isStreamer() {
      return streamer;
   }
   
   public void setStreamer(boolean streamer) {
      this.streamer = streamer;
   }
   
   public boolean isDeveloper() {
      return developer;
   }
   
   public void setDeveloper(boolean developer) {
      this.developer = developer;
   }
   
   public boolean isOwner() {
      return owner;
   }
   
   public void setOwner(boolean owner) {
      this.owner = owner;
   }
   
   public boolean isStaff() {
      return staff;
   }
   
   public void setStaff(boolean staff) {
      this.staff = staff;
   }
   
   public boolean isMember() {
      return member;
   }
   
   public void setMember(boolean member) {
      this.member = member;
   }
   
   public boolean isGuest() {
      return guest;
   }
   
   public void setGuest(boolean guest) {
      this.guest = guest;
   }
   
   public boolean isBanned() {
      return banned;
   }
   
   public void setBanned(boolean banned) {
      this.banned = banned;
   }
   
   public boolean isMuted() {
      return muted;
   }
   
   public void setMuted(boolean muted) {
      this.muted = muted;
   }
   
   public boolean isKicked() {
      return kicked;
   }
   
   public void setKicked(boolean kicked) {
      this.kicked = kicked;
   }
   
   public boolean isWarned() {
      return warned;
   }
   
   public void setWarned(boolean warned) {
      this.warned = warned;
   }
   
   public boolean isJailed() {
      return jailed;
   }
   
   public void setJailed(boolean jailed) {
      this.jailed = jailed;
   }
   
   public boolean isFrozen() {
      return frozen;
   }
   
   public void setFrozen(boolean frozen) {
      this.frozen = frozen;
   }
   
   public boolean isTeleported() {
      return teleported;
   }
   
   public void setTeleported(boolean teleported) {
      this.teleported = teleported;
   }
   
   public boolean isInvisible() {
      return invisible;
   }
   
   public void setInvisible(boolean invisible) {
      this.invisible = invisible;
   }
   
   public boolean isFlying() {
      return flying;
   }
   
   public void setFlying(boolean flying) {
      this.flying = flying;
   }
   
   public boolean isGod() {
      return god;
   }
   
   public void setGod(boolean god) {
      this.god = god;
   }
   
   public boolean isVanish() {
      return vanish;
   }
   
   public void setVanish(boolean vanish) {
      this.vanish = vanish;
   }
   
   public boolean isNoclip() {
      return noclip;
   }
   
   public void setNoclip(boolean noclip) {
      this.noclip = noclip;
   }
   
   public boolean isSpeed() {
      return speed;
   }
   
   public void setSpeed(boolean speed) {
      this.speed = speed;
   }
   
   public boolean isJump() {
      return jump;
   }
   
   public void setJump(boolean jump) {
      this.jump = jump;
   }
   
   public boolean isStrength() {
      return strength;
   }
   
   public void setStrength(boolean strength) {
      this.strength = strength;
   }
   
   public boolean isResistance() {
      return resistance;
   }
   
   public void setResistance(boolean resistance) {
      this.resistance = resistance;
   }
   
   public boolean isFireResistance() {
      return fireResistance;
   }
   
   public void setFireResistance(boolean fireResistance) {
      this.fireResistance = fireResistance;
   }
   
   public boolean isWaterBreathing() {
      return waterBreathing;
   }
   
   public void setWaterBreathing(boolean waterBreathing) {
      this.waterBreathing = waterBreathing;
   }
   
   public boolean isNightVision() {
      return nightVision;
   }
   
   public void setNightVision(boolean nightVision) {
      this.nightVision = nightVision;
   }
   
   public boolean isInvisibility() {
      return invisibility;
   }
   
   public void setInvisibility(boolean invisibility) {
      this.invisibility = invisibility;
   }
   
   public boolean isRegeneration() {
      return regeneration;
   }
   
   public void setRegeneration(boolean regeneration) {
      this.regeneration = regeneration;
   }
   
   public boolean isAbsorption() {
      return absorption;
   }
   
   public void setAbsorption(boolean absorption) {
      this.absorption = absorption;
   }
   
   public boolean isHealthBoost() {
      return healthBoost;
   }
   
   public void setHealthBoost(boolean healthBoost) {
      this.healthBoost = healthBoost;
   }
   
   public boolean isSaturation() {
      return saturation;
   }
   
   public void setSaturation(boolean saturation) {
      this.saturation = saturation;
   }
   
   public boolean isLuck() {
      return luck;
   }
   
   public void setLuck(boolean luck) {
      this.luck = luck;
   }
   
   public boolean isUnluck() {
      return unluck;
   }
   
   public void setUnluck(boolean unluck) {
      this.unluck = unluck;
   }
   
   public boolean isLevitation() {
      return levitation;
   }
   
   public void setLevitation(boolean levitation) {
      this.levitation = levitation;
   }
   
   public boolean isSlowFalling() {
      return slowFalling;
   }
   
   public void setSlowFalling(boolean slowFalling) {
      this.slowFalling = slowFalling;
   }
   
   public boolean isConduitPower() {
      return conduitPower;
   }
   
   public void setConduitPower(boolean conduitPower) {
      this.conduitPower = conduitPower;
   }
   
   public boolean isDolphinsGrace() {
      return dolphinsGrace;
   }
   
   public void setDolphinsGrace(boolean dolphinsGrace) {
      this.dolphinsGrace = dolphinsGrace;
   }
   
   public boolean isBadOmen() {
      return badOmen;
   }
   
   public void setBadOmen(boolean badOmen) {
      this.badOmen = badOmen;
   }
   
   public boolean isHeroOfTheVillage() {
      return heroOfTheVillage;
   }
   
   public void setHeroOfTheVillage(boolean heroOfTheVillage) {
      this.heroOfTheVillage = heroOfTheVillage;
   }
   
   public boolean isDarkness() {
      return darkness;
   }
   
   public void setDarkness(boolean darkness) {
      this.darkness = darkness;
   }
   
   public boolean isWindCharged() {
      return windCharged;
   }
   
   public void setWindCharged(boolean windCharged) {
      this.windCharged = windCharged;
   }
   
   public boolean isWeaving() {
      return weaving;
   }
   
   public void setWeaving(boolean weaving) {
      this.weaving = weaving;
   }
   
   public boolean isOmen() {
      return omen;
   }
   
   public void setOmen(boolean omen) {
      this.omen = omen;
   }
   
   public boolean isInfested() {
      return infested;
   }
   
   public void setInfested(boolean infested) {
      this.infested = infested;
   }
   
   public boolean isDensity() {
      return density;
   }
   
   public void setDensity(boolean density) {
      this.density = density;
   }
   
   public boolean isBreach() {
      return breach;
   }
   
   public void setBreach(boolean breach) {
      this.breach = breach;
   }
   
   public boolean isTrialOmen() {
      return trialOmen;
   }
   
   public void setTrialOmen(boolean trialOmen) {
      this.trialOmen = trialOmen;
   }
   
   public boolean isRaidOmen() {
      return raidOmen;
   }
   
   public void setRaidOmen(boolean raidOmen) {
      this.raidOmen = raidOmen;
   }
   
   /**
    * Get settings summary
    * @return settings summary
    */
   public String getSettingsSummary() {
      return String.format("PlayerSettings: %d enabled, %d disabled", 
         getEnabledCount(), getDisabledCount());
   }
   
   /**
    * Get enabled count
    * @return enabled count
    */
   public int getEnabledCount() {
      int count = 0;
      if (statsChangeMsg) count++;
      if (showSpectator) count++;
      if (showPlayers) count++;
      if (tournamentMessages) count++;
      if (playAgainRematch) count++;
      if (duelSounds) count++;
      if (sidebarEnabled) count++;
      if (duelRequests) count++;
      if (deathAnimation) count++;
      if (scoreboardEnabled) count++;
      if (tablistEnabled) count++;
      if (nametagEnabled) count++;
      if (chatEnabled) count++;
      if (privateMessages) count++;
      if (friendRequests) count++;
      if (partyInvites) count++;
      if (guildInvites) count++;
      if (announcements) count++;
      if (notifications) count++;
      if (sounds) count++;
      if (music) count++;
      if (particles) count++;
      if (effects) count++;
      if (animations) count++;
      if (blood) count++;
      if (damageIndicators) count++;
      if (healthBar) count++;
      if (hungerBar) count++;
      if (experienceBar) count++;
      if (hotbar) count++;
      if (inventory) count++;
      if (armor) count++;
      if (offhand) count++;
      if (helmet) count++;
      if (chestplate) count++;
      if (leggings) count++;
      if (boots) count++;
      if (sword) count++;
      if (bow) count++;
      if (arrow) count++;
      if (potion) count++;
      if (food) count++;
      if (block) count++;
      if (item) count++;
      if (entity) count++;
      if (mob) count++;
      if (animal) count++;
      if (monster) count++;
      if (npc) count++;
      if (player) count++;
      if (spectator) count++;
      if (admin) count++;
      if (moderator) count++;
      if (helper) count++;
      if (vip) count++;
      if (premium) count++;
      if (donator) count++;
      if (youtuber) count++;
      if (streamer) count++;
      if (developer) count++;
      if (owner) count++;
      if (staff) count++;
      if (member) count++;
      if (guest) count++;
      return count;
   }
   
   /**
    * Get disabled count
    * @return disabled count
    */
   public int getDisabledCount() {
      int count = 0;
      if (!statsChangeMsg) count++;
      if (!showSpectator) count++;
      if (!showPlayers) count++;
      if (!tournamentMessages) count++;
      if (!playAgainRematch) count++;
      if (!duelSounds) count++;
      if (!sidebarEnabled) count++;
      if (!duelRequests) count++;
      if (!deathAnimation) count++;
      if (!scoreboardEnabled) count++;
      if (!tablistEnabled) count++;
      if (!nametagEnabled) count++;
      if (!chatEnabled) count++;
      if (!privateMessages) count++;
      if (!friendRequests) count++;
      if (!partyInvites) count++;
      if (!guildInvites) count++;
      if (!announcements) count++;
      if (!notifications) count++;
      if (!sounds) count++;
      if (!music) count++;
      if (!particles) count++;
      if (!effects) count++;
      if (!animations) count++;
      if (!blood) count++;
      if (!damageIndicators) count++;
      if (!healthBar) count++;
      if (!hungerBar) count++;
      if (!experienceBar) count++;
      if (!hotbar) count++;
      if (!inventory) count++;
      if (!armor) count++;
      if (!offhand) count++;
      if (!helmet) count++;
      if (!chestplate) count++;
      if (!leggings) count++;
      if (!boots) count++;
      if (!sword) count++;
      if (!bow) count++;
      if (!arrow) count++;
      if (!potion) count++;
      if (!food) count++;
      if (!block) count++;
      if (!item) count++;
      if (!entity) count++;
      if (!mob) count++;
      if (!animal) count++;
      if (!monster) count++;
      if (!npc) count++;
      if (!player) count++;
      if (!spectator) count++;
      if (!admin) count++;
      if (!moderator) count++;
      if (!helper) count++;
      if (!vip) count++;
      if (!premium) count++;
      if (!donator) count++;
      if (!youtuber) count++;
      if (!streamer) count++;
      if (!developer) count++;
      if (!owner) count++;
      if (!staff) count++;
      if (!member) count++;
      if (!guest) count++;
      return count;
   }
   
   @Override
   public String toString() {
      return getSettingsSummary();
   }
}
