package dev.artixdev.api.practice.menu.buttons;

import java.text.DecimalFormat;
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

public final class DoubleTraitButton<T> extends Button {
   private final T target;
   private final String trait;
   private final BiConsumer<T, Double> writeFunction;
   private final Function<T, Double> readFunction;
   private final Runnable saveFunction;

   public DoubleTraitButton(T target, String trait, BiConsumer<T, Double> writeFunction, Function<T, Double> readFunction) {
      this(target, trait, writeFunction, readFunction, () -> {
      });
   }

   public DoubleTraitButton(T target, String trait, BiConsumer<T, Double> writeFunction, Function<T, Double> readFunction, Runnable saveFunction) {
      this.target = target;
      this.trait = trait;
      this.writeFunction = writeFunction;
      this.readFunction = readFunction;
      this.saveFunction = saveFunction;
   }

   public ItemStack getButtonItem(Player player) {
      ItemBuilder itemBuilder = new ItemBuilder(ItemBuilder.parseFromX(InternalMenuConfig.DOUBLE_TRAIT_BUTTON_MATERIAL, XMaterial.GHAST_TEAR));
      itemBuilder.name(InternalMenuConfig.DOUBLE_TRAIT_BUTTON_NAME.replace("<trait>", this.trait));
      itemBuilder.durability(InternalMenuConfig.DOUBLE_TRAIT_BUTTON_DURABILITY);
      Iterator var3 = InternalMenuConfig.DOUBLE_TRAIT_BUTTON_LORE.iterator();

      while(var3.hasNext()) {
         String string = (String)var3.next();
         itemBuilder.lore(string.replace("<trait>", this.trait).replace("<value>", Double.toString((Double)this.readFunction.apply(this.target))));
      }

      return itemBuilder.build();
   }

   public void clicked(Player player, ClickType clickType) {
      double current = (Double)this.readFunction.apply(this.target);
      double change = clickType.isShiftClick() ? 0.5D : 0.1D;
      if (clickType.isRightClick()) {
         change = -change;
      }

      DecimalFormat decimalFormat = new DecimalFormat("#0.0");

      double value;
      try {
         value = Double.parseDouble(decimalFormat.format(current + change));
      } catch (Exception e) {
         value = 0.0D;
      }

      this.writeFunction.accept(this.target, Math.max(0.0D, value));
      player.sendMessage(ChatColor.GREEN + "Set " + this.trait + " trait to " + (current + change));
   }
}
