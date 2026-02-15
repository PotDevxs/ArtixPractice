package dev.artixdev.libs.com.github.retrooper.packetevents.event;

public interface CancellableEvent {
   boolean isCancelled();

   void setCancelled(boolean cancelled);
}
