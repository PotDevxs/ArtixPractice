package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinaryReader;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonType;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.RawBsonDocument;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.DecoderContext;
import dev.artixdev.libs.org.bson.io.ByteBufferBsonInput;
import dev.artixdev.libs.org.bson.json.JsonMode;
import dev.artixdev.libs.org.bson.json.JsonWriter;
import dev.artixdev.libs.org.bson.json.JsonWriterSettings;

final class ByteBufBsonDocument extends BsonDocument {
   private static final long serialVersionUID = 2L;
   private final transient ByteBuf byteBuf;

   static List<ByteBufBsonDocument> createList(ByteBufferBsonOutput bsonOutput, int startPosition) {
      List<ByteBuf> duplicateByteBuffers = bsonOutput.getByteBuffers();
      CompositeByteBuf outputByteBuf = new CompositeByteBuf(duplicateByteBuffers);
      outputByteBuf.position(startPosition);
      List<ByteBufBsonDocument> documents = new ArrayList();
      int curDocumentStartPosition = startPosition;

      ByteBuf byteBuffer;
      while(outputByteBuf.hasRemaining()) {
         int documentSizeInBytes = outputByteBuf.getInt();
         byteBuffer = outputByteBuf.duplicate();
         byteBuffer.position(curDocumentStartPosition);
         byteBuffer.limit(curDocumentStartPosition + documentSizeInBytes);
         documents.add(new ByteBufBsonDocument(byteBuffer));
         curDocumentStartPosition += documentSizeInBytes;
         outputByteBuf.position(outputByteBuf.position() + documentSizeInBytes - 4);
      }

      Iterator<ByteBuf> iterator = duplicateByteBuffers.iterator();

      while(iterator.hasNext()) {
         byteBuffer = iterator.next();
         byteBuffer.release();
      }

      return documents;
   }

   static ByteBufBsonDocument createOne(ByteBufferBsonOutput bsonOutput, int startPosition) {
      List<ByteBuf> duplicateByteBuffers = bsonOutput.getByteBuffers();
      CompositeByteBuf outputByteBuf = new CompositeByteBuf(duplicateByteBuffers);
      outputByteBuf.position(startPosition);
      int documentSizeInBytes = outputByteBuf.getInt();
      ByteBuf slice = outputByteBuf.duplicate();
      slice.position(startPosition);
      slice.limit(startPosition + documentSizeInBytes);
      Iterator<ByteBuf> iterator = duplicateByteBuffers.iterator();

      while(iterator.hasNext()) {
         ByteBuf byteBuffer = iterator.next();
         byteBuffer.release();
      }

      return new ByteBufBsonDocument(slice);
   }

   public String toJson() {
      return this.toJson(JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build());
   }

   public String toJson(JsonWriterSettings settings) {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter, settings);
      ByteBuf duplicate = this.byteBuf.duplicate();

      String result;
      try {
         BsonBinaryReader reader = new BsonBinaryReader(new ByteBufferBsonInput(duplicate));

         try {
            jsonWriter.pipe(reader);
            result = stringWriter.toString();
         } catch (Throwable throwable) {
            try {
               reader.close();
            } catch (Throwable suppressed) {
               throwable.addSuppressed(suppressed);
            }

            throw throwable;
         }

         reader.close();
      } finally {
         duplicate.release();
      }

      return result;
   }

   public BsonBinaryReader asBsonReader() {
      return new BsonBinaryReader(new ByteBufferBsonInput(this.byteBuf.duplicate()));
   }

   public BsonDocument clone() {
      byte[] clonedBytes = new byte[this.byteBuf.remaining()];
      this.byteBuf.get(this.byteBuf.position(), clonedBytes);
      return new RawBsonDocument(clonedBytes);
   }

   @Nullable
   <T> T findInDocument(ByteBufBsonDocument.Finder<T> finder) {
      ByteBuf duplicateByteBuf = this.byteBuf.duplicate();

      Object result;
      try {
         BsonBinaryReader bsonReader;
         label106: {
            bsonReader = new BsonBinaryReader(new ByteBufferBsonInput(duplicateByteBuf));

            try {
               bsonReader.readStartDocument();

               while(bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                  T found = finder.find(duplicateByteBuf, bsonReader);
                  if (found != null) {
                     result = found;
                     break label106;
                  }
               }

               bsonReader.readEndDocument();
            } catch (Throwable throwable) {
               try {
                  bsonReader.close();
               } catch (Throwable suppressed) {
                  throwable.addSuppressed(suppressed);
               }

               throw throwable;
            }

            bsonReader.close();
            return finder.notFound();
         }

         bsonReader.close();
      } finally {
         duplicateByteBuf.release();
      }

      return (T) result;
   }

   int getSizeInBytes() {
      return this.byteBuf.getInt(this.byteBuf.position());
   }

   BsonDocument toBaseBsonDocument() {
      ByteBuf duplicateByteBuf = this.byteBuf.duplicate();

      BsonDocument result;
      try {
         BsonBinaryReader bsonReader = new BsonBinaryReader(new ByteBufferBsonInput(duplicateByteBuf));

         try {
            result = (new BsonDocumentCodec()).decode(bsonReader, DecoderContext.builder().build());
         } catch (Throwable throwable) {
            try {
               bsonReader.close();
            } catch (Throwable suppressed) {
               throwable.addSuppressed(suppressed);
            }

            throw throwable;
         }

         bsonReader.close();
      } finally {
         duplicateByteBuf.release();
      }

      return result;
   }

   ByteBufBsonDocument(ByteBuf byteBuf) {
      this.byteBuf = byteBuf;
   }

   public void clear() {
      throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
   }

   public BsonValue put(String key, BsonValue value) {
      throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
   }

   public BsonDocument append(String key, BsonValue value) {
      throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
   }

   public void putAll(Map<? extends String, ? extends BsonValue> m) {
      throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
   }

   public BsonValue remove(Object key) {
      throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
   }

   public boolean isEmpty() {
      return (Boolean)Assertions.assertNotNull((Boolean)this.findInDocument(new ByteBufBsonDocument.Finder<Boolean>() {
         public Boolean find(ByteBuf byteBuf, BsonBinaryReader bsonReader) {
            return false;
         }

         public Boolean notFound() {
            return true;
         }
      }));
   }

   public int size() {
      return (Integer)Assertions.assertNotNull((Integer)this.findInDocument(new ByteBufBsonDocument.Finder<Integer>() {
         private int size;

         @Nullable
         public Integer find(ByteBuf byteBuf, BsonBinaryReader bsonReader) {
            ++this.size;
            bsonReader.readName();
            bsonReader.skipValue();
            return null;
         }

         public Integer notFound() {
            return this.size;
         }
      }));
   }

   public Set<Entry<String, BsonValue>> entrySet() {
      return new ByteBufBsonDocument.ByteBufBsonDocumentEntrySet();
   }

   public Collection<BsonValue> values() {
      return new ByteBufBsonDocument.ByteBufBsonDocumentValuesCollection();
   }

   public Set<String> keySet() {
      return new ByteBufBsonDocument.ByteBufBsonDocumentKeySet();
   }

   public boolean containsKey(final Object key) {
      if (key == null) {
         throw new IllegalArgumentException("key can not be null");
      } else {
         Boolean containsKey = (Boolean)this.findInDocument(new ByteBufBsonDocument.Finder<Boolean>() {
            public Boolean find(ByteBuf byteBuf, BsonBinaryReader bsonReader) {
               if (bsonReader.readName().equals(key)) {
                  return true;
               } else {
                  bsonReader.skipValue();
                  return null;
               }
            }

            public Boolean notFound() {
               return false;
            }
         });
         return containsKey != null ? containsKey : false;
      }
   }

   public boolean containsValue(final Object value) {
      Boolean containsValue = (Boolean)this.findInDocument(new ByteBufBsonDocument.Finder<Boolean>() {
         public Boolean find(ByteBuf byteBuf, BsonBinaryReader bsonReader) {
            bsonReader.skipName();
            return ByteBufBsonHelper.readBsonValue(byteBuf, bsonReader).equals(value) ? true : null;
         }

         public Boolean notFound() {
            return false;
         }
      });
      return containsValue != null ? containsValue : false;
   }

   @Nullable
   public BsonValue get(final Object key) {
      Assertions.notNull("key", key);
      return (BsonValue)this.findInDocument(new ByteBufBsonDocument.Finder<BsonValue>() {
         public BsonValue find(ByteBuf byteBuf, BsonBinaryReader bsonReader) {
            if (bsonReader.readName().equals(key)) {
               return ByteBufBsonHelper.readBsonValue(byteBuf, bsonReader);
            } else {
               bsonReader.skipValue();
               return null;
            }
         }

         @Nullable
         public BsonValue notFound() {
            return null;
         }
      });
   }

   public String getFirstKey() {
      return (String)Assertions.assertNotNull((String)this.findInDocument(new ByteBufBsonDocument.Finder<String>() {
         public String find(ByteBuf byteBuf, BsonBinaryReader bsonReader) {
            return bsonReader.readName();
         }

         public String notFound() {
            throw new NoSuchElementException();
         }
      }));
   }

   private Object writeReplace() {
      return this.toBaseBsonDocument();
   }

   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
      throw new InvalidObjectException("Proxy required");
   }

   private interface Finder<T> {
      @Nullable
      T find(ByteBuf var1, BsonBinaryReader var2);

      @Nullable
      T notFound();
   }

   private class ByteBufBsonDocumentEntrySet extends AbstractSet<Entry<String, BsonValue>> {
      private ByteBufBsonDocumentEntrySet() {
      }

      public Iterator<Entry<String, BsonValue>> iterator() {
         return new Iterator<Entry<String, BsonValue>>() {
            private final ByteBuf duplicatedByteBuf;
            private final BsonBinaryReader bsonReader;

            {
               this.duplicatedByteBuf = ByteBufBsonDocument.this.byteBuf.duplicate();
               this.bsonReader = new BsonBinaryReader(new ByteBufferBsonInput(this.duplicatedByteBuf));
               this.bsonReader.readStartDocument();
               this.bsonReader.readBsonType();
            }

            public boolean hasNext() {
               return this.bsonReader.getCurrentBsonType() != BsonType.END_OF_DOCUMENT;
            }

            public Entry<String, BsonValue> next() {
               if (!this.hasNext()) {
                  throw new NoSuchElementException();
               } else {
                  String key = this.bsonReader.readName();
                  BsonValue value = ByteBufBsonHelper.readBsonValue(this.duplicatedByteBuf, this.bsonReader);
                  this.bsonReader.readBsonType();
                  return new SimpleEntry(key, value);
               }
            }
         };
      }

      public boolean isEmpty() {
         return !this.iterator().hasNext();
      }

      public int size() {
         return ByteBufBsonDocument.this.size();
      }

      // $FF: synthetic method
      ByteBufBsonDocumentEntrySet(Object x1) {
         this();
      }
   }

   private class ByteBufBsonDocumentValuesCollection extends AbstractCollection<BsonValue> {
      private final Set<Entry<String, BsonValue>> entrySet;

      private ByteBufBsonDocumentValuesCollection() {
         this.entrySet = ByteBufBsonDocument.this.new ByteBufBsonDocumentEntrySet();
      }

      public Iterator<BsonValue> iterator() {
         final Iterator<Entry<String, BsonValue>> entrySetIterator = this.entrySet.iterator();
         return new Iterator<BsonValue>() {
            public boolean hasNext() {
               return entrySetIterator.hasNext();
            }

            public BsonValue next() {
               return (BsonValue)((Entry)entrySetIterator.next()).getValue();
            }
         };
      }

      public boolean isEmpty() {
         return this.entrySet.isEmpty();
      }

      public int size() {
         return this.entrySet.size();
      }

      // $FF: synthetic method
      ByteBufBsonDocumentValuesCollection(Object x1) {
         this();
      }
   }

   private class ByteBufBsonDocumentKeySet extends AbstractSet<String> {
      private final Set<Entry<String, BsonValue>> entrySet;

      private ByteBufBsonDocumentKeySet() {
         this.entrySet = ByteBufBsonDocument.this.new ByteBufBsonDocumentEntrySet();
      }

      public Iterator<String> iterator() {
         final Iterator<Entry<String, BsonValue>> entrySetIterator = this.entrySet.iterator();
         return new Iterator<String>() {
            public boolean hasNext() {
               return entrySetIterator.hasNext();
            }

            public String next() {
               return (String)((Entry)entrySetIterator.next()).getKey();
            }
         };
      }

      public boolean isEmpty() {
         return this.entrySet.isEmpty();
      }

      public int size() {
         return this.entrySet.size();
      }

      // $FF: synthetic method
      ByteBufBsonDocumentKeySet(Object x1) {
         this();
      }
   }
}
