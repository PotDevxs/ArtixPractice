package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.com.mongodb.MongoCompressor;
import dev.artixdev.libs.com.mongodb.MongoCredential;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoSecurityException;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public class InternalStreamConnectionInitializer implements InternalConnectionInitializer {
   private final ClusterConnectionMode clusterConnectionMode;
   private final Authenticator authenticator;
   private final BsonDocument clientMetadataDocument;
   private final List<MongoCompressor> requestedCompressors;
   private final boolean checkSaslSupportedMechs;
   private final ServerApi serverApi;

   public InternalStreamConnectionInitializer(ClusterConnectionMode clusterConnectionMode, @Nullable Authenticator authenticator, @Nullable BsonDocument clientMetadataDocument, List<MongoCompressor> requestedCompressors, @Nullable ServerApi serverApi) {
      this.clusterConnectionMode = clusterConnectionMode;
      this.authenticator = authenticator;
      this.clientMetadataDocument = clientMetadataDocument;
      this.requestedCompressors = (List)Assertions.notNull("requestedCompressors", requestedCompressors);
      this.checkSaslSupportedMechs = authenticator instanceof DefaultAuthenticator;
      this.serverApi = serverApi;
   }

   public InternalConnectionInitializationDescription startHandshake(InternalConnection internalConnection) {
      Assertions.notNull("internalConnection", internalConnection);
      return this.initializeConnectionDescription(internalConnection);
   }

   public InternalConnectionInitializationDescription finishHandshake(InternalConnection internalConnection, InternalConnectionInitializationDescription description) {
      Assertions.notNull("internalConnection", internalConnection);
      Assertions.notNull("description", description);
      this.authenticate(internalConnection, description.getConnectionDescription());
      return this.completeConnectionDescriptionInitialization(internalConnection, description);
   }

   public void startHandshakeAsync(InternalConnection internalConnection, SingleResultCallback<InternalConnectionInitializationDescription> callback) {
      long startTime = System.nanoTime();
      CommandHelper.executeCommandAsync("admin", this.createHelloCommand(this.authenticator, internalConnection), this.clusterConnectionMode, this.serverApi, internalConnection, (helloResult, t) -> {
         if (t != null) {
            callback.onResult(null, (Throwable)(t instanceof MongoException ? this.mapHelloException((MongoException)t) : t));
         } else {
            this.setSpeculativeAuthenticateResponse(helloResult);
            callback.onResult(this.createInitializationDescription(helloResult, internalConnection, startTime), (Throwable)null);
         }

      });
   }

   public void finishHandshakeAsync(InternalConnection internalConnection, InternalConnectionInitializationDescription description, SingleResultCallback<InternalConnectionInitializationDescription> callback) {
      if (this.authenticator != null && description.getConnectionDescription().getServerType() != ServerType.REPLICA_SET_ARBITER) {
         this.authenticator.authenticateAsync(internalConnection, description.getConnectionDescription(), (result1, t1) -> {
            if (t1 != null) {
               callback.onResult(null, t1);
            } else {
               this.completeConnectionDescriptionInitializationAsync(internalConnection, description, callback);
            }

         });
      } else {
         this.completeConnectionDescriptionInitializationAsync(internalConnection, description, callback);
      }

   }

   private InternalConnectionInitializationDescription initializeConnectionDescription(InternalConnection internalConnection) {
      BsonDocument helloCommandDocument = this.createHelloCommand(this.authenticator, internalConnection);
      long start = System.nanoTime();

      BsonDocument helloResult;
      try {
         helloResult = CommandHelper.executeCommand("admin", helloCommandDocument, this.clusterConnectionMode, this.serverApi, internalConnection);
      } catch (MongoException e) {
         throw this.mapHelloException(e);
      }

      this.setSpeculativeAuthenticateResponse(helloResult);
      return this.createInitializationDescription(helloResult, internalConnection, start);
   }

   private MongoException mapHelloException(MongoException e) {
      if (this.checkSaslSupportedMechs && e.getCode() == 11) {
         MongoCredential credential = this.authenticator.getMongoCredential();
         return new MongoSecurityException(credential, String.format("Exception authenticating %s", credential), e);
      } else {
         return e;
      }
   }

   private InternalConnectionInitializationDescription createInitializationDescription(BsonDocument helloResult, InternalConnection internalConnection, long startTime) {
      ConnectionId connectionId = internalConnection.getDescription().getConnectionId();
      ConnectionDescription connectionDescription = DescriptionHelper.createConnectionDescription(this.clusterConnectionMode, connectionId, helloResult);
      ServerDescription serverDescription = DescriptionHelper.createServerDescription(internalConnection.getDescription().getServerAddress(), helloResult, System.nanoTime() - startTime);
      return new InternalConnectionInitializationDescription(connectionDescription, serverDescription);
   }

   private BsonDocument createHelloCommand(Authenticator authenticator, InternalConnection connection) {
      BsonDocument helloCommandDocument = (new BsonDocument(this.getHandshakeCommandName(), new BsonInt32(1))).append("helloOk", BsonBoolean.TRUE);
      if (this.clientMetadataDocument != null) {
         helloCommandDocument.append("client", this.clientMetadataDocument);
      }

      if (this.clusterConnectionMode == ClusterConnectionMode.LOAD_BALANCED) {
         helloCommandDocument.append("loadBalanced", BsonBoolean.TRUE);
      }

      if (!this.requestedCompressors.isEmpty()) {
         BsonArray compressors = new BsonArray(this.requestedCompressors.size());
         Iterator<MongoCompressor> iterator = this.requestedCompressors.iterator();

         while(iterator.hasNext()) {
            String compressorName = iterator.next().getName();
            compressors.add((BsonValue)(new BsonString(compressorName)));
         }

         helloCommandDocument.append("compression", compressors);
      }

      if (this.checkSaslSupportedMechs) {
         MongoCredential credential = authenticator.getMongoCredential();
         helloCommandDocument.append("saslSupportedMechs", new BsonString(credential.getSource() + "." + credential.getUserName()));
      }

      if (authenticator instanceof SpeculativeAuthenticator) {
         BsonDocument speculativeAuthenticateDocument = ((SpeculativeAuthenticator)authenticator).createSpeculativeAuthenticateCommand(connection);
         if (speculativeAuthenticateDocument != null) {
            helloCommandDocument.append("speculativeAuthenticate", speculativeAuthenticateDocument);
         }
      }

      return helloCommandDocument;
   }

   private InternalConnectionInitializationDescription completeConnectionDescriptionInitialization(InternalConnection internalConnection, InternalConnectionInitializationDescription description) {
      return description.getConnectionDescription().getConnectionId().getServerValue() != null ? description : this.applyGetLastErrorResult(CommandHelper.executeCommandWithoutCheckingForFailure("admin", new BsonDocument("getlasterror", new BsonInt32(1)), this.clusterConnectionMode, this.serverApi, internalConnection), description);
   }

   private void authenticate(InternalConnection internalConnection, ConnectionDescription connectionDescription) {
      if (this.authenticator != null && connectionDescription.getServerType() != ServerType.REPLICA_SET_ARBITER) {
         this.authenticator.authenticate(internalConnection, connectionDescription);
      }

   }

   private void setSpeculativeAuthenticateResponse(BsonDocument helloResult) {
      if (this.authenticator instanceof SpeculativeAuthenticator) {
         ((SpeculativeAuthenticator)this.authenticator).setSpeculativeAuthenticateResponse(helloResult.getDocument("speculativeAuthenticate", (BsonDocument)null));
      }

   }

   private void completeConnectionDescriptionInitializationAsync(InternalConnection internalConnection, InternalConnectionInitializationDescription description, SingleResultCallback<InternalConnectionInitializationDescription> callback) {
      if (description.getConnectionDescription().getConnectionId().getServerValue() != null) {
         callback.onResult(description, (Throwable)null);
      } else {
         CommandHelper.executeCommandAsync("admin", new BsonDocument("getlasterror", new BsonInt32(1)), this.clusterConnectionMode, this.serverApi, internalConnection, (result, t) -> {
            if (t != null) {
               callback.onResult(description, (Throwable)null);
            } else {
               callback.onResult(this.applyGetLastErrorResult(result, description), (Throwable)null);
            }

         });
      }
   }

   private InternalConnectionInitializationDescription applyGetLastErrorResult(BsonDocument getLastErrorResult, InternalConnectionInitializationDescription description) {
      ConnectionDescription connectionDescription = description.getConnectionDescription();
      ConnectionId connectionId;
      if (getLastErrorResult.containsKey("connectionId")) {
         connectionId = connectionDescription.getConnectionId().withServerValue(getLastErrorResult.getNumber("connectionId").intValue());
      } else {
         connectionId = connectionDescription.getConnectionId();
      }

      return description.withConnectionDescription(connectionDescription.withConnectionId(connectionId));
   }

   private String getHandshakeCommandName() {
      return this.serverApi == null ? "isMaster" : "hello";
   }
}
