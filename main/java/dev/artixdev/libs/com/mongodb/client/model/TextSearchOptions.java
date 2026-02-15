package dev.artixdev.libs.com.mongodb.client.model;

import java.util.Objects;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public final class TextSearchOptions {
   private String language;
   private Boolean caseSensitive;
   private Boolean diacriticSensitive;

   @Nullable
   public String getLanguage() {
      return this.language;
   }

   public TextSearchOptions language(@Nullable String language) {
      this.language = language;
      return this;
   }

   @Nullable
   public Boolean getCaseSensitive() {
      return this.caseSensitive;
   }

   public TextSearchOptions caseSensitive(@Nullable Boolean caseSensitive) {
      this.caseSensitive = caseSensitive;
      return this;
   }

   @Nullable
   public Boolean getDiacriticSensitive() {
      return this.diacriticSensitive;
   }

   public TextSearchOptions diacriticSensitive(@Nullable Boolean diacriticSensitive) {
      this.diacriticSensitive = diacriticSensitive;
      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TextSearchOptions that = (TextSearchOptions)o;
         if (!Objects.equals(this.language, that.language)) {
            return false;
         } else {
            return !Objects.equals(this.caseSensitive, that.caseSensitive) ? false : Objects.equals(this.diacriticSensitive, that.diacriticSensitive);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.language != null ? this.language.hashCode() : 0;
      result = 31 * result + (this.caseSensitive != null ? this.caseSensitive.hashCode() : 0);
      result = 31 * result + (this.diacriticSensitive != null ? this.diacriticSensitive.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "Text Search Options{language='" + this.language + '\'' + ", caseSensitive=" + this.caseSensitive + ", diacriticSensitive=" + this.diacriticSensitive + '}';
   }
}
