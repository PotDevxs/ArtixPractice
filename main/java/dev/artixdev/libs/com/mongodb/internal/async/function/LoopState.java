package dev.artixdev.libs.com.mongodb.internal.async.function;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.annotations.NotThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.async.SingleResultCallback;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@NotThreadSafe
public final class LoopState {
   private int iteration = 0;
   private boolean lastIteration;
   @Nullable
   private Map<LoopState.AttachmentKey<?>, LoopState.AttachmentValueContainer> attachments;

   boolean advance() {
      if (this.lastIteration) {
         return false;
      } else {
         ++this.iteration;
         this.removeAutoRemovableAttachments();
         return true;
      }
   }

   public boolean isFirstIteration() {
      return this.iteration == 0;
   }

   boolean isLastIteration() {
      return this.lastIteration;
   }

   public int iteration() {
      return this.iteration;
   }

   public boolean breakAndCompleteIf(Supplier<Boolean> predicate, SingleResultCallback<?> callback) {
      Assertions.assertFalse(this.lastIteration);

      try {
         this.lastIteration = (Boolean)predicate.get();
      } catch (Throwable throwable) {
         callback.onResult(null, throwable);
         return true;
      }

      if (this.lastIteration) {
         callback.onResult(null, (Throwable)null);
         return true;
      } else {
         return false;
      }
   }

   void markAsLastIteration() {
      Assertions.assertFalse(this.lastIteration);
      this.lastIteration = true;
   }

   public <V> LoopState attach(LoopState.AttachmentKey<V> key, V value, boolean autoRemove) {
      this.attachments().put((LoopState.AttachmentKey)Assertions.assertNotNull(key), new LoopState.AttachmentValueContainer(Assertions.assertNotNull(value), autoRemove));
      return this;
   }

   public <V> Optional<V> attachment(LoopState.AttachmentKey<V> key) {
      LoopState.AttachmentValueContainer valueContainer = (LoopState.AttachmentValueContainer)this.attachments().get(Assertions.assertNotNull(key));
      V value = valueContainer == null ? null : (V) valueContainer.value();
      return Optional.ofNullable(value);
   }

   private Map<LoopState.AttachmentKey<?>, LoopState.AttachmentValueContainer> attachments() {
      if (this.attachments == null) {
         this.attachments = new HashMap();
      }

      return this.attachments;
   }

   private void removeAutoRemovableAttachments() {
      if (this.attachments != null) {
         this.attachments.entrySet().removeIf((entry) -> {
            return ((LoopState.AttachmentValueContainer)entry.getValue()).autoRemove();
         });
      }
   }

   public String toString() {
      return "LoopState{iteration=" + this.iteration + ", attachments=" + this.attachments + '}';
   }

   @Immutable
   public interface AttachmentKey<V> {
   }

   private static final class AttachmentValueContainer {
      @Nullable
      private final Object value;
      private final boolean autoRemove;

      AttachmentValueContainer(@Nullable Object value, boolean autoRemove) {
         this.value = value;
         this.autoRemove = autoRemove;
      }

      @Nullable
      Object value() {
         return this.value;
      }

      boolean autoRemove() {
         return this.autoRemove;
      }

      public String toString() {
         return "AttachmentValueContainer{value=" + this.value + ", autoRemove=" + this.autoRemove + '}';
      }
   }
}
