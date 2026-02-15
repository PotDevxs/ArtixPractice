package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.data;

public class EntityData {
   private int index;
   private EntityDataType<?> type;
   private Object value;

   public EntityData(int index, EntityDataType<?> type, Object value) {
      this.index = index;
      this.type = type;
      this.value = value;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public EntityDataType<?> getType() {
      return this.type;
   }

   public void setType(EntityDataType<?> type) {
      this.type = type;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }
}
