package dev.artixdev.libs.com.mongodb.internal.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.internal.IgnorableRequestContext;
import dev.artixdev.libs.com.mongodb.internal.binding.StaticBindingContext;
import dev.artixdev.libs.com.mongodb.internal.connection.Cluster;
import dev.artixdev.libs.com.mongodb.internal.connection.Connection;
import dev.artixdev.libs.com.mongodb.internal.connection.NoOpSessionContext;
import dev.artixdev.libs.com.mongodb.internal.connection.OperationContext;
import dev.artixdev.libs.com.mongodb.internal.selector.ReadPreferenceServerSelector;
import dev.artixdev.libs.com.mongodb.internal.validator.NoOpFieldNameValidator;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.session.ServerSession;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonDocumentWriter;
import dev.artixdev.libs.org.bson.UuidRepresentation;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.EncoderContext;
import dev.artixdev.libs.org.bson.codecs.UuidCodec;

public class ServerSessionPool {
   private final ConcurrentLinkedDeque<ServerSessionPool.ServerSessionImpl> available;
   private final Cluster cluster;
   private final ServerSessionPool.Clock clock;
   private volatile boolean closed;
   @Nullable
   private final ServerApi serverApi;
   private final LongAdder inUseCount;

   public ServerSessionPool(Cluster cluster, @Nullable ServerApi serverApi) {
      this(cluster, serverApi, System::currentTimeMillis);
   }

   public ServerSessionPool(Cluster cluster, @Nullable ServerApi serverApi, ServerSessionPool.Clock clock) {
      this.available = new ConcurrentLinkedDeque();
      this.inUseCount = new LongAdder();
      this.cluster = cluster;
      this.serverApi = serverApi;
      this.clock = clock;
   }

   public ServerSession get() {
      Assertions.isTrue("server session pool is open", !this.closed);

      ServerSessionPool.ServerSessionImpl serverSession;
      for(serverSession = (ServerSessionPool.ServerSessionImpl)this.available.pollLast(); serverSession != null && this.shouldPrune(serverSession); serverSession = (ServerSessionPool.ServerSessionImpl)this.available.pollLast()) {
         serverSession.close();
      }

      if (serverSession == null) {
         serverSession = new ServerSessionPool.ServerSessionImpl();
      }

      this.inUseCount.increment();
      return serverSession;
   }

   public void release(ServerSession serverSession) {
      this.inUseCount.decrement();
      ServerSessionPool.ServerSessionImpl serverSessionImpl = (ServerSessionPool.ServerSessionImpl)serverSession;
      if (serverSessionImpl.isMarkedDirty()) {
         serverSessionImpl.close();
      } else {
         this.available.addLast(serverSessionImpl);
      }

   }

   public long getInUseCount() {
      return this.inUseCount.sum();
   }

   public void close() {
      this.closed = true;
      this.endClosedSessions();
   }

   private void endClosedSessions() {
      List<BsonDocument> identifiers = this.drainPool();
      if (!identifiers.isEmpty()) {
         List<ServerDescription> primaryPreferred = (new ReadPreferenceServerSelector(ReadPreference.primaryPreferred())).select(this.cluster.getCurrentDescription());
         if (!primaryPreferred.isEmpty()) {
            Connection connection = null;

            try {
               StaticBindingContext context = new StaticBindingContext(NoOpSessionContext.INSTANCE, this.serverApi, IgnorableRequestContext.INSTANCE, new OperationContext());
               connection = this.cluster.selectServer((clusterDescription) -> {
                  Iterator var2 = clusterDescription.getServerDescriptions().iterator();

                  ServerDescription cur;
                  do {
                     if (!var2.hasNext()) {
                        return Collections.emptyList();
                     }

                     cur = (ServerDescription)var2.next();
                  } while(!cur.getAddress().equals(((ServerDescription)primaryPreferred.get(0)).getAddress()));

                  return Collections.singletonList(cur);
               }, context.getOperationContext()).getServer().getConnection(context.getOperationContext());
               connection.command("admin", new BsonDocument("endSessions", new BsonArray(identifiers)), new NoOpFieldNameValidator(), ReadPreference.primaryPreferred(), new BsonDocumentCodec(), context);
            } catch (MongoException ignored) {
            } finally {
               if (connection != null) {
                  connection.release();
               }

            }

         }
      }
   }

   private List<BsonDocument> drainPool() {
      List<BsonDocument> identifiers = new ArrayList(this.available.size());

      for(ServerSessionPool.ServerSessionImpl nextSession = (ServerSessionPool.ServerSessionImpl)this.available.pollFirst(); nextSession != null; nextSession = (ServerSessionPool.ServerSessionImpl)this.available.pollFirst()) {
         identifiers.add(nextSession.getIdentifier());
      }

      return identifiers;
   }

   private boolean shouldPrune(ServerSessionPool.ServerSessionImpl serverSession) {
      Integer logicalSessionTimeoutMinutes = this.cluster.getCurrentDescription().getLogicalSessionTimeoutMinutes();
      if (logicalSessionTimeoutMinutes == null) {
         return false;
      } else {
         long currentTimeMillis = this.clock.millis();
         long timeSinceLastUse = currentTimeMillis - serverSession.getLastUsedAtMillis();
         long oneMinuteFromTimeout = TimeUnit.MINUTES.toMillis((long)(logicalSessionTimeoutMinutes - 1));
         return timeSinceLastUse > oneMinuteFromTimeout;
      }
   }

   private BsonBinary createNewServerSessionIdentifier() {
      UuidCodec uuidCodec = new UuidCodec(UuidRepresentation.STANDARD);
      BsonDocument holder = new BsonDocument();
      BsonDocumentWriter bsonDocumentWriter = new BsonDocumentWriter(holder);
      bsonDocumentWriter.writeStartDocument();
      bsonDocumentWriter.writeName("id");
      uuidCodec.encode(bsonDocumentWriter, (UUID)UUID.randomUUID(), EncoderContext.builder().build());
      bsonDocumentWriter.writeEndDocument();
      return holder.getBinary("id");
   }

   interface Clock {
      long millis();
   }

   final class ServerSessionImpl implements ServerSession {
      private final BsonDocument identifier;
      private long transactionNumber = 0L;
      private volatile long lastUsedAtMillis;
      private volatile boolean closed;
      private volatile boolean dirty;

      ServerSessionImpl() {
         this.lastUsedAtMillis = ServerSessionPool.this.clock.millis();
         this.dirty = false;
         this.identifier = new BsonDocument("id", ServerSessionPool.this.createNewServerSessionIdentifier());
      }

      void close() {
         this.closed = true;
      }

      long getLastUsedAtMillis() {
         return this.lastUsedAtMillis;
      }

      public long getTransactionNumber() {
         return this.transactionNumber;
      }

      public BsonDocument getIdentifier() {
         this.lastUsedAtMillis = ServerSessionPool.this.clock.millis();
         return this.identifier;
      }

      public long advanceTransactionNumber() {
         ++this.transactionNumber;
         return this.transactionNumber;
      }

      public boolean isClosed() {
         return this.closed;
      }

      public void markDirty() {
         this.dirty = true;
      }

      public boolean isMarkedDirty() {
         return this.dirty;
      }
   }
}
