package dev.artixdev.practice.utils.other;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import dev.artixdev.api.practice.nametag.util.VersionUtil;

/**
 * BlockUtils
 * 
 * Utility class for block-related operations.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class BlockUtils {
    
    /**
     * Check if a block is a player skull.
     * 
     * @param block the block to check
     * @return true if the block is a player skull
     */
    public static boolean isPlayerSkull(Block block) {
        if (block == null) {
            return false;
        }
        
        if (VersionUtil.MINOR_VERSION != 8) {
            return isPlayerSkullModern(block);
        } else {
            return isPlayerSkullLegacy(block);
        }
    }
    
    /**
     * Check if a block is a skeleton skull (modern versions).
     * 
     * @param block the block to check
     * @return true if the block is a skeleton skull
     */
    public static boolean isSkeletonSkull(Block block) {
        if (block == null) {
            return false;
        }
        
        if (VersionUtil.MINOR_VERSION != 8) {
            return isSkeletonSkullModern(block);
        } else {
            return isSkeletonSkullLegacy(block);
        }
    }
    
    /**
     * Check if a block is a skull (any type) for modern versions.
     * 
     * @param block the block to check
     * @return true if the block is a skull
     */
    private static boolean isPlayerSkullModern(Block block) {
        Material material = block.getType();
        String materialName = material.name();
        
        // Check for PLAYER_HEAD or PLAYER_WALL_HEAD
        if (materialName.equals("PLAYER_HEAD") || materialName.equals("PLAYER_WALL_HEAD")) {
            BlockState state = block.getState();
            if (state instanceof Skull) {
                Skull skull = (Skull) state;
                return skull.getSkullType() == SkullType.PLAYER;
            }
        }
        
        return false;
    }
    
    /**
     * Check if a block is a skull (any type) for legacy versions (1.8).
     * 
     * @param block the block to check
     * @return true if the block is a skull
     */
    private static boolean isPlayerSkullLegacy(Block block) {
        Material material = block.getType();
        String materialName = material.name();
        
        // Check for SKULL block
        if (materialName.equals("SKULL")) {
            BlockState state = block.getState();
            if (state instanceof Skull) {
                Skull skull = (Skull) state;
                return skull.getSkullType() == SkullType.PLAYER;
            }
        }
        
        return false;
    }
    
    /**
     * Check if a block is a skeleton skull for modern versions.
     * 
     * @param block the block to check
     * @return true if the block is a skeleton skull
     */
    private static boolean isSkeletonSkullModern(Block block) {
        Material material = block.getType();
        String materialName = material.name();
        
        // Check for SKELETON_SKULL or SKELETON_WALL_SKULL
        return materialName.equals("SKELETON_SKULL") || materialName.equals("SKELETON_WALL_SKULL");
    }
    
    /**
     * Check if a block is a skeleton skull for legacy versions (1.8).
     * 
     * @param block the block to check
     * @return true if the block is a skeleton skull
     */
    private static boolean isSkeletonSkullLegacy(Block block) {
        Material material = block.getType();
        String materialName = material.name();
        
        // Check for SKULL block
        if (materialName.equals("SKULL")) {
            BlockState state = block.getState();
            if (state instanceof Skull) {
                Skull skull = (Skull) state;
                return skull.getSkullType() == SkullType.SKELETON;
            }
        }
        
        return false;
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private BlockUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
