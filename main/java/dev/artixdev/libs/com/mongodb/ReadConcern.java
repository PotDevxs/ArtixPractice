package dev.artixdev.libs.com.mongodb;

import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public final class ReadConcern {
   private final ReadConcernLevel level;
   public static final ReadConcern DEFAULT = new ReadConcern();
   public static final ReadConcern LOCAL;
   public static final ReadConcern MAJORITY;
   public static final ReadConcern LINEARIZABLE;
   public static final ReadConcern SNAPSHOT;
   public static final ReadConcern AVAILABLE;

   public ReadConcern(ReadConcernLevel level) {
      this.level = (ReadConcernLevel)Assertions.notNull("level", level);
   }

   @Nullable
   public ReadConcernLevel getLevel() {
      return this.level;
   }

   public boolean isServerDefault() {
      return this.level == null;
   }

   public BsonDocument asDocument() {
      BsonDocument readConcern = new BsonDocument();
      if (this.level != null) {
         readConcern.put((String)"level", (BsonValue)(new BsonString(this.level.getValue())));
      }

      return readConcern;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ReadConcern that = (ReadConcern)o;
         return this.level == that.level;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.level != null ? this.level.hashCode() : 0;
   }

   public String toString() {
      return "ReadConcern{level=" + this.level + '}';
   }

   private ReadConcern() {
      this.level = null;
   }

   static {
      LOCAL = new ReadConcern(ReadConcernLevel.LOCAL);
      MAJORITY = new ReadConcern(ReadConcernLevel.MAJORITY);
      LINEARIZABLE = new ReadConcern(ReadConcernLevel.LINEARIZABLE);
      SNAPSHOT = new ReadConcern(ReadConcernLevel.SNAPSHOT);
      AVAILABLE = new ReadConcern(ReadConcernLevel.AVAILABLE);
   }
}
