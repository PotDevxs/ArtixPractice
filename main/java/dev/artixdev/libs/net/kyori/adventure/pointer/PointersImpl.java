package dev.artixdev.libs.net.kyori.adventure.pointer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

final class PointersImpl implements Pointers {
   static final Pointers EMPTY = new Pointers() {
      @NotNull
      public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
         return Optional.empty();
      }

      public <T> boolean supports(@NotNull Pointer<T> pointer) {
         return false;
      }

      @NotNull
      public Pointers.Builder toBuilder() {
         return new PointersImpl.BuilderImpl();
      }

      public String toString() {
         return "EmptyPointers";
      }
   };
   private final Map<Pointer<?>, Supplier<?>> pointers;

   PointersImpl(@NotNull PointersImpl.BuilderImpl builder) {
      this.pointers = new HashMap(builder.pointers);
   }

   @NotNull
   public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
      Objects.requireNonNull(pointer, "pointer");
      Supplier<?> supplier = (Supplier)this.pointers.get(pointer);
      if (supplier == null) {
         return Optional.empty();
      } else {
         @SuppressWarnings("unchecked")
         T value = (T) supplier.get();
         return Optional.ofNullable(value);
      }
   }

   public <T> boolean supports(@NotNull Pointer<T> pointer) {
      Objects.requireNonNull(pointer, "pointer");
      return this.pointers.containsKey(pointer);
   }

   @NotNull
   public Pointers.Builder toBuilder() {
      return new PointersImpl.BuilderImpl(this);
   }

   static final class BuilderImpl implements Pointers.Builder {
      private final Map<Pointer<?>, Supplier<?>> pointers;

      BuilderImpl() {
         this.pointers = new HashMap();
      }

      BuilderImpl(@NotNull PointersImpl pointers) {
         this.pointers = new HashMap(pointers.pointers);
      }

      @NotNull
      public <T> Pointers.Builder withDynamic(@NotNull Pointer<T> pointer, @NotNull Supplier<T> value) {
         this.pointers.put((Pointer)Objects.requireNonNull(pointer, "pointer"), (Supplier)Objects.requireNonNull(value, "value"));
         return this;
      }

      @NotNull
      public Pointers build() {
         return new PointersImpl(this);
      }
   }
}
