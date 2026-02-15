package me.drizzy.api.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface INMSImpl {
   void removeArrows(Player var1);

   void setCanCollide(Player var1, boolean var2);

   boolean isViewingInventory(Player var1);

   void sendEatingAnimation(Player var1);

   void setJumping(Player var1);

   void animateDeath(Player var1);

   void startRightClick(Player var1);

   void stopRightClick(Player var1);

   void setUnbreakable(ItemStack var1);

   void spawnLightning(Location var1);
}
