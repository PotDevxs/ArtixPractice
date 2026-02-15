package dev.artixdev.libs.org.bson.codecs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonWriter;
import dev.artixdev.libs.org.bson.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecConfigurationException;

abstract class AbstractCollectionCodec<T, C extends Collection<T>> implements Codec<C> {
   private final Class<C> clazz;
   private final Supplier<C> supplier;

   AbstractCollectionCodec(Class<C> clazz) {
      this.clazz = (Class)Assertions.notNull("clazz", clazz);
      if (clazz != Collection.class && clazz != List.class && clazz != AbstractCollection.class && clazz != AbstractList.class && clazz != ArrayList.class) {
         if (clazz != Set.class && clazz != AbstractSet.class && clazz != HashSet.class) {
            if (clazz != NavigableSet.class && clazz != SortedSet.class && clazz != TreeSet.class) {
               Supplier supplier;
               try {
                  Constructor<? extends Collection<?>> constructor = clazz.getDeclaredConstructor();
                  supplier = () -> {
                     try {
                        return (C) constructor.newInstance();
                     } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        throw new CodecConfigurationException(String.format("Can not invoke no-args constructor for Collection class %s", clazz), e);
                     }
                  };
               } catch (NoSuchMethodException e) {
                  supplier = () -> {
                     throw new CodecConfigurationException(String.format("No no-args constructor for Collection class %s", clazz), e);
                  };
               }

               this.supplier = supplier;
            } else {
               this.supplier = () -> (C) new TreeSet();
            }
         } else {
            this.supplier = () -> (C) new HashSet();
         }
      } else {
         this.supplier = () -> (C) new ArrayList();
      }

   }

   abstract T readValue(BsonReader reader, DecoderContext decoderContext);

   abstract void writeValue(BsonWriter writer, T value, EncoderContext encoderContext);

   public C decode(BsonReader reader, DecoderContext decoderContext) {
      reader.readStartArray();
      C collection = this.supplier.get();
      while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
         if (reader.getCurrentBsonType() == BsonType.NULL) {
            reader.readNull();
            collection.add(null);
         } else {
            collection.add(this.readValue(reader, decoderContext));
         }
      }
      reader.readEndArray();
      return collection;
   }

   public void encode(BsonWriter writer, C value, EncoderContext encoderContext) {
      writer.writeStartArray();
      Iterator<T> iterator = value.iterator();

      while(iterator.hasNext()) {
         T cur = iterator.next();
         if (cur == null) {
            writer.writeNull();
         } else {
            this.writeValue(writer, cur, encoderContext);
         }
      }

      writer.writeEndArray();
   }

   public Class<C> getEncoderClass() {
      return this.clazz;
   }
}
