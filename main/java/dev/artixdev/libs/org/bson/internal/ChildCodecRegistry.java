package dev.artixdev.libs.org.bson.internal;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.Codec;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

class ChildCodecRegistry<T> implements CodecRegistry {
   private final ChildCodecRegistry<?> parent;
   private final CycleDetectingCodecRegistry registry;
   private final Class<T> codecClass;
   private final List<Type> types;

   ChildCodecRegistry(CycleDetectingCodecRegistry registry, Class<T> codecClass, List<Type> types) {
      this.codecClass = codecClass;
      this.parent = null;
      this.registry = registry;
      this.types = types;
   }

   private ChildCodecRegistry(ChildCodecRegistry<?> parent, Class<T> codecClass, List<Type> types) {
      this.parent = parent;
      this.codecClass = codecClass;
      this.registry = parent.registry;
      this.types = types;
   }

   public Class<T> getCodecClass() {
      return this.codecClass;
   }

   public Optional<List<Type>> getTypes() {
      return Optional.ofNullable(this.types);
   }

   public <U> Codec<U> get(Class<U> clazz) {
      return (Codec)(this.hasCycles(clazz) ? new LazyCodec(this.registry, clazz, (List)null) : this.registry.get(new ChildCodecRegistry(this, clazz, (List)null)));
   }

   public <U> Codec<U> get(Class<U> clazz, List<Type> typeArguments) {
      Assertions.notNull("typeArguments", typeArguments);
      Assertions.isTrueArgument(String.format("typeArguments size should equal the number of type parameters in class %s, but is %d", clazz, typeArguments.size()), clazz.getTypeParameters().length == typeArguments.size());
      return (Codec)(this.hasCycles(clazz) ? new LazyCodec(this.registry, clazz, typeArguments) : this.registry.get(new ChildCodecRegistry(this, clazz, typeArguments)));
   }

   public <U> Codec<U> get(Class<U> clazz, CodecRegistry registry) {
      return this.get(clazz, Collections.emptyList(), registry);
   }

   public <U> Codec<U> get(Class<U> clazz, List<Type> typeArguments, CodecRegistry registry) {
      return this.registry.get(clazz, typeArguments, registry);
   }

   private <U> Boolean hasCycles(Class<U> theClass) {
      for(ChildCodecRegistry current = this; current != null; current = current.parent) {
         if (current.codecClass.equals(theClass)) {
            return true;
         }
      }

      return false;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ChildCodecRegistry<?> that = (ChildCodecRegistry)o;
         if (!this.codecClass.equals(that.codecClass)) {
            return false;
         } else if (!Objects.equals(this.parent, that.parent)) {
            return false;
         } else {
            return this.registry.equals(that.registry);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.parent != null ? this.parent.hashCode() : 0;
      result = 31 * result + this.registry.hashCode();
      result = 31 * result + this.codecClass.hashCode();
      return result;
   }
}
