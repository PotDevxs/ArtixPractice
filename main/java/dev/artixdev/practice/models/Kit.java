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

    public Object getDisplayIcon() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDisplayIcon'");
    }

    public String getKnockbackProfile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getKnockbackProfile'");
    }

    public String getBotKnockbackProfile() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBotKnockbackProfile'");
    }

    public String getBestOf() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBestOf'");
    }

    public String getSortPriority() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSortPriority'");
    }

    public String getNoDamageTicks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNoDamageTicks'");
    }

    public String getRespawnTicks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRespawnTicks'");
    }

    public String getCountdownTicks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCountdownTicks'");
    }

    public String isDisclaimerEnabled() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDisclaimerEnabled'");
    }

    public Object getContents() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContents'");
    }

    public Object getEditItems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEditItems'");
    }

    public JsonArray getEffects() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEffects'");
    }

    public List<String> getDisclaimer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDisclaimer'");
    }

    public List<String> getStartCommands() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStartCommands'");
    }

    public List<String> getEndCommands() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEndCommands'");
    }

    public String isEditable() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEditable'");
    }

    public String isRanked() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isRanked'");
    }

    public String isBuild() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBuild'");
    }

    public String isStickSpawn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isStickSpawn'");
    }

    public String isShowHP() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isShowHP'");
    }

    public String isNoRegen() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isNoRegen'");
    }

    public String isNoFall() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isNoFall'");
    }

    public String isNoHunger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isNoHunger'");
    }

    public String isSpleef() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isSpleef'");
    }

    public String isBattleRush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBattleRush'");
    }

    public String isFireballFight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFireballFight'");
    }

    public String isBedFight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBedFight'");
    }

    public String isTopFight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isTopFight'");
    }

    public String isStickFight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isStickFight'");
    }

    public String isPearlFight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPearlFight'");
    }

    public String isBridges() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBridges'");
    }

    public String isBoxing() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBoxing'");
    }

    public String isCombo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isCombo'");
    }

    public String isSumo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isSumo'");
    }

    public String isLiquidKill() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isLiquidKill'");
    }

    public String isPartyFFA() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPartyFFA'");
    }

    public String isPartySplit() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPartySplit'");
    }

    public String isPearlDamage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPearlDamage'");
    }

    public String isMlgRush() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isMlgRush'");
    }

    public String isBuildHeightDamage() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBuildHeightDamage'");
    }

    public void setDisplayIcon(ItemStack itemStack) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDisplayIcon'");
    }

    public void setKnockbackProfile(String knockbackProfile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setKnockbackProfile'");
    }

    public void setBotKnockbackProfile(String botKnockbackProfile) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBotKnockbackProfile'");
    }

    public void setBestOf(int bestOf) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBestOf'");
    }

    public void setSortPriority(int sortPriority) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSortPriority'");
    }

    public void setNoDamageTicks(int noDamageTicks) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNoDamageTicks'");
    }

    public void setRespawnTicks(int respawnTicks) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRespawnTicks'");
    }

    public void setCountdownTicks(int countdownTicks) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCountdownTicks'");
    }

    public void setDisclaimerEnabled(boolean disclaimerEnabled) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDisclaimerEnabled'");
    }

    public void setContents(ItemStack[] contents) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setContents'");
    }

    public void setArmor(ItemStack[] armor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setArmor'");
    }

    public void setEditItems(ItemStack[] editItems) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEditItems'");
    }

    public void setEditable(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setEditable'");
    }

    public void setRanked(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRanked'");
    }

    public void setBuild(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBuild'");
    }

    public void setStickSpawn(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStickSpawn'");
    }

    public void setShowHP(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setShowHP'");
    }

    public void setNoRegen(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNoRegen'");
    }

    public void setNoFall(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNoFall'");
    }

    public void setNoHunger(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNoHunger'");
    }

    public void setLiquidKill(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLiquidKill'");
    }

    public void setSpleef(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSpleef'");
    }

    public void setBattleRush(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBattleRush'");
    }

    public void setFireballFight(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFireballFight'");
    }

    public void setStickFight(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStickFight'");
    }

    public void setPearlFight(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPearlFight'");
    }

    public void setBridges(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBridges'");
    }

    public void setBoxing(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBoxing'");
    }

    public void setCombo(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCombo'");
    }

    public void setSumo(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSumo'");
    }

    public void setMlgRush(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMlgRush'");
    }

    public void setBedFight(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBedFight'");
    }

    public void setTopFight(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTopFight'");
    }

    public void setPartyFFA(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPartyFFA'");
    }

    public void setPartySplit(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPartySplit'");
    }

    public void setPearlDamage(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPearlDamage'");
    }

    public void setBuildHeightDamage(boolean asBoolean) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBuildHeightDamage'");
    }
}