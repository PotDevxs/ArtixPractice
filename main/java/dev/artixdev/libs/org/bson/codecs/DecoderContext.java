package dev.artixdev.libs.org.bson.codecs;

import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.assertions.Assertions;

public final class DecoderContext {
   private static final DecoderContext DEFAULT_CONTEXT = builder().build();
   private final boolean checkedDiscriminator;

   public boolean hasCheckedDiscriminator() {
      return this.checkedDiscriminator;
   }

   public static DecoderContext.Builder builder() {
      return new DecoderContext.Builder();
   }

   public <T> T decodeWithChildContext(Decoder<T> decoder, BsonReader reader) {
      Assertions.notNull("decoder", decoder);
      return decoder.decode(reader, DEFAULT_CONTEXT);
   }

   private DecoderContext(DecoderContext.Builder builder) {
      this.checkedDiscriminator = builder.hasCheckedDiscriminator();
   }

   // $FF: synthetic method
   DecoderContext(DecoderContext.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private boolean checkedDiscriminator;

      private Builder() {
      }

      public boolean hasCheckedDiscriminator() {
         return this.checkedDiscriminator;
      }

      public DecoderContext.Builder checkedDiscriminator(boolean checkedDiscriminator) {
         this.checkedDiscriminator = checkedDiscriminator;
         return this;
      }

      public DecoderContext build() {
         return new DecoderContext(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
