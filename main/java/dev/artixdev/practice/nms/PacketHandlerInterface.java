package dev.artixdev.practice.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.List;

public interface PacketHandlerInterface {
    
    void sendPacket(PacketType packetType, Location location, Player player);
    
    void sendPacket(PacketType packetType, Location location, List<Player> players);
    
    void sendPacket(PacketType packetType, Location location, double radius);
}
