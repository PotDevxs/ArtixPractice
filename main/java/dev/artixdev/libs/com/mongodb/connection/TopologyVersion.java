package dev.artixdev.libs.com.mongodb.connection;

import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.org.bson.BsonDocument;
import dev.artixdev.libs.org.bson.BsonInt64;
import dev.artixdev.libs.org.bson.BsonObjectId;
import dev.artixdev.libs.org.bson.types.ObjectId;

@ThreadSafe
public final class TopologyVersion {
   private final ObjectId processId;
   private final long counter;

   public TopologyVersion(BsonDocument topologyVersionDocument) {
      this.processId = topologyVersionDocument.getObjectId("processId").getValue();
      this.counter = topologyVersionDocument.getInt64("counter").getValue();
   }

   public TopologyVersion(ObjectId processId, long counter) {
      this.processId = processId;
      this.counter = counter;
   }

   public ObjectId getProcessId() {
      return this.processId;
   }

   public long getCounter() {
      return this.counter;
   }

   public BsonDocument asDocument() {
      return (new BsonDocument("processId", new BsonObjectId(this.processId))).append("counter", new BsonInt64(this.counter));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TopologyVersion that = (TopologyVersion)o;
         return this.counter != that.counter ? false : this.processId.equals(that.processId);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.processId.hashCode();
      result = 31 * result + (int)(this.counter ^ this.counter >>> 32);
      return result;
   }

   public String toString() {
      return "TopologyVersion{processId=" + this.processId + ", counter=" + this.counter + '}';
   }
}
