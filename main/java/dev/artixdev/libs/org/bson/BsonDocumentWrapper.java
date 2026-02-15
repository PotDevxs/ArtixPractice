package dev.artixdev.libs.org.bson;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.codecs.Encoder;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

public final class BsonDocumentWrapper<T> extends BsonDocument {
   private static final long serialVersionUID = 1L;
   private final transient T wrappedDocument;
   private final transient Encoder<T> encoder;
   private BsonDocument unwrapped;

   public static BsonDocument asBsonDocument(Object document, CodecRegistry codecRegistry) {
      if (document == null) {
         return null;
      } else {
         return (BsonDocument)(document instanceof BsonDocument ? (BsonDocument)document : new BsonDocumentWrapper(document, codecRegistry.get(document.getClass())));
      }
   }

   public BsonDocumentWrapper(T wrappedDocument, Encoder<T> encoder) {
      if (wrappedDocument == null) {
         throw new IllegalArgumentException("Document can not be null");
      } else {
         this.wrappedDocument = wrappedDocument;
         this.encoder = encoder;
      }
   }

   public T getWrappedDocument() {
      return this.wrappedDocument;
   }

   public Encoder<T> getEncoder() {
      return this.encoder;
   }

   public boolean isUnwrapped() {
      return this.unwrapped != null;
   }

   public int size() {
      return this.getUnwrapped().size();
   }

   public boolean isEmpty() {
      return this.getUnwrapped().isEmpty();
   }

   public boolean containsKey(Object key) {
      return this.getUnwrapped().containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.getUnwrapped().containsValue(value);
   }

   public BsonValue get(Object key) {
      return this.getUnwrapped().get(key);
   }

   public BsonValue put(String key, BsonValue value) {
      return this.getUnwrapped().put(key, value);
   }

   public BsonValue remove(Object key) {
      return this.getUnwrapped().remove(key);
   }

   public void putAll(Map<? extends String, ? extends BsonValue> m) {
      super.putAll(m);
   }

   public void clear() {
      super.clear();
   }

   public Set<String> keySet() {
      return this.getUnwrapped().keySet();
   }

   public Collection<BsonValue> values() {
      return this.getUnwrapped().values();
   }

   public Set<Entry<String, BsonValue>> entrySet() {
      return this.getUnwrapped().entrySet();
   }

   public boolean equals(Object o) {
      return this.getUnwrapped().equals(o);
   }

   public int hashCode() {
      return this.getUnwrapped().hashCode();
   }

   public String toString() {
      return this.getUnwrapped().toString();
   }

   public BsonDocument clone() {
      return this.getUnwrapped().clone();
   }

   private BsonDocument getUnwrapped() {
      if (this.encoder == null) {
         throw new BsonInvalidOperationException("Can not unwrap a BsonDocumentWrapper with no Encoder");
      } else {
         if (this.unwrapped == null) {
            BsonDocument unwrapped = new BsonDocument();
            BsonWriter writer = new BsonDocumentWriter(unwrapped);
            this.encoder.encode(writer, this.wrappedDocument, EncoderContext.builder().build());
            this.unwrapped = unwrapped;
         }

         return this.unwrapped;
      }
   }

   private Object writeReplace() {
      return this.getUnwrapped();
   }

   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
      throw new InvalidObjectException("Proxy required");
   }
}
