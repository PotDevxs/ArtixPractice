package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.function.Function;
import java.util.function.Predicate;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.util.Buildable;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface JoinConfiguration extends Buildable<JoinConfiguration, JoinConfiguration.Builder>, Examinable {
   @NotNull
   static JoinConfiguration.Builder builder() {
      return new JoinConfigurationImpl.BuilderImpl();
   }

   @NotNull
   static JoinConfiguration noSeparators() {
      return JoinConfigurationImpl.NULL;
   }

   @NotNull
   static JoinConfiguration newlines() {
      return JoinConfigurationImpl.STANDARD_NEW_LINES;
   }

   @NotNull
   static JoinConfiguration commas(boolean spaces) {
      return spaces ? JoinConfigurationImpl.STANDARD_COMMA_SPACE_SEPARATED : JoinConfigurationImpl.STANDARD_COMMA_SEPARATED;
   }

   @NotNull
   static JoinConfiguration arrayLike() {
      return JoinConfigurationImpl.STANDARD_ARRAY_LIKE;
   }

   @NotNull
   static JoinConfiguration separator(@Nullable ComponentLike separator) {
      return (JoinConfiguration)(separator == null ? JoinConfigurationImpl.NULL : (JoinConfiguration)builder().separator(separator).build());
   }

   @NotNull
   static JoinConfiguration separators(@Nullable ComponentLike separator, @Nullable ComponentLike lastSeparator) {
      return (JoinConfiguration)(separator == null && lastSeparator == null ? JoinConfigurationImpl.NULL : (JoinConfiguration)builder().separator(separator).lastSeparator(lastSeparator).build());
   }

   @Nullable
   Component prefix();

   @Nullable
   Component suffix();

   @Nullable
   Component separator();

   @Nullable
   Component lastSeparator();

   @Nullable
   Component lastSeparatorIfSerial();

   @NotNull
   Function<ComponentLike, Component> convertor();

   @NotNull
   Predicate<ComponentLike> predicate();

   @NotNull
   Style parentStyle();

   public interface Builder extends AbstractBuilder<JoinConfiguration>, Buildable.Builder<JoinConfiguration> {
      @Contract("_ -> this")
      @NotNull
      JoinConfiguration.Builder prefix(@Nullable ComponentLike var1);

      @Contract("_ -> this")
      @NotNull
      JoinConfiguration.Builder suffix(@Nullable ComponentLike var1);

      @Contract("_ -> this")
      @NotNull
      JoinConfiguration.Builder separator(@Nullable ComponentLike var1);

      @Contract("_ -> this")
      @NotNull
      JoinConfiguration.Builder lastSeparator(@Nullable ComponentLike var1);

      @Contract("_ -> this")
      @NotNull
      JoinConfiguration.Builder lastSeparatorIfSerial(@Nullable ComponentLike var1);

      @Contract("_ -> this")
      @NotNull
      JoinConfiguration.Builder convertor(@NotNull Function<ComponentLike, Component> var1);

      @Contract("_ -> this")
      @NotNull
      JoinConfiguration.Builder predicate(@NotNull Predicate<ComponentLike> var1);

      @Contract("_ -> this")
      @NotNull
      JoinConfiguration.Builder parentStyle(@NotNull Style var1);
   }
}
