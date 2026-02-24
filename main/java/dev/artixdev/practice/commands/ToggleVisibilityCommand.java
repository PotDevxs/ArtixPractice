package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;

@Register(
    name = "togglev",
    aliases = {"visibility", "tv", "togglevisibility", "toggleplayers"}
)
public class ToggleVisibilityCommand {

    @Command(
        name = "",
        aliases = {"help"},
        desc = "Toggle your profile's visibility setting"
    )
    public void toggleVisibilityCommand(@Sender Player player) {
        PlayerProfile profile = Main.getInstance().getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(ChatUtils.translate("&cSeu perfil não foi carregado."));
            return;
        }

        boolean currentVisibility = profile.isVisibilityEnabled();
        profile.setVisibilityEnabled(!currentVisibility);
        
        if (profile.isVisibilityEnabled()) {
            player.sendMessage(ChatUtils.translate("&aVisibilidade ativada. Outros jogadores podem te ver."));
            for (Player other : Main.getInstance().getServer().getOnlinePlayers()) {
                if (!other.equals(player)) other.showPlayer(player);
            }
        } else {
            player.sendMessage(ChatUtils.translate("&cVisibilidade desativada. Outros jogadores não podem te ver."));
            for (Player other : Main.getInstance().getServer().getOnlinePlayers()) {
                if (!other.equals(player)) other.hidePlayer(player);
            }
        }
    }
}
