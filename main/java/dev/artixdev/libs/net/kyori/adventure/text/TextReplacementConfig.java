package dev.artixdev.libs.net.kyori.adventure.text;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.util.Buildable;
import dev.artixdev.libs.net.kyori.adventure.util.IntFunction2;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.org.intellij.lang.annotations.RegExp;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface TextReplacementConfig extends Buildable<TextReplacementConfig, TextReplacementConfig.Builder>, Examinable {
   @NotNull
   static TextReplacementConfig.Builder builder() {
      return new TextReplacementConfigImpl.Builder();
   }

   @NotNull
   Pattern matchPattern();

   @FunctionalInterface
   public interface Condition {
      @NotNull
      PatternReplacementResult shouldReplace(@NotNull MatchResult var1, int var2, int var3);
   }

   public interface Builder extends AbstractBuilder<TextReplacementConfig>, Buildable.Builder<TextReplacementConfig> {
      @Contract("_ -> this")
      default TextReplacementConfig.Builder matchLiteral(String literal) {
         return this.match(Pattern.compile(literal, 16));
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder match(@NotNull @RegExp String pattern) {
         return this.match(Pattern.compile(pattern));
      }

      @Contract("_ -> this")
      @NotNull
      TextReplacementConfig.Builder match(@NotNull Pattern var1);

      @NotNull
      default TextReplacementConfig.Builder once() {
         return this.times(1);
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder times(int times) {
         return this.condition((index, replaced) -> {
            return replaced < times ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP;
         });
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder condition(@NotNull IntFunction2<PatternReplacementResult> condition) {
         return this.condition((result, matchCount, replaced) -> {
            return (PatternReplacementResult)condition.apply(matchCount, replaced);
         });
      }

      @Contract("_ -> this")
      @NotNull
      TextReplacementConfig.Builder condition(@NotNull TextReplacementConfig.Condition var1);

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder replacement(@NotNull String replacement) {
         Objects.requireNonNull(replacement, "replacement");
         return this.replacement((builder) -> {
            return builder.content(replacement);
         });
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder replacement(@Nullable ComponentLike replacement) {
         Component baked = ComponentLike.unbox(replacement);
         return this.replacement((result, input) -> {
            return baked;
         });
      }

      @Contract("_ -> this")
      @NotNull
      default TextReplacementConfig.Builder replacement(@NotNull Function<TextComponent.Builder, ComponentLike> replacement) {
         Objects.requireNonNull(replacement, "replacement");
         return this.replacement((result, input) -> {
            return (ComponentLike)replacement.apply(input);
         });
      }

      @Contract("_ -> this")
      @NotNull
      TextReplacementConfig.Builder replacement(@NotNull BiFunction<MatchResult, TextComponent.Builder, ComponentLike> var1);
   }
}
