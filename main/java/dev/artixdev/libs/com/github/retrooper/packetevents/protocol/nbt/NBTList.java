package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NBTList<T extends NBT> extends NBT {
   protected final NBTType<T> type;
   protected final List<T> tags = new ArrayList();

   public NBTList(NBTType<T> type) {
      this.type = type;
   }

   public NBTList(NBTType<T> type, List<T> tags) {
      this.type = type;
      this.tags.addAll(tags);
   }

   public static NBTList<NBTCompound> createCompoundList() {
      return new NBTList(NBTType.COMPOUND);
   }

   public static NBTList<NBTString> createStringList() {
      return new NBTList(NBTType.STRING);
   }

   public NBTType<NBTList> getType() {
      return NBTType.LIST;
   }

   public NBTType<T> getTagsType() {
      return this.type;
   }

   public boolean isEmpty() {
      return this.tags.isEmpty();
   }

   public int size() {
      return this.tags.size();
   }

   public List<T> getTags() {
      return Collections.unmodifiableList(this.tags);
   }

   public T getTag(int index) {
      return this.tags.get(index);
   }

   public void setTag(int index, T tag) {
      this.validateAddTag(tag);
      this.tags.set(index, tag);
   }

   public void addTag(int index, T tag) {
      this.validateAddTag(tag);
      this.tags.add(index, tag);
   }

   public void addTag(T tag) {
      this.validateAddTag(tag);
      this.tags.add(tag);
   }

   public void addTagUnsafe(NBT nbt) {
      @SuppressWarnings("unchecked")
      T tag = (T) nbt;
      this.addTag(tag);
   }

   protected void validateAddTag(T tag) {
      if (this.type != tag.getType()) {
         throw new IllegalArgumentException(MessageFormat.format("Invalid tag type. Expected {0}, got {1}.", this.type.getNBTClass(), tag.getClass()));
      }
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NBTList<T> other = (NBTList)obj;
         return Objects.equals(this.type, other.type) && Objects.equals(this.tags, other.tags);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.tags});
   }

   public NBTList<T> copy() {
      List<T> newTags = new ArrayList();
      for (T tag : this.tags) {
         @SuppressWarnings("unchecked")
         T copiedTag = (T) tag.copy();
         newTags.add(copiedTag);
      }
      return new NBTList(this.type, newTags);
   }
}
