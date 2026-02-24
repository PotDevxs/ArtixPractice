package dev.artixdev.practice.inventory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Match;

import java.util.List;

public class EnderPearlHandler implements EnderPearlHandlerInterface {
    private ItemStack enderPearl;
    private int chestplateSlot;
    private ItemStack boots;
    private int leggingsSlot;
    private int helmetSlot = -1;
    private ItemStack chestplate;
    public static final boolean DEBUG_MODE = false;
    private int bootsSlot;
    private ItemStack leggings;
    private static final Logger logger = LogManager.getLogger(EnderPearlHandler.class);
    private int enderPearlSlot;
    public static final int HANDLER_VERSION = 1;
    private ItemStack helmet;
    private static final String[] ENDER_PEARL_CONSTANTS = new String[2];
    private static final String[] ENDER_PEARL_MESSAGES = new String[2];

    private void handleEnderPearl(Match match, Player player, PlayerInventory inventory) {
        if (match.isEnderPearlEnabled()) {
            int slot = this.enderPearlSlot;
            
            if (slot > 8) {
                slot = 8;
            }

            inventory.setHeldItemSlot(slot);
            
            Player targetPlayer = match.getPlayer();
            Location location = targetPlayer.getLocation();
            Location targetLocation = location;
            double distance = match.getDistance();
            
            if (distance < 7.0D) {
                if (match.getKit() != null && match.getKit().isEnderPearlEnabled()) {
                    targetLocation = this.calculateEnderPearlLocation(location);
                }
            }
            
            targetLocation = targetLocation.subtract(0.0D, 0.5D, 0.0D);
            match.setLocation(targetLocation);
            
            EnderPearl enderPearl = player.launchProjectile(EnderPearl.class);
            String metadataKey = getEnderPearlMetadata();
            FixedMetadataValue metadata = new FixedMetadataValue(Main.getInstance(), match.getMatchId());
            enderPearl.setMetadata(metadataKey, metadata);
            enderPearl.setShooter(player);
            match.setEnderPearl(enderPearl);
            
            int amount = this.enderPearl.getAmount();
            if (amount <= 1) {
                this.enderPearl = null;
                inventory.setItem(8, null);
            } else {
                ItemStack newEnderPearl = this.enderPearl;
                int newAmount = newEnderPearl.getAmount() - 1;
                newEnderPearl.setAmount(newAmount);
                inventory.setItem(this.enderPearlSlot, this.enderPearl);
            }
            
            if (match.getBot() != null) {
                match.getBot().getNavigator().getDefaultParameters().speedModifier(1.0F);
            }
            match.setBotState(match.getBotState());
        }
    }

    private void updateInventory(Match match, PlayerInventory inventory, int slot, ItemStack item, boolean flag) {
        if (match == null || match.isEnded() || inventory == null) {
            return;
        }
        int clampedSlot = slot;
        if (clampedSlot < 0) {
            clampedSlot = 0;
        } else if (clampedSlot > 35) {
            clampedSlot = 35;
        }
        if (item != null) {
            inventory.setItem(clampedSlot, item.clone());
        }
        if (flag && clampedSlot >= 0 && clampedSlot <= 8) {
            inventory.setHeldItemSlot(clampedSlot);
        }
        if (inventory.getHolder() instanceof Player) {
            ((Player) inventory.getHolder()).updateInventory();
        }
    }

    @Override
    public boolean validateInventory(Match match) {
        if (match == null) {
            return false;
        }
        if (match.isEnded()) {
            return false;
        }
        List<Player> players = match.getPlayers();
        if (players == null || players.isEmpty()) {
            return false;
        }
        for (Player player : players) {
            if (player == null || !player.isOnline()) {
                return false;
            }
            if (player.getInventory() == null) {
                return false;
            }
        }
        return true;
    }

    private void updateInventory(PlayerInventory inventory) {
        if (inventory == null) {
            return;
        }
        if (helmet != null) {
            inventory.setHelmet(helmet.clone());
        }
        if (chestplate != null) {
            inventory.setChestplate(chestplate.clone());
        }
        if (leggings != null) {
            inventory.setLeggings(leggings.clone());
        }
        if (boots != null) {
            inventory.setBoots(boots.clone());
        }
        if (enderPearl != null && enderPearlSlot >= 0 && enderPearlSlot <= 35) {
            inventory.setItem(enderPearlSlot, enderPearl.clone());
        }
        if (enderPearlSlot >= 0 && enderPearlSlot <= 8) {
            inventory.setHeldItemSlot(enderPearlSlot);
        }
        if (inventory.getHolder() instanceof Player) {
            ((Player) inventory.getHolder()).updateInventory();
        }
    }

    public EnderPearlHandler() {
        this.chestplateSlot = -1;
        this.leggingsSlot = -1;
        this.bootsSlot = -1;
    }

    private boolean isValidLocation(Location location) {
        if (location == null) {
            return false;
        }
        if (location.getWorld() == null) {
            return false;
        }
        double y = location.getY();
        if (y < 0.0 || Double.isNaN(y) || Double.isInfinite(y)) {
            return false;
        }
        // Reasonable world height upper bound (works across MC versions)
        return y <= 320.0;
    }

    private Location calculateEnderPearlLocation(Location location) {
        if (location == null || location.getWorld() == null) {
            return location != null ? location.clone() : null;
        }
        if (!isValidLocation(location)) {
            return location.clone();
        }
        int x = location.getBlockX();
        int z = location.getBlockZ();
        int startY = Math.min(location.getBlockY(), location.getWorld().getMaxHeight() - 1);
        for (int y = startY; y >= 0; y--) {
            Block block = location.getWorld().getBlockAt(x, y, z);
            Material type = block.getType();
            if (type != Material.AIR) {
                Location result = location.clone();
                result.setX(x + 0.5);
                result.setY(y + 1.0);
                result.setZ(z + 0.5);
                return result;
            }
        }
        return location.clone();
    }

    private Location getEnderPearlLocation(Location location) {
        return null;
    }

    static {
        ENDER_PEARL_CONSTANTS[0] = "EnderPearlHandler";
        ENDER_PEARL_CONSTANTS[1] = "EnderPearl";
        ENDER_PEARL_MESSAGES[0] = "EnderPearlHandler";
        ENDER_PEARL_MESSAGES[1] = "EnderPearl";
    }

    private static String getEnderPearlMetadata() {
        return ENDER_PEARL_CONSTANTS.length > 1 ? ENDER_PEARL_CONSTANTS[1] : "practice_ender_pearl";
    }
}
