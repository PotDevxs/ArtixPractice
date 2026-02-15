package dev.artixdev.libs.com.mongodb.client.model.changestream;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonCreator;
import dev.artixdev.libs.org.bson.codecs.pojo.annotations.BsonProperty;

public final class UpdateDescription {
   private final List<String> removedFields;
   private final BsonDocument updatedFields;
   private final List<TruncatedArray> truncatedArrays;
   private final BsonDocument disambiguatedPaths;

   public UpdateDescription(@Nullable List<String> removedFields, @Nullable BsonDocument updatedFields) {
      this(removedFields, updatedFields, (List)null);
   }

   public UpdateDescription(@Nullable List<String> removedFields, @Nullable BsonDocument updatedFields, @Nullable List<TruncatedArray> truncatedArrays) {
      this(removedFields, updatedFields, truncatedArrays, (BsonDocument)null);
   }

   @BsonCreator
   public UpdateDescription(@Nullable @BsonProperty("removedFields") List<String> removedFields, @Nullable @BsonProperty("updatedFields") BsonDocument updatedFields, @Nullable @BsonProperty("truncatedArrays") List<TruncatedArray> truncatedArrays, @Nullable @BsonProperty("disambiguatedPaths") BsonDocument disambiguatedPaths) {
      this.removedFields = removedFields;
      this.updatedFields = updatedFields;
      this.truncatedArrays = truncatedArrays == null ? Collections.emptyList() : truncatedArrays;
      this.disambiguatedPaths = disambiguatedPaths;
   }

   @Nullable
   public List<String> getRemovedFields() {
      return this.removedFields;
   }

   @Nullable
   public BsonDocument getUpdatedFields() {
      return this.updatedFields;
   }

   @NonNull
   public List<TruncatedArray> getTruncatedArrays() {
      return this.truncatedArrays;
   }

   @Nullable
   public BsonDocument getDisambiguatedPaths() {
      return this.disambiguatedPaths;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         UpdateDescription that = (UpdateDescription)o;
         return Objects.equals(this.removedFields, that.removedFields) && Objects.equals(this.updatedFields, that.updatedFields) && Objects.equals(this.truncatedArrays, that.truncatedArrays) && Objects.equals(this.disambiguatedPaths, that.disambiguatedPaths);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.removedFields, this.updatedFields, this.truncatedArrays, this.disambiguatedPaths});
   }

   public String toString() {
      return "UpdateDescription{removedFields=" + this.removedFields + ", updatedFields=" + this.updatedFields + ", truncatedArrays=" + this.truncatedArrays + ", disambiguatedPaths=" + this.disambiguatedPaths + "}";
   }
}
