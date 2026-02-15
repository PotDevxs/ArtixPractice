package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.binding.BindingContext;
import dev.artixdev.libs.com.mongodb.internal.binding.ReferenceCounted;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;

@ThreadSafe
public interface Connection extends ReferenceCounted {
   Connection retain();

   ConnectionDescription getDescription();

   @Nullable
   <T> T command(String var1, BsonDocument var2, FieldNameValidator var3, @Nullable ReadPreference var4, Decoder<T> var5, BindingContext var6);

   @Nullable
   <T> T command(String var1, BsonDocument var2, FieldNameValidator var3, @Nullable ReadPreference var4, Decoder<T> var5, BindingContext var6, boolean var7, @Nullable SplittablePayload var8, @Nullable FieldNameValidator var9);

   void markAsPinned(Connection.PinningMode var1);

   public static enum PinningMode {
      CURSOR,
      TRANSACTION;

      // $FF: synthetic method
      private static Connection.PinningMode[] $values() {
         return new Connection.PinningMode[]{CURSOR, TRANSACTION};
      }
   }
}
