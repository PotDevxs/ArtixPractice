package dev.artixdev.libs.com.github.retrooper.packetevents.injector;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;

public interface ChannelInjector {
   default boolean isServerBound() {
      return true;
   }

   void inject();

   void uninject();

   void updateUser(Object var1, User var2);

   void setPlayer(Object var1, Object var2);

   boolean isProxy();
}
