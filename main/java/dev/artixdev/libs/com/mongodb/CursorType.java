package dev.artixdev.libs.com.mongodb;

public enum CursorType {
   NonTailable {
      public boolean isTailable() {
         return false;
      }
   },
   Tailable {
      public boolean isTailable() {
         return true;
      }
   },
   TailableAwait {
      public boolean isTailable() {
         return true;
      }
   };

   private CursorType() {
   }

   public abstract boolean isTailable();

   // $FF: synthetic method
   private static CursorType[] $values() {
      return new CursorType[]{NonTailable, Tailable, TailableAwait};
   }

   // $FF: synthetic method
   CursorType(Object x2) {
      this();
   }
}
