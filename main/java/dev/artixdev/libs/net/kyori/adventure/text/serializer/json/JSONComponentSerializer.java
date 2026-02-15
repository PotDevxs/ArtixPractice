package dev.artixdev.libs.net.kyori.adventure.text.serializer.json;

import java.util.function.Supplier;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import dev.artixdev.libs.net.kyori.adventure.util.PlatformAPI;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface JSONComponentSerializer extends ComponentSerializer<Component, Component, String> {
   @NotNull
   static JSONComponentSerializer json() {
      return JSONComponentSerializerAccessor.Instances.INSTANCE;
   }

   @NotNull
   static JSONComponentSerializer.Builder builder() {
      return (JSONComponentSerializer.Builder)JSONComponentSerializerAccessor.Instances.BUILDER_SUPPLIER.get();
   }

   @PlatformAPI
   @ApiStatus.Internal
   public interface Provider {
      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      JSONComponentSerializer instance();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      Supplier<JSONComponentSerializer.Builder> builder();
   }

   public interface Builder {
      @NotNull
      JSONComponentSerializer.Builder downsampleColors();

      @NotNull
      JSONComponentSerializer.Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer var1);

      @NotNull
      JSONComponentSerializer.Builder emitLegacyHoverEvent();

      JSONComponentSerializer build();
   }
}
