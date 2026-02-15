package dev.artixdev.libs.org.bson.types;

import dev.artixdev.libs.org.bson.BSONObject;

public class CodeWScope extends Code {
   private final BSONObject scope;
   private static final long serialVersionUID = -6284832275113680002L;

   public CodeWScope(String code, BSONObject scope) {
      super(code);
      this.scope = scope;
   }

   public BSONObject getScope() {
      return this.scope;
   }

   public boolean equals(Object o) {
      if (o == null) {
         return false;
      } else if (this.getClass() != o.getClass()) {
         return false;
      } else {
         CodeWScope c = (CodeWScope)o;
         return this.getCode().equals(c.getCode()) && this.scope.equals(c.scope);
      }
   }

   public int hashCode() {
      return this.getCode().hashCode() ^ this.scope.hashCode();
   }
}
