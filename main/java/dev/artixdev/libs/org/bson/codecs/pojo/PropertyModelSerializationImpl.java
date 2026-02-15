package dev.artixdev.libs.org.bson.codecs.pojo;

class PropertyModelSerializationImpl<T> implements PropertySerialization<T> {
   public boolean shouldSerialize(T value) {
      return value != null;
   }
}
