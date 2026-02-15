package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.recipe.data;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.ItemStack;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class MerchantOffer implements RecipeData {
   private ItemStack firstInputItem;
   private ItemStack secondInputItem;
   private ItemStack outputItem;
   private int uses;
   private int maxUses;
   private int xp;
   private int specialPrice;
   private float priceMultiplier;
   private int demand;

   private MerchantOffer(ItemStack firstInputItem, ItemStack secondInputItem, ItemStack outputItem, int uses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
      this.firstInputItem = firstInputItem;
      this.secondInputItem = secondInputItem;
      this.outputItem = outputItem;
      this.uses = uses;
      this.maxUses = maxUses;
      this.xp = xp;
      this.priceMultiplier = priceMultiplier;
      this.demand = demand;
      this.specialPrice = specialPrice;
   }

   public static MerchantOffer of(ItemStack buyItem1, @Nullable ItemStack buyItem2, ItemStack sellItem, int uses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
      return new MerchantOffer(buyItem1, buyItem2, sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
   }

   public static MerchantOffer of(ItemStack buyItem1, ItemStack sellItem, int uses, int maxUses, int xp, int specialPrice, float priceMultiplier, int demand) {
      return new MerchantOffer(buyItem1, (ItemStack)null, sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
   }

   public static MerchantOffer of(ItemStack buyItem1, ItemStack sellItem, int uses, int maxUses, int xp, float priceMultiplier, int demand) {
      return new MerchantOffer(buyItem1, (ItemStack)null, sellItem, uses, maxUses, xp, 0, priceMultiplier, demand);
   }

   public ItemStack getFirstInputItem() {
      return this.firstInputItem;
   }

   public void setFirstInputItem(ItemStack firstInputItem) {
      this.firstInputItem = firstInputItem;
   }

   @Nullable
   public ItemStack getSecondInputItem() {
      return this.secondInputItem;
   }

   public void setSecondInputItem(@Nullable ItemStack secondInputItem) {
      this.secondInputItem = secondInputItem;
   }

   public ItemStack getOutputItem() {
      return this.outputItem;
   }

   public void setOutputItem(ItemStack outputItem) {
      this.outputItem = outputItem;
   }

   public int getUses() {
      return this.uses;
   }

   public void setUses(int uses) {
      this.uses = uses;
   }

   public int getMaxUses() {
      return this.maxUses;
   }

   public void setMaxUses(int maxUses) {
      this.maxUses = maxUses;
   }

   public int getXp() {
      return this.xp;
   }

   public void setXp(int xp) {
      this.xp = xp;
   }

   public float getPriceMultiplier() {
      return this.priceMultiplier;
   }

   public void setPriceMultiplier(float priceMultiplier) {
      this.priceMultiplier = priceMultiplier;
   }

   public int getDemand() {
      return this.demand;
   }

   public void setDemand(int demand) {
      this.demand = demand;
   }

   public int getSpecialPrice() {
      return this.specialPrice;
   }

   public void setSpecialPrice(int specialPrice) {
      this.specialPrice = specialPrice;
   }

   public boolean isOutOfStock() {
      return this.uses >= this.maxUses;
   }
}
