package dev.artixdev.libs.com.mongodb;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;
import dev.artixdev.libs.org.bson.BsonBoolean;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt32;
import dev.artixdev.libs.org.bson.BsonString;
import dev.artixdev.libs.org.bson.BsonValue;

@Immutable
public class WriteConcern implements Serializable {
   private static final long serialVersionUID = 1884671104750417011L;
   private static final Map<String, WriteConcern> NAMED_CONCERNS;
   private final Object w;
   private final Integer wTimeoutMS;
   private final Boolean journal;
   public static final WriteConcern ACKNOWLEDGED = new WriteConcern((Object)null, (Integer)null, (Boolean)null);
   public static final WriteConcern W1 = new WriteConcern(1);
   public static final WriteConcern W2 = new WriteConcern(2);
   public static final WriteConcern W3 = new WriteConcern(3);
   public static final WriteConcern UNACKNOWLEDGED = new WriteConcern(0);
   public static final WriteConcern JOURNALED;
   public static final WriteConcern MAJORITY;

   public WriteConcern(int w) {
      this(w, (Integer)null, (Boolean)null);
   }

   public WriteConcern(String w) {
      this(w, (Integer)null, (Boolean)null);
      Assertions.notNull("w", w);
   }

   public WriteConcern(int w, int wTimeoutMS) {
      this(w, wTimeoutMS, (Boolean)null);
   }

   private WriteConcern(@Nullable Object w, @Nullable Integer wTimeoutMS, @Nullable Boolean journal) {
      if (w instanceof Integer) {
         Assertions.isTrueArgument("w >= 0", (Integer)w >= 0);
         if ((Integer)w == 0) {
            Assertions.isTrueArgument("journal is false when w is 0", journal == null || !journal);
         }
      } else if (w != null) {
         Assertions.isTrueArgument("w must be String or int", w instanceof String);
      }

      Assertions.isTrueArgument("wtimeout >= 0", wTimeoutMS == null || wTimeoutMS >= 0);
      this.w = w;
      this.wTimeoutMS = wTimeoutMS;
      this.journal = journal;
   }

   @Nullable
   public Object getWObject() {
      return this.w;
   }

   public int getW() {
      Assertions.isTrue("w is an Integer", this.w != null && this.w instanceof Integer);
      return (Integer)this.w;
   }

   public String getWString() {
      Assertions.isTrue("w is a String", this.w != null && this.w instanceof String);
      return (String)this.w;
   }

   @Nullable
   public Integer getWTimeout(TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      return this.wTimeoutMS == null ? null : (int)timeUnit.convert((long)this.wTimeoutMS, TimeUnit.MILLISECONDS);
   }

   @Nullable
   public Boolean getJournal() {
      return this.journal;
   }

   public boolean isServerDefault() {
      return this.equals(ACKNOWLEDGED);
   }

   public BsonDocument asDocument() {
      BsonDocument document = new BsonDocument();
      this.addW(document);
      this.addWTimeout(document);
      this.addJ(document);
      return document;
   }

   public boolean isAcknowledged() {
      if (!(this.w instanceof Integer)) {
         return true;
      } else {
         return (Integer)this.w > 0 || this.journal != null && this.journal;
      }
   }

   public static WriteConcern valueOf(String name) {
      return (WriteConcern)NAMED_CONCERNS.get(name.toLowerCase());
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         WriteConcern that = (WriteConcern)o;
         if (!Objects.equals(this.w, that.w)) {
            return false;
         } else if (!Objects.equals(this.wTimeoutMS, that.wTimeoutMS)) {
            return false;
         } else {
            return Objects.equals(this.journal, that.journal);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.w != null ? this.w.hashCode() : 0;
      result = 31 * result + (this.wTimeoutMS != null ? this.wTimeoutMS.hashCode() : 0);
      result = 31 * result + (this.journal != null ? this.journal.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "WriteConcern{w=" + this.w + ", wTimeout=" + this.wTimeoutMS + " ms, journal=" + this.journal + "}";
   }

   public WriteConcern withW(int w) {
      return new WriteConcern(w, this.wTimeoutMS, this.journal);
   }

   public WriteConcern withW(String w) {
      Assertions.notNull("w", w);
      return new WriteConcern(w, this.wTimeoutMS, this.journal);
   }

   public WriteConcern withJournal(@Nullable Boolean journal) {
      return new WriteConcern(this.w, this.wTimeoutMS, journal);
   }

   public WriteConcern withWTimeout(long wTimeout, TimeUnit timeUnit) {
      Assertions.notNull("timeUnit", timeUnit);
      long newWTimeOutMS = TimeUnit.MILLISECONDS.convert(wTimeout, timeUnit);
      Assertions.isTrueArgument("wTimeout >= 0", wTimeout >= 0L);
      Assertions.isTrueArgument("wTimeout <= 2147483647 ms", newWTimeOutMS <= 2147483647L);
      return new WriteConcern(this.w, (int)newWTimeOutMS, this.journal);
   }

   private void addW(BsonDocument document) {
      if (this.w instanceof String) {
         document.put((String)"w", (BsonValue)(new BsonString((String)this.w)));
      } else if (this.w instanceof Integer) {
         document.put((String)"w", (BsonValue)(new BsonInt32((Integer)this.w)));
      }

   }

   private void addJ(BsonDocument document) {
      if (this.journal != null) {
         document.put((String)"j", (BsonValue)BsonBoolean.valueOf(this.journal));
      }

   }

   private void addWTimeout(BsonDocument document) {
      if (this.wTimeoutMS != null) {
         document.put((String)"wtimeout", (BsonValue)(new BsonInt32(this.wTimeoutMS)));
      }

   }

   static {
      JOURNALED = ACKNOWLEDGED.withJournal(true);
      MAJORITY = new WriteConcern("majority");
      NAMED_CONCERNS = new HashMap();
      Field[] var0 = WriteConcern.class.getFields();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Field f = var0[var2];
         if (Modifier.isStatic(f.getModifiers()) && f.getType().equals(WriteConcern.class)) {
            String key = f.getName().toLowerCase();

            try {
               NAMED_CONCERNS.put(key, (WriteConcern)f.get((Object)null));
            } catch (IllegalAccessException e) {
               throw new RuntimeException(e);
            }
         }
      }

   }
}
