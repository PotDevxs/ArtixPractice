package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.concurrent.atomic.AtomicBoolean;
import dev.artixdev.libs.com.mongodb.ClientSessionOptions;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.MongoQueryException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.MongoTimeoutException;
import dev.artixdev.libs.com.mongodb.ReadConcern;
import dev.artixdev.libs.com.mongodb.ReadPreference;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.TransactionOptions;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.ClientSession;
import dev.artixdev.libs.com.mongodb.client.SynchronousContextProvider;
import dev.artixdev.libs.com.mongodb.internal.IgnorableRequestContext;
import dev.artixdev.libs.com.mongodb.internal.binding.ClusterAwareReadWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ClusterBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.Cluster;
import dev.artixdev.libs.com.mongodb.internal.operation.ReadOperation;
import dev.artixdev.libs.com.mongodb.internal.operation.WriteOperation;
import dev.artixdev.libs.com.mongodb.internal.session.ServerSessionPool;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.codecs.configuration.CodecRegistry;

final class MongoClientDelegate {
   private final Cluster cluster;
   private final ServerSessionPool serverSessionPool;
   private final Object originator;
   private final OperationExecutor operationExecutor;
   private final Crypt crypt;
   @Nullable
   private final ServerApi serverApi;
   private final CodecRegistry codecRegistry;
   @Nullable
   private final SynchronousContextProvider contextProvider;
   private final AtomicBoolean closed;

   MongoClientDelegate(Cluster cluster, CodecRegistry codecRegistry, Object originator, @Nullable OperationExecutor operationExecutor, @Nullable Crypt crypt, @Nullable ServerApi serverApi, @Nullable SynchronousContextProvider contextProvider) {
      this.cluster = cluster;
      this.codecRegistry = codecRegistry;
      this.contextProvider = contextProvider;
      this.serverSessionPool = new ServerSessionPool(cluster, serverApi);
      this.originator = originator;
      this.operationExecutor = (OperationExecutor)(operationExecutor == null ? new MongoClientDelegate.DelegateOperationExecutor() : operationExecutor);
      this.crypt = crypt;
      this.serverApi = serverApi;
      this.closed = new AtomicBoolean();
   }

   public OperationExecutor getOperationExecutor() {
      return this.operationExecutor;
   }

   public ClientSession createClientSession(ClientSessionOptions options, ReadConcern readConcern, WriteConcern writeConcern, ReadPreference readPreference) {
      Assertions.notNull("readConcern", readConcern);
      Assertions.notNull("writeConcern", writeConcern);
      Assertions.notNull("readPreference", readPreference);
      ClientSessionOptions mergedOptions = ClientSessionOptions.builder(options).defaultTransactionOptions(TransactionOptions.merge(options.getDefaultTransactionOptions(), TransactionOptions.builder().readConcern(readConcern).writeConcern(writeConcern).readPreference(readPreference).build())).build();
      return new ClientSessionImpl(this.serverSessionPool, this.originator, mergedOptions, this);
   }

   public void close() {
      if (!this.closed.getAndSet(true)) {
         if (this.crypt != null) {
            this.crypt.close();
         }

         this.serverSessionPool.close();
         this.cluster.close();
      }

   }

   public Cluster getCluster() {
      return this.cluster;
   }

   public CodecRegistry getCodecRegistry() {
      return this.codecRegistry;
   }

   public ServerSessionPool getServerSessionPool() {
      return this.serverSessionPool;
   }

   private class DelegateOperationExecutor implements OperationExecutor {
      private DelegateOperationExecutor() {
      }

      public <T> T execute(ReadOperation<T> operation, ReadPreference readPreference, ReadConcern readConcern) {
         return this.execute(operation, readPreference, readConcern, (ClientSession)null);
      }

      public <T> T execute(WriteOperation<T> operation, ReadConcern readConcern) {
         return this.execute(operation, readConcern, (ClientSession)null);
      }

      public <T> T execute(ReadOperation<T> operation, ReadPreference readPreference, ReadConcern readConcern, @Nullable ClientSession session) {
         if (session != null) {
            session.notifyOperationInitiated(operation);
         }

         ClientSession actualClientSession = this.getClientSession(session);
         ReadBinding binding = this.getReadBinding(readPreference, readConcern, actualClientSession, session == null);

         T var7;
         try {
            if (actualClientSession.hasActiveTransaction() && !binding.getReadPreference().equals(ReadPreference.primary())) {
               throw new MongoClientException("Read preference in a transaction must be primary");
            }

            var7 = operation.execute(binding);
         } catch (MongoException e) {
            this.labelException(actualClientSession, e);
            this.clearTransactionContextOnTransientTransactionError(session, e);
            throw e;
         } finally {
            binding.release();
         }

         return var7;
      }

      public <T> T execute(WriteOperation<T> operation, ReadConcern readConcern, @Nullable ClientSession session) {
         if (session != null) {
            session.notifyOperationInitiated(operation);
         }

         ClientSession actualClientSession = this.getClientSession(session);
         WriteBinding binding = this.getWriteBinding(readConcern, actualClientSession, session == null);

         T var6;
         try {
            var6 = operation.execute(binding);
         } catch (MongoException e) {
            this.labelException(actualClientSession, e);
            this.clearTransactionContextOnTransientTransactionError(session, e);
            throw e;
         } finally {
            binding.release();
         }

         return var6;
      }

      WriteBinding getWriteBinding(ReadConcern readConcern, ClientSession session, boolean ownsSession) {
         return this.getReadWriteBinding(ReadPreference.primary(), readConcern, session, ownsSession);
      }

      ReadBinding getReadBinding(ReadPreference readPreference, ReadConcern readConcern, ClientSession session, boolean ownsSession) {
         return this.getReadWriteBinding(readPreference, readConcern, session, ownsSession);
      }

      ReadWriteBinding getReadWriteBinding(ReadPreference readPreference, ReadConcern readConcern, ClientSession session, boolean ownsSession) {
         ClusterAwareReadWriteBinding readWriteBinding = new ClusterBinding(MongoClientDelegate.this.cluster, this.getReadPreferenceForBinding(readPreference, session), readConcern, MongoClientDelegate.this.serverApi, this.getContext());
         if (MongoClientDelegate.this.crypt != null) {
            readWriteBinding = new CryptBinding((ClusterAwareReadWriteBinding)readWriteBinding, MongoClientDelegate.this.crypt);
         }

         return new ClientSessionBinding(session, ownsSession, (ClusterAwareReadWriteBinding)readWriteBinding);
      }

      private RequestContext getContext() {
         RequestContext context = null;
         if (MongoClientDelegate.this.contextProvider != null) {
            context = MongoClientDelegate.this.contextProvider.getContext();
         }

         return (RequestContext)(context == null ? IgnorableRequestContext.INSTANCE : context);
      }

      private void labelException(ClientSession session, MongoException e) {
         if (session.hasActiveTransaction() && (e instanceof MongoSocketException || e instanceof MongoTimeoutException || e instanceof MongoQueryException && e.getCode() == 91) && !e.hasErrorLabel("UnknownTransactionCommitResult")) {
            e.addLabel("TransientTransactionError");
         }

      }

      private void clearTransactionContextOnTransientTransactionError(@Nullable ClientSession session, MongoException e) {
         if (session != null && e.hasErrorLabel("TransientTransactionError")) {
            session.clearTransactionContext();
         }

      }

      private ReadPreference getReadPreferenceForBinding(ReadPreference readPreference, @Nullable ClientSession session) {
         if (session == null) {
            return readPreference;
         } else if (session.hasActiveTransaction()) {
            ReadPreference readPreferenceForBinding = session.getTransactionOptions().getReadPreference();
            if (readPreferenceForBinding == null) {
               throw new MongoInternalException("Invariant violated.  Transaction options read preference can not be null");
            } else {
               return readPreferenceForBinding;
            }
         } else {
            return readPreference;
         }
      }

      ClientSession getClientSession(@Nullable ClientSession clientSessionFromOperation) {
         ClientSession session;
         if (clientSessionFromOperation != null) {
            Assertions.isTrue("ClientSession from same MongoClient", clientSessionFromOperation.getOriginator() == MongoClientDelegate.this.originator);
            session = clientSessionFromOperation;
         } else {
            session = MongoClientDelegate.this.createClientSession(ClientSessionOptions.builder().causallyConsistent(false).build(), ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, ReadPreference.primary());
         }

         return session;
      }

      // $FF: synthetic method
      DelegateOperationExecutor(Object x1) {
         this();
      }
   }
}
