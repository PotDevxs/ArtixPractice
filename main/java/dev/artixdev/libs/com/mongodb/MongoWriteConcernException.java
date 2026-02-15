package dev.artixdev.libs.com.mongodb;

import java.util.Iterator;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.bulk.WriteConcernError;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class MongoWriteConcernException extends MongoServerException {
   private static final long serialVersionUID = 4577579466973523211L;
   private final WriteConcernError writeConcernError;
   private final WriteConcernResult writeConcernResult;

   public MongoWriteConcernException(WriteConcernError writeConcernError, ServerAddress serverAddress) {
      this(writeConcernError, (WriteConcernResult)null, serverAddress);
   }

   public MongoWriteConcernException(WriteConcernError writeConcernError, @Nullable WriteConcernResult writeConcernResult, ServerAddress serverAddress) {
      super(writeConcernError.getCode(), writeConcernError.getMessage(), serverAddress);
      this.writeConcernResult = writeConcernResult;
      this.writeConcernError = (WriteConcernError)Assertions.notNull("writeConcernError", writeConcernError);
      Iterator var4 = writeConcernError.getErrorLabels().iterator();

      while(var4.hasNext()) {
         String errorLabel = (String)var4.next();
         super.addLabel(errorLabel);
      }

   }

   public void addLabel(String errorLabel) {
      this.writeConcernError.addLabel(errorLabel);
      super.addLabel(errorLabel);
   }

   public WriteConcernError getWriteConcernError() {
      return this.writeConcernError;
   }

   public WriteConcernResult getWriteResult() {
      return this.writeConcernResult;
   }
}
