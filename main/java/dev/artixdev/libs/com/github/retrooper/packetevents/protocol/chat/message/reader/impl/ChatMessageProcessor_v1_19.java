package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.impl;

import java.time.Instant;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public class ChatMessageProcessor_v1_19 implements ChatMessageProcessor {
   public ChatMessage readChatMessage(@NotNull PacketWrapper<?> wrapper) {
      Component chatContent = wrapper.readComponent();
      Component unsignedChatContent = (Component)wrapper.readOptional(PacketWrapper::readComponent);
      int id = wrapper.readVarInt();
      ChatType type = ChatTypes.getById(wrapper.getServerVersion().toClientVersion(), id);
      UUID senderUUID = wrapper.readUUID();
      Component senderDisplayName = wrapper.readComponent();
      Component teamName = (Component)wrapper.readOptional(PacketWrapper::readComponent);
      Instant timestamp = wrapper.readTimestamp();
      long salt = wrapper.readLong();
      byte[] signature = wrapper.readByteArray();
      return new ChatMessage_v1_19(chatContent, unsignedChatContent, type, senderUUID, senderDisplayName, teamName, timestamp, salt, signature);
   }

   public void writeChatMessage(@NotNull PacketWrapper<?> wrapper, @NotNull ChatMessage data) {
      ChatMessage_v1_19 newData = (ChatMessage_v1_19)data;
      wrapper.writeComponent(newData.getChatContent());
      wrapper.writeOptional(newData.getUnsignedChatContent(), PacketWrapper::writeComponent);
      wrapper.writeVarInt(newData.getType().getId(wrapper.getServerVersion().toClientVersion()));
      wrapper.writeUUID(newData.getSenderUUID());
      wrapper.writeComponent(newData.getSenderDisplayName());
      wrapper.writeOptional(newData.getTeamName(), PacketWrapper::writeComponent);
      wrapper.writeTimestamp(newData.getTimestamp());
      wrapper.writeLong(newData.getSalt());
      wrapper.writeByteArray(newData.getSignature());
   }
}
