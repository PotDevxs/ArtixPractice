package dev.artixdev.libs.com.mongodb.internal.connection;

import dev.artixdev.libs.com.mongodb.MongoException;

public class MongoWriteConcernWithResponseException extends MongoException {
   private static final long serialVersionUID = 1707360842648550287L;
   private final MongoException cause;
   private final Object response;

   public MongoWriteConcernWithResponseException(MongoException exception, Object response) {
      super(exception.getCode(), exception.getMessage(), (Throwable)exception);
      this.cause = exception;
      this.response = response;
   }

   public MongoException getCause() {
      return this.cause;
   }

   public Object getResponse() {
      return this.response;
   }
}
