package dev.artixdev.libs.net.kyori.adventure.chat;

import dev.artixdev.libs.net.kyori.adventure.internal.Internals;
import dev.artixdev.libs.net.kyori.adventure.key.Key;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

final class ChatTypeImpl implements ChatType {
   private final Key key;

   ChatTypeImpl(@NotNull Key key) {
      this.key = key;
   }

   @NotNull
   public Key key() {
      return this.key;
   }

   public String toString() {
      return Internals.toString(this);
   }

   static final class BoundImpl implements ChatType.Bound {
      private final ChatType chatType;
      private final Component name;
      @Nullable
      private final Component target;

      BoundImpl(ChatType chatType, Component name, @Nullable Component target) {
         this.chatType = chatType;
         this.name = name;
         this.target = target;
      }

      @NotNull
      public ChatType type() {
         return this.chatType;
      }

      @NotNull
      public Component name() {
         return this.name;
      }

      @Nullable
      public Component target() {
         return this.target;
      }

      public String toString() {
         return Internals.toString(this);
      }
   }
}
