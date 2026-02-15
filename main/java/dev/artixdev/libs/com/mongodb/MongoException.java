package dev.artixdev.libs.com.mongodb;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonArray;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonValue;

public class MongoException extends RuntimeException {
   public static final String TRANSIENT_TRANSACTION_ERROR_LABEL = "TransientTransactionError";
   public static final String UNKNOWN_TRANSACTION_COMMIT_RESULT_LABEL = "UnknownTransactionCommitResult";
   private static final long serialVersionUID = -4415279469780082174L;
   private final int code;
   private final Set<String> errorLabels = new HashSet();

   @Nullable
   public static MongoException fromThrowable(@Nullable Throwable t) {
      return t == null ? null : fromThrowableNonNull(t);
   }

   public static MongoException fromThrowableNonNull(Throwable t) {
      return t instanceof MongoException ? (MongoException)t : new MongoException(t.getMessage(), t);
   }

   public MongoException(String msg) {
      super(msg);
      this.code = -3;
   }

   public MongoException(int code, String msg) {
      super(msg);
      this.code = code;
   }

   public MongoException(@Nullable String msg, @Nullable Throwable t) {
      super(msg, t);
      this.code = -4;
   }

   public MongoException(int code, String msg, Throwable t) {
      super(msg, t);
      this.code = code;
      if (t instanceof MongoException) {
         this.addLabels((Collection)((MongoException)t).getErrorLabels());
      }

   }

   public MongoException(int code, String msg, BsonDocument response) {
      super(msg);
      this.code = code;
      this.addLabels(response.getArray("errorLabels", new BsonArray()));
   }

   public int getCode() {
      return this.code;
   }

   public void addLabel(String errorLabel) {
      Assertions.notNull("errorLabel", errorLabel);
      this.errorLabels.add(errorLabel);
   }

   public void removeLabel(String errorLabel) {
      Assertions.notNull("errorLabel", errorLabel);
      this.errorLabels.remove(errorLabel);
   }

   public Set<String> getErrorLabels() {
      return Collections.unmodifiableSet(this.errorLabels);
   }

   public boolean hasErrorLabel(String errorLabel) {
      Assertions.notNull("errorLabel", errorLabel);
      return this.errorLabels.contains(errorLabel);
   }

   protected void addLabels(BsonArray labels) {
      Iterator var2 = labels.iterator();

      while(var2.hasNext()) {
         BsonValue errorLabel = (BsonValue)var2.next();
         this.addLabel(errorLabel.asString().getValue());
      }

   }

   protected void addLabels(Collection<String> labels) {
      Iterator var2 = labels.iterator();

      while(var2.hasNext()) {
         String errorLabel = (String)var2.next();
         this.addLabel(errorLabel);
      }

   }
}
