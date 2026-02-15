package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.AuthenticationMechanism;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoSecurityException;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.operation.ServerVersionHelper;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;

class DefaultAuthenticator extends Authenticator implements SpeculativeAuthenticator {
   static final int USER_NOT_FOUND_CODE = 11;
   private static final BsonString DEFAULT_MECHANISM_NAME;
   private Authenticator delegate;

   DefaultAuthenticator(MongoCredentialWithCache credential, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      super(credential, clusterConnectionMode, serverApi);
      Assertions.isTrueArgument("unspecified authentication mechanism", credential.getAuthenticationMechanism() == null);
   }

   void authenticate(InternalConnection connection, ConnectionDescription connectionDescription) {
      if (ServerVersionHelper.serverIsLessThanVersionFourDotZero(connectionDescription)) {
         (new ScramShaAuthenticator(this.getMongoCredentialWithCache().withMechanism(AuthenticationMechanism.SCRAM_SHA_1), this.getClusterConnectionMode(), this.getServerApi())).authenticate(connection, connectionDescription);
      } else {
         try {
            this.setDelegate(connectionDescription);
            this.delegate.authenticate(connection, connectionDescription);
         } catch (Exception e) {
            throw this.wrapException(e);
         }
      }

   }

   void authenticateAsync(InternalConnection connection, ConnectionDescription connectionDescription, SingleResultCallback<Void> callback) {
      if (ServerVersionHelper.serverIsLessThanVersionFourDotZero(connectionDescription)) {
         (new ScramShaAuthenticator(this.getMongoCredentialWithCache().withMechanism(AuthenticationMechanism.SCRAM_SHA_1), this.getClusterConnectionMode(), this.getServerApi())).authenticateAsync(connection, connectionDescription, callback);
      } else {
         this.setDelegate(connectionDescription);
         this.delegate.authenticateAsync(connection, connectionDescription, callback);
      }

   }

   public BsonDocument createSpeculativeAuthenticateCommand(InternalConnection connection) {
      this.delegate = this.getAuthenticatorForHello();
      return ((SpeculativeAuthenticator)this.delegate).createSpeculativeAuthenticateCommand(connection);
   }

   @Nullable
   public BsonDocument getSpeculativeAuthenticateResponse() {
      return this.delegate != null ? ((SpeculativeAuthenticator)this.delegate).getSpeculativeAuthenticateResponse() : null;
   }

   public void setSpeculativeAuthenticateResponse(BsonDocument response) {
      ((SpeculativeAuthenticator)this.delegate).setSpeculativeAuthenticateResponse(response);
   }

   Authenticator getAuthenticatorForHello() {
      return new ScramShaAuthenticator(this.getMongoCredentialWithCache().withMechanism(AuthenticationMechanism.SCRAM_SHA_256), this.getClusterConnectionMode(), this.getServerApi());
   }

   private void setDelegate(ConnectionDescription connectionDescription) {
      if (this.delegate == null || ((SpeculativeAuthenticator)this.delegate).getSpeculativeAuthenticateResponse() == null) {
         if (connectionDescription.getSaslSupportedMechanisms() != null) {
            BsonArray saslSupportedMechs = connectionDescription.getSaslSupportedMechanisms();
            AuthenticationMechanism mechanism = saslSupportedMechs.contains(DEFAULT_MECHANISM_NAME) ? AuthenticationMechanism.SCRAM_SHA_256 : AuthenticationMechanism.SCRAM_SHA_1;
            this.delegate = new ScramShaAuthenticator(this.getMongoCredentialWithCache().withMechanism(mechanism), this.getClusterConnectionMode(), this.getServerApi());
         } else {
            this.delegate = new ScramShaAuthenticator(this.getMongoCredentialWithCache().withMechanism(AuthenticationMechanism.SCRAM_SHA_1), this.getClusterConnectionMode(), this.getServerApi());
         }

      }
   }

   private MongoException wrapException(Throwable t) {
      if (t instanceof MongoSecurityException) {
         return (MongoSecurityException)t;
      } else {
         return (MongoException)(t instanceof MongoException && ((MongoException)t).getCode() == 11 ? new MongoSecurityException(this.getMongoCredential(), String.format("Exception authenticating %s", this.getMongoCredential()), t) : (MongoException)Assertions.assertNotNull(MongoException.fromThrowable(t)));
      }
   }

   static {
      DEFAULT_MECHANISM_NAME = new BsonString(AuthenticationMechanism.SCRAM_SHA_256.getMechanismName());
   }
}
