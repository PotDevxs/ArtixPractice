package dev.artixdev.libs.com.mongodb;

import java.io.Serializable;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;

@Immutable
public final class ServerCursor implements Serializable {
   private static final long serialVersionUID = -7013636754565190109L;
   private final long id;
   private final ServerAddress address;

   public ServerCursor(long id, ServerAddress address) {
      if (id == 0L) {
         throw new IllegalArgumentException();
      } else {
         this.id = id;
         this.address = address;
      }
   }

   public long getId() {
      return this.id;
   }

   public ServerAddress getAddress() {
      return this.address;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ServerCursor that = (ServerCursor)o;
         if (this.id != that.id) {
            return false;
         } else {
            return this.address.equals(that.address);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = (int)(this.id ^ this.id >>> 32);
      result = 31 * result + this.address.hashCode();
      return result;
   }

   public String toString() {
      return "ServerCursor{getId=" + this.id + ", address=" + this.address + '}';
   }
}
