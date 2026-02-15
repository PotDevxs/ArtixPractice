package dev.artixdev.libs.com.mongodb;

import java.io.Serializable;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class DBRef implements Serializable {
   private static final long serialVersionUID = -849581217713362618L;
   private final Object id;
   private final String collectionName;
   private final String databaseName;

   public DBRef(String collectionName, Object id) {
      this((String)null, collectionName, id);
   }

   public DBRef(@Nullable String databaseName, String collectionName, Object id) {
      this.id = Assertions.notNull("id", id);
      this.collectionName = (String)Assertions.notNull("collectionName", collectionName);
      this.databaseName = databaseName;
   }

   public Object getId() {
      return this.id;
   }

   public String getCollectionName() {
      return this.collectionName;
   }

   @Nullable
   public String getDatabaseName() {
      return this.databaseName;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         DBRef dbRef = (DBRef)o;
         if (!this.id.equals(dbRef.id)) {
            return false;
         } else if (!this.collectionName.equals(dbRef.collectionName)) {
            return false;
         } else {
            return Objects.equals(this.databaseName, dbRef.databaseName);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.id.hashCode();
      result = 31 * result + this.collectionName.hashCode();
      result = 31 * result + (this.databaseName != null ? this.databaseName.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "{ \"$ref\" : \"" + this.collectionName + "\", \"$id\" : \"" + this.id + "\"" + (this.databaseName == null ? "" : ", \"$db\" : \"" + this.databaseName + "\"") + " }";
   }
}
