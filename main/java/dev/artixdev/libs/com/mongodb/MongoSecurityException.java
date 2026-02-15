package dev.artixdev.libs.com.mongodb;

public class MongoSecurityException extends MongoClientException {
   private static final long serialVersionUID = -7044790409935567275L;
   private final MongoCredential credential;

   public MongoSecurityException(MongoCredential credential, String message, Throwable cause) {
      super(message, cause);
      this.credential = credential;
   }

   public MongoSecurityException(MongoCredential credential, String message) {
      super(message);
      this.credential = credential;
   }

   public MongoCredential getCredential() {
      return this.credential;
   }
}
