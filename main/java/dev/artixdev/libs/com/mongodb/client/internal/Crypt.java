package dev.artixdev.libs.com.mongodb.client.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.annotations.Beta;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.client.MongoClient;
import dev.artixdev.libs.com.mongodb.client.model.vault.DataKeyOptions;
import dev.artixdev.libs.com.mongodb.client.model.vault.EncryptOptions;
import dev.artixdev.libs.com.mongodb.client.model.vault.RewrapManyDataKeyOptions;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCrypt;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCryptContext;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCryptException;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoDataKeyOptions;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoKeyDecryptor;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoRewrapManyDataKeyOptions;
import dev.artixdev.libs.com.mongodb.crypt.capi.MongoCryptContext.State;
import dev.artixdev.libs.com.mongodb.internal.capi.MongoCryptHelper;
import dev.artixdev.libs.com.mongodb.internal.client.vault.EncryptOptionsHelper;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;
import dev.artixdev.libs.org.bson.RawBsonDocument;

public class Crypt implements Closeable {
   private static final RawBsonDocument EMPTY_RAW_BSON_DOCUMENT = RawBsonDocument.parse("{}");
   private final MongoCrypt mongoCrypt;
   private final Map<String, Map<String, Object>> kmsProviders;
   private final Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers;
   private final CollectionInfoRetriever collectionInfoRetriever;
   private final CommandMarker commandMarker;
   private final KeyRetriever keyRetriever;
   private final KeyManagementService keyManagementService;
   private final boolean bypassAutoEncryption;
   @Nullable
   private final MongoClient collectionInfoRetrieverClient;
   @Nullable
   private final MongoClient keyVaultClient;

   Crypt(MongoCrypt mongoCrypt, KeyRetriever keyRetriever, KeyManagementService keyManagementService, Map<String, Map<String, Object>> kmsProviders, Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers) {
      this(mongoCrypt, keyRetriever, keyManagementService, kmsProviders, kmsProviderPropertySuppliers, false, (CollectionInfoRetriever)null, (CommandMarker)null, (MongoClient)null, (MongoClient)null);
   }

   Crypt(MongoCrypt mongoCrypt, KeyRetriever keyRetriever, KeyManagementService keyManagementService, Map<String, Map<String, Object>> kmsProviders, Map<String, Supplier<Map<String, Object>>> kmsProviderPropertySuppliers, boolean bypassAutoEncryption, @Nullable CollectionInfoRetriever collectionInfoRetriever, @Nullable CommandMarker commandMarker, @Nullable MongoClient collectionInfoRetrieverClient, @Nullable MongoClient keyVaultClient) {
      this.mongoCrypt = mongoCrypt;
      this.keyRetriever = keyRetriever;
      this.keyManagementService = keyManagementService;
      this.kmsProviders = kmsProviders;
      this.kmsProviderPropertySuppliers = kmsProviderPropertySuppliers;
      this.bypassAutoEncryption = bypassAutoEncryption;
      this.collectionInfoRetriever = collectionInfoRetriever;
      this.commandMarker = commandMarker;
      this.collectionInfoRetrieverClient = collectionInfoRetrieverClient;
      this.keyVaultClient = keyVaultClient;
   }

   RawBsonDocument encrypt(String databaseName, RawBsonDocument command) {
      Assertions.notNull("databaseName", databaseName);
      Assertions.notNull("command", command);
      if (this.bypassAutoEncryption) {
         return command;
      } else {
         try {
            MongoCryptContext encryptionContext = this.mongoCrypt.createEncryptionContext(databaseName, command);

            RawBsonDocument var4;
            try {
               var4 = this.executeStateMachine(encryptionContext, databaseName);
            } catch (Throwable e) {
               if (encryptionContext != null) {
                  try {
                     encryptionContext.close();
                  } catch (Throwable suppressed) {
                     e.addSuppressed(suppressed);
                  }
               }

               throw e;
            }

            if (encryptionContext != null) {
               encryptionContext.close();
            }

            return var4;
         } catch (MongoCryptException e) {
            throw this.wrapInMongoException(e);
         }
      }
   }

   RawBsonDocument decrypt(RawBsonDocument commandResponse) {
      Assertions.notNull("commandResponse", commandResponse);

      try {
         MongoCryptContext decryptionContext = this.mongoCrypt.createDecryptionContext(commandResponse);

         RawBsonDocument var3;
         try {
            var3 = this.executeStateMachine(decryptionContext, (String)null);
         } catch (Throwable e) {
            if (decryptionContext != null) {
               try {
                  decryptionContext.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (decryptionContext != null) {
            decryptionContext.close();
         }

         return var3;
      } catch (MongoCryptException e) {
         throw this.wrapInMongoException(e);
      }
   }

   BsonDocument createDataKey(String kmsProvider, DataKeyOptions options) {
      Assertions.notNull("kmsProvider", kmsProvider);
      Assertions.notNull("options", options);

      try {
         MongoCryptContext dataKeyCreationContext = this.mongoCrypt.createDataKeyContext(kmsProvider, MongoDataKeyOptions.builder().keyAltNames(options.getKeyAltNames()).masterKey(options.getMasterKey()).keyMaterial(options.getKeyMaterial()).build());

         RawBsonDocument var4;
         try {
            var4 = this.executeStateMachine(dataKeyCreationContext, (String)null);
         } catch (Throwable e) {
            if (dataKeyCreationContext != null) {
               try {
                  dataKeyCreationContext.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (dataKeyCreationContext != null) {
            dataKeyCreationContext.close();
         }

         return var4;
      } catch (MongoCryptException e) {
         throw this.wrapInMongoException(e);
      }
   }

   BsonBinary encryptExplicitly(BsonValue value, EncryptOptions options) {
      Assertions.notNull("value", value);
      Assertions.notNull("options", options);

      try {
         MongoCryptContext encryptionContext = this.mongoCrypt.createExplicitEncryptionContext(new BsonDocument("v", value), EncryptOptionsHelper.asMongoExplicitEncryptOptions(options));

         BsonBinary var4;
         try {
            var4 = this.executeStateMachine(encryptionContext, (String)null).getBinary("v");
         } catch (Throwable e) {
            if (encryptionContext != null) {
               try {
                  encryptionContext.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (encryptionContext != null) {
            encryptionContext.close();
         }

         return var4;
      } catch (MongoCryptException e) {
         throw this.wrapInMongoException(e);
      }
   }

   @Beta({Beta.Reason.SERVER})
   BsonDocument encryptExpression(BsonDocument expression, EncryptOptions options) {
      Assertions.notNull("expression", expression);
      Assertions.notNull("options", options);

      try {
         MongoCryptContext encryptionContext = this.mongoCrypt.createEncryptExpressionContext(new BsonDocument("v", expression), EncryptOptionsHelper.asMongoExplicitEncryptOptions(options));

         BsonDocument var4;
         try {
            var4 = this.executeStateMachine(encryptionContext, (String)null).getDocument("v");
         } catch (Throwable e) {
            if (encryptionContext != null) {
               try {
                  encryptionContext.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (encryptionContext != null) {
            encryptionContext.close();
         }

         return var4;
      } catch (MongoCryptException e) {
         throw this.wrapInMongoException(e);
      }
   }

   BsonValue decryptExplicitly(BsonBinary value) {
      Assertions.notNull("value", value);

      try {
         MongoCryptContext decryptionContext = this.mongoCrypt.createExplicitDecryptionContext(new BsonDocument("v", value));

         BsonValue var3;
         try {
            var3 = (BsonValue)Assertions.assertNotNull(this.executeStateMachine(decryptionContext, (String)null).get("v"));
         } catch (Throwable e) {
            if (decryptionContext != null) {
               try {
                  decryptionContext.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (decryptionContext != null) {
            decryptionContext.close();
         }

         return var3;
      } catch (MongoCryptException e) {
         throw this.wrapInMongoException(e);
      }
   }

   BsonDocument rewrapManyDataKey(BsonDocument filter, RewrapManyDataKeyOptions options) {
      Assertions.notNull("filter", filter);

      try {
         MongoCryptContext rewrapManyDatakeyContext = this.mongoCrypt.createRewrapManyDatakeyContext(filter, MongoRewrapManyDataKeyOptions.builder().provider(options.getProvider()).masterKey(options.getMasterKey()).build());

         RawBsonDocument var4;
         try {
            var4 = this.executeStateMachine(rewrapManyDatakeyContext, (String)null);
         } catch (Throwable e) {
            if (rewrapManyDatakeyContext != null) {
               try {
                  rewrapManyDatakeyContext.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (rewrapManyDatakeyContext != null) {
            rewrapManyDatakeyContext.close();
         }

         return var4;
      } catch (MongoCryptException e) {
         throw this.wrapInMongoException(e);
      }
   }

   public void close() {
      MongoCrypt ignored = this.mongoCrypt;

      try {
         CommandMarker ignored1 = this.commandMarker;

         try {
            MongoClient ignored2 = this.collectionInfoRetrieverClient;

            try {
               MongoClient ignored3 = this.keyVaultClient;
               if (ignored3 != null) {
                  ignored3.close();
               }
            } catch (Throwable e) {
               if (ignored2 != null) {
                  try {
                     ignored2.close();
                  } catch (Throwable suppressed) {
                     e.addSuppressed(suppressed);
                  }
               }

               throw e;
            }

            if (ignored2 != null) {
               ignored2.close();
            }
         } catch (Throwable e) {
            if (ignored1 != null) {
               try {
                  ignored1.close();
               } catch (Throwable suppressed) {
                  e.addSuppressed(suppressed);
               }
            }

            throw e;
         }

         if (ignored1 != null) {
            ignored1.close();
         }
      } catch (Throwable e) {
         if (ignored != null) {
            try {
               ignored.close();
            } catch (Throwable suppressed) {
               e.addSuppressed(suppressed);
            }
         }

         throw e;
      }

      if (ignored != null) {
         ignored.close();
      }

   }

   private RawBsonDocument executeStateMachine(MongoCryptContext cryptContext, @Nullable String databaseName) {
      while(true) {
         State state = cryptContext.getState();
         switch(state) {
         case NEED_MONGO_COLLINFO:
            this.collInfo(cryptContext, (String)Assertions.notNull("databaseName", databaseName));
            break;
         case NEED_MONGO_MARKINGS:
            this.mark(cryptContext, (String)Assertions.notNull("databaseName", databaseName));
            break;
         case NEED_KMS_CREDENTIALS:
            this.fetchCredentials(cryptContext);
            break;
         case NEED_MONGO_KEYS:
            this.fetchKeys(cryptContext);
            break;
         case NEED_KMS:
            this.decryptKeys(cryptContext);
            break;
         case READY:
            return cryptContext.finish();
         case DONE:
            return EMPTY_RAW_BSON_DOCUMENT;
         default:
            throw new MongoInternalException("Unsupported encryptor state + " + state);
         }
      }
   }

   private void fetchCredentials(MongoCryptContext cryptContext) {
      cryptContext.provideKmsProviderCredentials(MongoCryptHelper.fetchCredentials(this.kmsProviders, this.kmsProviderPropertySuppliers));
   }

   private void collInfo(MongoCryptContext cryptContext, String databaseName) {
      try {
         BsonDocument collectionInfo = ((CollectionInfoRetriever)Assertions.assertNotNull(this.collectionInfoRetriever)).filter(databaseName, cryptContext.getMongoOperation());
         if (collectionInfo != null) {
            cryptContext.addMongoOperationResult(collectionInfo);
         }

         cryptContext.completeMongoOperation();
      } catch (Throwable e) {
         throw MongoException.fromThrowableNonNull(e);
      }
   }

   private void mark(MongoCryptContext cryptContext, String databaseName) {
      try {
         RawBsonDocument markedCommand = ((CommandMarker)Assertions.assertNotNull(this.commandMarker)).mark(databaseName, cryptContext.getMongoOperation());
         cryptContext.addMongoOperationResult(markedCommand);
         cryptContext.completeMongoOperation();
      } catch (Throwable e) {
         throw this.wrapInMongoException(e);
      }
   }

   private void fetchKeys(MongoCryptContext keyBroker) {
      try {
         Iterator var2 = this.keyRetriever.find(keyBroker.getMongoOperation()).iterator();

         while(var2.hasNext()) {
            BsonDocument bsonDocument = (BsonDocument)var2.next();
            keyBroker.addMongoOperationResult(bsonDocument);
         }

         keyBroker.completeMongoOperation();
      } catch (Throwable e) {
         throw MongoException.fromThrowableNonNull(e);
      }
   }

   private void decryptKeys(MongoCryptContext cryptContext) {
      try {
         for(MongoKeyDecryptor keyDecryptor = cryptContext.nextKeyDecryptor(); keyDecryptor != null; keyDecryptor = cryptContext.nextKeyDecryptor()) {
            this.decryptKey(keyDecryptor);
         }

         cryptContext.completeKeyDecryptors();
      } catch (Throwable e) {
         throw (MongoInterruptedException)InterruptionUtil.translateInterruptedException(e, "Interrupted while doing IO").orElseThrow(() -> {
            return this.wrapInMongoException(e);
         });
      }
   }

   private void decryptKey(MongoKeyDecryptor keyDecryptor) throws IOException {
      InputStream inputStream = this.keyManagementService.stream(keyDecryptor.getKmsProvider(), keyDecryptor.getHostName(), keyDecryptor.getMessage());

      try {
         for(int bytesNeeded = keyDecryptor.bytesNeeded(); bytesNeeded > 0; bytesNeeded = keyDecryptor.bytesNeeded()) {
            byte[] bytes = new byte[bytesNeeded];
            int bytesRead = inputStream.read(bytes, 0, bytes.length);
            keyDecryptor.feed(ByteBuffer.wrap(bytes, 0, bytesRead));
         }
      } catch (Throwable e) {
         if (inputStream != null) {
            try {
               inputStream.close();
            } catch (Throwable suppressed) {
               e.addSuppressed(suppressed);
            }
         }

         throw e;
      }

      if (inputStream != null) {
         inputStream.close();
      }

   }

   private MongoException wrapInMongoException(Throwable t) {
      return (MongoException)(t instanceof MongoException ? (MongoException)t : new MongoClientException("Exception in encryption library: " + t.getMessage(), t));
   }
}
