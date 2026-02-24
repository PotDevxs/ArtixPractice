package dev.artixdev.practice.configs;

import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.storage.impl.BasicYamlStorage;

public class HotbarConfig extends BasicYamlStorage {
   public HotbarConfig(JavaPlugin plugin, String name) {
      super(plugin, name, true);
   }

   public void addSeparateComments() {
      this.addCommentWithBlankLine("CUSTOM_ITEMS", "Custom Items that can be anything and have any command with any layout.\nThis is the format, any and all spaces must be exactly the same to follow indentation.\n\nLAYOUT_TYPE:\n   ANY_NAME:\n      NAME: \"&cTeleport to Spawn &7(Right-Click)\"\n      ENABLED: true\n      COMMAND: \"spawn\" (You can also put a built-in action here)\n      MATERIAL: DIAMOND\n      DURABILITY: 0\n      SLOT: 0\n      LAYOUT_TYPE: LOBBY\n      LORE: []\n");
   }

   public String[] getHeader() {
      return new String[]{"Layout Types: LOBBY, QUEUE, PARTY_LEADER (Leader Only), PARTY_MEMBER (Member Only), PARTY (Both), SPECTATING, EVENT, OTHER (For built-in items)", "ACTION is optional and it's field can be removed if need be. It can also be replaced by COMMAND followed by command without '/'.", "You need help with the configuration or have any questions related to Artix?", "Join us in our Discord: https://dsc.gg/artix", null, "NOTE: All Material IDs are supported from 1.8x-1.20.x. Recommended: https://minecraftitemids.com/"};
   }
}
