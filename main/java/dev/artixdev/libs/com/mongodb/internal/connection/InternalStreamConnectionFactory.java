package dev.artixdev.libs.com.mongodb.internal.connection;

import java.util.List;
import dev.artixdev.libs.com.mongodb.AuthenticationMechanism;
import dev.artixdev.libs.com.mongodb.LoggerSettings;
import dev.artixdev.libs.com.mongodb.MongoCompressor;
import dev.artixdev.libs.com.mongodb.MongoDriverInformation;
import dev.artixdev.libs.com.mongodb.ServerApi;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.connection.StreamFactory;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.InetAddressResolver;
import dev.artixdev.libs.org.bson.BsonDocument;

class InternalStreamConnectionFactory implements InternalConnectionFactory {
   private final ClusterConnectionMode clusterConnectionMode;
   private final boolean isMonitoringConnection;
   private final StreamFactory streamFactory;
   private final BsonDocument clientMetadataDocument;
   private final List<MongoCompressor> compressorList;
   private final LoggerSettings loggerSettings;
   private final CommandListener commandListener;
   @Nullable
   private final ServerApi serverApi;
   private final InetAddressResolver inetAddressResolver;
   private final MongoCredentialWithCache credential;

   InternalStreamConnectionFactory(ClusterConnectionMode clusterConnectionMode, StreamFactory streamFactory, @Nullable MongoCredentialWithCache credential, @Nullable String applicationName, @Nullable MongoDriverInformation mongoDriverInformation, List<MongoCompressor> compressorList, LoggerSettings loggerSettings, @Nullable CommandListener commandListener, @Nullable ServerApi serverApi, @Nullable InetAddressResolver inetAddressResolver) {
      this(clusterConnectionMode, false, streamFactory, credential, applicationName, mongoDriverInformation, compressorList, loggerSettings, commandListener, serverApi, inetAddressResolver);
   }

   InternalStreamConnectionFactory(ClusterConnectionMode clusterConnectionMode, boolean isMonitoringConnection, StreamFactory streamFactory, @Nullable MongoCredentialWithCache credential, @Nullable String applicationName, @Nullable MongoDriverInformation mongoDriverInformation, List<MongoCompressor> compressorList, LoggerSettings loggerSettings, @Nullable CommandListener commandListener, @Nullable ServerApi serverApi, @Nullable InetAddressResolver inetAddressResolver) {
      this.clusterConnectionMode = clusterConnectionMode;
      this.isMonitoringConnection = isMonitoringConnection;
      this.streamFactory = (StreamFactory)Assertions.notNull("streamFactory", streamFactory);
      this.compressorList = (List)Assertions.notNull("compressorList", compressorList);
      this.loggerSettings = loggerSettings;
      this.commandListener = commandListener;
      this.serverApi = serverApi;
      this.inetAddressResolver = inetAddressResolver;
      this.clientMetadataDocument = ClientMetadataHelper.createClientMetadataDocument(applicationName, mongoDriverInformation);
      this.credential = credential;
   }

   public InternalConnection create(ServerId serverId, ConnectionGenerationSupplier connectionGenerationSupplier) {
      Authenticator authenticator = this.credential == null ? null : this.createAuthenticator(this.credential);
      return new InternalStreamConnection(this.clusterConnectionMode, this.isMonitoringConnection, serverId, connectionGenerationSupplier, this.streamFactory, this.compressorList, this.loggerSettings, this.commandListener, new InternalStreamConnectionInitializer(this.clusterConnectionMode, authenticator, this.clientMetadataDocument, this.compressorList, this.serverApi), this.inetAddressResolver);
   }

   private Authenticator createAuthenticator(MongoCredentialWithCache credential) {
      if (credential.getAuthenticationMechanism() == null) {
         return new DefaultAuthenticator(credential, this.clusterConnectionMode, this.serverApi);
      } else {
         switch((AuthenticationMechanism)Assertions.assertNotNull(credential.getAuthenticationMechanism())) {
         case GSSAPI:
            return new GSSAPIAuthenticator(credential, this.clusterConnectionMode, this.serverApi);
         case PLAIN:
            return new PlainAuthenticator(credential, this.clusterConnectionMode, this.serverApi);
         case MONGODB_X509:
            return new X509Authenticator(credential, this.clusterConnectionMode, this.serverApi);
         case SCRAM_SHA_1:
         case SCRAM_SHA_256:
            return new ScramShaAuthenticator(credential, this.clusterConnectionMode, this.serverApi);
         case MONGODB_AWS:
            return new AwsAuthenticator(credential, this.clusterConnectionMode, this.serverApi);
         default:
            throw new IllegalArgumentException("Unsupported authentication mechanism " + credential.getAuthenticationMechanism());
         }
      }
   }
}
