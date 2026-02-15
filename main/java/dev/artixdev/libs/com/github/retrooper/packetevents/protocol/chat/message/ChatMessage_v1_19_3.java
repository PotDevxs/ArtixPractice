package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class ChatMessage_v1_19_3 extends ChatMessage_v1_16 {
   int index;
   byte[] signature;
   String plainContent;
   Instant timestamp;
   long salt;
   LastSeenMessages.Packed lastSeenMessagesPacked;
   @Nullable
   Component unsignedChatContent;
   FilterMask filterMask;
   ChatMessage_v1_19_1.ChatTypeBoundNetwork chatType;

   public ChatMessage_v1_19_3(UUID senderUUID, int index, byte[] signature, String plainContent, Instant timestamp, long salt, LastSeenMessages.Packed lastSeenMessagesPacked, @Nullable Component unsignedChatContent, FilterMask filterMask, ChatMessage_v1_19_1.ChatTypeBoundNetwork chatType) {
      super(Component.text(plainContent), chatType.getType(), senderUUID);
      this.index = index;
      this.signature = signature;
      this.plainContent = plainContent;
      this.timestamp = timestamp;
      this.salt = salt;
      this.lastSeenMessagesPacked = lastSeenMessagesPacked;
      this.unsignedChatContent = unsignedChatContent;
      this.filterMask = filterMask;
      this.chatType = chatType;
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public byte[] getSignature() {
      return this.signature;
   }

   public void setSignature(byte[] signature) {
      this.signature = signature;
   }

   public Component getChatContent() {
      return Component.text(this.plainContent);
   }

   /** @deprecated */
   @Deprecated
   public void setChatContent(Component chatContent) {
      throw new UnsupportedOperationException("PacketEvents is not able to serialize components to plain-text. Please use the #setPlainContent instead to update the content.");
   }

   public String getPlainContent() {
      return this.plainContent;
   }

   public void setPlainContent(String plainContent) {
      this.plainContent = plainContent;
   }

   public Instant getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(Instant timestamp) {
      this.timestamp = timestamp;
   }

   public long getSalt() {
      return this.salt;
   }

   public void setSalt(long salt) {
      this.salt = salt;
   }

   public LastSeenMessages.Packed getLastSeenMessagesPacked() {
      return this.lastSeenMessagesPacked;
   }

   public void setLastSeenMessagesPacked(LastSeenMessages.Packed lastSeenMessagesPacked) {
      this.lastSeenMessagesPacked = lastSeenMessagesPacked;
   }

   public Optional<Component> getUnsignedChatContent() {
      return Optional.ofNullable(this.unsignedChatContent);
   }

   public void setUnsignedChatContent(@Nullable Component unsignedChatContent) {
      this.unsignedChatContent = unsignedChatContent;
   }

   public FilterMask getFilterMask() {
      return this.filterMask;
   }

   public void setFilterMask(FilterMask filterMask) {
      this.filterMask = filterMask;
   }

   public ChatMessage_v1_19_1.ChatTypeBoundNetwork getChatType() {
      return this.chatType;
   }

   public void setChatType(ChatMessage_v1_19_1.ChatTypeBoundNetwork chatType) {
      this.chatType = chatType;
   }
}
