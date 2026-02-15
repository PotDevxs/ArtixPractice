package dev.artixdev.libs.com.mongodb.internal.connection;

import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.MongoSecurityException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.SubjectProvider;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBinary;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;

abstract class SaslAuthenticator extends Authenticator implements SpeculativeAuthenticator {
   public static final Logger LOGGER = Loggers.getLogger("authenticator");
   private static final String SUBJECT_PROVIDER_CACHE_KEY = "SUBJECT_PROVIDER";

   SaslAuthenticator(MongoCredentialWithCache credential, ClusterConnectionMode clusterConnectionMode, @Nullable ServerApi serverApi) {
      super(credential, clusterConnectionMode, serverApi);
   }

   public void authenticate(InternalConnection connection, ConnectionDescription connectionDescription) {
      this.doAsSubject(() -> {
         SaslClient saslClient = this.createSaslClient(connection.getDescription().getServerAddress());
         this.throwIfSaslClientIsNull(saslClient);

         try {
            BsonDocument responseDocument = this.getNextSaslResponse(saslClient, connection);

            byte[] response;
            for(BsonInt32 conversationId = responseDocument.getInt32("conversationId"); !responseDocument.getBoolean("done").getValue(); responseDocument = this.sendSaslContinue(conversationId, response, connection)) {
               response = saslClient.evaluateChallenge(responseDocument.getBinary("payload").getData());
               if (response == null) {
                  throw new MongoSecurityException(this.getMongoCredential(), "SASL protocol error: no client response to challenge for credential " + this.getMongoCredential());
               }
            }

            if (!saslClient.isComplete()) {
               saslClient.evaluateChallenge(responseDocument.getBinary("payload").getData());
               if (!saslClient.isComplete()) {
                  throw new MongoSecurityException(this.getMongoCredential(), "SASL protocol error: server completed challenges before client completed responses " + this.getMongoCredential());
               }
            }
         } catch (Exception exception) {
            throw this.wrapException(exception);
         } finally {
            this.disposeOfSaslClient(saslClient);
         }

         return null;
      });
   }

   void authenticateAsync(InternalConnection connection, ConnectionDescription connectionDescription, SingleResultCallback<Void> callback) {
      try {
         this.doAsSubject(() -> {
            SaslClient saslClient = this.createSaslClient(connection.getDescription().getServerAddress());
            this.throwIfSaslClientIsNull(saslClient);
            this.getNextSaslResponseAsync(saslClient, connection, callback);
            return null;
         });
      } catch (Throwable throwable) {
         callback.onResult(null, throwable);
      }

   }

   public abstract String getMechanismName();

   protected abstract SaslClient createSaslClient(ServerAddress var1);

   protected void appendSaslStartOptions(BsonDocument saslStartCommand) {
   }

   private void throwIfSaslClientIsNull(@Nullable SaslClient saslClient) {
      if (saslClient == null) {
         throw new MongoSecurityException(this.getMongoCredential(), String.format("This JDK does not support the %s SASL mechanism", this.getMechanismName()));
      }
   }

   private BsonDocument getNextSaslResponse(SaslClient saslClient, InternalConnection connection) {
      BsonDocument response = this.getSpeculativeAuthenticateResponse();
      if (response != null) {
         return response;
      } else {
         try {
            byte[] serverResponse = saslClient.hasInitialResponse() ? saslClient.evaluateChallenge(new byte[0]) : null;
            return this.sendSaslStart(serverResponse, connection);
         } catch (Exception exception) {
            throw this.wrapException(exception);
         }
      }
   }

   private void getNextSaslResponseAsync(SaslClient saslClient, InternalConnection connection, SingleResultCallback<Void> callback) {
      BsonDocument response = this.getSpeculativeAuthenticateResponse();
      SingleResultCallback errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER);

      try {
         if (response == null) {
            byte[] serverResponse = saslClient.hasInitialResponse() ? saslClient.evaluateChallenge(new byte[0]) : null;
            this.sendSaslStartAsync(serverResponse, connection, (result, t) -> {
               if (t != null) {
                  errHandlingCallback.onResult(null, this.wrapException(t));
               } else {
                  Assertions.assertNotNull(result);
                  if (result.getBoolean("done").getValue()) {
                     this.verifySaslClientComplete(saslClient, result, errHandlingCallback);
                  } else {
                     (new SaslAuthenticator.Continuator(saslClient, result, connection, errHandlingCallback)).start();
                  }

               }
            });
         } else if (response.getBoolean("done").getValue()) {
            this.verifySaslClientComplete(saslClient, response, errHandlingCallback);
         } else {
            (new SaslAuthenticator.Continuator(saslClient, response, connection, errHandlingCallback)).start();
         }
      } catch (Exception exception) {
         callback.onResult(null, this.wrapException(exception));
      }

   }

   private void verifySaslClientComplete(SaslClient saslClient, BsonDocument result, SingleResultCallback<Void> callback) {
      if (saslClient.isComplete()) {
         callback.onResult(null, (Throwable)null);
      } else {
         try {
            saslClient.evaluateChallenge(result.getBinary("payload").getData());
            if (saslClient.isComplete()) {
               callback.onResult(null, (Throwable)null);
            } else {
               callback.onResult(null, new MongoSecurityException(this.getMongoCredential(), "SASL protocol error: server completed challenges before client completed responses " + this.getMongoCredential()));
            }
         } catch (SaslException e) {
            callback.onResult(null, this.wrapException(e));
         }
      }

   }

   @Nullable
   protected Subject getSubject() {
      Subject subject = (Subject)this.getMongoCredential().getMechanismProperty("JAVA_SUBJECT", (Object)null);
      if (subject != null) {
         return subject;
      } else {
         try {
            return this.getSubjectProvider().getSubject();
         } catch (LoginException e) {
            throw new MongoSecurityException(this.getMongoCredential(), "Failed to login Subject", e);
         }
      }
   }

   @NonNull
   private SubjectProvider getSubjectProvider() {
      return (SubjectProvider)Locks.withInterruptibleLock(this.getMongoCredentialWithCache().getLock(), () -> {
         SubjectProvider subjectProvider = (SubjectProvider)this.getMongoCredentialWithCache().getFromCache("SUBJECT_PROVIDER", SubjectProvider.class);
         if (subjectProvider == null) {
            subjectProvider = (SubjectProvider)this.getMongoCredential().getMechanismProperty("JAVA_SUBJECT_PROVIDER", (Object)null);
            if (subjectProvider == null) {
               subjectProvider = this.getDefaultSubjectProvider();
            }

            this.getMongoCredentialWithCache().putInCache("SUBJECT_PROVIDER", subjectProvider);
         }

         return subjectProvider;
      });
   }

   @NonNull
   protected SubjectProvider getDefaultSubjectProvider() {
      return () -> {
         return null;
      };
   }

   private BsonDocument sendSaslStart(@Nullable byte[] outToken, InternalConnection connection) {
      BsonDocument startDocument = this.createSaslStartCommandDocument(outToken);
      this.appendSaslStartOptions(startDocument);
      return CommandHelper.executeCommand(this.getMongoCredential().getSource(), startDocument, this.getClusterConnectionMode(), this.getServerApi(), connection);
   }

   private BsonDocument sendSaslContinue(BsonInt32 conversationId, byte[] outToken, InternalConnection connection) {
      return CommandHelper.executeCommand(this.getMongoCredential().getSource(), this.createSaslContinueDocument(conversationId, outToken), this.getClusterConnectionMode(), this.getServerApi(), connection);
   }

   private void sendSaslStartAsync(@Nullable byte[] outToken, InternalConnection connection, SingleResultCallback<BsonDocument> callback) {
      BsonDocument startDocument = this.createSaslStartCommandDocument(outToken);
      this.appendSaslStartOptions(startDocument);
      CommandHelper.executeCommandAsync(this.getMongoCredential().getSource(), startDocument, this.getClusterConnectionMode(), this.getServerApi(), connection, callback);
   }

   private void sendSaslContinueAsync(BsonInt32 conversationId, byte[] outToken, InternalConnection connection, SingleResultCallback<BsonDocument> callback) {
      CommandHelper.executeCommandAsync(this.getMongoCredential().getSource(), this.createSaslContinueDocument(conversationId, outToken), this.getClusterConnectionMode(), this.getServerApi(), connection, callback);
   }

   protected BsonDocument createSaslStartCommandDocument(@Nullable byte[] outToken) {
      return (new BsonDocument("saslStart", new BsonInt32(1))).append("mechanism", new BsonString(this.getMechanismName())).append("payload", new BsonBinary(outToken != null ? outToken : new byte[0]));
   }

   private BsonDocument createSaslContinueDocument(BsonInt32 conversationId, byte[] outToken) {
      return (new BsonDocument("saslContinue", new BsonInt32(1))).append("conversationId", conversationId).append("payload", new BsonBinary(outToken));
   }

   private void disposeOfSaslClient(SaslClient saslClient) {
      try {
         saslClient.dispose();
      } catch (SaslException e) {
      }

   }

   protected MongoException wrapException(Throwable t) {
      if (t instanceof MongoInterruptedException) {
         return (MongoInterruptedException)t;
      } else {
         return t instanceof MongoSecurityException ? (MongoSecurityException)t : new MongoSecurityException(this.getMongoCredential(), "Exception authenticating " + this.getMongoCredential(), t);
      }
   }

   void doAsSubject(PrivilegedAction<Void> action) {
      Subject subject = this.getSubject();
      if (subject == null) {
         action.run();
      } else {
         Subject.doAs(subject, action);
      }

   }

   private final class Continuator implements SingleResultCallback<BsonDocument> {
      private final SaslClient saslClient;
      private final BsonDocument saslStartDocument;
      private final InternalConnection connection;
      private final SingleResultCallback<Void> callback;

      Continuator(SaslClient saslClient, BsonDocument saslStartDocument, InternalConnection connection, SingleResultCallback<Void> callback) {
         this.saslClient = saslClient;
         this.saslStartDocument = saslStartDocument;
         this.connection = connection;
         this.callback = callback;
      }

      public void onResult(@Nullable BsonDocument result, @Nullable Throwable t) {
         if (t != null) {
            this.callback.onResult(null, SaslAuthenticator.this.wrapException(t));
            SaslAuthenticator.this.disposeOfSaslClient(this.saslClient);
         } else {
            Assertions.assertNotNull(result);
            if (result.getBoolean("done").getValue()) {
               SaslAuthenticator.this.verifySaslClientComplete(this.saslClient, result, this.callback);
               SaslAuthenticator.this.disposeOfSaslClient(this.saslClient);
            } else {
               this.continueConversation(result);
            }

         }
      }

      public void start() {
         this.continueConversation(this.saslStartDocument);
      }

      private void continueConversation(BsonDocument result) {
         try {
            SaslAuthenticator.this.doAsSubject(() -> {
               try {
                  SaslAuthenticator.this.sendSaslContinueAsync(this.saslStartDocument.getInt32("conversationId"), this.saslClient.evaluateChallenge(result.getBinary("payload").getData()), this.connection, this);
                  return null;
               } catch (SaslException e) {
                  throw SaslAuthenticator.this.wrapException(e);
               }
            });
         } catch (Throwable throwable) {
            this.callback.onResult(null, throwable);
            SaslAuthenticator.this.disposeOfSaslClient(this.saslClient);
         }

      }
   }
}
