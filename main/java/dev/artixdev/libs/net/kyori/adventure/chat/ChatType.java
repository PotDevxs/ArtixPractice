package dev.artixdev.libs.net.kyori.adventure.chat;

import java.util.Objects;
import java.util.stream.Stream;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.key.Keyed;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.ComponentLike;
import dev.artixdev.libs.net.kyori.examination.Examinable;
import dev.artixdev.libs.net.kyori.examination.ExaminableProperty;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public interface ChatType extends Keyed, Examinable {
   ChatType CHAT = new ChatTypeImpl(Key.key("chat"));
   ChatType SAY_COMMAND = new ChatTypeImpl(Key.key("say_command"));
   ChatType MSG_COMMAND_INCOMING = new ChatTypeImpl(Key.key("msg_command_incoming"));
   ChatType MSG_COMMAND_OUTGOING = new ChatTypeImpl(Key.key("msg_command_outgoing"));
   ChatType TEAM_MSG_COMMAND_INCOMING = new ChatTypeImpl(Key.key("team_msg_command_incoming"));
   ChatType TEAM_MSG_COMMAND_OUTGOING = new ChatTypeImpl(Key.key("team_msg_command_outgoing"));
   ChatType EMOTE_COMMAND = new ChatTypeImpl(Key.key("emote_command"));

   @NotNull
   static ChatType chatType(@NotNull Keyed key) {
      return (ChatType)(key instanceof ChatType ? (ChatType)key : new ChatTypeImpl(((Keyed)Objects.requireNonNull(key, "key")).key()));
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   default ChatType.Bound bind(@NotNull ComponentLike name) {
      return this.bind(name, (ComponentLike)null);
   }

   @Contract(
      value = "_, _ -> new",
      pure = true
   )
   @NotNull
   default ChatType.Bound bind(@NotNull ComponentLike name, @Nullable ComponentLike target) {
      return new ChatTypeImpl.BoundImpl(this, (Component)Objects.requireNonNull(name.asComponent(), "name"), ComponentLike.unbox(target));
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("key", (Object)this.key()));
   }

   public interface Bound extends Examinable {
      @Contract(
         pure = true
      )
      @NotNull
      ChatType type();

      @Contract(
         pure = true
      )
      @NotNull
      Component name();

      @Contract(
         pure = true
      )
      @Nullable
      Component target();

      @NotNull
      default Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("type", (Object)this.type()), ExaminableProperty.of("name", (Object)this.name()), ExaminableProperty.of("target", (Object)this.target()));
      }
   }
}
