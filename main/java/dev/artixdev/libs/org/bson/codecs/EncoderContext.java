package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonWriter;

public final class EncoderContext {
   private static final EncoderContext DEFAULT_CONTEXT = builder().build();
   private final boolean encodingCollectibleDocument;

   public static EncoderContext.Builder builder() {
      return new EncoderContext.Builder();
   }

   public boolean isEncodingCollectibleDocument() {
      return this.encodingCollectibleDocument;
   }

   public <T> void encodeWithChildContext(Encoder<T> encoder, BsonWriter writer, T value) {
      encoder.encode(writer, value, DEFAULT_CONTEXT);
   }

   public EncoderContext getChildContext() {
      return DEFAULT_CONTEXT;
   }

   private EncoderContext(EncoderContext.Builder builder) {
      this.encodingCollectibleDocument = builder.encodingCollectibleDocument;
   }

   // $FF: synthetic method
   EncoderContext(EncoderContext.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private boolean encodingCollectibleDocument;

      private Builder() {
      }

      public EncoderContext.Builder isEncodingCollectibleDocument(boolean encodingCollectibleDocument) {
         this.encodingCollectibleDocument = encodingCollectibleDocument;
         return this;
      }

      public EncoderContext build() {
         return new EncoderContext(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
