package dev.artixdev.libs.net.kyori.adventure.nbt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class ListTagBuilder<T extends BinaryTag> implements ListBinaryTag.Builder<T> {
   @Nullable
   private List<BinaryTag> tags;
   private BinaryTagType<? extends BinaryTag> elementType;

   ListTagBuilder() {
      this(BinaryTagTypes.END);
   }

   ListTagBuilder(BinaryTagType<? extends BinaryTag> type) {
      this.elementType = type;
   }

   @NotNull
   public ListBinaryTag.Builder<T> add(BinaryTag tag) {
      ListBinaryTagImpl.noAddEnd(tag);
      if (this.elementType == BinaryTagTypes.END) {
         this.elementType = tag.type();
      }

      ListBinaryTagImpl.mustBeSameType(tag, this.elementType);
      if (this.tags == null) {
         this.tags = new ArrayList();
      }

      this.tags.add(tag);
      return this;
   }

   @NotNull
   public ListBinaryTag.Builder<T> add(Iterable<? extends T> tagsToAdd) {
      Iterator var2 = tagsToAdd.iterator();

      while(var2.hasNext()) {
         @SuppressWarnings("unchecked")
         T tag = (T) (Object) var2.next();
         this.add(tag);
      }

      return this;
   }

   @NotNull
   public ListBinaryTag build() {
      return (ListBinaryTag)(this.tags == null ? ListBinaryTag.empty() : new ListBinaryTagImpl(this.elementType, new ArrayList(this.tags)));
   }
}
