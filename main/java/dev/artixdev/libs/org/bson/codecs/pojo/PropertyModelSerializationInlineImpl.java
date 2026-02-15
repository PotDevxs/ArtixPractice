package dev.artixdev.libs.org.bson.codecs.pojo;

class PropertyModelSerializationInlineImpl<T> implements PropertySerialization<T> {
   private final PropertySerialization<T> wrapped;

   PropertyModelSerializationInlineImpl(PropertySerialization<T> wrapped) {
      this.wrapped = wrapped;
   }

   public boolean shouldSerialize(T value) {
      return this.wrapped.shouldSerialize(value);
   }

   public boolean inline() {
      return true;
   }
}
