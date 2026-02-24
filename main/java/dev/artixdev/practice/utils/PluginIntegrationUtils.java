package dev.artixdev.practice.utils;

import java.lang.reflect.Method;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.practice.interfaces.PluginIntegrationInterface;

public class PluginIntegrationUtils implements PluginIntegrationInterface {
    
    // Check if StaffMode plugin is available
    private static boolean isStaffModeAvailable() {
        try {
            Class.forName("cc.insidious.staffmode.api.StaffModeAPI");
            return Bukkit.getPluginManager().getPlugin("StaffMode") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    // Check if Akuma plugin is available
    private static boolean isAkumaAvailable() {
        try {
            Class.forName("cc.insidious.akuma.api.AkumaAPI");
            return Bukkit.getPluginManager().getPlugin("Akuma") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    public boolean canSeePlayer(Player player1, Player player2) {
        // Check if player1 can see player2
        return true; // Placeholder
    }

    public boolean isPlayerFrozen(Player player) {
        if (!isStaffModeAvailable() || player == null) {
            return false;
        }
        
        try {
            Class<?> staffModeAPIClass = Class.forName("cc.insidious.staffmode.api.StaffModeAPI");
            Method getInstanceMethod = staffModeAPIClass.getMethod("getInstance");
            Object staffModeAPI = getInstanceMethod.invoke(null);
            
            Method getFreezeHandlerMethod = staffModeAPIClass.getMethod("getFreezeHandler");
            Object freezeHandler = getFreezeHandlerMethod.invoke(staffModeAPI);
            
            Class<?> freezeHandlerClass = Class.forName("cc.insidious.staffmode.api.freeze.IFreezeHandler");
            Method isFrozenMethod = freezeHandlerClass.getMethod("isFrozen", Player.class);
            return (Boolean) isFrozenMethod.invoke(freezeHandler, player);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlayerVanished(Player player) {
        if (!isStaffModeAvailable() || player == null) {
            return false;
        }
        
        try {
            Class<?> staffModeAPIClass = Class.forName("cc.insidious.staffmode.api.StaffModeAPI");
            Method getInstanceMethod = staffModeAPIClass.getMethod("getInstance");
            Object staffModeAPI = getInstanceMethod.invoke(null);
            
            Method getVanishHandlerMethod = staffModeAPIClass.getMethod("getVanishHandler");
            Object vanishHandler = getVanishHandlerMethod.invoke(staffModeAPI);
            
            Class<?> vanishHandlerClass = Class.forName("cc.insidious.staffmode.api.vanish.IVanishHandler");
            Method isVanishedMethod = vanishHandlerClass.getMethod("isVanished", Player.class);
            return (Boolean) isVanishedMethod.invoke(vanishHandler, player);
        } catch (Exception e) {
            return false;
        }
    }

    public String getPlayerOriginalName(Player player) {
        if (!isAkumaAvailable() || player == null) {
            return player != null ? player.getName() : null;
        }
        
        try {
            Class<?> akumaAPIClass = Class.forName("cc.insidious.akuma.api.AkumaAPI");
            Method getInstanceMethod = akumaAPIClass.getMethod("getInstance");
            Object akumaAPI = getInstanceMethod.invoke(null);
            
            Method getDisguiseHandlerMethod = akumaAPIClass.getMethod("getDisguiseHandler");
            Object disguiseHandler = getDisguiseHandlerMethod.invoke(akumaAPI);
            
            Class<?> disguiseHandlerClass = Class.forName("cc.insidious.akuma.api.disguise.IDisguiseHandler");
            Method getDisguisedPlayerMethod = disguiseHandlerClass.getMethod("getDisguisedPlayer", UUID.class);
            Object disguisedPlayer = getDisguisedPlayerMethod.invoke(disguiseHandler, player.getUniqueId());
            
            if (disguisedPlayer != null) {
                Class<?> disguisedPlayerClass = Class.forName("cc.insidious.akuma.api.disguise.player.DisguisedPlayer");
                Method getOriginalNameMethod = disguisedPlayerClass.getMethod("getOriginalName");
                return (String) getOriginalNameMethod.invoke(disguisedPlayer);
            }
        } catch (Exception e) {
            // Fallback to player name
        }
        
        return player != null ? player.getName() : null;
    }

    public boolean isPlayerInStaffMode(Player player) {
        if (!isStaffModeAvailable() || player == null) {
            return false;
        }
        
        try {
            Class<?> staffModeAPIClass = Class.forName("cc.insidious.staffmode.api.StaffModeAPI");
            Method getInstanceMethod = staffModeAPIClass.getMethod("getInstance");
            Object staffModeAPI = getInstanceMethod.invoke(null);
            
            Method getStaffModeHandlerMethod = staffModeAPIClass.getMethod("getStaffModeHandler");
            Object staffModeHandler = getStaffModeHandlerMethod.invoke(staffModeAPI);
            
            Class<?> staffModeHandlerClass = Class.forName("cc.insidious.staffmode.api.mode.IStaffModeHandler");
            Method isInStaffModeMethod = staffModeHandlerClass.getMethod("isInStaffMode", Player.class);
            return (Boolean) isInStaffModeMethod.invoke(staffModeHandler, player);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPluginEnabled() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        return pluginManager.isPluginEnabled("ArtixPractice");
    }

    public String getPlayerDisguiseName(Player player) {
        if (!isAkumaAvailable() || player == null) {
            return player != null ? player.getName() : null;
        }
        
        try {
            Class<?> akumaAPIClass = Class.forName("cc.insidious.akuma.api.AkumaAPI");
            Method getInstanceMethod = akumaAPIClass.getMethod("getInstance");
            Object akumaAPI = getInstanceMethod.invoke(null);
            
            Method getDisguiseHandlerMethod = akumaAPIClass.getMethod("getDisguiseHandler");
            Object disguiseHandler = getDisguiseHandlerMethod.invoke(akumaAPI);
            
            Class<?> disguiseHandlerClass = Class.forName("cc.insidious.akuma.api.disguise.IDisguiseHandler");
            Method getDisguisedPlayerMethod = disguiseHandlerClass.getMethod("getDisguisedPlayer", UUID.class);
            Object disguisedPlayer = getDisguisedPlayerMethod.invoke(disguiseHandler, player.getUniqueId());
            
            if (disguisedPlayer != null) {
                Class<?> disguisedPlayerClass = Class.forName("cc.insidious.akuma.api.disguise.player.DisguisedPlayer");
                Method getDisguiseMethod = disguisedPlayerClass.getMethod("getDisguise");
                Object disguise = getDisguiseMethod.invoke(disguisedPlayer);
                
                if (disguise != null) {
                    Class<?> disguiseClass = disguise.getClass();
                    Method getDisguiseNameMethod = disguiseClass.getMethod("getDisguiseName");
                    return (String) getDisguiseNameMethod.invoke(disguise);
                }
            }
        } catch (Exception e) {
            // Fallback to player name
        }
        
        return player != null ? player.getName() : null;
    }

    public Skin getPlayerSkin(Player player) {
        if (player == null) return null;
        return Skin.DEFAULT_SKIN;
    }
}
