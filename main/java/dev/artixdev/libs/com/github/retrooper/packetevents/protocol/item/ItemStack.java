package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class ItemStack {
   public static final ItemStack EMPTY;
   private final ItemType type;
   private int amount;
   @Nullable
   private NBTCompound nbt;
   private int legacyData;
   private boolean cachedIsEmpty;

   private ItemStack(ItemType type, int amount, @Nullable NBTCompound nbt, int legacyData) {
      this.legacyData = -1;
      this.cachedIsEmpty = false;
      this.type = type;
      this.amount = amount;
      this.nbt = nbt;
      this.legacyData = legacyData;
      this.updateCachedEmptyStatus();
   }

   public int getMaxStackSize() {
      return this.getType().getMaxAmount();
   }

   public boolean isStackable() {
      return this.getMaxStackSize() > 1 && (!this.isDamageableItem() || !this.isDamaged());
   }

   public boolean isDamageableItem() {
      if (!this.cachedIsEmpty && this.getType().getMaxDurability() > 0) {
         NBTCompound tag = this.getNBT();
         return tag == null || !tag.getBoolean("Unbreakable");
      } else {
         return false;
      }
   }

   public boolean isDamaged() {
      return this.isDamageableItem() && this.getDamageValue() > 0;
   }

   public int getDamageValue() {
      if (this.nbt == null) {
         return 0;
      } else {
         NBTInt damage = (NBTInt)this.nbt.getTagOfTypeOrNull("Damage", NBTInt.class);
         return damage == null ? 0 : damage.getAsInt();
      }
   }

   public void setDamageValue(int damage) {
      this.getOrCreateTag().setTag("Damage", new NBTInt(Math.max(0, damage)));
   }

   public int getMaxDamage() {
      return this.getType().getMaxDurability();
   }

   public NBTCompound getOrCreateTag() {
      if (this.nbt == null) {
         this.nbt = new NBTCompound();
      }

      return this.nbt;
   }

   private void updateCachedEmptyStatus() {
      this.cachedIsEmpty = this.isEmpty();
   }

   public ItemType getType() {
      return this.cachedIsEmpty ? ItemTypes.AIR : this.type;
   }

   public int getAmount() {
      return this.cachedIsEmpty ? 0 : this.amount;
   }

   public void shrink(int amount) {
      this.setAmount(this.getAmount() - amount);
   }

   public void grow(int amount) {
      this.setAmount(this.getAmount() + amount);
   }

   public void setAmount(int amount) {
      this.amount = amount;
      this.updateCachedEmptyStatus();
   }

   public ItemStack split(int toTake) {
      int i = Math.min(toTake, this.getAmount());
      ItemStack itemstack = this.copy();
      itemstack.setAmount(i);
      this.shrink(i);
      return itemstack;
   }

   public ItemStack copy() {
      return this.cachedIsEmpty ? EMPTY : new ItemStack(this.type, this.amount, this.nbt == null ? null : this.nbt.copy(), this.legacyData);
   }

   @Nullable
   public NBTCompound getNBT() {
      return this.nbt;
   }

   public void setNBT(NBTCompound nbt) {
      this.nbt = nbt;
   }

   public int getLegacyData() {
      return this.legacyData;
   }

   public void setLegacyData(int legacyData) {
      this.legacyData = legacyData;
   }

   public boolean isEnchantable(ClientVersion version) {
      if (this.getType() == ItemTypes.BOOK) {
         return this.getAmount() == 1;
      } else if (this.getType() == ItemTypes.ENCHANTED_BOOK) {
         return false;
      } else {
         return this.getMaxStackSize() == 1 && this.canBeDepleted() && !this.isEnchanted(version);
      }
   }

   public boolean isEnchanted(ClientVersion version) {
      String tagName = this.getEnchantmentsTagName(version);
      return !this.isEmpty() && this.nbt != null && this.nbt.getCompoundListTagOrNull(tagName) != null && !this.nbt.getCompoundListTagOrNull(tagName).getTags().isEmpty();
   }

   private List<Enchantment> getEnchantments(@Nullable NBTCompound nbt, ClientVersion version) {
      String tagName = this.getEnchantmentsTagName(version);
      if (nbt != null && nbt.getCompoundListTagOrNull(tagName) != null) {
         NBTList<NBTCompound> nbtList = nbt.getCompoundListTagOrNull(tagName);
         List<NBTCompound> compounds = nbtList.getTags();
         List<Enchantment> enchantments = new ArrayList(compounds.size());
         Iterator var7 = compounds.iterator();

         while(var7.hasNext()) {
            NBTCompound compound = (NBTCompound)var7.next();
            EnchantmentType type = getEnchantmentTypeFromTag(compound, version);
            if (type != null) {
               NBTShort levelTag = (NBTShort)compound.getTagOfTypeOrNull("lvl", NBTShort.class);
               int level = levelTag.getAsInt();
               Enchantment enchantment = Enchantment.builder().type(type).level(level).build();
               enchantments.add(enchantment);
            }
         }

         return enchantments;
      } else {
         return new ArrayList(0);
      }
   }

   public List<Enchantment> getEnchantments(ClientVersion version) {
      return this.getEnchantments(this.nbt, version);
   }

   public int getEnchantmentLevel(EnchantmentType enchantment, ClientVersion version) {
      if (this.isEnchanted(version)) {
         assert this.nbt != null;

         String tagName = this.getEnchantmentsTagName(version);
         Iterator var4 = this.nbt.getCompoundListTagOrNull(tagName).getTags().iterator();

         while(var4.hasNext()) {
            NBTCompound base = (NBTCompound)var4.next();
            EnchantmentType type = getEnchantmentTypeFromTag(base, version);
            if (enchantment == type) {
               return ((NBTShort)base.getTagOfTypeOrNull("lvl", NBTShort.class)).getAsInt();
            }
         }
      }

      return 0;
   }

   @Nullable
   private static EnchantmentType getEnchantmentTypeFromTag(NBTCompound tag, ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
         String id = tag.getStringTagValueOrNull("id");
         return EnchantmentTypes.getByName(id);
      } else {
         NBTShort idTag = (NBTShort)tag.getTagOfTypeOrNull("id", NBTShort.class);
         return EnchantmentTypes.getById(version, idTag.getAsInt());
      }
   }

   public void setEnchantments(List<Enchantment> enchantments, ClientVersion version) {
      this.nbt = this.getOrCreateTag();
      String tagName = this.getEnchantmentsTagName(version);
      if (enchantments.isEmpty()) {
         if (this.nbt.getTagOrNull(tagName) != null) {
            this.nbt.removeTag(tagName);
         }
      } else {
         List<NBTCompound> list = new ArrayList();
         Iterator var5 = enchantments.iterator();

         while(var5.hasNext()) {
            Enchantment enchantment = (Enchantment)var5.next();
            NBTCompound compound = new NBTCompound();
            if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
               compound.setTag("id", new NBTString(enchantment.getType().getName().toString()));
            } else {
               compound.setTag("id", new NBTShort((short)enchantment.getType().getId(version)));
            }

            compound.setTag("lvl", new NBTShort((short)enchantment.getLevel()));
            list.add(compound);
         }

         assert this.nbt != null;

         this.nbt.setTag(tagName, new NBTList(NBTType.COMPOUND, list));
      }

   }

   public String getEnchantmentsTagName(ClientVersion version) {
      String tagName = version.isNewerThanOrEquals(ClientVersion.V_1_13) ? "Enchantments" : "ench";
      if (this.type == ItemTypes.ENCHANTED_BOOK) {
         tagName = "StoredEnchantments";
      }

      return tagName;
   }

   public boolean canBeDepleted() {
      return this.getType().getMaxDurability() > 0;
   }

   public boolean is(ItemType type) {
      return this.getType() == type;
   }

   public static boolean isSameItemSameTags(ItemStack stack, ItemStack otherStack) {
      return stack.is(otherStack.getType()) && tagMatches(stack, otherStack);
   }

   public static boolean tagMatches(ItemStack left, ItemStack right) {
      if (left == right) {
         return true;
      } else if (left == null) {
         return right.isEmpty();
      } else {
         return right == null ? left.isEmpty() : Objects.equals(left.nbt, right.nbt);
      }
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemStack)) {
         return false;
      } else {
         ItemStack itemStack = (ItemStack)obj;
         return this.getType().equals(itemStack.getType()) && this.amount == itemStack.amount && Objects.equals(this.nbt, itemStack.nbt) && this.legacyData == itemStack.legacyData;
      }
   }

   public String toString() {
      if (this.cachedIsEmpty) {
         return "ItemStack[null]";
      } else {
         String identifier = this.type == null ? "null" : this.type.getName().toString();
         int maxAmount = this.getType().getMaxAmount();
         return "ItemStack[type=" + identifier + ", amount=" + this.amount + "/" + maxAmount + ", nbt tag names: " + (this.nbt != null ? this.nbt.getTagNames() : "[null]") + ", legacyData=" + this.legacyData + "]";
      }
   }

   public boolean isEmpty() {
      return this.type == null || this.type == ItemTypes.AIR || this.amount <= 0;
   }

   public static ItemStack.Builder builder() {
      return new ItemStack.Builder();
   }

   // $FF: synthetic method
   ItemStack(ItemType x0, int x1, NBTCompound x2, int x3, Object x4) {
      this(x0, x1, x2, x3);
   }

   static {
      EMPTY = new ItemStack(ItemTypes.AIR, 0, new NBTCompound(), 0);
   }

   public static class Builder {
      private ItemType type;
      private int amount = 1;
      private NBTCompound nbt = null;
      private int legacyData = -1;

      public ItemStack.Builder type(ItemType type) {
         this.type = type;
         return this;
      }

      public ItemStack.Builder amount(int amount) {
         this.amount = amount;
         return this;
      }

      public ItemStack.Builder nbt(NBTCompound nbt) {
         this.nbt = nbt;
         return this;
      }

      public ItemStack.Builder legacyData(int legacyData) {
         this.legacyData = legacyData;
         return this;
      }

      public ItemStack build() {
         return new ItemStack(this.type, this.amount, this.nbt, this.legacyData);
      }
   }
}
