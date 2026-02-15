package dev.artixdev.libs.com.mongodb.internal.connection;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import dev.artixdev.libs.com.mongodb.LoggerSettings;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoCompressor;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.MongoInterruptedException;
import dev.artixdev.libs.com.mongodb.MongoSocketClosedException;
import dev.artixdev.libs.com.mongodb.MongoSocketReadException;
import dev.artixdev.libs.com.mongodb.MongoSocketReadTimeoutException;
import dev.artixdev.libs.com.mongodb.MongoSocketWriteException;
import dev.artixdev.libs.com.mongodb.RequestContext;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.UnixServerAddress;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.AsyncCompletionHandler;
import dev.artixdev.libs.com.mongodb.connection.ClusterConnectionMode;
import dev.artixdev.libs.com.mongodb.connection.ClusterId;
import dev.artixdev.libs.com.mongodb.connection.ConnectionDescription;
import dev.artixdev.libs.com.mongodb.connection.ConnectionId;
import dev.artixdev.libs.com.mongodb.connection.ServerConnectionState;
import dev.artixdev.libs.com.mongodb.connection.ServerDescription;
import dev.artixdev.libs.com.mongodb.connection.ServerId;
import dev.artixdev.libs.com.mongodb.connection.ServerType;
import dev.artixdev.libs.com.mongodb.connection.Stream;
import dev.artixdev.libs.com.mongodb.connection.StreamFactory;
import dev.artixdev.libs.com.mongodb.event.CommandListener;
import dev.artixdev.libs.com.mongodb.internal.ResourceUtil;
import dev.artixdev.libs.com.mongodb.internal.async.ErrorHandlingResultCallback;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.internal.logging.LogMessage;
import dev.artixdev.libs.com.mongodb.internal.logging.StructuredLogger;
import dev.artixdev.libs.com.mongodb.internal.session.SessionContext;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.com.mongodb.spi.dns.InetAddressResolver;
import dev.artixdev.libs.org.bson.BsonBinaryReader;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonReader;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.codecs.BsonDocumentCodec;
import dev.artixdev.libs.org.bson.codecs.Decoder;
import dev.artixdev.libs.org.bson.io.ByteBufferBsonInput;
import dev.artixdev.libs.org.bson.types.ObjectId;

@NotThreadSafe
public class InternalStreamConnection implements InternalConnection {
   private static final Set<String> SECURITY_SENSITIVE_COMMANDS = new HashSet(Arrays.asList("authenticate", "saslStart", "saslContinue", "getnonce", "createUser", "updateUser", "copydbgetnonce", "copydbsaslstart", "copydb"));
   private static final Set<String> SECURITY_SENSITIVE_HELLO_COMMANDS;
   private static final Logger LOGGER;
   private final ClusterConnectionMode clusterConnectionMode;
   private final boolean isMonitoringConnection;
   private final ServerId serverId;
   private final ConnectionGenerationSupplier connectionGenerationSupplier;
   private final StreamFactory streamFactory;
   private final InternalConnectionInitializer connectionInitializer;
   private final InetAddressResolver inetAddressResolver;
   private volatile ConnectionDescription description;
   private volatile ServerDescription initialServerDescription;
   private volatile Stream stream;
   private final AtomicBoolean isClosed;
   private final AtomicBoolean opened;
   private final List<MongoCompressor> compressorList;
   private final LoggerSettings loggerSettings;
   private final CommandListener commandListener;
   @Nullable
   private volatile Compressor sendCompressor;
   private final Map<Byte, Compressor> compressorMap;
   private volatile boolean hasMoreToCome;
   private volatile int responseTo;
   private int generation;
   private static final StructuredLogger COMMAND_PROTOCOL_LOGGER;

   static Set<String> getSecuritySensitiveCommands() {
      return Collections.unmodifiableSet(SECURITY_SENSITIVE_COMMANDS);
   }

   static Set<String> getSecuritySensitiveHelloCommands() {
      return Collections.unmodifiableSet(SECURITY_SENSITIVE_HELLO_COMMANDS);
   }

   public InternalStreamConnection(ClusterConnectionMode clusterConnectionMode, ServerId serverId, ConnectionGenerationSupplier connectionGenerationSupplier, StreamFactory streamFactory, List<MongoCompressor> compressorList, CommandListener commandListener, InternalConnectionInitializer connectionInitializer, @Nullable InetAddressResolver inetAddressResolver) {
      this(clusterConnectionMode, false, serverId, connectionGenerationSupplier, streamFactory, compressorList, LoggerSettings.builder().build(), commandListener, connectionInitializer, inetAddressResolver);
   }

   public InternalStreamConnection(ClusterConnectionMode clusterConnectionMode, boolean isMonitoringConnection, ServerId serverId, ConnectionGenerationSupplier connectionGenerationSupplier, StreamFactory streamFactory, List<MongoCompressor> compressorList, LoggerSettings loggerSettings, CommandListener commandListener, InternalConnectionInitializer connectionInitializer, @Nullable InetAddressResolver inetAddressResolver) {
      this.isClosed = new AtomicBoolean();
      this.opened = new AtomicBoolean();
      this.generation = -1;
      this.clusterConnectionMode = clusterConnectionMode;
      this.isMonitoringConnection = isMonitoringConnection;
      this.serverId = (ServerId)Assertions.notNull("serverId", serverId);
      this.connectionGenerationSupplier = (ConnectionGenerationSupplier)Assertions.notNull("connectionGeneration", connectionGenerationSupplier);
      this.streamFactory = (StreamFactory)Assertions.notNull("streamFactory", streamFactory);
      this.compressorList = (List)Assertions.notNull("compressorList", compressorList);
      this.compressorMap = this.createCompressorMap(compressorList);
      this.loggerSettings = loggerSettings;
      this.commandListener = commandListener;
      this.connectionInitializer = (InternalConnectionInitializer)Assertions.notNull("connectionInitializer", connectionInitializer);
      this.description = new ConnectionDescription(serverId);
      this.initialServerDescription = ServerDescription.builder().address(serverId.getAddress()).type(ServerType.UNKNOWN).state(ServerConnectionState.CONNECTING).build();
      this.inetAddressResolver = inetAddressResolver;
      if (clusterConnectionMode != ClusterConnectionMode.LOAD_BALANCED) {
         this.generation = connectionGenerationSupplier.getGeneration();
      }

   }

   public ConnectionDescription getDescription() {
      return this.description;
   }

   public ServerDescription getInitialServerDescription() {
      return this.initialServerDescription;
   }

   public int getGeneration() {
      return this.generation;
   }

   public void open() {
      Assertions.isTrue("Open already called", this.stream == null);
      this.stream = this.streamFactory.create(this.getServerAddressWithResolver());

      try {
         this.stream.open();
         InternalConnectionInitializationDescription initializationDescription = this.connectionInitializer.startHandshake(this);
         this.initAfterHandshakeStart(initializationDescription);
         initializationDescription = this.connectionInitializer.finishHandshake(this, initializationDescription);
         this.initAfterHandshakeFinish(initializationDescription);
      } catch (Throwable throwable) {
         this.close();
         if (throwable instanceof MongoException) {
            throw (MongoException)throwable;
         } else {
            throw new MongoException(throwable.toString(), throwable);
         }
      }
   }

   public void openAsync(final SingleResultCallback<Void> callback) {
      Assertions.isTrue("Open already called", this.stream == null, callback);

      try {
         this.stream = this.streamFactory.create(this.getServerAddressWithResolver());
         this.stream.openAsync(new AsyncCompletionHandler<Void>() {
            public void completed(@Nullable Void aVoid) {
               InternalStreamConnection.this.connectionInitializer.startHandshakeAsync(InternalStreamConnection.this, (initialResult, initialException) -> {
                  if (initialException != null) {
                     InternalStreamConnection.this.close();
                     callback.onResult(null, initialException);
                  } else {
                     Assertions.assertNotNull(initialResult);
                     InternalStreamConnection.this.initAfterHandshakeStart(initialResult);
                     InternalStreamConnection.this.connectionInitializer.finishHandshakeAsync(InternalStreamConnection.this, initialResult, (completedResult, completedException) -> {
                        if (completedException != null) {
                           InternalStreamConnection.this.close();
                           callback.onResult(null, completedException);
                        } else {
                           Assertions.assertNotNull(completedResult);
                           InternalStreamConnection.this.initAfterHandshakeFinish(completedResult);
                           callback.onResult(null, (Throwable)null);
                        }

                     });
                  }

               });
            }

            public void failed(Throwable t) {
               InternalStreamConnection.this.close();
               callback.onResult(null, t);
            }
         });
      } catch (Throwable throwable) {
         this.close();
         callback.onResult(null, throwable);
      }

   }

   private ServerAddress getServerAddressWithResolver() {
      return (ServerAddress)(this.serverId.getAddress() instanceof UnixServerAddress ? this.serverId.getAddress() : new ServerAddressWithResolver(this.serverId.getAddress(), this.inetAddressResolver));
   }

   private void initAfterHandshakeStart(InternalConnectionInitializationDescription initializationDescription) {
      this.description = initializationDescription.getConnectionDescription();
      this.initialServerDescription = initializationDescription.getServerDescription();
      if (this.clusterConnectionMode == ClusterConnectionMode.LOAD_BALANCED) {
         this.generation = this.connectionGenerationSupplier.getGeneration((ObjectId)Assertions.assertNotNull(this.description.getServiceId()));
      }

   }

   private void initAfterHandshakeFinish(InternalConnectionInitializationDescription initializationDescription) {
      this.description = initializationDescription.getConnectionDescription();
      this.initialServerDescription = initializationDescription.getServerDescription();
      this.opened.set(true);
      this.sendCompressor = this.findSendCompressor(this.description);
   }

   private Map<Byte, Compressor> createCompressorMap(List<MongoCompressor> compressorList) {
      Map<Byte, Compressor> compressorMap = new HashMap(this.compressorList.size());
      Iterator<MongoCompressor> iterator = compressorList.iterator();

      while(iterator.hasNext()) {
         MongoCompressor mongoCompressor = iterator.next();
         Compressor compressor = this.createCompressor(mongoCompressor);
         compressorMap.put(compressor.getId(), compressor);
      }

      return compressorMap;
   }

   @Nullable
   private Compressor findSendCompressor(ConnectionDescription description) {
      if (description.getCompressors().isEmpty()) {
         return null;
      } else {
         String firstCompressorName = (String)description.getCompressors().get(0);
         Iterator<Compressor> iterator = this.compressorMap.values().iterator();

         Compressor compressor;
         do {
            if (!iterator.hasNext()) {
               throw new MongoInternalException("Unexpected compressor negotiated: " + firstCompressorName);
            }

            compressor = iterator.next();
         } while(!compressor.getName().equals(firstCompressorName));

         return compressor;
      }
   }

   private Compressor createCompressor(MongoCompressor mongoCompressor) {
      String var2 = mongoCompressor.getName();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -898026669:
         if (var2.equals("snappy")) {
            var3 = 1;
         }
         break;
      case 3741643:
         if (var2.equals("zlib")) {
            var3 = 0;
         }
         break;
      case 3748713:
         if (var2.equals("zstd")) {
            var3 = 2;
         }
      }

      switch(var3) {
      case 0:
         return new ZlibCompressor(mongoCompressor);
      case 1:
         return new SnappyCompressor();
      case 2:
         return new ZstdCompressor();
      default:
         throw new MongoClientException("Unsupported compressor " + mongoCompressor.getName());
      }
   }

   public void close() {
      if (!this.isClosed.getAndSet(true) && this.stream != null) {
         this.stream.close();
      }

   }

   public boolean opened() {
      return this.opened.get();
   }

   public boolean isClosed() {
      return this.isClosed.get();
   }

   @Nullable
   public <T> T sendAndReceive(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, RequestContext requestContext, OperationContext operationContext) {
      ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(this);

      CommandEventSender commandEventSender;
      try {
         message.encode(bsonOutput, sessionContext);
         commandEventSender = this.createCommandEventSender(message, bsonOutput, requestContext, operationContext);
         commandEventSender.sendStartedEvent();

         try {
            this.sendCommandMessage(message, bsonOutput, sessionContext);
         } catch (Exception exception) {
            commandEventSender.sendFailedEvent(exception);
            throw exception;
         }
      } catch (Throwable throwable) {
         try {
            bsonOutput.close();
         } catch (Throwable suppressed) {
            throwable.addSuppressed(suppressed);
         }

         throw throwable;
      }

      bsonOutput.close();
      if (message.isResponseExpected()) {
         return this.receiveCommandMessageResponse(decoder, commandEventSender, sessionContext, 0);
      } else {
         commandEventSender.sendSucceededEventForOneWayCommand();
         return null;
      }
   }

   public <T> void send(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext) {
      ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(this);

      try {
         message.encode(bsonOutput, sessionContext);
         this.sendCommandMessage(message, bsonOutput, sessionContext);
         if (message.isResponseExpected()) {
            this.hasMoreToCome = true;
         }
      } catch (Throwable throwable) {
         try {
            bsonOutput.close();
         } catch (Throwable suppressed) {
            throwable.addSuppressed(suppressed);
         }

         throw throwable;
      }

      bsonOutput.close();
   }

   public <T> T receive(Decoder<T> decoder, SessionContext sessionContext) {
      Assertions.isTrue("Response is expected", this.hasMoreToCome);
      return this.receiveCommandMessageResponse(decoder, new NoOpCommandEventSender(), sessionContext, 0);
   }

   public boolean supportsAdditionalTimeout() {
      return this.stream.supportsAdditionalTimeout();
   }

   public <T> T receive(Decoder<T> decoder, SessionContext sessionContext, int additionalTimeout) {
      Assertions.isTrue("Response is expected", this.hasMoreToCome);
      return this.receiveCommandMessageResponse(decoder, new NoOpCommandEventSender(), sessionContext, additionalTimeout);
   }

   public boolean hasMoreToCome() {
      return this.hasMoreToCome;
   }

   private void sendCommandMessage(CommandMessage message, ByteBufferBsonOutput bsonOutput, SessionContext sessionContext) {
      Compressor localSendCompressor = this.sendCompressor;
      if (localSendCompressor != null && !SECURITY_SENSITIVE_COMMANDS.contains(message.getCommandDocument(bsonOutput).getFirstKey())) {
         List byteBuffers = bsonOutput.getByteBuffers();

         ByteBufferBsonOutput compressedBsonOutput;
         try {
            CompressedMessage compressedMessage = new CompressedMessage(message.getOpCode(), byteBuffers, localSendCompressor, ProtocolHelper.getMessageSettings(this.description));
            compressedBsonOutput = new ByteBufferBsonOutput(this);
            compressedMessage.encode(compressedBsonOutput, sessionContext);
         } finally {
            ResourceUtil.release(byteBuffers);
            bsonOutput.close();
         }

         List compressedByteBuffers = compressedBsonOutput.getByteBuffers();

         try {
            this.sendMessage(compressedByteBuffers, message.getId());
         } finally {
            ResourceUtil.release(compressedByteBuffers);
            compressedBsonOutput.close();
         }
      } else {
         List byteBuffers = bsonOutput.getByteBuffers();

         try {
            this.sendMessage(byteBuffers, message.getId());
         } finally {
            ResourceUtil.release(byteBuffers);
            bsonOutput.close();
         }
      }

      this.responseTo = message.getId();
   }

   private <T> T receiveCommandMessageResponse(Decoder<T> decoder, CommandEventSender commandEventSender, SessionContext sessionContext, int additionalTimeout) {
      boolean commandSuccessful = false;

      try {
         ResponseBuffers responseBuffers = this.receiveMessageWithAdditionalTimeout(additionalTimeout);

         T result;
         try {
            this.updateSessionContext(sessionContext, responseBuffers);
            if (!ProtocolHelper.isCommandOk(responseBuffers)) {
               throw ProtocolHelper.getCommandFailureException(responseBuffers.getResponseDocument(this.responseTo, new BsonDocumentCodec()), this.description.getServerAddress());
            }

            commandSuccessful = true;
            commandEventSender.sendSucceededEvent(responseBuffers);
            T commandResult = this.getCommandResult(decoder, responseBuffers, this.responseTo);
            this.hasMoreToCome = responseBuffers.getReplyHeader().hasMoreToCome();
            if (this.hasMoreToCome) {
               this.responseTo = responseBuffers.getReplyHeader().getRequestId();
            } else {
               this.responseTo = 0;
            }

            result = commandResult;
         } catch (Throwable throwable) {
            if (responseBuffers != null) {
               try {
                  responseBuffers.close();
               } catch (Throwable suppressed) {
                  throwable.addSuppressed(suppressed);
               }
            }

            throw throwable;
         }

         if (responseBuffers != null) {
            responseBuffers.close();
         }

         return result;
      } catch (Exception exception) {
         if (!commandSuccessful) {
            commandEventSender.sendFailedEvent(exception);
         }

         throw exception;
      }
   }

   public <T> void sendAndReceiveAsync(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, RequestContext requestContext, OperationContext operationContext, SingleResultCallback<T> callback) {
      Assertions.notNull("stream is open", this.stream, callback);
      if (this.isClosed()) {
         callback.onResult(null, new MongoSocketClosedException("Can not read from a closed socket", this.getServerAddress()));
      } else {
         ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(this);
         ByteBufferBsonOutput compressedBsonOutput = new ByteBufferBsonOutput(this);

         try {
            message.encode(bsonOutput, sessionContext);
            CommandEventSender commandEventSender = this.createCommandEventSender(message, bsonOutput, requestContext, operationContext);
            commandEventSender.sendStartedEvent();
            Compressor localSendCompressor = this.sendCompressor;
            if (localSendCompressor != null && !SECURITY_SENSITIVE_COMMANDS.contains(message.getCommandDocument(bsonOutput).getFirstKey())) {
               List byteBuffers = bsonOutput.getByteBuffers();

               try {
                  CompressedMessage compressedMessage = new CompressedMessage(message.getOpCode(), byteBuffers, localSendCompressor, ProtocolHelper.getMessageSettings(this.description));
                  compressedMessage.encode(compressedBsonOutput, sessionContext);
               } finally {
                  ResourceUtil.release(byteBuffers);
                  bsonOutput.close();
               }

               this.sendCommandMessageAsync(message.getId(), decoder, sessionContext, callback, compressedBsonOutput, commandEventSender, message.isResponseExpected());
            } else {
               this.sendCommandMessageAsync(message.getId(), decoder, sessionContext, callback, bsonOutput, commandEventSender, message.isResponseExpected());
            }
         } catch (Throwable throwable) {
            bsonOutput.close();
            compressedBsonOutput.close();
            callback.onResult(null, throwable);
         }

      }
   }

   private <T> void sendCommandMessageAsync(int messageId, Decoder<T> decoder, SessionContext sessionContext, SingleResultCallback<T> callback, ByteBufferBsonOutput bsonOutput, CommandEventSender commandEventSender, boolean responseExpected) {
      List<ByteBuf> byteBuffers = bsonOutput.getByteBuffers();
      this.sendMessageAsync(byteBuffers, messageId, (result, t) -> {
         ResourceUtil.release(byteBuffers);
         bsonOutput.close();
         if (t != null) {
            commandEventSender.sendFailedEvent(t);
            callback.onResult(null, t);
         } else if (!responseExpected) {
            commandEventSender.sendSucceededEventForOneWayCommand();
            callback.onResult(null, (Throwable)null);
         } else {
            this.readAsync(16, new InternalStreamConnection.MessageHeaderCallback((responseBuffers, t1) -> {
               if (t1 != null) {
                  commandEventSender.sendFailedEvent(t1);
                  callback.onResult(null, t1);
               } else {
                  Assertions.assertNotNull(responseBuffers);

                  try {
                     this.updateSessionContext(sessionContext, responseBuffers);
                     boolean commandOk = ProtocolHelper.isCommandOk((BsonReader)(new BsonBinaryReader(new ByteBufferBsonInput(responseBuffers.getBodyByteBuffer()))));
                     responseBuffers.reset();
                     if (!commandOk) {
                        MongoException commandFailureException = ProtocolHelper.getCommandFailureException(responseBuffers.getResponseDocument(messageId, new BsonDocumentCodec()), this.description.getServerAddress());
                        commandEventSender.sendFailedEvent(commandFailureException);
                        throw commandFailureException;
                     }

                     commandEventSender.sendSucceededEvent(responseBuffers);
                     T result1 = this.getCommandResult(decoder, responseBuffers, messageId);
                     callback.onResult(result1, (Throwable)null);
                  } catch (Throwable throwable) {
                     callback.onResult(null, throwable);
                  } finally {
                     responseBuffers.close();
                  }

               }
            }));
         }

      });
   }

   private <T> T getCommandResult(Decoder<T> decoder, ResponseBuffers responseBuffers, int messageId) {
      T result = (new ReplyMessage<T>(responseBuffers, decoder, (long)messageId)).getDocuments().get(0);
      MongoException writeConcernBasedError = ProtocolHelper.createSpecialWriteConcernException(responseBuffers, this.description.getServerAddress());
      if (writeConcernBasedError != null) {
         throw new MongoWriteConcernWithResponseException(writeConcernBasedError, result);
      } else {
         return result;
      }
   }

   public void sendMessage(List<ByteBuf> byteBuffers, int lastRequestId) {
      Assertions.notNull("stream is open", this.stream);
      if (this.isClosed()) {
         throw new MongoSocketClosedException("Cannot write to a closed stream", this.getServerAddress());
      } else {
         try {
            this.stream.write(byteBuffers);
         } catch (Exception exception) {
            this.close();
            throw this.translateWriteException(exception);
         }
      }
   }

   public ResponseBuffers receiveMessage(int responseTo) {
      Assertions.notNull("stream is open", this.stream);
      if (this.isClosed()) {
         throw new MongoSocketClosedException("Cannot read from a closed stream", this.getServerAddress());
      } else {
         return this.receiveMessageWithAdditionalTimeout(0);
      }
   }

   private ResponseBuffers receiveMessageWithAdditionalTimeout(int additionalTimeout) {
      try {
         return this.receiveResponseBuffers(additionalTimeout);
      } catch (Throwable throwable) {
         this.close();
         throw this.translateReadException(throwable);
      }
   }

   public void sendMessageAsync(List<ByteBuf> byteBuffers, int lastRequestId, SingleResultCallback<Void> callback) {
      Assertions.notNull("stream is open", this.stream, callback);
      if (this.isClosed()) {
         callback.onResult(null, new MongoSocketClosedException("Can not read from a closed socket", this.getServerAddress()));
      } else {
         this.writeAsync(byteBuffers, ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER));
      }
   }

   private void writeAsync(List<ByteBuf> byteBuffers, final SingleResultCallback<Void> callback) {
      try {
         this.stream.writeAsync(byteBuffers, new AsyncCompletionHandler<Void>() {
            public void completed(@Nullable Void v) {
               callback.onResult(null, (Throwable)null);
            }

            public void failed(Throwable t) {
               InternalStreamConnection.this.close();
               callback.onResult(null, InternalStreamConnection.this.translateWriteException(t));
            }
         });
      } catch (Throwable throwable) {
         this.close();
         callback.onResult(null, throwable);
      }

   }

   public void receiveMessageAsync(int responseTo, SingleResultCallback<ResponseBuffers> callback) {
      Assertions.isTrue("stream is open", this.stream != null, callback);
      if (this.isClosed()) {
         callback.onResult(null, new MongoSocketClosedException("Can not read from a closed socket", this.getServerAddress()));
      } else {
         this.readAsync(16, new InternalStreamConnection.MessageHeaderCallback((result, t) -> {
            if (t != null) {
               this.close();
               callback.onResult(null, t);
            } else {
               callback.onResult(result, (Throwable)null);
            }

         }));
      }
   }

   private void readAsync(int numBytes, final SingleResultCallback<ByteBuf> callback) {
      if (this.isClosed()) {
         callback.onResult(null, new MongoSocketClosedException("Cannot read from a closed stream", this.getServerAddress()));
      } else {
         try {
            this.stream.readAsync(numBytes, new AsyncCompletionHandler<ByteBuf>() {
               public void completed(@Nullable ByteBuf buffer) {
                  callback.onResult(buffer, (Throwable)null);
               }

               public void failed(Throwable t) {
                  InternalStreamConnection.this.close();
                  callback.onResult(null, InternalStreamConnection.this.translateReadException(t));
               }
            });
         } catch (Exception exception) {
            this.close();
            callback.onResult(null, this.translateReadException(exception));
         }

      }
   }

   private ConnectionId getId() {
      return this.description.getConnectionId();
   }

   private ServerAddress getServerAddress() {
      return this.description.getServerAddress();
   }

   private void updateSessionContext(SessionContext sessionContext, ResponseBuffers responseBuffers) {
      sessionContext.advanceOperationTime(ProtocolHelper.getOperationTime(responseBuffers));
      sessionContext.advanceClusterTime(ProtocolHelper.getClusterTime(responseBuffers));
      sessionContext.setSnapshotTimestamp(ProtocolHelper.getSnapshotTimestamp(responseBuffers));
      if (sessionContext.hasActiveTransaction()) {
         BsonDocument recoveryToken = ProtocolHelper.getRecoveryToken(responseBuffers);
         if (recoveryToken != null) {
            sessionContext.setRecoveryToken(recoveryToken);
         }
      }

   }

   private MongoException translateWriteException(Throwable e) {
      if (e instanceof MongoException) {
         return (MongoException)e;
      } else {
         Optional<MongoInterruptedException> interruptedException = InterruptionUtil.translateInterruptedException(e, "Interrupted while sending message");
         if (interruptedException.isPresent()) {
            return (MongoException)interruptedException.get();
         } else {
            return (MongoException)(e instanceof IOException ? new MongoSocketWriteException("Exception sending message", this.getServerAddress(), e) : new MongoInternalException("Unexpected exception", e));
         }
      }
   }

   private MongoException translateReadException(Throwable e) {
      if (e instanceof MongoException) {
         return (MongoException)e;
      } else {
         Optional<MongoInterruptedException> interruptedException = InterruptionUtil.translateInterruptedException(e, "Interrupted while receiving message");
         if (interruptedException.isPresent()) {
            return (MongoException)interruptedException.get();
         } else if (e instanceof SocketTimeoutException) {
            return new MongoSocketReadTimeoutException("Timeout while receiving message", this.getServerAddress(), e);
         } else if (e instanceof IOException) {
            return new MongoSocketReadException("Exception receiving message", this.getServerAddress(), e);
         } else {
            return e instanceof RuntimeException ? new MongoInternalException("Unexpected runtime exception", e) : new MongoInternalException("Unexpected exception", e);
         }
      }
   }

   private ResponseBuffers receiveResponseBuffers(int additionalTimeout) throws IOException {
      ByteBuf messageHeaderBuffer = this.stream.read(16, additionalTimeout);

      MessageHeader messageHeader;
      try {
         messageHeader = new MessageHeader(messageHeaderBuffer, this.description.getMaxMessageSize());
      } finally {
         messageHeaderBuffer.release();
      }

      ByteBuf messageBuffer = this.stream.read(messageHeader.getMessageLength() - 16, additionalTimeout);
      boolean releaseMessageBuffer = true;

      ResponseBuffers var7;
      try {
         if (messageHeader.getOpCode() == OpCode.OP_COMPRESSED.getValue()) {
            CompressedHeader compressedHeader = new CompressedHeader(messageBuffer, messageHeader);
            Compressor compressor = this.getCompressor(compressedHeader);
            ByteBuf buffer = this.getBuffer(compressedHeader.getUncompressedSize());
            compressor.uncompress(messageBuffer, buffer);
            buffer.flip();
            ResponseBuffers var9 = new ResponseBuffers(new ReplyHeader(buffer, compressedHeader), buffer);
            return var9;
         }

         ResponseBuffers responseBuffers = new ResponseBuffers(new ReplyHeader(messageBuffer, messageHeader), messageBuffer);
         releaseMessageBuffer = false;
         var7 = responseBuffers;
      } finally {
         if (releaseMessageBuffer) {
            messageBuffer.release();
         }

      }

      return var7;
   }

   private Compressor getCompressor(CompressedHeader compressedHeader) {
      Compressor compressor = (Compressor)this.compressorMap.get(compressedHeader.getCompressorId());
      if (compressor == null) {
         throw new MongoClientException("Unsupported compressor with identifier " + compressedHeader.getCompressorId());
      } else {
         return compressor;
      }
   }

   public ByteBuf getBuffer(int size) {
      Assertions.notNull("open", this.stream);
      return this.stream.getBuffer(size);
   }

   private CommandEventSender createCommandEventSender(CommandMessage message, ByteBufferBsonOutput bsonOutput, RequestContext requestContext, OperationContext operationContext) {
      return (CommandEventSender)(this.isMonitoringConnection || !this.opened() || this.commandListener == null && !COMMAND_PROTOCOL_LOGGER.isRequired(LogMessage.Level.DEBUG, this.getClusterId()) ? new NoOpCommandEventSender() : new LoggingCommandEventSender(SECURITY_SENSITIVE_COMMANDS, SECURITY_SENSITIVE_HELLO_COMMANDS, this.description, this.commandListener, requestContext, operationContext, message, bsonOutput, COMMAND_PROTOCOL_LOGGER, this.loggerSettings));
   }

   private ClusterId getClusterId() {
      return this.description.getConnectionId().getServerId().getClusterId();
   }

   static {
      SECURITY_SENSITIVE_HELLO_COMMANDS = new HashSet(Arrays.asList("hello", "isMaster", CommandHelper.LEGACY_HELLO_LOWER));
      LOGGER = Loggers.getLogger("connection");
      COMMAND_PROTOCOL_LOGGER = new StructuredLogger("protocol.command");
   }

   private class MessageHeaderCallback implements SingleResultCallback<ByteBuf> {
      private final SingleResultCallback<ResponseBuffers> callback;

      MessageHeaderCallback(SingleResultCallback<ResponseBuffers> callback) {
         this.callback = callback;
      }

      public void onResult(@Nullable ByteBuf result, @Nullable Throwable t) {
         if (t != null) {
            this.callback.onResult(null, t);
         } else {
            try {
               Assertions.assertNotNull(result);
               MessageHeader messageHeader = new MessageHeader(result, InternalStreamConnection.this.description.getMaxMessageSize());
               InternalStreamConnection.this.readAsync(messageHeader.getMessageLength() - 16, new InternalStreamConnection.MessageHeaderCallback.MessageCallback(messageHeader));
            } catch (Throwable throwable) {
               this.callback.onResult(null, throwable);
            } finally {
               if (result != null) {
                  result.release();
               }

            }

         }
      }

      private class MessageCallback implements SingleResultCallback<ByteBuf> {
         private final MessageHeader messageHeader;

         MessageCallback(MessageHeader messageHeader) {
            this.messageHeader = messageHeader;
         }

         public void onResult(@Nullable ByteBuf result, @Nullable Throwable t) {
            if (t != null) {
               MessageHeaderCallback.this.callback.onResult(null, t);
            } else {
               boolean releaseResult = true;
               Assertions.assertNotNull(result);

               try {
                  ReplyHeader replyHeader;
                  ByteBuf responseBuffer;
                  if (this.messageHeader.getOpCode() == OpCode.OP_COMPRESSED.getValue()) {
                     try {
                        CompressedHeader compressedHeader = new CompressedHeader(result, this.messageHeader);
                        Compressor compressor = InternalStreamConnection.this.getCompressor(compressedHeader);
                        ByteBuf buffer = InternalStreamConnection.this.getBuffer(compressedHeader.getUncompressedSize());
                        compressor.uncompress(result, buffer);
                        buffer.flip();
                        replyHeader = new ReplyHeader(buffer, compressedHeader);
                        responseBuffer = buffer;
                     } finally {
                        releaseResult = false;
                        result.release();
                     }
                  } else {
                     replyHeader = new ReplyHeader(result, this.messageHeader);
                     responseBuffer = result;
                     releaseResult = false;
                  }

                  MessageHeaderCallback.this.callback.onResult(new ResponseBuffers(replyHeader, responseBuffer), (Throwable)null);
               } catch (Throwable throwable) {
                  MessageHeaderCallback.this.callback.onResult(null, throwable);
               } finally {
                  if (releaseResult) {
                     result.release();
                  }

               }

            }
         }
      }
   }
}
