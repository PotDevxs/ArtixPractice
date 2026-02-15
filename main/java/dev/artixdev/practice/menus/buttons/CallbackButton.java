package dev.artixdev.practice.menus.buttons;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.enums.PlayerState;
import dev.artixdev.practice.utils.other.Callback;
import dev.artixdev.practice.models.PlayerProfile;

/**
 * Callback Button
 * Button that executes a callback when clicked
 */
public class CallbackButton extends Button {
   
   private final List<String> lore;
   private final Callback<PlayerProfile> callback;
   private final PlayerProfile playerProfile;
   private final int slot;
   
   /**
    * Constructor
    * @param lore the button lore
    * @param callback the callback
    * @param playerProfile the player profile
    * @param slot the slot
    */
   public CallbackButton(List<String> lore, Callback<PlayerProfile> callback, PlayerProfile playerProfile, int slot) {
      this.lore = ImmutableList.copyOf(Preconditions.checkNotNull(lore, "Lore cannot be null"));
      this.callback = Preconditions.checkNotNull(callback, "Callback cannot be null");
      this.playerProfile = Preconditions.checkNotNull(playerProfile, "PlayerProfile cannot be null");
      this.slot = slot;
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      try {
         // Execute callback
         callback.call(playerProfile);
         
         // Send feedback to player
         player.sendMessage("Action executed successfully!");
         
      } catch (Exception e) {
         player.sendMessage("An error occurred while executing the action.");
         e.printStackTrace();
      }
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      // Create item based on player profile
      return createItemForPlayer(player);
   }
   
   /**
    * Create item for player
    * @param player the player
    * @return item stack
    */
   private ItemStack createItemForPlayer(Player player) {
      try {
         // Get item material based on player profile
         org.bukkit.Material material = getMaterialForProfile(playerProfile);
         
         // Create item stack
         ItemStack item = new ItemStack(material);
         
         // Set item meta
         org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
         if (meta != null) {
            meta.setDisplayName(getDisplayName(playerProfile));
            meta.setLore(lore);
            item.setItemMeta(meta);
         }
         
         return item;
         
      } catch (Exception e) {
         // Return default item on error
         return new ItemStack(org.bukkit.Material.BARRIER);
      }
   }
   
   /**
    * Get material for profile
    * @param profile the player profile
    * @return material
    */
   private org.bukkit.Material getMaterialForProfile(PlayerProfile profile) {
      // Return different materials based on profile state
      PlayerState state = profile.getState();
      if (state == null) {
         return org.bukkit.Material.PAPER;
      }
      
      switch (state) {
         case FIGHTING:
            return org.bukkit.Material.DIAMOND_SWORD;
         case QUEUE:
            return XMaterial.CLOCK.parseMaterial();
         case SPECTATING:
            return XMaterial.ENDER_EYE.parseMaterial();
         case LOBBY:
            return org.bukkit.Material.NETHER_STAR;
         default:
            return org.bukkit.Material.PAPER;
      }
   }
   
   /**
    * Get display name
    * @param profile the player profile
    * @return display name
    */
   private String getDisplayName(PlayerProfile profile) {
      return String.format("&a%s &7- &f%s", 
         profile.getName(), 
         profile.getState().name());
   }
   
   /**
    * Get callback
    * @return callback
    */
   public Callback<PlayerProfile> getCallback() {
      return callback;
   }
   
   /**
    * Get player profile
    * @return player profile
    */
   public PlayerProfile getPlayerProfile() {
      return playerProfile;
   }
   
   /**
    * Get slot
    * @return slot
    */
   public int getSlot() {
      return slot;
   }
   
   /**
    * Get lore
    * @return lore
    */
   public List<String> getLore() {
      return lore;
   }
   
   @Override
   public String toString() {
      return String.format("CallbackButton{slot=%d, profile=%s, callback=%s}", 
         slot, playerProfile.getName(), callback.getClass().getSimpleName());
   }
}
