package dev.artixdev.libs.org.bson;

public class BsonJavaScriptWithScope extends BsonValue {
   private final String code;
   private final BsonDocument scope;

   public BsonJavaScriptWithScope(String code, BsonDocument scope) {
      if (code == null) {
         throw new IllegalArgumentException("code can not be null");
      } else if (scope == null) {
         throw new IllegalArgumentException("scope can not be null");
      } else {
         this.code = code;
         this.scope = scope;
      }
   }

   public BsonType getBsonType() {
      return BsonType.JAVASCRIPT_WITH_SCOPE;
   }

   public String getCode() {
      return this.code;
   }

   public BsonDocument getScope() {
      return this.scope;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         BsonJavaScriptWithScope that = (BsonJavaScriptWithScope)o;
         if (!this.code.equals(that.code)) {
            return false;
         } else {
            return this.scope.equals(that.scope);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.code.hashCode();
      result = 31 * result + this.scope.hashCode();
      return result;
   }

   public String toString() {
      return "BsonJavaScriptWithScope{code=" + this.getCode() + "scope=" + this.scope + '}';
   }

   static BsonJavaScriptWithScope clone(BsonJavaScriptWithScope from) {
      return new BsonJavaScriptWithScope(from.code, from.scope.clone());
   }
}
