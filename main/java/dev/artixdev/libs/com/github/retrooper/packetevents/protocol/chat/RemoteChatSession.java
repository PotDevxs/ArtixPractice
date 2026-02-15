package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat;

import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.PublicProfileKey;

public class RemoteChatSession {
   private final UUID sessionId;
   private final PublicProfileKey publicProfileKey;

   public RemoteChatSession(UUID sessionId, PublicProfileKey publicProfileKey) {
      this.sessionId = sessionId;
      this.publicProfileKey = publicProfileKey;
   }

   public UUID getSessionId() {
      return this.sessionId;
   }

   public PublicProfileKey getPublicProfileKey() {
      return this.publicProfileKey;
   }
}
