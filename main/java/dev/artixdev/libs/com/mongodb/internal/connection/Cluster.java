package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.Closeable;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.connection.ClusterDescription;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ClusterSettings;
import dev.artixdev.libs.com.mongodb.event.ServerDescriptionChangedEvent;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.selector.ServerSelector;

public interface Cluster extends Closeable {
   ClusterSettings getSettings();

   ClusterId getClusterId();

   @Nullable
   ClusterableServer getServer(ServerAddress var1);

   ClusterDescription getCurrentDescription();

   ClusterClock getClock();

   ServerTuple selectServer(ServerSelector var1, OperationContext var2);

   void selectServerAsync(ServerSelector var1, OperationContext var2, SingleResultCallback<ServerTuple> var3);

   void close();

   boolean isClosed();

   void withLock(Runnable var1);

   void onChange(ServerDescriptionChangedEvent var1);
}
