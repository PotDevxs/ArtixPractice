package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.BindingContext;
import dev.artixdev.libs.com.mongodb.internal.binding.ReferenceCounted;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.FieldNameValidator;
import dev.artixdev.libs.org.bson.codecs.Decoder;

@ThreadSafe
public interface AsyncConnection extends ReferenceCounted {
   AsyncConnection retain();

   ConnectionDescription getDescription();

   <T> void commandAsync(String var1, BsonDocument var2, FieldNameValidator var3, @Nullable ReadPreference var4, Decoder<T> var5, BindingContext var6, SingleResultCallback<T> var7);

   <T> void commandAsync(String var1, BsonDocument var2, FieldNameValidator var3, @Nullable ReadPreference var4, Decoder<T> var5, BindingContext var6, boolean var7, @Nullable SplittablePayload var8, @Nullable FieldNameValidator var9, SingleResultCallback<T> var10);

   void markAsPinned(Connection.PinningMode var1);
}
