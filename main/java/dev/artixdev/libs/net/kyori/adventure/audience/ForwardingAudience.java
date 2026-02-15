package dev.artixdev.libs.net.kyori.adventure.audience;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import dev.artixdev.libs.net.kyori.adventure.bossbar.BossBar;
import dev.artixdev.libs.net.kyori.adventure.chat.ChatType;
import dev.artixdev.libs.net.kyori.adventure.chat.SignedMessage;
import dev.artixdev.libs.net.kyori.adventure.identity.Identified;
import dev.artixdev.libs.net.kyori.adventure.identity.Identity;
import dev.artixdev.libs.net.kyori.adventure.inventory.Book;
import dev.artixdev.libs.net.kyori.adventure.pointer.Pointer;
import dev.artixdev.libs.net.kyori.adventure.pointer.Pointers;
import dev.artixdev.libs.net.kyori.adventure.sound.Sound;
import dev.artixdev.libs.net.kyori.adventure.sound.SoundStop;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.title.TitlePart;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.UnknownNullability;

@FunctionalInterface
public interface ForwardingAudience extends Audience {
   @ApiStatus.OverrideOnly
   @NotNull
   Iterable<? extends Audience> audiences();

   @NotNull
   default Pointers pointers() {
      return Pointers.empty();
   }

   @NotNull
   default Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
      List<Audience> audiences = null;
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         if (filter.test(audience)) {
            Audience filtered = audience.filterAudience(filter);
            if (filtered != Audience.empty()) {
               if (audiences == null) {
                  audiences = new ArrayList();
               }

               audiences.add(filtered);
            }
         }
      }

      return (Audience)(audiences != null ? Audience.audience((Iterable)audiences) : Audience.empty());
   }

   default void forEachAudience(@NotNull Consumer<? super Audience> action) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.forEachAudience(action);
      }

   }

   default void sendMessage(@NotNull Component message) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendMessage(message);
      }

   }

   default void sendMessage(@NotNull Component message, @NotNull ChatType.Bound boundChatType) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.sendMessage(message, boundChatType);
      }

   }

   default void sendMessage(@NotNull SignedMessage signedMessage, @NotNull ChatType.Bound boundChatType) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.sendMessage(signedMessage, boundChatType);
      }

   }

   default void deleteMessage(@NotNull SignedMessage.Signature signature) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.deleteMessage(signature);
      }

   }

   /** @deprecated */
   @Deprecated
   default void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
      Iterator var4 = this.audiences().iterator();

      while(var4.hasNext()) {
         Audience audience = (Audience)var4.next();
         audience.sendMessage(source, message, type);
      }

   }

   /** @deprecated */
   @Deprecated
   default void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
      Iterator var4 = this.audiences().iterator();

      while(var4.hasNext()) {
         Audience audience = (Audience)var4.next();
         audience.sendMessage(source, message, type);
      }

   }

   default void sendActionBar(@NotNull Component message) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendActionBar(message);
      }

   }

   default void sendPlayerListHeader(@NotNull Component header) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendPlayerListHeader(header);
      }

   }

   default void sendPlayerListFooter(@NotNull Component footer) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.sendPlayerListFooter(footer);
      }

   }

   default void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.sendPlayerListHeaderAndFooter(header, footer);
      }

   }

   default <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.sendTitlePart(part, value);
      }

   }

   default void clearTitle() {
      Iterator var1 = this.audiences().iterator();

      while(var1.hasNext()) {
         Audience audience = (Audience)var1.next();
         audience.clearTitle();
      }

   }

   default void resetTitle() {
      Iterator var1 = this.audiences().iterator();

      while(var1.hasNext()) {
         Audience audience = (Audience)var1.next();
         audience.resetTitle();
      }

   }

   default void showBossBar(@NotNull BossBar bar) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.showBossBar(bar);
      }

   }

   default void hideBossBar(@NotNull BossBar bar) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.hideBossBar(bar);
      }

   }

   default void playSound(@NotNull Sound sound) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.playSound(sound);
      }

   }

   default void playSound(@NotNull Sound sound, double x, double y, double z) {
      Iterator var8 = this.audiences().iterator();

      while(var8.hasNext()) {
         Audience audience = (Audience)var8.next();
         audience.playSound(sound, x, y, z);
      }

   }

   default void playSound(@NotNull Sound sound, @NotNull Sound.Emitter emitter) {
      Iterator var3 = this.audiences().iterator();

      while(var3.hasNext()) {
         Audience audience = (Audience)var3.next();
         audience.playSound(sound, emitter);
      }

   }

   default void stopSound(@NotNull SoundStop stop) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.stopSound(stop);
      }

   }

   default void openBook(@NotNull Book book) {
      Iterator var2 = this.audiences().iterator();

      while(var2.hasNext()) {
         Audience audience = (Audience)var2.next();
         audience.openBook(book);
      }

   }

   public interface Single extends ForwardingAudience {
      @ApiStatus.OverrideOnly
      @NotNull
      Audience audience();

      /** @deprecated */
      @Deprecated
      @NotNull
      default Iterable<? extends Audience> audiences() {
         return Collections.singleton(this.audience());
      }

      @NotNull
      default <T> Optional<T> get(@NotNull Pointer<T> pointer) {
         return this.audience().get(pointer);
      }

      @Contract("_, null -> null; _, !null -> !null")
      @Nullable
      default <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
         return this.audience().getOrDefault(pointer, defaultValue);
      }

      @UnknownNullability
      default <T> T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
         return this.audience().getOrDefaultFrom(pointer, defaultValue);
      }

      @NotNull
      default Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
         Audience audience = this.audience();
         return (Audience)(filter.test(audience) ? this : Audience.empty());
      }

      default void forEachAudience(@NotNull Consumer<? super Audience> action) {
         this.audience().forEachAudience(action);
      }

      @NotNull
      default Pointers pointers() {
         return this.audience().pointers();
      }

      default void sendMessage(@NotNull Component message) {
         this.audience().sendMessage(message);
      }

      default void sendMessage(@NotNull Component message, @NotNull ChatType.Bound boundChatType) {
         this.audience().sendMessage(message, boundChatType);
      }

      default void sendMessage(@NotNull SignedMessage signedMessage, @NotNull ChatType.Bound boundChatType) {
         this.audience().sendMessage(signedMessage, boundChatType);
      }

      default void deleteMessage(@NotNull SignedMessage.Signature signature) {
         this.audience().deleteMessage(signature);
      }

      /** @deprecated */
      @Deprecated
      default void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
         this.audience().sendMessage(source, message, type);
      }

      /** @deprecated */
      @Deprecated
      default void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
         this.audience().sendMessage(source, message, type);
      }

      default void sendActionBar(@NotNull Component message) {
         this.audience().sendActionBar(message);
      }

      default void sendPlayerListHeader(@NotNull Component header) {
         this.audience().sendPlayerListHeader(header);
      }

      default void sendPlayerListFooter(@NotNull Component footer) {
         this.audience().sendPlayerListFooter(footer);
      }

      default void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
         this.audience().sendPlayerListHeaderAndFooter(header, footer);
      }

      default <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
         this.audience().sendTitlePart(part, value);
      }

      default void clearTitle() {
         this.audience().clearTitle();
      }

      default void resetTitle() {
         this.audience().resetTitle();
      }

      default void showBossBar(@NotNull BossBar bar) {
         this.audience().showBossBar(bar);
      }

      default void hideBossBar(@NotNull BossBar bar) {
         this.audience().hideBossBar(bar);
      }

      default void playSound(@NotNull Sound sound) {
         this.audience().playSound(sound);
      }

      default void playSound(@NotNull Sound sound, double x, double y, double z) {
         this.audience().playSound(sound, x, y, z);
      }

      default void playSound(@NotNull Sound sound, @NotNull Sound.Emitter emitter) {
         this.audience().playSound(sound, emitter);
      }

      default void stopSound(@NotNull SoundStop stop) {
         this.audience().stopSound(stop);
      }

      default void openBook(@NotNull Book book) {
         this.audience().openBook(book);
      }
   }
}
