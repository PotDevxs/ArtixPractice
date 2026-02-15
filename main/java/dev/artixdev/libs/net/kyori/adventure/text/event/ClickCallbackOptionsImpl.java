package dev.artixdev.libs.net.kyori.adventure.text.event;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Objects;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.internal.Internals;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

final class ClickCallbackOptionsImpl implements ClickCallback.Options {
   static final ClickCallback.Options DEFAULT = (new ClickCallbackOptionsImpl.BuilderImpl()).build();
   private final int uses;
   private final Duration lifetime;

   ClickCallbackOptionsImpl(int uses, Duration lifetime) {
      this.uses = uses;
      this.lifetime = lifetime;
   }

   public int uses() {
      return this.uses;
   }

   @NotNull
   public Duration lifetime() {
      return this.lifetime;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("uses", this.uses), ExaminableProperty.of("expiration", (Object)this.lifetime));
   }

   public String toString() {
      return Internals.toString(this);
   }

   static final class BuilderImpl implements ClickCallback.Options.Builder {
      private static final int DEFAULT_USES = 1;
      private int uses;
      private Duration lifetime;

      BuilderImpl() {
         this.uses = 1;
         this.lifetime = ClickCallback.DEFAULT_LIFETIME;
      }

      BuilderImpl(@NotNull ClickCallback.Options existing) {
         this.uses = existing.uses();
         this.lifetime = existing.lifetime();
      }

      @NotNull
      public ClickCallback.Options build() {
         return new ClickCallbackOptionsImpl(this.uses, this.lifetime);
      }

      @NotNull
      public ClickCallback.Options.Builder uses(int uses) {
         this.uses = uses;
         return this;
      }

      @NotNull
      public ClickCallback.Options.Builder lifetime(@NotNull TemporalAmount lifetime) {
         this.lifetime = lifetime instanceof Duration ? (Duration)lifetime : Duration.from((TemporalAmount)Objects.requireNonNull(lifetime, "lifetime"));
         return this;
      }
   }
}
