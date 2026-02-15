package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.AuthenticationMechanism;
import dev.artixdev.libs.com.mongodb.MongoCommandException;
import dev.artixdev.libs.com.mongodb.MongoSecurityException;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

class X509Authenticator extends Authenticator implements SpeculativeAuthenticator {
   public static final Logger LOGGER = Loggers.getLogger("authenticator");
   private BsonDocument speculativeAuthenticateResponse;

   X509Authenticator(MongoCredentialWithCache credential, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      super(credential, clusterConnectionMode, serverApi);
   }

   void authenticate(InternalConnection connection, ConnectionDescription connectionDescription) {
      if (this.speculativeAuthenticateResponse == null) {
         try {
            BsonDocument authCommand = this.getAuthCommand(this.getMongoCredential().getUserName());
            CommandHelper.executeCommand(this.getMongoCredential().getSource(), authCommand, this.getClusterConnectionMode(), this.getServerApi(), connection);
         } catch (MongoCommandException e) {
            throw new MongoSecurityException(this.getMongoCredential(), "Exception authenticating", e);
         }
      }
   }

   void authenticateAsync(InternalConnection connection, ConnectionDescription connectionDescription, SingleResultCallback<Void> callback) {
      if (this.speculativeAuthenticateResponse != null) {
         callback.onResult(null, (Throwable)null);
      } else {
         SingleResultCallback errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER);

         try {
            CommandHelper.executeCommandAsync(this.getMongoCredential().getSource(), this.getAuthCommand(this.getMongoCredential().getUserName()), this.getClusterConnectionMode(), this.getServerApi(), connection, (nonceResult, t) -> {
               if (t != null) {
                  errHandlingCallback.onResult(null, this.translateThrowable(t));
               } else {
                  errHandlingCallback.onResult(null, (Throwable)null);
               }

            });
         } catch (Throwable throwable) {
            errHandlingCallback.onResult(null, throwable);
         }
      }

   }

   public BsonDocument createSpeculativeAuthenticateCommand(InternalConnection connection) {
      return this.getAuthCommand(this.getMongoCredential().getUserName()).append("db", new BsonString("$external"));
   }

   public void setSpeculativeAuthenticateResponse(BsonDocument response) {
      this.speculativeAuthenticateResponse = response;
   }

   public BsonDocument getSpeculativeAuthenticateResponse() {
      return this.speculativeAuthenticateResponse;
   }

   private BsonDocument getAuthCommand(@Nullable String userName) {
      BsonDocument cmd = new BsonDocument();
      cmd.put((String)"authenticate", (BsonValue)(new BsonInt32(1)));
      if (userName != null) {
         cmd.put((String)"user", (BsonValue)(new BsonString(userName)));
      }

      cmd.put((String)"mechanism", (BsonValue)(new BsonString(AuthenticationMechanism.MONGODB_X509.getMechanismName())));
      return cmd;
   }

   private Throwable translateThrowable(Throwable t) {
      return (Throwable)(t instanceof MongoCommandException ? new MongoSecurityException(this.getMongoCredential(), "Exception authenticating", t) : t);
   }
}
