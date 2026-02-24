package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Require;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.Messages;

@Register(name = "silent")
public class SilentCommand {

    @Require("artix.profile.silent")
    @Command(name = "", desc = "Go into silent mode")
    public void silentCommand(@Sender Player player) {
        PlayerProfile profile = Main.getInstance().getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(ChatUtils.translate("&cSeu perfil não foi carregado."));
            return;
        }

        boolean currentSilent = profile.isSilentMode();
        profile.setSilentMode(!currentSilent);
        
        if (profile.isSilentMode()) {
            player.sendMessage(Messages.get("SILENT.ENABLED"));
        } else {
            player.sendMessage(Messages.get("SILENT.DISABLED"));
        }
    }
}
