package dev.artixdev.libs.org.bson.codecs.pojo;

import java.util.Objects;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.codecs.Codec;

public final class PropertyModel<T> {
   private final String name;
   private final String readName;
   private final String writeName;
   private final TypeData<T> typeData;
   private final Codec<T> codec;
   private final PropertySerialization<T> propertySerialization;
   private final Boolean useDiscriminator;
   private final PropertyAccessor<T> propertyAccessor;
   private final String error;
   private volatile Codec<T> cachedCodec;
   private final BsonType bsonRepresentation;

   PropertyModel(String name, String readName, String writeName, TypeData<T> typeData, Codec<T> codec, PropertySerialization<T> propertySerialization, Boolean useDiscriminator, PropertyAccessor<T> propertyAccessor, String error, BsonType bsonRepresentation) {
      this.name = name;
      this.readName = readName;
      this.writeName = writeName;
      this.typeData = typeData;
      this.codec = codec;
      this.cachedCodec = codec;
      this.propertySerialization = propertySerialization;
      this.useDiscriminator = useDiscriminator;
      this.propertyAccessor = propertyAccessor;
      this.error = error;
      this.bsonRepresentation = bsonRepresentation;
   }

   public static <T> PropertyModelBuilder<T> builder() {
      return new PropertyModelBuilder();
   }

   public String getName() {
      return this.name;
   }

   public String getWriteName() {
      return this.writeName;
   }

   public String getReadName() {
      return this.readName;
   }

   public boolean isWritable() {
      return this.writeName != null;
   }

   public boolean isReadable() {
      return this.readName != null;
   }

   public TypeData<T> getTypeData() {
      return this.typeData;
   }

   public Codec<T> getCodec() {
      return this.codec;
   }

   public BsonType getBsonRepresentation() {
      return this.bsonRepresentation;
   }

   public boolean shouldSerialize(T value) {
      return this.propertySerialization.shouldSerialize(value);
   }

   public PropertyAccessor<T> getPropertyAccessor() {
      return this.propertyAccessor;
   }

   public Boolean useDiscriminator() {
      return this.useDiscriminator;
   }

   public String toString() {
      return "PropertyModel{propertyName='" + this.name + "', readName='" + this.readName + "', writeName='" + this.writeName + "', typeData=" + this.typeData + "}";
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         PropertyModel that;
         label121: {
            that = (PropertyModel)o;
            if (this.getName() != null) {
               if (this.getName().equals(that.getName())) {
                  break label121;
               }
            } else if (that.getName() == null) {
               break label121;
            }

            return false;
         }

         label114: {
            if (this.getReadName() != null) {
               if (this.getReadName().equals(that.getReadName())) {
                  break label114;
               }
            } else if (that.getReadName() == null) {
               break label114;
            }

            return false;
         }

         if (this.getWriteName() != null) {
            if (!this.getWriteName().equals(that.getWriteName())) {
               return false;
            }
         } else if (that.getWriteName() != null) {
            return false;
         }

         if (this.getTypeData() != null) {
            if (!this.getTypeData().equals(that.getTypeData())) {
               return false;
            }
         } else if (that.getTypeData() != null) {
            return false;
         }

         label93: {
            if (this.getCodec() != null) {
               if (this.getCodec().equals(that.getCodec())) {
                  break label93;
               }
            } else if (that.getCodec() == null) {
               break label93;
            }

            return false;
         }

         label86: {
            if (this.getPropertySerialization() != null) {
               if (this.getPropertySerialization().equals(that.getPropertySerialization())) {
                  break label86;
               }
            } else if (that.getPropertySerialization() == null) {
               break label86;
            }

            return false;
         }

         if (!Objects.equals(this.useDiscriminator, that.useDiscriminator)) {
            return false;
         } else {
            label78: {
               if (this.getPropertyAccessor() != null) {
                  if (this.getPropertyAccessor().equals(that.getPropertyAccessor())) {
                     break label78;
                  }
               } else if (that.getPropertyAccessor() == null) {
                  break label78;
               }

               return false;
            }

            label71: {
               if (this.getError() != null) {
                  if (this.getError().equals(that.getError())) {
                     break label71;
                  }
               } else if (that.getError() == null) {
                  break label71;
               }

               return false;
            }

            if (this.getCachedCodec() != null) {
               if (!this.getCachedCodec().equals(that.getCachedCodec())) {
                  return false;
               }
            } else if (that.getCachedCodec() != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.getName() != null ? this.getName().hashCode() : 0;
      result = 31 * result + (this.getReadName() != null ? this.getReadName().hashCode() : 0);
      result = 31 * result + (this.getWriteName() != null ? this.getWriteName().hashCode() : 0);
      result = 31 * result + (this.getTypeData() != null ? this.getTypeData().hashCode() : 0);
      result = 31 * result + (this.getCodec() != null ? this.getCodec().hashCode() : 0);
      result = 31 * result + (this.getPropertySerialization() != null ? this.getPropertySerialization().hashCode() : 0);
      result = 31 * result + (this.useDiscriminator != null ? this.useDiscriminator.hashCode() : 0);
      result = 31 * result + (this.getPropertyAccessor() != null ? this.getPropertyAccessor().hashCode() : 0);
      result = 31 * result + (this.getError() != null ? this.getError().hashCode() : 0);
      result = 31 * result + (this.getCachedCodec() != null ? this.getCachedCodec().hashCode() : 0);
      return result;
   }

   boolean hasError() {
      return this.error != null;
   }

   String getError() {
      return this.error;
   }

   PropertySerialization<T> getPropertySerialization() {
      return this.propertySerialization;
   }

   void cachedCodec(Codec<T> codec) {
      this.cachedCodec = codec;
   }

   Codec<T> getCachedCodec() {
      return this.cachedCodec;
   }
}
