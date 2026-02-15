package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public interface ChatMessageProcessor {
   ChatMessage readChatMessage(@NotNull PacketWrapper<?> var1);

   void writeChatMessage(@NotNull PacketWrapper<?> var1, @NotNull ChatMessage var2);
}
