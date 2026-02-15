package dev.artixdev.libs.com.mongodb.internal.binding;

import java.util.function.BiConsumer;
import dev.artixdev.libs.com.mongodb.connection.ClusterType;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.session.ClientSession;

public final class TransactionContext<C extends ReferenceCounted> extends AbstractReferenceCounted {
   private final ClusterType clusterType;
   private C pinnedConnection;

   public TransactionContext(ClusterType clusterType) {
      this.clusterType = clusterType;
   }

   @Nullable
   public C getPinnedConnection() {
      return this.pinnedConnection;
   }

   public void pinConnection(C connection, BiConsumer<C, Connection.PinningMode> markAsPinnedOperation) {
      this.pinnedConnection = (C) connection.retain();
      markAsPinnedOperation.accept(connection, Connection.PinningMode.TRANSACTION);
   }

   public boolean isConnectionPinningRequired() {
      return this.clusterType == ClusterType.LOAD_BALANCED;
   }

   public int release() {
      int count = super.release();
      if (count == 0 && this.pinnedConnection != null) {
         this.pinnedConnection.release();
      }

      return count;
   }

   @Nullable
   public static <C extends TransactionContext<? extends ReferenceCounted>> C get(ClientSession session) {
      return (C) session.getTransactionContext();
   }
}
