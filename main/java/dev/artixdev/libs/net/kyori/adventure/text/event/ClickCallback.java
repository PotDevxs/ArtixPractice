package dev.artixdev.libs.net.kyori.adventure.text.event;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.function.Consumer;
import java.util.function.Predicate;
import dev.artixdev.libs.net.kyori.adventure.audience.Audience;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.permission.PermissionChecker;
import dev.artixdev.libs.net.kyori.adventure.util.PlatformAPI;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.CheckReturnValue;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ClickCallback<T extends Audience> {
   Duration DEFAULT_LIFETIME = Duration.ofHours(12L);
   int UNLIMITED_USES = -1;

   @CheckReturnValue
   @Contract(
      pure = true
   )
   @NotNull
   static <W extends Audience, N extends W> ClickCallback<W> widen(@NotNull ClickCallback<N> original, @NotNull Class<N> type, @Nullable Consumer<? super Audience> otherwise) {
      return (audience) -> {
         if (type.isInstance(audience)) {
            @SuppressWarnings("unchecked")
            N castAudience = type.cast(audience);
            original.accept(castAudience);
         } else if (otherwise != null) {
            otherwise.accept(audience);
         }

      };
   }

   @CheckReturnValue
   @Contract(
      pure = true
   )
   @NotNull
   static <W extends Audience, N extends W> ClickCallback<W> widen(@NotNull ClickCallback<N> original, @NotNull Class<N> type) {
      return widen(original, type, (Consumer)null);
   }

   void accept(@NotNull T var1);

   @CheckReturnValue
   @Contract(
      pure = true
   )
   @NotNull
   default ClickCallback<T> filter(@NotNull Predicate<T> filter) {
      return this.filter(filter, (Consumer)null);
   }

   @CheckReturnValue
   @Contract(
      pure = true
   )
   @NotNull
   default ClickCallback<T> filter(@NotNull Predicate<T> filter, @Nullable Consumer<? super Audience> otherwise) {
      return (audience) -> {
         if (filter.test(audience)) {
            this.accept(audience);
         } else if (otherwise != null) {
            otherwise.accept(audience);
         }

      };
   }

   @CheckReturnValue
   @Contract(
      pure = true
   )
   @NotNull
   default ClickCallback<T> requiringPermission(@NotNull String permission) {
      return this.requiringPermission(permission, (Consumer)null);
   }

   @CheckReturnValue
   @Contract(
      pure = true
   )
   @NotNull
   default ClickCallback<T> requiringPermission(@NotNull String permission, @Nullable Consumer<? super Audience> otherwise) {
      return this.filter((audience) -> {
         return ((PermissionChecker)audience.getOrDefault(PermissionChecker.POINTER, ClickCallbackInternals.ALWAYS_FALSE)).test(permission);
      }, otherwise);
   }

   @PlatformAPI
   @ApiStatus.Internal
   public interface Provider {
      @NotNull
      ClickEvent create(@NotNull ClickCallback<Audience> var1, @NotNull ClickCallback.Options var2);
   }

   @ApiStatus.NonExtendable
   public interface Options extends Examinable {
      @NotNull
      static ClickCallback.Options.Builder builder() {
         return new ClickCallbackOptionsImpl.BuilderImpl();
      }

      @NotNull
      static ClickCallback.Options.Builder builder(@NotNull ClickCallback.Options existing) {
         return new ClickCallbackOptionsImpl.BuilderImpl(existing);
      }

      int uses();

      @NotNull
      Duration lifetime();

      @ApiStatus.NonExtendable
      public interface Builder extends AbstractBuilder<ClickCallback.Options> {
         @NotNull
         ClickCallback.Options.Builder uses(int var1);

         @NotNull
         ClickCallback.Options.Builder lifetime(@NotNull TemporalAmount var1);
      }
   }
}
