package dev.artixdev.libs.com.github.retrooper.packetevents.event;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;

public class UserDisconnectEvent extends PacketEvent implements UserEvent {
   private final User user;

   public UserDisconnectEvent(User user) {
      this.user = user;
   }

   public User getUser() {
      return this.user;
   }

   public void call(PacketListenerCommon listener) {
      listener.onUserDisconnect(this);
   }
}
