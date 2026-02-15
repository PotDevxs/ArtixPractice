package dev.artixdev.api.practice.menu.buttons;

import java.util.function.BiConsumer;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.bukkit.InternalMenuConfig;
import dev.artixdev.api.practice.menu.util.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

public final class BooleanTraitButton<T> extends Button {
   private final T target;
   private final String trait;
   private final BiConsumer<T, Boolean> writeFunction;
   private final Function<T, Boolean> readFunction;
   private final Runnable saveFunction;

   public BooleanTraitButton(T target, String trait, BiConsumer<T, Boolean> writeFunction, Function<T, Boolean> readFunction) {
      this(target, trait, writeFunction, readFunction, () -> {
      });
   }

   public BooleanTraitButton(T target, String trait, BiConsumer<T, Boolean> writeFunction, Function<T, Boolean> readFunction, Runnable saveFunction) {
      this.target = target;
      this.trait = trait;
      this.writeFunction = writeFunction;
      this.readFunction = readFunction;
      this.saveFunction = saveFunction;
   }

   public ItemStack getButtonItem(Player player) {
      boolean enabled = (Boolean)this.readFunction.apply(this.target);
      ItemBuilder itemBuilder = new ItemBuilder(ItemBuilder.parseFromX(enabled ? InternalMenuConfig.BOOLEAN_TRAIT_ENABLED_BUTTON_MATERIAL : InternalMenuConfig.BOOLEAN_TRAIT_DISABLED_BUTTON_MATERIAL, enabled ? XMaterial.LIME_WOOL : XMaterial.RED_WOOL));
      itemBuilder.name((enabled ? InternalMenuConfig.BOOLEAN_TRAIT_ENABLED_BUTTON_NAME : InternalMenuConfig.BOOLEAN_TRAIT_DISABLED_BUTTON_NAME).replace("<trait>", this.trait));
      itemBuilder.durability(enabled ? InternalMenuConfig.BOOLEAN_TRAIT_ENABLED_BUTTON_DURABILITY : InternalMenuConfig.BOOLEAN_TRAIT_DISABLED_BUTTON_DURABILITY);
      itemBuilder.lore(enabled ? InternalMenuConfig.BOOLEAN_TRAIT_ENABLED_BUTTON_LORE : InternalMenuConfig.BOOLEAN_TRAIT_DISABLED_BUTTON_LORE);
      return itemBuilder.build();
   }

   public void clicked(Player player, ClickType clickType) {
      boolean current = (Boolean)this.readFunction.apply(this.target);
      this.writeFunction.accept(this.target, !current);
      this.saveFunction.run();
      player.sendMessage(ChatColor.GREEN + "Set " + this.trait + " trait to " + (current ? "off" : "on"));
   }
}
