package dev.artixdev.api.practice.nametag.listener;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.nametag.NameTagHandler;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class DisguiseListener extends SimplePacketListenerAbstract {
   private final NameTagHandler nameTagHandler = NameTagHandler.getInstance();

   public void onPacketPlaySend(PacketPlaySendEvent event) {
      PacketType.Play.Server type = event.getPacketType();
      if (type == PacketType.Play.Server.RESPAWN) {
         Player player = (Player)event.getPlayer();
         this.nameTagHandler.reloadPlayer(player);
      }

   }
}
