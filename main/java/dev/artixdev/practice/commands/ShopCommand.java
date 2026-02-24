package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.menus.ShopMenu;

@Register(name = "shop")
public class ShopCommand {

    private final Main plugin = Main.getInstance();

    @Command(name = "", aliases = {"open"}, desc = "Open the coin shop")
    public void openShop(@Sender Player player) {
        if (player == null) return;
        MenuHandler.getInstance().openMenu(new ShopMenu(), player);
    }
}
