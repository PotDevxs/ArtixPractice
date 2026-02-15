package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatType;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class ChatMessageLegacy extends ChatMessage {
   public ChatMessageLegacy(Component chatContent, ChatType type) {
      super(chatContent, type);
   }
}
