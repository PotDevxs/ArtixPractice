package dev.artixdev.libs.com.github.retrooper.packetevents.event;

public interface PacketListener {
   default PacketListenerAbstract asAbstract(PacketListenerPriority priority) {
      return new PacketListenerAbstract(priority) {
         public void onUserConnect(UserConnectEvent event) {
            PacketListener.this.onUserConnect(event);
         }

         public void onUserLogin(UserLoginEvent event) {
            PacketListener.this.onUserLogin(event);
         }

         public void onUserDisconnect(UserDisconnectEvent event) {
            PacketListener.this.onUserDisconnect(event);
         }

         public void onPacketReceive(PacketReceiveEvent event) {
            PacketListener.this.onPacketReceive(event);
         }

         public void onPacketSend(PacketSendEvent event) {
            PacketListener.this.onPacketSend(event);
         }

         public void onPacketEventExternal(PacketEvent event) {
            PacketListener.this.onPacketEventExternal(event);
         }
      };
   }

   default void onUserConnect(UserConnectEvent event) {
   }

   default void onUserLogin(UserLoginEvent event) {
   }

   default void onUserDisconnect(UserDisconnectEvent event) {
   }

   default void onPacketReceive(PacketReceiveEvent event) {
   }

   default void onPacketSend(PacketSendEvent event) {
   }

   default void onPacketEventExternal(PacketEvent event) {
   }
}
