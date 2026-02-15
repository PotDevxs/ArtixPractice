package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface StorageNBTComponent extends NBTComponent<StorageNBTComponent, StorageNBTComponent.Builder>, ScopedComponent<StorageNBTComponent> {
   @NotNull
   Key storage();

   @Contract(
      pure = true
   )
   @NotNull
   StorageNBTComponent storage(@NotNull Key var1);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("storage", (Object)this.storage())), NBTComponent.super.examinableProperties());
   }

   public interface Builder extends NBTComponentBuilder<StorageNBTComponent, StorageNBTComponent.Builder> {
      @Contract("_ -> this")
      @NotNull
      StorageNBTComponent.Builder storage(@NotNull Key var1);
   }
}
