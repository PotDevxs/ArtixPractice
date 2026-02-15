package dev.artixdev.libs.com.mongodb.client.model;

import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

public final class Collation {
   private final String locale;
   private final Boolean caseLevel;
   private final CollationCaseFirst caseFirst;
   private final CollationStrength strength;
   private final Boolean numericOrdering;
   private final CollationAlternate alternate;
   private final CollationMaxVariable maxVariable;
   private final Boolean normalization;
   private final Boolean backwards;

   public static Collation.Builder builder() {
      return new Collation.Builder();
   }

   public static Collation.Builder builder(Collation options) {
      return new Collation.Builder(options);
   }

   @Nullable
   public String getLocale() {
      return this.locale;
   }

   @Nullable
   public Boolean getCaseLevel() {
      return this.caseLevel;
   }

   @Nullable
   public CollationCaseFirst getCaseFirst() {
      return this.caseFirst;
   }

   @Nullable
   public CollationStrength getStrength() {
      return this.strength;
   }

   @Nullable
   public Boolean getNumericOrdering() {
      return this.numericOrdering;
   }

   @Nullable
   public CollationAlternate getAlternate() {
      return this.alternate;
   }

   @Nullable
   public CollationMaxVariable getMaxVariable() {
      return this.maxVariable;
   }

   @Nullable
   public Boolean getNormalization() {
      return this.normalization;
   }

   @Nullable
   public Boolean getBackwards() {
      return this.backwards;
   }

   public BsonDocument asDocument() {
      BsonDocument collation = new BsonDocument();
      if (this.locale != null) {
         collation.put((String)"locale", (BsonValue)(new BsonString(this.locale)));
      }

      if (this.caseLevel != null) {
         collation.put((String)"caseLevel", (BsonValue)(new BsonBoolean(this.caseLevel)));
      }

      if (this.caseFirst != null) {
         collation.put((String)"caseFirst", (BsonValue)(new BsonString(this.caseFirst.getValue())));
      }

      if (this.strength != null) {
         collation.put((String)"strength", (BsonValue)(new BsonInt32(this.strength.getIntRepresentation())));
      }

      if (this.numericOrdering != null) {
         collation.put((String)"numericOrdering", (BsonValue)(new BsonBoolean(this.numericOrdering)));
      }

      if (this.alternate != null) {
         collation.put((String)"alternate", (BsonValue)(new BsonString(this.alternate.getValue())));
      }

      if (this.maxVariable != null) {
         collation.put((String)"maxVariable", (BsonValue)(new BsonString(this.maxVariable.getValue())));
      }

      if (this.normalization != null) {
         collation.put((String)"normalization", (BsonValue)(new BsonBoolean(this.normalization)));
      }

      if (this.backwards != null) {
         collation.put((String)"backwards", (BsonValue)(new BsonBoolean(this.backwards)));
      }

      return collation;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Collation that = (Collation)o;
         if (this.locale != null) {
            if (!this.locale.equals(that.getLocale())) {
               return false;
            }
         } else if (that.getLocale() != null) {
            return false;
         }

         label78: {
            if (this.caseLevel != null) {
               if (this.caseLevel.equals(that.getCaseLevel())) {
                  break label78;
               }
            } else if (that.getCaseLevel() == null) {
               break label78;
            }

            return false;
         }

         if (this.getCaseFirst() != that.getCaseFirst()) {
            return false;
         } else if (this.getStrength() != that.getStrength()) {
            return false;
         } else {
            if (this.numericOrdering != null) {
               if (!this.numericOrdering.equals(that.getNumericOrdering())) {
                  return false;
               }
            } else if (that.getNumericOrdering() != null) {
               return false;
            }

            if (this.getAlternate() != that.getAlternate()) {
               return false;
            } else if (this.getMaxVariable() != that.getMaxVariable()) {
               return false;
            } else {
               if (this.normalization != null) {
                  if (!this.normalization.equals(that.getNormalization())) {
                     return false;
                  }
               } else if (that.getNormalization() != null) {
                  return false;
               }

               if (this.backwards != null) {
                  if (!this.backwards.equals(that.getBackwards())) {
                     return false;
                  }
               } else if (that.getBackwards() != null) {
                  return false;
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.locale != null ? this.locale.hashCode() : 0;
      result = 31 * result + (this.caseLevel != null ? this.caseLevel.hashCode() : 0);
      result = 31 * result + (this.caseFirst != null ? this.caseFirst.hashCode() : 0);
      result = 31 * result + (this.strength != null ? this.strength.hashCode() : 0);
      result = 31 * result + (this.numericOrdering != null ? this.numericOrdering.hashCode() : 0);
      result = 31 * result + (this.alternate != null ? this.alternate.hashCode() : 0);
      result = 31 * result + (this.maxVariable != null ? this.maxVariable.hashCode() : 0);
      result = 31 * result + (this.normalization != null ? this.normalization.hashCode() : 0);
      result = 31 * result + (this.backwards != null ? this.backwards.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "Collation{locale='" + this.locale + "', caseLevel=" + this.caseLevel + ", caseFirst=" + this.caseFirst + ", strength=" + this.strength + ", numericOrdering=" + this.numericOrdering + ", alternate=" + this.alternate + ", maxVariable=" + this.maxVariable + ", normalization=" + this.normalization + ", backwards=" + this.backwards + "}";
   }

   private Collation(Collation.Builder builder) {
      this.locale = builder.locale;
      this.caseLevel = builder.caseLevel;
      this.caseFirst = builder.caseFirst;
      this.strength = builder.strength;
      this.numericOrdering = builder.numericOrdering;
      this.alternate = builder.alternate;
      this.maxVariable = builder.maxVariable;
      this.normalization = builder.normalization;
      this.backwards = builder.backwards;
   }

   // $FF: synthetic method
   Collation(Collation.Builder x0, Object x1) {
      this(x0);
   }

   @NotThreadSafe
   public static final class Builder {
      private String locale;
      private Boolean caseLevel;
      private CollationCaseFirst caseFirst;
      private CollationStrength strength;
      private Boolean numericOrdering;
      private CollationAlternate alternate;
      private CollationMaxVariable maxVariable;
      private Boolean normalization;
      private Boolean backwards;

      private Builder() {
      }

      private Builder(Collation options) {
         this.locale = options.getLocale();
         this.caseLevel = options.getCaseLevel();
         this.caseFirst = options.getCaseFirst();
         this.strength = options.getStrength();
         this.numericOrdering = options.getNumericOrdering();
         this.alternate = options.getAlternate();
         this.maxVariable = options.getMaxVariable();
         this.normalization = options.getNormalization();
         this.backwards = options.getBackwards();
      }

      public Collation.Builder locale(@Nullable String locale) {
         this.locale = locale;
         return this;
      }

      public Collation.Builder caseLevel(@Nullable Boolean caseLevel) {
         this.caseLevel = caseLevel;
         return this;
      }

      public Collation.Builder collationCaseFirst(@Nullable CollationCaseFirst caseFirst) {
         this.caseFirst = caseFirst;
         return this;
      }

      public Collation.Builder collationStrength(@Nullable CollationStrength strength) {
         this.strength = strength;
         return this;
      }

      public Collation.Builder numericOrdering(@Nullable Boolean numericOrdering) {
         this.numericOrdering = numericOrdering;
         return this;
      }

      public Collation.Builder collationAlternate(@Nullable CollationAlternate alternate) {
         this.alternate = alternate;
         return this;
      }

      public Collation.Builder collationMaxVariable(@Nullable CollationMaxVariable maxVariable) {
         this.maxVariable = maxVariable;
         return this;
      }

      public Collation.Builder normalization(@Nullable Boolean normalization) {
         this.normalization = normalization;
         return this;
      }

      public Collation.Builder backwards(@Nullable Boolean backwards) {
         this.backwards = backwards;
         return this;
      }

      public Collation build() {
         return new Collation(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }

      // $FF: synthetic method
      Builder(Collation x0, Object x1) {
         this(x0);
      }
   }
}
