package dev.artixdev.libs.com.github.retrooper.packetevents.event;

public abstract class PacketListenerCommon {
   private final PacketListenerPriority priority;

   public PacketListenerCommon(PacketListenerPriority priority) {
      this.priority = priority;
   }

   public PacketListenerCommon() {
      this.priority = PacketListenerPriority.NORMAL;
   }

   public PacketListenerPriority getPriority() {
      return this.priority;
   }

   public void onUserConnect(UserConnectEvent event) {
   }

   public void onUserLogin(UserLoginEvent event) {
   }

   public void onUserDisconnect(UserDisconnectEvent event) {
   }

   void onPacketReceive(PacketReceiveEvent event) {
   }

   void onPacketSend(PacketSendEvent event) {
   }

   public void onPacketEventExternal(PacketEvent event) {
   }
}
