package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;

interface InternalConnectionInitializer {
   InternalConnectionInitializationDescription startHandshake(InternalConnection var1);

   InternalConnectionInitializationDescription finishHandshake(InternalConnection var1, InternalConnectionInitializationDescription var2);

   void startHandshakeAsync(InternalConnection var1, SingleResultCallback<InternalConnectionInitializationDescription> var2);

   void finishHandshakeAsync(InternalConnection var1, InternalConnectionInitializationDescription var2, SingleResultCallback<InternalConnectionInitializationDescription> var3);
}
