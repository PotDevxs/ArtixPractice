package dev.artixdev.libs.com.mongodb.internal.bulk;

public class WriteRequestWithIndex {
   private final int index;
   private final WriteRequest writeRequest;

   public WriteRequestWithIndex(WriteRequest writeRequest, int index) {
      this.writeRequest = writeRequest;
      this.index = index;
   }

   public int getIndex() {
      return this.index;
   }

   public WriteRequest getWriteRequest() {
      return this.writeRequest;
   }

   public WriteRequest.Type getType() {
      return this.writeRequest.getType();
   }
}
