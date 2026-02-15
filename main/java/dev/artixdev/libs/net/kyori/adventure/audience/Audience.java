package dev.artixdev.libs.net.kyori.adventure.audience;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import dev.artixdev.libs.net.kyori.adventure.bossbar.BossBar;
import dev.artixdev.libs.net.kyori.adventure.chat.ChatType;
import dev.artixdev.libs.net.kyori.adventure.chat.SignedMessage;
import dev.artixdev.libs.net.kyori.adventure.identity.Identified;
import dev.artixdev.libs.net.kyori.adventure.identity.Identity;
import dev.artixdev.libs.net.kyori.adventure.inventory.Book;
import dev.artixdev.libs.net.kyori.adventure.pointer.Pointered;
import dev.artixdev.libs.net.kyori.adventure.sound.Sound;
import dev.artixdev.libs.net.kyori.adventure.sound.SoundStop;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.ComponentLike;
import dev.artixdev.libs.net.kyori.adventure.title.Title;
import dev.artixdev.libs.net.kyori.adventure.title.TitlePart;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface Audience extends Pointered {
   @NotNull
   static Audience empty() {
      return EmptyAudience.INSTANCE;
   }

   @NotNull
   static Audience audience(@NotNull Audience... audiences) {
      int length = audiences.length;
      if (length == 0) {
         return empty();
      } else {
         return (Audience)(length == 1 ? audiences[0] : audience((Iterable)Arrays.asList(audiences)));
      }
   }

   @NotNull
   static ForwardingAudience audience(@NotNull Iterable<? extends Audience> audiences) {
      return () -> {
         return audiences;
      };
   }

   @NotNull
   static Collector<? super Audience, ?, ForwardingAudience> toAudience() {
      return Audiences.COLLECTOR;
   }

   @NotNull
   default Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
      return filter.test(this) ? this : empty();
   }

   default void forEachAudience(@NotNull Consumer<? super Audience> action) {
      action.accept(this);
   }

   @ForwardingAudienceOverrideNotRequired
   default void sendMessage(@NotNull ComponentLike message) {
      this.sendMessage(message.asComponent());
   }

   default void sendMessage(@NotNull Component message) {
      this.sendMessage(message, MessageType.SYSTEM);
   }

   /** @deprecated */
   @Deprecated
   @ForwardingAudienceOverrideNotRequired
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   default void sendMessage(@NotNull ComponentLike message, @NotNull MessageType type) {
      this.sendMessage(message.asComponent(), type);
   }

   /** @deprecated */
   @Deprecated
   @ForwardingAudienceOverrideNotRequired
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   default void sendMessage(@NotNull Component message, @NotNull MessageType type) {
      this.sendMessage(Identity.nil(), message, type);
   }

   /** @deprecated */
   @Deprecated
   @ForwardingAudienceOverrideNotRequired
   default void sendMessage(@NotNull Identified source, @NotNull ComponentLike message) {
      this.sendMessage(source, message.asComponent());
   }

   /** @deprecated */
   @Deprecated
   @ForwardingAudienceOverrideNotRequired
   default void sendMessage(@NotNull Identity source, @NotNull ComponentLike message) {
      this.sendMessage(source, message.asComponent());
   }

   /** @deprecated */
   @Deprecated
   @ForwardingAudienceOverrideNotRequired
   default void sendMessage(@NotNull Identified source, @NotNull Component message) {
      this.sendMessage(source, message, MessageType.CHAT);
   }

   /** @deprecated */
   @Deprecated
   @ForwardingAudienceOverrideNotRequired
   default void sendMessage(@NotNull Identity source, @NotNull Component message) {
      this.sendMessage(source, message, MessageType.CHAT);
   }

   /** @deprecated */
   @Deprecated
   @ForwardingAudienceOverrideNotRequired
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   default void sendMessage(@NotNull Identified source, @NotNull ComponentLike message, @NotNull MessageType type) {
      this.sendMessage(source, message.asComponent(), type);
   }

   /** @deprecated */
   @Deprecated
   @ForwardingAudienceOverrideNotRequired
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   default void sendMessage(@NotNull Identity source, @NotNull ComponentLike message, @NotNull MessageType type) {
      this.sendMessage(source, message.asComponent(), type);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   default void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
      this.sendMessage(source.identity(), message, type);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   default void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
   }

   default void sendMessage(@NotNull Component message, @NotNull ChatType.Bound boundChatType) {
      this.sendMessage(message, MessageType.CHAT);
   }

   @ForwardingAudienceOverrideNotRequired
   default void sendMessage(@NotNull ComponentLike message, @NotNull ChatType.Bound boundChatType) {
      this.sendMessage(message.asComponent(), boundChatType);
   }

   default void sendMessage(@NotNull SignedMessage signedMessage, @NotNull ChatType.Bound boundChatType) {
      Component content = signedMessage.unsignedContent() != null ? signedMessage.unsignedContent() : Component.text(signedMessage.message());
      if (signedMessage.isSystem()) {
         this.sendMessage((Component)content);
      } else {
         this.sendMessage((Identity)signedMessage.identity(), (Component)content, MessageType.CHAT);
      }

   }

   @ForwardingAudienceOverrideNotRequired
   default void deleteMessage(@NotNull SignedMessage signedMessage) {
      if (signedMessage.canDelete()) {
         this.deleteMessage((SignedMessage.Signature)Objects.requireNonNull(signedMessage.signature()));
      }

   }

   default void deleteMessage(@NotNull SignedMessage.Signature signature) {
   }

   @ForwardingAudienceOverrideNotRequired
   default void sendActionBar(@NotNull ComponentLike message) {
      this.sendActionBar(message.asComponent());
   }

   default void sendActionBar(@NotNull Component message) {
   }

   @ForwardingAudienceOverrideNotRequired
   default void sendPlayerListHeader(@NotNull ComponentLike header) {
      this.sendPlayerListHeader(header.asComponent());
   }

   default void sendPlayerListHeader(@NotNull Component header) {
      this.sendPlayerListHeaderAndFooter((Component)header, (Component)Component.empty());
   }

   @ForwardingAudienceOverrideNotRequired
   default void sendPlayerListFooter(@NotNull ComponentLike footer) {
      this.sendPlayerListFooter(footer.asComponent());
   }

   default void sendPlayerListFooter(@NotNull Component footer) {
      this.sendPlayerListHeaderAndFooter((Component)Component.empty(), (Component)footer);
   }

   @ForwardingAudienceOverrideNotRequired
   default void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
      this.sendPlayerListHeaderAndFooter(header.asComponent(), footer.asComponent());
   }

   default void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
   }

   @ForwardingAudienceOverrideNotRequired
   default void showTitle(@NotNull Title title) {
      Title.Times times = title.times();
      if (times != null) {
         this.sendTitlePart(TitlePart.TIMES, times);
      }

      this.sendTitlePart(TitlePart.SUBTITLE, title.subtitle());
      this.sendTitlePart(TitlePart.TITLE, title.title());
   }

   default <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
   }

   default void clearTitle() {
   }

   default void resetTitle() {
   }

   default void showBossBar(@NotNull BossBar bar) {
   }

   default void hideBossBar(@NotNull BossBar bar) {
   }

   default void playSound(@NotNull Sound sound) {
   }

   default void playSound(@NotNull Sound sound, double x, double y, double z) {
   }

   default void playSound(@NotNull Sound sound, @NotNull Sound.Emitter emitter) {
   }

   @ForwardingAudienceOverrideNotRequired
   default void stopSound(@NotNull Sound sound) {
      this.stopSound(((Sound)Objects.requireNonNull(sound, "sound")).asStop());
   }

   default void stopSound(@NotNull SoundStop stop) {
   }

   @ForwardingAudienceOverrideNotRequired
   default void openBook(@NotNull Book.Builder book) {
      this.openBook(book.build());
   }

   default void openBook(@NotNull Book book) {
   }
}
