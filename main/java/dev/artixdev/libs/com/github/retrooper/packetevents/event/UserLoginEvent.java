package dev.artixdev.libs.com.github.retrooper.packetevents.event;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;

public class UserLoginEvent extends PacketEvent implements CallableEvent, PlayerEvent<Object>, UserEvent {
   private final User user;
   private final Object player;

   public UserLoginEvent(User user, Object player) {
      this.user = user;
      this.player = player;
   }

   public User getUser() {
      return this.user;
   }

   public Object getPlayer() {
      return this.player;
   }

   public void call(PacketListenerCommon listener) {
      listener.onUserLogin(this);
   }
}
