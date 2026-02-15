package dev.artixdev.api.practice.command.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.util.CC;

public class DrinkHelpService {
   private final DrinkCommandService commandService;
   private HelpFormatter helpFormatter;

   public DrinkHelpService(DrinkCommandService commandService) {
      this.commandService = commandService;
      this.helpFormatter = (sender, container) -> {
         String containerName = container.getName();
         String formattedName = containerName.substring(0, 1).toUpperCase() + containerName.substring(1);
         sender.sendMessage(CC.CHAT_BAR);
         sender.sendMessage(CC.translate("&c&l" + formattedName + " Help"));
         sender.sendMessage("");
         List<DrinkCommand> commands = new ArrayList(container.getCommands().values());
         Collections.reverse(commands);
         Iterator var5;
         DrinkCommand c;
         if (!(sender instanceof Player)) {
            var5 = commands.iterator();

            while(var5.hasNext()) {
               c = (DrinkCommand)var5.next();
               sender.sendMessage(CC.translate(" &7* &c/" + container.getName() + (c.getName().length() > 0 ? " &c" + c.getName() : "") + (c.getMostApplicableUsage().length() > 0 ? " &7" + c.getMostApplicableUsage() : "") + " - &7" + c.getDescription()));
            }

            sender.sendMessage(CC.CHAT_BAR);
         } else {
            var5 = commands.iterator();

            while(var5.hasNext()) {
               c = (DrinkCommand)var5.next();
               String text = CC.translate(" &7* &c/" + container.getName() + (c.getName().length() > 0 ? " &c" + c.getName() : "") + (c.getMostApplicableUsage().length() > 0 ? " &7" + c.getMostApplicableUsage() : "") + " &7- " + c.getDescription());
               String hover = ChatColor.GRAY + "/" + container.getName() + " " + c.getName() + " - " + ChatColor.WHITE + c.getDescription();
               String command = "/" + container.getName() + " " + c.getName();
               TextComponent msg = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text));
               msg.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)));
               msg.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, command));
               Player player = (Player)sender;
               player.spigot().sendMessage(msg);
            }

            sender.sendMessage(CC.CHAT_BAR);
         }
      };
   }

   public void sendHelpFor(CommandSender sender, DrinkCommandContainer container) {
      this.helpFormatter.sendHelpFor(sender, container);
   }

   public void sendUsageMessage(CommandSender sender, DrinkCommandContainer container, DrinkCommand command) {
      sender.sendMessage(this.getUsageMessage(container, command));
   }

   public String getUsageMessage(DrinkCommandContainer container, DrinkCommand command) {
      String usage = ChatColor.GRAY + "Usage: /" + container.getName() + " ";
      if (command.getName().length() > 0) {
         usage = usage + command.getName() + " ";
      }

      if (command.getUsage() != null && command.getUsage().length() > 0) {
         usage = usage + command.getUsage();
      } else {
         usage = usage + command.getGeneratedUsage();
      }

      return usage;
   }

   public DrinkCommandService getCommandService() {
      return this.commandService;
   }

   public HelpFormatter getHelpFormatter() {
      return this.helpFormatter;
   }

   public void setHelpFormatter(HelpFormatter helpFormatter) {
      this.helpFormatter = helpFormatter;
   }
}
