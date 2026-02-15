package dev.artixdev.practice.commands;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.command.annotation.Command;
import dev.artixdev.api.practice.command.annotation.Register;
import dev.artixdev.api.practice.command.annotation.Sender;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;

@Register(
    name = "toggleduels",
    aliases = {"tdl", "tduels", "tdr", "toggleduelrequests", "duelreqeusts"}
)
public class ToggleDuelsCommand {

    @Command(
        name = "",
        aliases = {"help"},
        desc = "Toggle your profile's duel requests setting"
    )
    public void toggleDuelsCommand(@Sender Player player) {
        PlayerProfile profile = Main.getInstance().getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(ChatUtils.translate("&cSeu perfil não foi carregado."));
            return;
        }

        boolean currentDuels = profile.isDuelsEnabled();
        profile.setDuelsEnabled(!currentDuels);
        
        if (profile.isDuelsEnabled()) {
            player.sendMessage(ChatUtils.translate("&aSolicitações de duelos ativadas. Você pode receber convites de duelos."));
        } else {
            player.sendMessage(ChatUtils.translate("&cSolicitações de duelos desativadas. Você não receberá mais convites de duelos."));
        }
    }
}
