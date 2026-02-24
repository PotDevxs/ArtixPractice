package dev.artixdev.practice.commands;

import org.bukkit.command.CommandSender;
import dev.artixdev.practice.utils.Messages;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.OptArg;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Require;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.api.practice.command.util.CommandIgnore;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.KnockbackProfile;

@Register(
   name = "botkb",
   aliases = {"botknockback"}
)
@CommandIgnore
public class BotKnockbackCommand {

    private static Main plugin() {
        return Main.getInstance();
    }

    @Command(
       name = "delete",
       usage = "<profile>",
       aliases = {"remove"},
       desc = "Delete a knockback profile"
    )
    @Require("artix.bot.knockback")
    public void deleteKnockbackProfile(@Sender CommandSender sender, String profile) {
        if (profile == null || profile.isEmpty()) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_DELETE"));
            return;
        }
        if (plugin().getKnockbackProfileManager().deleteProfile(profile)) {
            sender.sendMessage(Messages.get("BOTKB.PROFILE_DELETED", "profile", profile));
        } else {
            sender.sendMessage(Messages.get("BOTKB.PROFILE_NOT_FOUND_DEFAULT"));
        }
    }

    @Command(
       name = "create",
       usage = "<name>",
       aliases = {"new"},
       desc = "Create a new knockback profile"
    )
    @Require("artix.bot.knockback")
    public void createKnockbackProfile(@Sender CommandSender sender, String name) {
        if (name == null || name.isEmpty()) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_CREATE"));
            return;
        }
        if (plugin().getKnockbackProfileManager().createProfile(name)) {
            sender.sendMessage(Messages.get("BOTKB.PROFILE_CREATED", "name", name));
        } else {
            sender.sendMessage(Messages.get("BOTKB.PROFILE_EXISTS"));
        }
    }

    @Command(
       name = "list",
       desc = "List all knockback profiles"
    )
    @Require("artix.bot.knockback")
    public void listKnockbackProfiles(@Sender CommandSender sender) {
        java.util.List<String> names = plugin().getKnockbackProfileManager().getProfileNames();
        sender.sendMessage(Messages.get("BOTKB.LIST_HEADER", "list", String.join(", ", names)));
    }

    @Command(
       name = "set",
       usage = "<profile> <property> <value>",
       desc = "Set a property of a knockback profile"
    )
    @Require("artix.bot.knockback")
    public void setKnockbackProperty(@Sender CommandSender sender, String profile, String property, String value) {
        if (profile == null || property == null || value == null) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_SET"));
            return;
        }
        plugin().getKnockbackProfileManager().setProperty(profile, property, value);
        sender.sendMessage(Messages.get("BOTKB.SET_SUCCESS", "profile", profile, "property", property, "value", value));
    }

    @Command(
       name = "get",
       usage = "<profile> <property>",
       desc = "Get a property of a knockback profile"
    )
    @Require("artix.bot.knockback")
    public void getKnockbackProperty(@Sender CommandSender sender, String profile, String property) {
        if (profile == null || property == null) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_GET"));
            return;
        }
        String val = plugin().getKnockbackProfileManager().getProperty(profile, property);
        if (val != null) {
            sender.sendMessage(Messages.get("BOTKB.GET_SUCCESS", "profile", profile, "property", property, "value", val));
        } else {
            sender.sendMessage(Messages.get("BOTKB.GET_NOT_FOUND"));
        }
    }

    @Command(
       name = "apply",
       usage = "<profile> [player]",
       desc = "Apply a knockback profile to a bot (by name) or player for testing"
    )
    @Require("artix.bot.knockback")
    public void applyKnockbackProfile(@Sender CommandSender sender, String profile, @OptArg Player target) {
        if (profile == null || profile.isEmpty()) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_APPLY"));
            return;
        }
        KnockbackProfile p = plugin().getKnockbackProfileManager().getProfile(profile);
        if (p == null) {
            sender.sendMessage(Messages.get("BOTKB.APPLY_NOT_FOUND", "profile", profile));
            return;
        }
        if (target != null) {
            sender.sendMessage(Messages.get("BOTKB.APPLY_SUCCESS", "profile", profile, "player", target.getName()));
            return;
        }
        sender.sendMessage(Messages.get("BOTKB.APPLY_AVAILABLE", "profile", profile));
    }

    @Command(
       name = "reset",
       usage = "<profile>",
       desc = "Reset a knockback profile to default values"
    )
    @Require("artix.bot.knockback")
    public void resetKnockbackProfile(@Sender CommandSender sender, String profile) {
        if (profile == null || profile.isEmpty()) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_RESET"));
            return;
        }
        plugin().getKnockbackProfileManager().resetProfile(profile);
        sender.sendMessage(Messages.get("BOTKB.RESET_SUCCESS", "profile", profile));
    }

    @Command(
       name = "copy",
       usage = "<source> <destination>",
       desc = "Copy a knockback profile"
    )
    @Require("artix.bot.knockback")
    public void copyKnockbackProfile(@Sender CommandSender sender, String source, String destination) {
        if (source == null || destination == null) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_COPY"));
            return;
        }
        if (plugin().getKnockbackProfileManager().copyProfile(source, destination)) {
            sender.sendMessage(Messages.get("BOTKB.COPY_SUCCESS", "source", source, "destination", destination));
        } else {
            sender.sendMessage(Messages.get("BOTKB.COPY_FAIL"));
        }
    }

    @Command(
       name = "rename",
       usage = "<profile> <newName>",
       desc = "Rename a knockback profile"
    )
    @Require("artix.bot.knockback")
    public void renameKnockbackProfile(@Sender CommandSender sender, String profile, String newName) {
        if (profile == null || newName == null) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_RENAME"));
            return;
        }
        if (plugin().getKnockbackProfileManager().renameProfile(profile, newName)) {
            sender.sendMessage(Messages.get("BOTKB.RENAME_SUCCESS", "profile", profile, "newName", newName));
        } else {
            sender.sendMessage(Messages.get("BOTKB.RENAME_FAIL"));
        }
    }

    @Command(
       name = "info",
       usage = "<profile>",
       desc = "Show information about a knockback profile"
    )
    @Require("artix.bot.knockback")
    public void showKnockbackProfileInfo(@Sender CommandSender sender, String profile) {
        if (profile == null || profile.isEmpty()) {
            sender.sendMessage(Messages.get("BOTKB.USAGE_INFO"));
            return;
        }
        KnockbackProfile p = plugin().getKnockbackProfileManager().getProfile(profile);
        if (p == null) {
            sender.sendMessage(Messages.get("BOTKB.INFO_NOT_FOUND"));
            return;
        }
        sender.sendMessage(Messages.get("BOTKB.INFO_HEADER", "profile", p.getName()));
        sender.sendMessage(Messages.get("BOTKB.INFO_LINE_1", "horizontal", String.valueOf(p.getHorizontal()), "vertical", String.valueOf(p.getVertical())));
        sender.sendMessage(Messages.get("BOTKB.INFO_LINE_2", "verticalLimit", String.valueOf(p.getVerticalLimit()), "friction", String.valueOf(p.getFriction())));
        sender.sendMessage(Messages.get("BOTKB.INFO_LINE_3", "extraHorizontal", String.valueOf(p.getExtraHorizontal()), "extraVertical", String.valueOf(p.getExtraVertical()), "delay", String.valueOf(p.getDelay())));
    }
}
