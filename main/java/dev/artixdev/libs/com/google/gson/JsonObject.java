package dev.artixdev.libs.com.google.gson;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import dev.artixdev.libs.com.google.gson.internal.LinkedTreeMap;

public final class JsonObject extends JsonElement {
   private final LinkedTreeMap<String, JsonElement> members = new LinkedTreeMap(false);

   public JsonObject deepCopy() {
      JsonObject result = new JsonObject();
      Iterator<Entry<String, JsonElement>> iterator = this.members.entrySet().iterator();

      while (iterator.hasNext()) {
         Entry<String, JsonElement> entry = iterator.next();
         result.add((String)entry.getKey(), ((JsonElement)entry.getValue()).deepCopy());
      }

      return result;
   }

   public void add(String property, JsonElement value) {
      this.members.put(property, value == null ? JsonNull.INSTANCE : value);
   }

   public JsonElement remove(String property) {
      return (JsonElement)this.members.remove(property);
   }

   public void addProperty(String property, String value) {
      this.add(property, (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
   }

   public void addProperty(String property, Number value) {
      this.add(property, (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
   }

   public void addProperty(String property, Boolean value) {
      this.add(property, (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
   }

   public void addProperty(String property, Character value) {
      this.add(property, (JsonElement)(value == null ? JsonNull.INSTANCE : new JsonPrimitive(value)));
   }

   public Set<Entry<String, JsonElement>> entrySet() {
      return this.members.entrySet();
   }

   public Set<String> keySet() {
      return this.members.keySet();
   }

   public int size() {
      return this.members.size();
   }

   public boolean isEmpty() {
      return this.members.size() == 0;
   }

   public boolean has(String memberName) {
      return this.members.containsKey(memberName);
   }

   public JsonElement get(String memberName) {
      return (JsonElement)this.members.get(memberName);
   }

   public JsonPrimitive getAsJsonPrimitive(String memberName) {
      return (JsonPrimitive)this.members.get(memberName);
   }

   public JsonArray getAsJsonArray(String memberName) {
      return (JsonArray)this.members.get(memberName);
   }

   public JsonObject getAsJsonObject(String memberName) {
      return (JsonObject)this.members.get(memberName);
   }

   public Map<String, JsonElement> asMap() {
      return this.members;
   }

   public boolean equals(Object o) {
      return o == this || o instanceof JsonObject && ((JsonObject)o).members.equals(this.members);
   }

   public int hashCode() {
      return this.members.hashCode();
   }
}
