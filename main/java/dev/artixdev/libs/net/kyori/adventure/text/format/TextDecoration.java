package dev.artixdev.libs.net.kyori.adventure.text.format;

import java.util.Objects;
import dev.artixdev.libs.net.kyori.adventure.util.Index;
import dev.artixdev.libs.net.kyori.adventure.util.TriState;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public enum TextDecoration implements StyleBuilderApplicable, TextFormat {
   OBFUSCATED("obfuscated"),
   BOLD("bold"),
   STRIKETHROUGH("strikethrough"),
   UNDERLINED("underlined"),
   ITALIC("italic");

   public static final Index<String, TextDecoration> NAMES = Index.create(TextDecoration.class, (constant) -> {
      return constant.name;
   });
   private final String name;

   private TextDecoration(String name) {
      this.name = name;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public final TextDecorationAndState as(boolean state) {
      return this.withState(state);
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   public final TextDecorationAndState as(@NotNull TextDecoration.State state) {
      return this.withState(state);
   }

   @NotNull
   public final TextDecorationAndState withState(boolean state) {
      return new TextDecorationAndStateImpl(this, TextDecoration.State.byBoolean(state));
   }

   @NotNull
   public final TextDecorationAndState withState(@NotNull TextDecoration.State state) {
      return new TextDecorationAndStateImpl(this, state);
   }

   @NotNull
   public final TextDecorationAndState withState(@NotNull TriState state) {
      return new TextDecorationAndStateImpl(this, TextDecoration.State.byTriState(state));
   }

   public void styleApply(@NotNull Style.Builder style) {
      style.decorate(this);
   }

   @NotNull
   public String toString() {
      return this.name;
   }

   public static enum State {
      NOT_SET("not_set"),
      FALSE("false"),
      TRUE("true");

      private final String name;

      private State(String name) {
         this.name = name;
      }

      public String toString() {
         return this.name;
      }

      @NotNull
      public static TextDecoration.State byBoolean(boolean flag) {
         return flag ? TRUE : FALSE;
      }

      @NotNull
      public static TextDecoration.State byBoolean(@Nullable Boolean flag) {
         return flag == null ? NOT_SET : byBoolean(flag);
      }

      @NotNull
      public static TextDecoration.State byTriState(@NotNull TriState flag) {
         Objects.requireNonNull(flag);
         switch(flag) {
         case TRUE:
            return TRUE;
         case FALSE:
            return FALSE;
         case NOT_SET:
            return NOT_SET;
         default:
            throw new IllegalArgumentException("Unable to turn TriState: " + flag + " into a TextDecoration.State");
         }
      }
   }
}
