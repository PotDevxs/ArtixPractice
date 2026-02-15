package dev.artixdev.libs.com.mongodb.internal.binding;

import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.connection.AsyncConnection;

public interface AsyncConnectionSource extends BindingContext, ReferenceCounted {
   ServerDescription getServerDescription();

   ReadPreference getReadPreference();

   void getConnection(SingleResultCallback<AsyncConnection> var1);

   AsyncConnectionSource retain();
}
