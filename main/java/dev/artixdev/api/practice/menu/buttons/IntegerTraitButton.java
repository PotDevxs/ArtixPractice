package dev.artixdev.api.practice.menu.buttons;

import java.util.Iterator;
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

public final class IntegerTraitButton<T> extends Button {
   private final T target;
   private final String trait;
   private final BiConsumer<T, Integer> writeFunction;
   private final Function<T, Integer> readFunction;
   private final Runnable saveFunction;

   public IntegerTraitButton(T target, String trait, BiConsumer<T, Integer> writeFunction, Function<T, Integer> readFunction) {
      this(target, trait, writeFunction, readFunction, () -> {
      });
   }

   public IntegerTraitButton(T target, String trait, BiConsumer<T, Integer> writeFunction, Function<T, Integer> readFunction, Runnable saveFunction) {
      this.target = target;
      this.trait = trait;
      this.writeFunction = writeFunction;
      this.readFunction = readFunction;
      this.saveFunction = saveFunction;
   }

   public ItemStack getButtonItem(Player player) {
      ItemBuilder itemBuilder = new ItemBuilder(ItemBuilder.parseFromX(InternalMenuConfig.INTEGER_TRAIT_BUTTON_MATERIAL, XMaterial.GHAST_TEAR));
      itemBuilder.name(InternalMenuConfig.INTEGER_TRAIT_BUTTON_NAME.replace("<trait>", this.trait));
      itemBuilder.durability(InternalMenuConfig.INTEGER_TRAIT_BUTTON_DURABILITY);
      Iterator var3 = InternalMenuConfig.INTEGER_TRAIT_BUTTON_LORE.iterator();

      while(var3.hasNext()) {
         String string = (String)var3.next();
         itemBuilder.lore(string.replace("<trait>", this.trait).replace("<value>", Integer.toString((Integer)this.readFunction.apply(this.target))));
      }

      itemBuilder.amount((Integer)this.readFunction.apply(this.target));
      return itemBuilder.build();
   }

   public void clicked(Player player, ClickType clickType) {
      int current = (Integer)this.readFunction.apply(this.target);
      int change = clickType.isShiftClick() ? 10 : 1;
      if (clickType.isRightClick()) {
         change = -change;
      }

      this.writeFunction.accept(this.target, Math.max(0, current + change));
      this.saveFunction.run();
      player.sendMessage(ChatColor.GREEN + "Set " + this.trait + " trait to " + (current + change));
   }
}
