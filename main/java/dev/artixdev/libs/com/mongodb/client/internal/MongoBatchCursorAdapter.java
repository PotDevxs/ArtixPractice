package dev.artixdev.libs.com.mongodb.client.internal;

import java.util.List;
import java.util.NoSuchElementException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.ServerCursor;
import dev.artixdev.libs.com.mongodb.client.MongoCursor;
import dev.artixdev.libs.com.mongodb.internal.operation.BatchCursor;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

public class MongoBatchCursorAdapter<T> implements MongoCursor<T> {
   private final BatchCursor<T> batchCursor;
   private List<T> curBatch;
   private int curPos;

   public MongoBatchCursorAdapter(BatchCursor<T> batchCursor) {
      this.batchCursor = batchCursor;
   }

   public void remove() {
      throw new UnsupportedOperationException("Cursors do not support removal");
   }

   public void close() {
      this.batchCursor.close();
   }

   public boolean hasNext() {
      return this.curBatch != null || this.batchCursor.hasNext();
   }

   public T next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         if (this.curBatch == null) {
            this.curBatch = this.batchCursor.next();
         }

         return this.getNextInBatch();
      }
   }

   public int available() {
      int available = this.batchCursor.available();
      if (this.curBatch != null) {
         available += this.curBatch.size() - this.curPos;
      }

      return available;
   }

   @Nullable
   public T tryNext() {
      if (this.curBatch == null) {
         this.curBatch = this.batchCursor.tryNext();
      }

      return this.curBatch == null ? null : this.getNextInBatch();
   }

   @Nullable
   public ServerCursor getServerCursor() {
      return this.batchCursor.getServerCursor();
   }

   public ServerAddress getServerAddress() {
      return this.batchCursor.getServerAddress();
   }

   private T getNextInBatch() {
      T nextInBatch = this.curBatch.get(this.curPos);
      if (this.curPos < this.curBatch.size() - 1) {
         ++this.curPos;
      } else {
         this.curBatch = null;
         this.curPos = 0;
      }

      return nextInBatch;
   }
}
