package dev.artixdev.libs.com.github.retrooper.packetevents.event;

public abstract class PacketListenerAbstract extends PacketListenerCommon {
   public PacketListenerAbstract(PacketListenerPriority priority) {
      super(priority);
   }

   public PacketListenerAbstract() {
   }

   public void onPacketReceive(PacketReceiveEvent event) {
   }

   public void onPacketSend(PacketSendEvent event) {
   }
}
