package dev.artixdev.practice.models;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.libs.com.google.gson.JsonArray;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;
import dev.artixdev.practice.enums.KitType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Kit {
    @SerializedName("_id")
    private String name;
    private boolean enabled = true;
    private ItemStack icon;
    private ItemStack[] inventoryContents = new ItemStack[36];
    private ItemStack[] armorContents = new ItemStack[0];
    private ItemStack[] hotbarContents = new ItemStack[0];
    private Collection<PotionEffect> potionEffects = new ArrayList<>();
    private List<String> description = new ArrayList<>();
    private List<String> lore = new ArrayList<>();
    private List<String> permissions = new ArrayList<>();
    private String displayName;
    private String descriptionText;
    private int priority = 1;
    private int health = 20;
    private int foodLevel = 20;
    private int maxUses = 5;
    private int cooldown = 5;
    private final transient Map<UUID, KitEffect[]> playerEffects = new ConcurrentHashMap<>();
    
    // Various boolean flags for kit properties
    private boolean allowBuild = false;
    private boolean allowBreak = true;
    private boolean allowPvP = true;
    private boolean allowHunger = false;
    private boolean allowDamage = true;
    private boolean allowRegen = true;
    private boolean allowFlight = false;
    private boolean allowInventory = true;
    private boolean allowMovement = true;
    private boolean allowInteraction = true;
    private boolean allowChat = true;
    private boolean allowCommands = true;
    private boolean allowItems = true;
    private boolean allowArmor = true;
    private boolean allowWeapons = true;
    private boolean allowTools = true;
    private boolean allowBlocks = true;
    private boolean allowEntities = true;
    private boolean allowWorld = true;
    private boolean allowTime = true;
    private boolean allowWeather = true;
    private boolean allowGravity = true;
    private boolean allowCollision = true;
    private boolean allowPhysics = true;
    private boolean allowRedstone = true;
    private boolean allowExplosions = true;
    private boolean allowFire = true;
    private boolean allowWater = true;
    private boolean allowLava = true;
    private boolean allowVoid = true;
    private boolean allowFall = true;
    private boolean allowSuffocation = true;
    private boolean allowDrowning = true;
    private boolean allowStarvation = true;
    private boolean allowPoison = true;
    private boolean allowWither = true;
    private boolean allowMining = true;
    private boolean allowFarming = true;
    private boolean allowBreeding = true;
    private boolean allowTaming = true;
    private boolean allowRiding = true;
    private boolean allowFishing = true;
    private boolean allowEnchanting = true;
    private boolean allowBrewing = true;
    private boolean allowCrafting = true;
    private boolean allowSmelting = true;
    private boolean allowTrading = true;
    private boolean allowVillagers = true;
    private boolean allowAnimals = true;
    private boolean allowMonsters = true;
    private boolean allowBosses = true;
    private boolean allowPlayers = true;
    private boolean allowNPCs = true;
    private boolean enderPearlEnabled = true;

    private String knockbackProfile;
    private String botKnockbackProfile;
    private boolean ranked = false;
    private int bestOf = 1;
    private int sortPriority = 0;
    private int noDamageTicks = 20;
    private int respawnTicks = 20;
    private int countdownTicks = 5;
    private boolean disclaimerEnabled = false;
    private List<String> disclaimer = new ArrayList<>();
    private List<String> startCommands = new ArrayList<>();
    private List<String> endCommands = new ArrayList<>();
    private boolean editable = true;
    private boolean stickSpawn = false;
    private boolean showHP = true;
    private boolean spleef = false;
    private boolean battleRush = false;
    private boolean fireballFight = false;
    private boolean bedFight = false;
    private boolean topFight = false;
    private boolean stickFight = false;
    private boolean pearlFight = false;
    private boolean bridges = false;
    private boolean boxing = false;
    private boolean combo = false;
    private boolean sumo = false;
    private boolean mlgRush = false;

    public Kit() {
        this.displayName = "Default Kit";
        this.descriptionText = "A default practice kit";
        this.icon = new ItemStack(XMaterial.DIAMOND_SWORD.parseMaterial());
        this.inventoryContents = new ItemStack[36];
        this.armorContents = new ItemStack[0];
        this.hotbarContents = new ItemStack[0];
        this.potionEffects = new ArrayList<>();
        this.description = new ArrayList<>();
        this.lore = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.allowBuild = false;
        this.allowBreak = true;
        this.priority = 1;
        this.health = 20;
        this.foodLevel = 20;
        this.maxUses = 5;
        this.cooldown = 5;
    }

    public Kit(String name) {
        this();
        this.name = name;
        this.displayName = "Default Kit";
        this.descriptionText = "A default practice kit";
        this.icon = new ItemStack(XMaterial.DIAMOND_SWORD.parseMaterial());
        this.inventoryContents = new ItemStack[36];
        this.armorContents = new ItemStack[0];
        this.hotbarContents = new ItemStack[0];
        this.potionEffects = new ArrayList<>();
        this.description = new ArrayList<>();
        this.lore = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.allowBuild = false;
        this.allowBreak = true;
        this.priority = 1;
        this.health = 20;
        this.foodLevel = 20;
        this.maxUses = 5;
        this.cooldown = 5;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public ItemStack getIcon() { return icon; }
    public void setIcon(ItemStack icon) { this.icon = icon; }
    
    public ItemStack[] getInventoryContents() { return inventoryContents; }
    public void setInventoryContents(ItemStack[] inventoryContents) { this.inventoryContents = inventoryContents; }
    
    public ItemStack[] getArmorContents() { return armorContents; }
    public void setArmorContents(ItemStack[] armorContents) { this.armorContents = armorContents; }
    
    public ItemStack[] getHotbarContents() { return hotbarContents; }
    public void setHotbarContents(ItemStack[] hotbarContents) { this.hotbarContents = hotbarContents; }
    
    public Collection<PotionEffect> getPotionEffects() { return potionEffects; }
    public void setPotionEffects(Collection<PotionEffect> potionEffects) { this.potionEffects = potionEffects; }
    
    public List<String> getDescription() { return description; }
    public void setDescription(List<String> description) { this.description = description; }
    
    public List<String> getLore() { return lore; }
    public void setLore(List<String> lore) { this.lore = lore; }
    
    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getDescriptionText() { return descriptionText; }
    public void setDescriptionText(String descriptionText) { this.descriptionText = descriptionText; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    
    public int getFoodLevel() { return foodLevel; }
    public void setFoodLevel(int foodLevel) { this.foodLevel = foodLevel; }
    
    public int getMaxUses() { return maxUses; }
    public void setMaxUses(int maxUses) { this.maxUses = maxUses; }
    
    public int getCooldown() { return cooldown; }
    public void setCooldown(int cooldown) { this.cooldown = cooldown; }
    
    public Map<UUID, KitEffect[]> getPlayerEffects() { return playerEffects; }
    
    // Boolean flag getters and setters
    public boolean isAllowBuild() { return allowBuild; }
    public void setAllowBuild(boolean allowBuild) { this.allowBuild = allowBuild; }
    
    public boolean isAllowBreak() { return allowBreak; }
    public void setAllowBreak(boolean allowBreak) { this.allowBreak = allowBreak; }
    
    public boolean isAllowPvP() { return allowPvP; }
    public void setAllowPvP(boolean allowPvP) { this.allowPvP = allowPvP; }

    public boolean isEnderPearlEnabled() { return enderPearlEnabled; }
    public void setEnderPearlEnabled(boolean enderPearlEnabled) { this.enderPearlEnabled = enderPearlEnabled; }
    
    // Apply kit to player
    public void applyToPlayer(Player player) {
        // Apply inventory contents
        player.getInventory().setContents(inventoryContents);
        player.getInventory().setArmorContents(armorContents);
        
        // Apply potion effects
        for (PotionEffect effect : potionEffects) {
            player.addPotionEffect(effect, true);
        }
        
        // Set health and food
        player.setHealth(health);
        player.setFoodLevel(foodLevel);
    }
    
    // Check if player can use this kit
    public boolean canPlayerUse(Player player) {
        if (!enabled) return false;
        
        // Check permissions
        for (String permission : permissions) {
            if (!player.hasPermission(permission)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Kit) {
            Kit other = (Kit) obj;
            return Objects.equals(this.name, other.name);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public ItemStack[] getItems() {
        return inventoryContents;
    }

    public ItemStack[] getArmor() {
        return armorContents;
    }

    public KitType getKitType() {
        // Try to determine kit type from name
        if (name == null) return KitType.CUSTOM;
        
        String kitName = name.toUpperCase();
        for (KitType type : KitType.values()) {
            if (type.name().equals(kitName)) {
                return type;
            }
        }
        return KitType.CUSTOM;
    }

    public ItemStack getDisplayIcon() {
        return icon != null ? icon : new ItemStack(XMaterial.DIAMOND_SWORD.parseMaterial());
    }

    public String getKnockbackProfile() {
        return knockbackProfile;
    }

    public String getBotKnockbackProfile() {
        return botKnockbackProfile;
    }

    public int getBestOf() {
        return bestOf;
    }

    public int getSortPriority() {
        return sortPriority;
    }

    public int getNoDamageTicks() {
        return noDamageTicks;
    }

    public int getRespawnTicks() {
        return respawnTicks;
    }

    public int getCountdownTicks() {
        return countdownTicks;
    }

    public boolean isDisclaimerEnabled() {
        return disclaimerEnabled;
    }

    public ItemStack[] getContents() {
        return inventoryContents != null ? inventoryContents : new ItemStack[36];
    }

    public ItemStack[] getEditItems() {
        return inventoryContents != null ? inventoryContents : new ItemStack[36];
    }

    public JsonArray getEffects() {
        JsonArray arr = new JsonArray();
        if (potionEffects != null) {
            for (PotionEffect e : potionEffects) {
                arr.add(e.getType().getName() + ":" + e.getDuration() + ":" + e.getAmplifier());
            }
        }
        return arr;
    }

    public List<String> getDisclaimer() {
        return disclaimer != null ? disclaimer : new ArrayList<>();
    }

    public List<String> getStartCommands() {
        return startCommands != null ? startCommands : new ArrayList<>();
    }

    public List<String> getEndCommands() {
        return endCommands != null ? endCommands : new ArrayList<>();
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isRanked() {
        return ranked;
    }

    public boolean isBuild() {
        return allowBuild;
    }

    public boolean isStickSpawn() {
        return stickSpawn;
    }

    public boolean isShowHP() {
        return showHP;
    }

    public boolean isNoRegen() {
        return !allowRegen;
    }

    public boolean isNoFall() {
        return !allowFall;
    }

    public boolean isNoHunger() {
        return !allowHunger;
    }

    public boolean isSpleef() {
        return spleef;
    }

    public boolean isBattleRush() {
        return battleRush;
    }

    public boolean isFireballFight() {
        return fireballFight;
    }

    public boolean isBedFight() {
        return bedFight;
    }

    public boolean isTopFight() {
        return topFight;
    }

    public boolean isStickFight() {
        return stickFight;
    }

    public boolean isPearlFight() {
        return pearlFight;
    }

    public boolean isBridges() {
        return bridges;
    }

    public boolean isBoxing() {
        return boxing;
    }

    public boolean isCombo() {
        return combo;
    }

    public boolean isSumo() {
        return sumo;
    }

    public boolean isLiquidKill() {
        return false;
    }

    public boolean isPartyFFA() {
        return false;
    }

    public boolean isPartySplit() {
        return false;
    }

    public boolean isPearlDamage() {
        return false;
    }

    public boolean isMlgRush() {
        return mlgRush;
    }

    public boolean isBuildHeightDamage() {
        return false;
    }

    public void setDisplayIcon(ItemStack itemStack) {
        this.icon = itemStack;
    }

    public void setKnockbackProfile(String knockbackProfile) {
        this.knockbackProfile = knockbackProfile;
    }

    public void setBotKnockbackProfile(String botKnockbackProfile) {
        this.botKnockbackProfile = botKnockbackProfile;
    }

    public void setBestOf(int bestOf) {
        this.bestOf = bestOf;
    }

    public void setSortPriority(int sortPriority) {
        this.sortPriority = sortPriority;
    }

    public void setNoDamageTicks(int noDamageTicks) {
        this.noDamageTicks = noDamageTicks;
    }

    public void setRespawnTicks(int respawnTicks) {
        this.respawnTicks = respawnTicks;
    }

    public void setCountdownTicks(int countdownTicks) {
        this.countdownTicks = countdownTicks;
    }

    public void setDisclaimerEnabled(boolean disclaimerEnabled) {
        this.disclaimerEnabled = disclaimerEnabled;
    }

    public void setContents(ItemStack[] contents) {
        if (contents != null) this.inventoryContents = contents;
    }

    public void setArmor(ItemStack[] armor) {
        if (armor != null) this.armorContents = armor;
    }

    public void setEditItems(ItemStack[] editItems) {
        if (editItems != null) this.inventoryContents = editItems;
    }

    public void setEditable(boolean asBoolean) {
        this.editable = asBoolean;
    }

    public void setRanked(boolean asBoolean) {
        this.ranked = asBoolean;
    }

    public void setBuild(boolean asBoolean) {
        this.allowBuild = asBoolean;
    }

    public void setStickSpawn(boolean asBoolean) {
        this.stickSpawn = asBoolean;
    }

    public void setShowHP(boolean asBoolean) {
        this.showHP = asBoolean;
    }

    public void setNoRegen(boolean b) {
        this.allowRegen = !b;
    }

    public void setNoFall(boolean asBoolean) {
        this.allowFall = !asBoolean;
    }

    public void setNoHunger(boolean asBoolean) {
        this.allowHunger = !asBoolean;
    }

    public void setLiquidKill(boolean asBoolean) { }
    public void setSpleef(boolean asBoolean) { this.spleef = asBoolean; }
    public void setBattleRush(boolean asBoolean) { this.battleRush = asBoolean; }
    public void setFireballFight(boolean asBoolean) { this.fireballFight = asBoolean; }
    public void setStickFight(boolean asBoolean) { this.stickFight = asBoolean; }
    public void setPearlFight(boolean asBoolean) { this.pearlFight = asBoolean; }
    public void setBridges(boolean asBoolean) { this.bridges = asBoolean; }
    public void setBoxing(boolean asBoolean) { this.boxing = asBoolean; }
    public void setCombo(boolean asBoolean) { this.combo = asBoolean; }
    public void setSumo(boolean asBoolean) { this.sumo = asBoolean; }
    public void setMlgRush(boolean asBoolean) { this.mlgRush = asBoolean; }
    public void setBedFight(boolean asBoolean) { this.bedFight = asBoolean; }
    public void setTopFight(boolean asBoolean) { this.topFight = asBoolean; }
    public void setPartyFFA(boolean asBoolean) { }
    public void setPartySplit(boolean asBoolean) { }
    public void setPearlDamage(boolean asBoolean) { }
    public void setBuildHeightDamage(boolean asBoolean) { }
}