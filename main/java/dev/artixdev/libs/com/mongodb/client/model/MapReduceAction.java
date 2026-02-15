package dev.artixdev.libs.com.mongodb.client.model;

/** @deprecated */
@Deprecated
public enum MapReduceAction {
   REPLACE("replace"),
   MERGE("merge"),
   REDUCE("reduce");

   private final String value;

   private MapReduceAction(String value) {
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   // $FF: synthetic method
   private static MapReduceAction[] $values() {
      return new MapReduceAction[]{REPLACE, MERGE, REDUCE};
   }
}
