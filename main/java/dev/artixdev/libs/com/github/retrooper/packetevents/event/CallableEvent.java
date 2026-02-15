package dev.artixdev.libs.com.github.retrooper.packetevents.event;

public interface CallableEvent {
   default void call(PacketListenerCommon listener) {
      listener.onPacketEventExternal((PacketEvent)this);
   }
}
