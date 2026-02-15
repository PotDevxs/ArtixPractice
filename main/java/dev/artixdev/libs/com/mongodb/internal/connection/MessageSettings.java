package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.connection.ServerType;

@Immutable
public final class MessageSettings {
   private static final int DEFAULT_MAX_DOCUMENT_SIZE = 16777216;
   private static final int DEFAULT_MAX_MESSAGE_SIZE = 33554432;
   private static final int DEFAULT_MAX_BATCH_COUNT = 1000;
   private final int maxDocumentSize;
   private final int maxMessageSize;
   private final int maxBatchCount;
   private final int maxWireVersion;
   private final ServerType serverType;
   private final boolean sessionSupported;

   public static MessageSettings.Builder builder() {
      return new MessageSettings.Builder();
   }

   public int getMaxDocumentSize() {
      return this.maxDocumentSize;
   }

   public int getMaxMessageSize() {
      return this.maxMessageSize;
   }

   public int getMaxBatchCount() {
      return this.maxBatchCount;
   }

   public int getMaxWireVersion() {
      return this.maxWireVersion;
   }

   public ServerType getServerType() {
      return this.serverType;
   }

   public boolean isSessionSupported() {
      return this.sessionSupported;
   }

   private MessageSettings(MessageSettings.Builder builder) {
      this.maxDocumentSize = builder.maxDocumentSize;
      this.maxMessageSize = builder.maxMessageSize;
      this.maxBatchCount = builder.maxBatchCount;
      this.maxWireVersion = builder.maxWireVersion;
      this.serverType = builder.serverType;
      this.sessionSupported = builder.sessionSupported;
   }

   // $FF: synthetic method
   MessageSettings(MessageSettings.Builder x0, Object x1) {
      this(x0);
   }

   @NotThreadSafe
   public static final class Builder {
      private int maxDocumentSize = 16777216;
      private int maxMessageSize = 33554432;
      private int maxBatchCount = 1000;
      private int maxWireVersion;
      private ServerType serverType;
      private boolean sessionSupported;

      public MessageSettings build() {
         return new MessageSettings(this);
      }

      public MessageSettings.Builder maxDocumentSize(int maxDocumentSize) {
         this.maxDocumentSize = maxDocumentSize;
         return this;
      }

      public MessageSettings.Builder maxMessageSize(int maxMessageSize) {
         this.maxMessageSize = maxMessageSize;
         return this;
      }

      public MessageSettings.Builder maxBatchCount(int maxBatchCount) {
         this.maxBatchCount = maxBatchCount;
         return this;
      }

      public MessageSettings.Builder maxWireVersion(int maxWireVersion) {
         this.maxWireVersion = maxWireVersion;
         return this;
      }

      public MessageSettings.Builder serverType(ServerType serverType) {
         this.serverType = serverType;
         return this;
      }

      public MessageSettings.Builder sessionSupported(boolean sessionSupported) {
         this.sessionSupported = sessionSupported;
         return this;
      }
   }
}
