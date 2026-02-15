package me.drizzy.api.hider;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractPacketListener extends PacketAdapter {
   public AbstractPacketListener(JavaPlugin plugin, PacketType... packetTypes) {
      super(plugin, packetTypes);
   }

   public abstract Player getPlayerWhoDropped(Item var1);
}
