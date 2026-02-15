package dev.artixdev.api.practice.nametag.packet;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import java.util.Collection;
import dev.artixdev.api.practice.nametag.NameTagHandler;
import dev.artixdev.api.practice.nametag.util.ColorUtil;
import dev.artixdev.api.practice.nametag.util.VersionUtil;

public class ScoreboardPacket {
   private final NameTagHandler nameTagHandler = NameTagHandler.getInstance();
   private final PacketContainer container;

   public ScoreboardPacket(String name, String prefix, String suffix) {
      this.container = new PacketContainer(Server.SCOREBOARD_TEAM);
      this.setCreateFields(name, prefix, suffix);
   }

   public ScoreboardPacket(String name, Collection<String> players) {
      this.container = new PacketContainer(Server.SCOREBOARD_TEAM);
      this.setAddFields(name, players);
   }

   private void setCreateFields(String name, String prefix, String suffix) {
      String collisionRule = this.nameTagHandler.isCollisionEnabled() ? "always" : "never";
      if (prefix.length() > 16) {
         prefix = prefix.substring(0, 16);
      }

      if (suffix.length() > 16) {
         suffix = suffix.substring(0, 16);
      }

      this.container.getModifier().writeDefaults();
      switch(VersionUtil.MINOR_VERSION) {
      case 8:
         this.container.getStrings().writeSafely(0, name).writeSafely(1, name).writeSafely(2, ColorUtil.color(prefix)).writeSafely(3, ColorUtil.color(suffix)).writeSafely(4, "always");
         this.container.getIntegers().writeSafely(1, 0).writeSafely(2, 1);
         break;
      case 12:
         this.container.getStrings().writeSafely(0, name).writeSafely(1, name).writeSafely(2, ColorUtil.color(prefix)).writeSafely(3, ColorUtil.color(prefix)).writeSafely(4, "always").writeSafely(5, collisionRule);
         this.container.getIntegers().writeSafely(0, -1).writeSafely(1, 0).writeSafely(2, 1);
         break;
      default:
         if (!VersionUtil.canHex()) {
            throw new IllegalArgumentException("[NameTagAPI] Version not supported!");
         }

         throw new IllegalStateException("[NameTagAPI] How did you get here?!");
      }

   }

   private void setAddFields(String name, Collection<String> players) {
      String collisionRule = this.nameTagHandler.isCollisionEnabled() ? "always" : "never";
      this.container.getModifier().writeDefaults();
      switch(VersionUtil.MINOR_VERSION) {
      case 8:
         this.container.getStrings().writeSafely(0, name);
         this.container.getIntegers().writeSafely(1, 3);
         this.container.getSpecificModifier(Collection.class).writeSafely(0, players);
         break;
      case 12:
         this.container.getStrings().writeSafely(0, name).writeSafely(4, "always").writeSafely(5, collisionRule);
         this.container.getIntegers().writeSafely(0, -1).writeSafely(1, 3);
         this.container.getSpecificModifier(Collection.class).writeSafely(0, players);
         break;
      default:
         if (!VersionUtil.canHex()) {
            throw new IllegalArgumentException("[NameTagAPI] Version not supported!");
         }

         throw new IllegalStateException("[NameTagAPI] How did you get here?!");
      }

   }

   public NameTagHandler getNameTagHandler() {
      return this.nameTagHandler;
   }

   public PacketContainer getContainer() {
      return this.container;
   }
}
