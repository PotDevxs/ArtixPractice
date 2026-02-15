package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatType;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class ChatMessage {
   private Component chatContent;
   private ChatType type;

   protected ChatMessage(Component chatContent, ChatType type) {
      this.chatContent = chatContent;
      this.type = type;
   }

   public Component getChatContent() {
      return this.chatContent;
   }

   public ChatType getType() {
      return this.type;
   }

   public void setChatContent(Component chatContent) {
      this.chatContent = chatContent;
   }

   public void setType(ChatType type) {
      this.type = type;
   }
}
