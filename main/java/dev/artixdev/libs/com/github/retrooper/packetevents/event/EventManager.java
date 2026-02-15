package dev.artixdev.libs.com.github.retrooper.packetevents.event;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class EventManager {
   private final Map<Byte, Set<PacketListenerCommon>> listenersMap = new ConcurrentHashMap();

   public void callEvent(PacketEvent event) {
      this.callEvent(event, (Runnable)null);
   }

   public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {
      for(byte priority = PacketListenerPriority.LOWEST.getId(); priority <= PacketListenerPriority.MONITOR.getId(); ++priority) {
         Set<PacketListenerCommon> listeners = (Set)this.listenersMap.get(priority);
         if (listeners != null) {
            Iterator<PacketListenerCommon> listenerIterator = listeners.iterator();

            while (listenerIterator.hasNext()) {
               PacketListenerCommon listener = listenerIterator.next();

               try {
                  event.call(listener);
               } catch (Exception e) {
                  if (e.getClass() != InvalidHandshakeException.class) {
                     PacketEvents.getAPI().getLogger().log(Level.WARNING, "PacketEvents caught an unhandled exception while calling your listener.", e);
                  }
               }

               if (postCallListenerAction != null) {
                  postCallListenerAction.run();
               }
            }
         }
      }

      if (event instanceof ProtocolPacketEvent && !((ProtocolPacketEvent)event).needsReEncode()) {
         ((ProtocolPacketEvent)event).setLastUsedWrapper((PacketWrapper)null);
      }

   }

   public PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority) {
      PacketListenerCommon packetListenerAbstract = listener.asAbstract(priority);
      return this.registerListener(packetListenerAbstract);
   }

   public PacketListenerCommon registerListener(PacketListenerCommon listener) {
      byte priority = listener.getPriority().getId();
      Set<PacketListenerCommon> listenerSet = (Set)this.listenersMap.get(priority);
      if (listenerSet == null) {
         listenerSet = ConcurrentHashMap.newKeySet();
      }

      ((Set)listenerSet).add(listener);
      this.listenersMap.put(priority, listenerSet);
      return listener;
   }

   public PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners) {
      for (PacketListenerCommon listener : listeners) {
         this.registerListener(listener);
      }

      return listeners;
   }

   public void unregisterListener(PacketListenerCommon listener) {
      Set<PacketListenerCommon> listenerSet = (Set)this.listenersMap.get(listener.getPriority().getId());
      if (listenerSet != null) {
         listenerSet.remove(listener);
      }
   }

   public void unregisterListeners(PacketListenerCommon... listeners) {
      for (PacketListenerCommon listener : listeners) {
         this.unregisterListener(listener);
      }

   }

   public void unregisterAllListeners() {
      this.listenersMap.clear();
   }
}
