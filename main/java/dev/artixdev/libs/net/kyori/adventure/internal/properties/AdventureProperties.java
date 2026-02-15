package dev.artixdev.libs.net.kyori.adventure.internal.properties;

import java.util.function.Function;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class AdventureProperties {
   public static final AdventureProperties.Property<Boolean> DEBUG = property("debug", Boolean::parseBoolean, false);
   public static final AdventureProperties.Property<String> DEFAULT_TRANSLATION_LOCALE = property("defaultTranslationLocale", Function.<String>identity(), null);
   public static final AdventureProperties.Property<Boolean> SERVICE_LOAD_FAILURES_ARE_FATAL;
   public static final AdventureProperties.Property<Boolean> TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED;

   private AdventureProperties() {
   }

   @NotNull
   public static <T> AdventureProperties.Property<T> property(@NotNull String name, @NotNull Function<String, T> parser, @Nullable T defaultValue) {
      return AdventurePropertiesImpl.property(name, parser, defaultValue);
   }

   static {
      SERVICE_LOAD_FAILURES_ARE_FATAL = property("serviceLoadFailuresAreFatal", Boolean::parseBoolean, Boolean.TRUE);
      TEXT_WARN_WHEN_LEGACY_FORMATTING_DETECTED = property("text.warnWhenLegacyFormattingDetected", Boolean::parseBoolean, Boolean.FALSE);
   }

   @ApiStatus.Internal
   @ApiStatus.NonExtendable
   public interface Property<T> {
      @Nullable
      T value();
   }
}
