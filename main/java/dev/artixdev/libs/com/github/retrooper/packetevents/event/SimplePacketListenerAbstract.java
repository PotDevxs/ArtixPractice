package dev.artixdev.libs.com.github.retrooper.packetevents.event;

import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketConfigReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketConfigSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketHandshakeReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketLoginReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketLoginSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketStatusReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketStatusSendEvent;

public abstract class SimplePacketListenerAbstract extends PacketListenerCommon {
   public SimplePacketListenerAbstract(PacketListenerPriority priority) {
      super(priority);
   }

   public SimplePacketListenerAbstract() {
   }

   void onPacketReceive(PacketReceiveEvent event) {
      if (event instanceof PacketHandshakeReceiveEvent) {
         this.onPacketHandshakeReceive((PacketHandshakeReceiveEvent)event);
      } else if (event instanceof PacketStatusReceiveEvent) {
         this.onPacketStatusReceive((PacketStatusReceiveEvent)event);
      } else if (event instanceof PacketLoginReceiveEvent) {
         this.onPacketLoginReceive((PacketLoginReceiveEvent)event);
      } else if (event instanceof PacketConfigReceiveEvent) {
         this.onPacketConfigReceive((PacketConfigReceiveEvent)event);
      } else if (event instanceof PacketPlayReceiveEvent) {
         this.onPacketPlayReceive((PacketPlayReceiveEvent)event);
      }

   }

   void onPacketSend(PacketSendEvent event) {
      if (event instanceof PacketStatusSendEvent) {
         this.onPacketStatusSend((PacketStatusSendEvent)event);
      } else if (event instanceof PacketLoginSendEvent) {
         this.onPacketLoginSend((PacketLoginSendEvent)event);
      } else if (event instanceof PacketConfigSendEvent) {
         this.onPacketConfigSend((PacketConfigSendEvent)event);
      } else if (event instanceof PacketPlaySendEvent) {
         this.onPacketPlaySend((PacketPlaySendEvent)event);
      }

   }

   public void onPacketHandshakeReceive(PacketHandshakeReceiveEvent event) {
   }

   public void onPacketStatusReceive(PacketStatusReceiveEvent event) {
   }

   public void onPacketStatusSend(PacketStatusSendEvent event) {
   }

   public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
   }

   public void onPacketLoginSend(PacketLoginSendEvent event) {
   }

   public void onPacketConfigReceive(PacketConfigReceiveEvent event) {
   }

   public void onPacketConfigSend(PacketConfigSendEvent event) {
   }

   public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
   }

   public void onPacketPlaySend(PacketPlaySendEvent event) {
   }
}
