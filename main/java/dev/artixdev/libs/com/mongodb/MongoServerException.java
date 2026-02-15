package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.lang.Nullable;

public abstract class MongoServerException extends MongoException {
   private static final long serialVersionUID = -5213859742051776206L;
   @Nullable
   private final String errorCodeName;
   private final ServerAddress serverAddress;

   public MongoServerException(String message, ServerAddress serverAddress) {
      super(message);
      this.serverAddress = serverAddress;
      this.errorCodeName = null;
   }

   public MongoServerException(int code, String message, ServerAddress serverAddress) {
      super(code, message);
      this.serverAddress = serverAddress;
      this.errorCodeName = null;
   }

   public MongoServerException(int code, @Nullable String errorCodeName, String message, ServerAddress serverAddress) {
      super(code, message);
      this.errorCodeName = errorCodeName;
      this.serverAddress = serverAddress;
   }

   public ServerAddress getServerAddress() {
      return this.serverAddress;
   }

   @Nullable
   public String getErrorCodeName() {
      return this.errorCodeName;
   }
}
