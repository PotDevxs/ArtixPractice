package dev.artixdev.libs.net.kyori.adventure.audience;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import dev.artixdev.libs.net.kyori.adventure.chat.ChatType;
import dev.artixdev.libs.net.kyori.adventure.chat.SignedMessage;
import dev.artixdev.libs.net.kyori.adventure.identity.Identified;
import dev.artixdev.libs.net.kyori.adventure.identity.Identity;
import dev.artixdev.libs.net.kyori.adventure.inventory.Book;
import dev.artixdev.libs.net.kyori.adventure.pointer.Pointer;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.ComponentLike;
import dev.artixdev.libs.org.jetbrains.annotations.Contract;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;
import dev.artixdev.libs.org.jetbrains.annotations.UnknownNullability;

final class EmptyAudience implements Audience {
   static final EmptyAudience INSTANCE = new EmptyAudience();

   @NotNull
   public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
      return Optional.empty();
   }

   @Contract("_, null -> null; _, !null -> !null")
   @Nullable
   public <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
      return defaultValue;
   }

   @UnknownNullability
   public <T> T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
      return defaultValue.get();
   }

   @NotNull
   public Audience filterAudience(@NotNull Predicate<? super Audience> filter) {
      return this;
   }

   public void forEachAudience(@NotNull Consumer<? super Audience> action) {
   }

   public void sendMessage(@NotNull ComponentLike message) {
   }

   public void sendMessage(@NotNull Component message) {
   }

   /** @deprecated */
   @Deprecated
   public void sendMessage(@NotNull Identified source, @NotNull Component message, @NotNull MessageType type) {
   }

   /** @deprecated */
   @Deprecated
   public void sendMessage(@NotNull Identity source, @NotNull Component message, @NotNull MessageType type) {
   }

   public void sendMessage(@NotNull Component message, @NotNull ChatType.Bound boundChatType) {
   }

   public void sendMessage(@NotNull SignedMessage signedMessage, @NotNull ChatType.Bound boundChatType) {
   }

   public void deleteMessage(@NotNull SignedMessage.Signature signature) {
   }

   public void sendActionBar(@NotNull ComponentLike message) {
   }

   public void sendPlayerListHeader(@NotNull ComponentLike header) {
   }

   public void sendPlayerListFooter(@NotNull ComponentLike footer) {
   }

   public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
   }

   public void openBook(@NotNull Book.Builder book) {
   }

   public boolean equals(Object that) {
      return this == that;
   }

   public int hashCode() {
      return 0;
   }

   public String toString() {
      return "EmptyAudience";
   }
}
