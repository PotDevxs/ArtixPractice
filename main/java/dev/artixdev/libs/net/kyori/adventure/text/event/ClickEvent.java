package dev.artixdev.libs.net.kyori.adventure.text.event;

import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.audience.Audience;
import dev.artixdev.libs.net.kyori.adventure.builder.AbstractBuilder;
import dev.artixdev.libs.net.kyori.adventure.internal.Internals;
import dev.artixdev.libs.net.kyori.adventure.text.format.Style;
import dev.artixdev.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import dev.artixdev.libs.net.kyori.adventure.util.Index;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public final class ClickEvent implements StyleBuilderApplicable, Examinable {
   private final ClickEvent.Action action;
   private final String value;

   @NotNull
   public static ClickEvent openUrl(@NotNull String url) {
      return new ClickEvent(ClickEvent.Action.OPEN_URL, url);
   }

   @NotNull
   public static ClickEvent openUrl(@NotNull URL url) {
      return openUrl(url.toExternalForm());
   }

   @NotNull
   public static ClickEvent openFile(@NotNull String file) {
      return new ClickEvent(ClickEvent.Action.OPEN_FILE, file);
   }

   @NotNull
   public static ClickEvent runCommand(@NotNull String command) {
      return new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
   }

   @NotNull
   public static ClickEvent suggestCommand(@NotNull String command) {
      return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
   }

   @NotNull
   public static ClickEvent changePage(@NotNull String page) {
      return new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page);
   }

   @NotNull
   public static ClickEvent changePage(int page) {
      return changePage(String.valueOf(page));
   }

   @NotNull
   public static ClickEvent copyToClipboard(@NotNull String text) {
      return new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text);
   }

   @NotNull
   public static ClickEvent callback(@NotNull ClickCallback<Audience> function) {
      return ClickCallbackInternals.PROVIDER.create((ClickCallback)Objects.requireNonNull(function, "function"), ClickCallbackOptionsImpl.DEFAULT);
   }

   @NotNull
   public static ClickEvent callback(@NotNull ClickCallback<Audience> function, @NotNull ClickCallback.Options options) {
      return ClickCallbackInternals.PROVIDER.create((ClickCallback)Objects.requireNonNull(function, "function"), (ClickCallback.Options)Objects.requireNonNull(options, "options"));
   }

   @NotNull
   public static ClickEvent callback(@NotNull ClickCallback<Audience> function, @NotNull Consumer<ClickCallback.Options.Builder> optionsBuilder) {
      return ClickCallbackInternals.PROVIDER.create((ClickCallback)Objects.requireNonNull(function, "function"), (ClickCallback.Options)AbstractBuilder.configureAndBuild(ClickCallback.Options.builder(), (Consumer)Objects.requireNonNull(optionsBuilder, "optionsBuilder")));
   }

   @NotNull
   public static ClickEvent clickEvent(@NotNull ClickEvent.Action action, @NotNull String value) {
      return new ClickEvent(action, value);
   }

   private ClickEvent(@NotNull ClickEvent.Action action, @NotNull String value) {
      this.action = (ClickEvent.Action)Objects.requireNonNull(action, "action");
      this.value = (String)Objects.requireNonNull(value, "value");
   }

   @NotNull
   public ClickEvent.Action action() {
      return this.action;
   }

   @NotNull
   public String value() {
      return this.value;
   }

   public void styleApply(@NotNull Style.Builder style) {
      style.clickEvent(this);
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         ClickEvent that = (ClickEvent)other;
         return this.action == that.action && Objects.equals(this.value, that.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.action.hashCode();
      result = 31 * result + this.value.hashCode();
      return result;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("action", (Object)this.action), ExaminableProperty.of("value", this.value));
   }

   public String toString() {
      return Internals.toString(this);
   }

   public static enum Action {
      OPEN_URL("open_url", true),
      OPEN_FILE("open_file", false),
      RUN_COMMAND("run_command", true),
      SUGGEST_COMMAND("suggest_command", true),
      CHANGE_PAGE("change_page", true),
      COPY_TO_CLIPBOARD("copy_to_clipboard", true);

      public static final Index<String, ClickEvent.Action> NAMES = Index.create(ClickEvent.Action.class, (constant) -> {
         return constant.name;
      });
      private final String name;
      private final boolean readable;

      private Action(@NotNull String name, boolean readable) {
         this.name = name;
         this.readable = readable;
      }

      public boolean readable() {
         return this.readable;
      }

      @NotNull
      public String toString() {
         return this.name;
      }
   }
}
