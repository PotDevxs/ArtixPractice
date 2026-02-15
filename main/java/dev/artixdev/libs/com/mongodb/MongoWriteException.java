package dev.artixdev.libs.com.mongodb;

public class MongoWriteException extends MongoServerException {
   private static final long serialVersionUID = -1906795074458258147L;
   private final WriteError error;

   public MongoWriteException(WriteError error, ServerAddress serverAddress) {
      super(error.getCode(), "Write operation error on server " + serverAddress + ". Write error: " + error + ".", serverAddress);
      this.error = error;
   }

   public WriteError getError() {
      return this.error;
   }
}
