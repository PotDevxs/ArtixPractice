package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt;

import java.util.Objects;

public class NBTString extends NBT {
   protected final String string;

   public NBTString(String string) {
      this.string = string;
   }

   public NBTType<NBTString> getType() {
      return NBTType.STRING;
   }

   public String getValue() {
      return this.string;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         NBTString other = (NBTString)obj;
         return Objects.equals(this.string, other.string);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.string});
   }

   public NBTString copy() {
      return this;
   }
}
