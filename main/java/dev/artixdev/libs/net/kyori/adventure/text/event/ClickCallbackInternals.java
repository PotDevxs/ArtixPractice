package dev.artixdev.libs.net.kyori.adventure.text.event;

import dev.artixdev.libs.net.kyori.adventure.audience.Audience;
import dev.artixdev.libs.net.kyori.adventure.permission.PermissionChecker;
import dev.artixdev.libs.net.kyori.adventure.util.Services;
import dev.artixdev.libs.net.kyori.adventure.util.TriState;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

final class ClickCallbackInternals {
   static final PermissionChecker ALWAYS_FALSE;
   static final ClickCallback.Provider PROVIDER;

   private ClickCallbackInternals() {
   }

   static {
      ALWAYS_FALSE = PermissionChecker.always(TriState.FALSE);
      PROVIDER = (ClickCallback.Provider)Services.service(ClickCallback.Provider.class).orElseGet(ClickCallbackInternals.Fallback::new);
   }

   static final class Fallback implements ClickCallback.Provider {
      @NotNull
      public ClickEvent create(@NotNull ClickCallback<Audience> callback, @NotNull ClickCallback.Options options) {
         return ClickEvent.suggestCommand("Callbacks are not supported on this platform!");
      }
   }
}
