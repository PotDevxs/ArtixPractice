package dev.artixdev.libs.com.mongodb.internal.operation;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoNamespace;
import dev.artixdev.libs.com.mongodb.WriteConcern;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncReadWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.AsyncWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.ReadWriteBinding;
import dev.artixdev.libs.com.mongodb.internal.binding.WriteBinding;
import dev.artixdev.libs.com.mongodb.internal.connection.AsyncConnection;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.codecs.BsonValueCodec;

public class DropCollectionOperation implements AsyncWriteOperation<Void>, WriteOperation<Void> {
   private static final String ENCRYPT_PREFIX = "enxcol_.";
   private static final BsonValueCodec BSON_VALUE_CODEC = new BsonValueCodec();
   private final MongoNamespace namespace;
   private final WriteConcern writeConcern;
   private BsonDocument encryptedFields;
   private boolean autoEncryptedFields;

   public DropCollectionOperation(MongoNamespace namespace) {
      this(namespace, (WriteConcern)null);
   }

   public DropCollectionOperation(MongoNamespace namespace, @Nullable WriteConcern writeConcern) {
      this.namespace = (MongoNamespace)Assertions.notNull("namespace", namespace);
      this.writeConcern = writeConcern;
   }

   public WriteConcern getWriteConcern() {
      return this.writeConcern;
   }

   public DropCollectionOperation encryptedFields(BsonDocument encryptedFields) {
      this.encryptedFields = encryptedFields;
      return this;
   }

   public DropCollectionOperation autoEncryptedFields(boolean autoEncryptedFields) {
      this.autoEncryptedFields = autoEncryptedFields;
      return this;
   }

   public Void execute(WriteBinding binding) {
      BsonDocument localEncryptedFields = this.getEncryptedFields((ReadWriteBinding)binding);
      return (Void)SyncOperationHelper.withConnection(binding, (connection) -> {
         this.getCommands(localEncryptedFields).forEach((command) -> {
            try {
               SyncOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), (BsonDocument)command.get(), connection, SyncOperationHelper.writeConcernErrorTransformer());
            } catch (MongoCommandException e) {
               CommandOperationHelper.rethrowIfNotNamespaceError(e);
            }

         });
         return null;
      });
   }

   public void executeAsync(AsyncWriteBinding binding, SingleResultCallback<Void> callback) {
      SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
      this.getEncryptedFields((AsyncReadWriteBinding)binding, (result, t) -> {
         if (t != null) {
            errHandlingCallback.onResult(null, t);
         } else {
            AsyncOperationHelper.withAsyncConnection(binding, (connection, t1) -> {
               if (t1 != null) {
                  errHandlingCallback.onResult(null, t1);
               } else {
                  (new DropCollectionOperation.ProcessCommandsCallback(binding, connection, this.getCommands(result), AsyncOperationHelper.releasingCallback(errHandlingCallback, connection))).onResult((Void)null, (Throwable)null);
               }

            });
         }

      });
   }

   private List<Supplier<BsonDocument>> getCommands(BsonDocument encryptedFields) {
      return encryptedFields == null ? Collections.singletonList(this::dropCollectionCommand) : Arrays.asList(() -> {
         return this.getDropEncryptedFieldsCollectionCommand(encryptedFields, "esc");
      }, () -> {
         return this.getDropEncryptedFieldsCollectionCommand(encryptedFields, "ecoc");
      }, this::dropCollectionCommand);
   }

   private BsonDocument getDropEncryptedFieldsCollectionCommand(BsonDocument encryptedFields, String collectionSuffix) {
      BsonString defaultCollectionName = new BsonString("enxcol_." + this.namespace.getCollectionName() + "." + collectionSuffix);
      return new BsonDocument("drop", (BsonValue)encryptedFields.getOrDefault(collectionSuffix + "Collection", defaultCollectionName));
   }

   private BsonDocument dropCollectionCommand() {
      BsonDocument commandDocument = new BsonDocument("drop", new BsonString(this.namespace.getCollectionName()));
      WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, commandDocument);
      return commandDocument;
   }

   @Nullable
   private BsonDocument getEncryptedFields(ReadWriteBinding readWriteBinding) {
      if (this.encryptedFields == null && this.autoEncryptedFields) {
         BatchCursor cursor = this.listCollectionOperation().execute(readWriteBinding);

         BsonDocument var3;
         try {
            var3 = this.getCollectionEncryptedFields(this.encryptedFields, cursor.tryNext());
         } catch (Throwable throwable) {
            if (cursor != null) {
               try {
                  cursor.close();
               } catch (Throwable suppressed) {
                  throwable.addSuppressed(suppressed);
               }
            }

            throw throwable;
         }

         if (cursor != null) {
            cursor.close();
         }

         return var3;
      } else {
         return this.encryptedFields;
      }
   }

   private void getEncryptedFields(AsyncReadWriteBinding asyncReadWriteBinding, SingleResultCallback<BsonDocument> callback) {
      if (this.encryptedFields == null && this.autoEncryptedFields) {
         this.listCollectionOperation().executeAsync(asyncReadWriteBinding, (cursor, t) -> {
            if (t != null) {
               callback.onResult(null, t);
            } else {
               cursor.next((bsonValues, t1) -> {
                  if (t1 != null) {
                     callback.onResult(null, t1);
                  } else {
                     callback.onResult(this.getCollectionEncryptedFields(this.encryptedFields, bsonValues), (Throwable)null);
                  }

               });
            }

         });
      } else {
         callback.onResult(this.encryptedFields, (Throwable)null);
      }

   }

   private BsonDocument getCollectionEncryptedFields(BsonDocument defaultEncryptedFields, @Nullable List<BsonValue> bsonValues) {
      return bsonValues != null && bsonValues.size() > 0 ? ((BsonValue)bsonValues.get(0)).asDocument().getDocument("options", new BsonDocument()).getDocument("encryptedFields", new BsonDocument()) : defaultEncryptedFields;
   }

   private ListCollectionsOperation<BsonValue> listCollectionOperation() {
      return (new ListCollectionsOperation(this.namespace.getDatabaseName(), BSON_VALUE_CODEC)).filter(new BsonDocument("name", new BsonString(this.namespace.getCollectionName()))).batchSize(1);
   }

   class ProcessCommandsCallback implements SingleResultCallback<Void> {
      private final AsyncWriteBinding binding;
      private final AsyncConnection connection;
      private final SingleResultCallback<Void> finalCallback;
      private final Deque<Supplier<BsonDocument>> commands;

      ProcessCommandsCallback(AsyncWriteBinding binding, AsyncConnection connection, List<Supplier<BsonDocument>> commands, SingleResultCallback<Void> finalCallback) {
         this.binding = binding;
         this.connection = connection;
         this.finalCallback = finalCallback;
         this.commands = new ArrayDeque(commands);
      }

      public void onResult(@Nullable Void result, @Nullable Throwable t) {
         if (t != null && !CommandOperationHelper.isNamespaceError(t)) {
            this.finalCallback.onResult(null, t);
         } else {
            Supplier<BsonDocument> nextCommandFunction = (Supplier)this.commands.poll();
            if (nextCommandFunction == null) {
               this.finalCallback.onResult(null, (Throwable)null);
            } else {
               AsyncOperationHelper.executeCommandAsync(this.binding, DropCollectionOperation.this.namespace.getDatabaseName(), (BsonDocument)nextCommandFunction.get(), this.connection, AsyncOperationHelper.writeConcernErrorTransformerAsync(), this);
            }

         }
      }
   }
}
