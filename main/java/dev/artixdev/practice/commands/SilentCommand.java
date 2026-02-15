package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Require;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;

@Register(name = "silent")
public class SilentCommand {

    @Require("bolt.profile.silent")
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
            player.sendMessage(ChatUtils.translate("&aModo silencioso ativado. Você não receberá mais mensagens de duelos."));
        } else {
            player.sendMessage(ChatUtils.translate("&cModo silencioso desativado. Você voltou a receber mensagens de duelos."));
        }
    }
}
