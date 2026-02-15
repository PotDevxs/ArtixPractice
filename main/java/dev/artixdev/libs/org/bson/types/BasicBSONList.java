package dev.artixdev.libs.org.bson.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import dev.artixdev.libs.org.bson.BSONObject;

public class BasicBSONList extends ArrayList<Object> implements BSONObject {
   private static final long serialVersionUID = -4415279469780082174L;

   public Object put(String key, Object v) {
      return this.put(this._getInt(key), v);
   }

   public Object put(int key, Object value) {
      while(key >= this.size()) {
         this.add((Object)null);
      }

      this.set(key, value);
      return value;
   }

   public void putAll(Map m) {
      Iterator<?> iterator = m.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry entry = (Entry)iterator.next();
         this.put(entry.getKey().toString(), entry.getValue());
      }

   }

   public void putAll(BSONObject o) {
      Iterator<?> iterator = o.keySet().iterator();

      while(iterator.hasNext()) {
         String k = (String)iterator.next();
         this.put(k, o.get(k));
      }

   }

   public Object get(String key) {
      int i = this._getInt(key);
      if (i < 0) {
         return null;
      } else {
         return i >= this.size() ? null : this.get(i);
      }
   }

   public Object removeField(String key) {
      int i = this._getInt(key);
      if (i < 0) {
         return null;
      } else {
         return i >= this.size() ? null : this.remove(i);
      }
   }

   public boolean containsField(String key) {
      int i = this._getInt(key, false);
      if (i < 0) {
         return false;
      } else {
         return i >= 0 && i < this.size();
      }
   }

   public Set<String> keySet() {
      return new StringRangeSet(this.size());
   }

   public Map toMap() {
      Map m = new HashMap();
      Iterator i = this.keySet().iterator();

      while(i.hasNext()) {
         Object s = i.next();
         m.put(s, this.get(String.valueOf(s)));
      }

      return m;
   }

   int _getInt(String s) {
      return this._getInt(s, true);
   }

   int _getInt(String s, boolean err) {
      try {
         return Integer.parseInt(s);
      } catch (Exception exception) {
         if (err) {
            throw new IllegalArgumentException("BasicBSONList can only work with numeric keys, not: [" + s + "]");
         } else {
            return -1;
         }
      }
   }
}
