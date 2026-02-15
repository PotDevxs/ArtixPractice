package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class NBTCompound extends NBT {
   protected final Map<String, NBT> tags = new LinkedHashMap();

   public NBTType<NBTCompound> getType() {
      return NBTType.COMPOUND;
   }

   public boolean isEmpty() {
      return this.tags.isEmpty();
   }

   public Set<String> getTagNames() {
      return Collections.unmodifiableSet(this.tags.keySet());
   }

   public Map<String, NBT> getTags() {
      return Collections.unmodifiableMap(this.tags);
   }

   public NBT getTagOrThrow(String key) {
      NBT tag = this.getTagOrNull(key);
      if (tag == null) {
         throw new IllegalStateException(MessageFormat.format("NBT {0} does not exist", key));
      } else {
         return tag;
      }
   }

   public NBT getTagOrNull(String key) {
      return (NBT)this.tags.get(key);
   }

   public <T extends NBT> T getTagOfTypeOrThrow(String key, Class<T> type) {
      NBT tag = this.getTagOrThrow(key);
      if (type.isInstance(tag)) {
         return type.cast(tag);
      } else {
         throw new IllegalStateException(MessageFormat.format("NBT {0} has unexpected type, expected {1}, but got {2}", key, type, tag.getClass()));
      }
   }

   public <T extends NBT> T getTagOfTypeOrNull(String key, Class<T> type) {
      NBT tag = this.getTagOrNull(key);
      return type.isInstance(tag) ? type.cast(tag) : null;
   }

   public <T extends NBT> NBTList<T> getTagListOfTypeOrThrow(String key, Class<T> type) {
      NBTList<? extends NBT> list = (NBTList)this.getTagOfTypeOrThrow(key, NBTList.class);
      if (!type.isAssignableFrom(list.getTagsType().getNBTClass())) {
         throw new IllegalStateException(MessageFormat.format("NBTList {0} tags type has unexpected type, expected {1}, but got {2}", key, type, list.getTagsType().getNBTClass()));
      } else {
         @SuppressWarnings("unchecked")
         NBTList<T> result = (NBTList<T>) list;
         return result;
      }
   }

   public <T extends NBT> NBTList<T> getTagListOfTypeOrNull(String key, Class<T> type) {
      NBTList<? extends NBT> list = (NBTList)this.getTagOfTypeOrNull(key, NBTList.class);
      if (list != null && type.isAssignableFrom(list.getTagsType().getNBTClass())) {
         @SuppressWarnings("unchecked")
         NBTList<T> result = (NBTList<T>) list;
         return result;
      } else {
         return null;
      }
   }

   public NBTCompound getCompoundTagOrThrow(String key) {
      return (NBTCompound)this.getTagOfTypeOrThrow(key, NBTCompound.class);
   }

   public NBTCompound getCompoundTagOrNull(String key) {
      return (NBTCompound)this.getTagOfTypeOrNull(key, NBTCompound.class);
   }

   public NBTNumber getNumberTagOrThrow(String key) {
      return (NBTNumber)this.getTagOfTypeOrThrow(key, NBTNumber.class);
   }

   public NBTNumber getNumberTagOrNull(String key) {
      return (NBTNumber)this.getTagOfTypeOrNull(key, NBTNumber.class);
   }

   public NBTString getStringTagOrThrow(String key) {
      return (NBTString)this.getTagOfTypeOrThrow(key, NBTString.class);
   }

   public NBTString getStringTagOrNull(String key) {
      return (NBTString)this.getTagOfTypeOrNull(key, NBTString.class);
   }

   public NBTList<NBTCompound> getCompoundListTagOrThrow(String key) {
      return this.getTagListOfTypeOrThrow(key, NBTCompound.class);
   }

   public NBTList<NBTCompound> getCompoundListTagOrNull(String key) {
      return this.getTagListOfTypeOrNull(key, NBTCompound.class);
   }

   public NBTList<NBTNumber> getNumberTagListTagOrThrow(String key) {
      return this.getTagListOfTypeOrThrow(key, NBTNumber.class);
   }

   public NBTList<NBTNumber> getNumberListTagOrNull(String key) {
      return this.getTagListOfTypeOrNull(key, NBTNumber.class);
   }

   public NBTList<NBTString> getStringListTagOrThrow(String key) {
      return this.getTagListOfTypeOrThrow(key, NBTString.class);
   }

   public NBTList<NBTString> getStringListTagOrNull(String key) {
      return this.getTagListOfTypeOrNull(key, NBTString.class);
   }

   public String getStringTagValueOrThrow(String key) {
      return this.getStringTagOrThrow(key).getValue();
   }

   public String getStringTagValueOrNull(String key) {
      NBT tag = this.getTagOrNull(key);
      return tag instanceof NBTString ? ((NBTString)tag).getValue() : null;
   }

   public String getStringTagValueOrDefault(String key, String defaultValue) {
      NBT tag = this.getTagOrNull(key);
      return tag instanceof NBTString ? ((NBTString)tag).getValue() : defaultValue;
   }

   public NBT removeTag(String key) {
      return (NBT)this.tags.remove(key);
   }

   public <T extends NBT> T removeTagAndReturnIfType(String key, Class<T> type) {
      NBT tag = this.removeTag(key);
      return type.isInstance(tag) ? type.cast(tag) : null;
   }

   public <T extends NBT> NBTList<T> removeTagAndReturnIfListType(String key, Class<T> type) {
      NBTList<?> list = (NBTList)this.removeTagAndReturnIfType(key, NBTList.class);
      if (list != null && type.isAssignableFrom(list.getTagsType().getNBTClass())) {
         @SuppressWarnings("unchecked")
         NBTList<T> result = (NBTList<T>) list;
         return result;
      } else {
         return null;
      }
   }

   public void setTag(String key, NBT tag) {
      if (tag != null) {
         this.tags.put(key, tag);
      } else {
         this.tags.remove(key);
      }

   }

   public NBTCompound copy() {
      NBTCompound clone = new NBTCompound();
      for (Entry<String, NBT> entry : this.tags.entrySet()) {
         clone.setTag(entry.getKey(), entry.getValue().copy());
      }
      return clone;
   }

   public boolean getBoolean(String string) {
      NBTByte nbtByte = (NBTByte)this.getTagOfTypeOrNull(string, NBTByte.class);
      return nbtByte != null && nbtByte.getAsByte() != 0;
   }

   public boolean equals(Object other) {
      if (other instanceof NBTCompound) {
         return this.isEmpty() && ((NBTCompound)other).isEmpty() ? true : this.tags.equals(((NBTCompound)other).tags);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.tags.hashCode();
   }
}
