package dev.artixdev.libs.com.mongodb.internal.operation.retry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.bulk.BulkWriteResult;
import dev.artixdev.libs.com.mongodb.internal.async.function.LoopState;
import dev.artixdev.libs.com.mongodb.internal.operation.MixedBulkWriteOperation;
import dev.artixdev.libs.org.bson.BsonDocument;

public final class AttachmentKeys {
   private static final LoopState.AttachmentKey<Integer> MAX_WIRE_VERSION = new AttachmentKeys.DefaultAttachmentKey("maxWireVersion");
   private static final LoopState.AttachmentKey<BsonDocument> COMMAND = new AttachmentKeys.DefaultAttachmentKey("command");
   private static final LoopState.AttachmentKey<Boolean> RETRYABLE_COMMAND_FLAG = new AttachmentKeys.DefaultAttachmentKey("retryableCommandFlag");
   private static final LoopState.AttachmentKey<Supplier<String>> COMMAND_DESCRIPTION_SUPPLIER = new AttachmentKeys.DefaultAttachmentKey("commandDescriptionSupplier");
   private static final LoopState.AttachmentKey<MixedBulkWriteOperation.BulkWriteTracker> BULK_WRITE_TRACKER = new AttachmentKeys.DefaultAttachmentKey("bulkWriteTracker");
   private static final LoopState.AttachmentKey<BulkWriteResult> BULK_WRITE_RESULT = new AttachmentKeys.DefaultAttachmentKey("bulkWriteResult");

   public static LoopState.AttachmentKey<Integer> maxWireVersion() {
      return MAX_WIRE_VERSION;
   }

   public static LoopState.AttachmentKey<BsonDocument> command() {
      return COMMAND;
   }

   public static LoopState.AttachmentKey<Boolean> retryableCommandFlag() {
      return RETRYABLE_COMMAND_FLAG;
   }

   public static LoopState.AttachmentKey<Supplier<String>> commandDescriptionSupplier() {
      return COMMAND_DESCRIPTION_SUPPLIER;
   }

   public static LoopState.AttachmentKey<MixedBulkWriteOperation.BulkWriteTracker> bulkWriteTracker() {
      return BULK_WRITE_TRACKER;
   }

   public static LoopState.AttachmentKey<BulkWriteResult> bulkWriteResult() {
      return BULK_WRITE_RESULT;
   }

   private AttachmentKeys() {
      Assertions.fail();
   }

   @Immutable
   private static final class DefaultAttachmentKey<V> implements LoopState.AttachmentKey<V> {
      private static final Set<String> AVOID_KEY_DUPLICATION = new HashSet();
      private final String key;

      private DefaultAttachmentKey(String key) {
         Assertions.assertTrue(AVOID_KEY_DUPLICATION.add(key));
         this.key = key;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            AttachmentKeys.DefaultAttachmentKey<?> that = (AttachmentKeys.DefaultAttachmentKey)o;
            return this.key.equals(that.key);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.key.hashCode();
      }

      public String toString() {
         return this.key;
      }

      // $FF: synthetic method
      DefaultAttachmentKey(String x0, Object x1) {
         this(x0);
      }
   }
}
