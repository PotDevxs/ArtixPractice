package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.impl;

import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public class ChatMessageProcessor_v1_16 implements ChatMessageProcessor {
   public ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper) {
      Component chatContent = wrapper.readComponent();
      int id = wrapper.readByte();
      ChatType type = ChatTypes.getById(wrapper.getServerVersion().toClientVersion(), id);
      UUID senderUUID = wrapper.readUUID();
      return new ChatMessage_v1_16(chatContent, type, senderUUID);
   }

   public void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data) {
      wrapper.writeComponent(data.getChatContent());
      wrapper.writeByte(data.getType().getId(wrapper.getServerVersion().toClientVersion()));
      wrapper.writeUUID(((ChatMessage_v1_16)data).getSenderUUID());
   }
}
