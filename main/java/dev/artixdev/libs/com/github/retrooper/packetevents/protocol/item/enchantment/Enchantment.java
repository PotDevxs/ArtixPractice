package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.enchantment;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;

public class Enchantment {
   private EnchantmentType type;
   private int level;

   private Enchantment(EnchantmentType type, int level) {
      this.type = type;
      this.level = level;
   }

   public EnchantmentType getType() {
      return this.type;
   }

   public void setType(EnchantmentType type) {
      this.type = type;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public static Enchantment.Builder builder() {
      return new Enchantment.Builder();
   }

   // $FF: synthetic method
   Enchantment(EnchantmentType x0, int x1, Object x2) {
      this(x0, x1);
   }

   public static class Builder {
      private EnchantmentType type;
      private int level;

      public Enchantment.Builder type(EnchantmentType type) {
         this.type = type;
         return this;
      }

      public Enchantment.Builder level(int level) {
         this.level = level;
         return this;
      }

      public Enchantment build() {
         return new Enchantment(this.type, this.level);
      }
   }
}
