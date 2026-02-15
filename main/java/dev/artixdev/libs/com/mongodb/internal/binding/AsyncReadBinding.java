package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;

public interface AsyncReadBinding extends BindingContext, ReferenceCounted {
   ReadPreference getReadPreference();

   void getReadConnectionSource(SingleResultCallback<AsyncConnectionSource> var1);

   void getReadConnectionSource(int var1, ReadPreference var2, SingleResultCallback<AsyncConnectionSource> var3);

   AsyncReadBinding retain();
}
