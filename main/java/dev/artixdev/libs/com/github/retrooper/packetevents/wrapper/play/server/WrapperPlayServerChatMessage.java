package dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.ChatMessageProcessor;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessorLegacy;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_16;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_19;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_19_1;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.message.reader.impl.ChatMessageProcessor_v1_19_3;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;

public class WrapperPlayServerChatMessage extends PacketWrapper<WrapperPlayServerChatMessage> {
   private static final ChatMessageProcessor CHAT_LEGACY_PROCESSOR = new ChatMessageProcessorLegacy();
   private static final ChatMessageProcessor CHAT_V1_16_PROCESSOR = new ChatMessageProcessor_v1_16();
   private static final ChatMessageProcessor CHAT_V1_19_PROCESSOR = new ChatMessageProcessor_v1_19();
   private static final ChatMessageProcessor CHAT_V1_19_1_PROCESSOR = new ChatMessageProcessor_v1_19_1();
   private static final ChatMessageProcessor CHAT_V1_19_3_PROCESSOR = new ChatMessageProcessor_v1_19_3();
   private ChatMessage message;

   public WrapperPlayServerChatMessage(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerChatMessage(ChatMessage message) {
      super((PacketTypeCommon)PacketType.Play.Server.CHAT_MESSAGE);
      this.message = message;
   }

   public void read() {
      this.message = this.getProcessor().readChatMessage(this);
   }

   public void write() {
      this.getProcessor().writeChatMessage(this, this.message);
   }

   public void copy(WrapperPlayServerChatMessage wrapper) {
      this.message = wrapper.message;
   }

   public ChatMessage getMessage() {
      return this.message;
   }

   public void setMessage(ChatMessage message) {
      this.message = message;
   }

   @ApiStatus.Internal
   protected ChatMessageProcessor getProcessor() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         return CHAT_V1_19_3_PROCESSOR;
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
         return CHAT_V1_19_1_PROCESSOR;
      } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
         return CHAT_V1_19_PROCESSOR;
      } else {
         return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16) ? CHAT_V1_16_PROCESSOR : CHAT_LEGACY_PROCESSOR;
      }
   }
}
