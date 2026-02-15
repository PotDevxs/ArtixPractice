package dev.artixdev.libs.com.mongodb;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonCreator;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonIgnore;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonProperty;

@Immutable
public final class MongoNamespace {
   public static final String COMMAND_COLLECTION_NAME = "$cmd";
   private static final Set<Character> PROHIBITED_CHARACTERS_IN_DATABASE_NAME = new HashSet(Arrays.asList('\u0000', '/', '\\', ' ', '"', '.'));
   private final String databaseName;
   private final String collectionName;
   @BsonIgnore
   private final String fullName;

   public static void checkDatabaseNameValidity(String databaseName) {
      Assertions.notNull("databaseName", databaseName);
      Assertions.isTrueArgument("databaseName is not empty", !databaseName.isEmpty());

      for(int i = 0; i < databaseName.length(); ++i) {
         if (PROHIBITED_CHARACTERS_IN_DATABASE_NAME.contains(databaseName.charAt(i))) {
            throw new IllegalArgumentException("state should be: databaseName does not contain '" + databaseName.charAt(i) + "'");
         }
      }

   }

   public static void checkCollectionNameValidity(String collectionName) {
      Assertions.notNull("collectionName", collectionName);
      Assertions.isTrueArgument("collectionName is not empty", !collectionName.isEmpty());
   }

   public MongoNamespace(String fullName) {
      Assertions.notNull("fullName", fullName);
      this.fullName = fullName;
      this.databaseName = getDatatabaseNameFromFullName(fullName);
      this.collectionName = getCollectionNameFullName(fullName);
      checkDatabaseNameValidity(this.databaseName);
      checkCollectionNameValidity(this.collectionName);
   }

   @BsonCreator
   public MongoNamespace(@BsonProperty("db") String databaseName, @BsonProperty("coll") String collectionName) {
      checkDatabaseNameValidity(databaseName);
      checkCollectionNameValidity(collectionName);
      this.databaseName = databaseName;
      this.collectionName = collectionName;
      this.fullName = databaseName + '.' + collectionName;
   }

   @BsonProperty("db")
   public String getDatabaseName() {
      return this.databaseName;
   }

   @BsonProperty("coll")
   public String getCollectionName() {
      return this.collectionName;
   }

   public String getFullName() {
      return this.fullName;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         MongoNamespace that = (MongoNamespace)o;
         if (!this.collectionName.equals(that.collectionName)) {
            return false;
         } else {
            return this.databaseName.equals(that.databaseName);
         }
      } else {
         return false;
      }
   }

   public String toString() {
      return this.fullName;
   }

   public int hashCode() {
      int result = this.databaseName.hashCode();
      result = 31 * result + this.collectionName.hashCode();
      return result;
   }

   private static String getCollectionNameFullName(String namespace) {
      int firstDot = namespace.indexOf(46);
      return firstDot == -1 ? namespace : namespace.substring(firstDot + 1);
   }

   private static String getDatatabaseNameFromFullName(String namespace) {
      int firstDot = namespace.indexOf(46);
      return firstDot == -1 ? "" : namespace.substring(0, firstDot);
   }
}
