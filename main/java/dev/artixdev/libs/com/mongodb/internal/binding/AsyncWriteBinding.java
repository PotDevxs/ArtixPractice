package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;

public interface AsyncWriteBinding extends BindingContext, ReferenceCounted {
   void getWriteConnectionSource(SingleResultCallback<AsyncConnectionSource> var1);

   AsyncWriteBinding retain();
}
