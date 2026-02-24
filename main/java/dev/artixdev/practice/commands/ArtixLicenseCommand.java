package dev.artixdev.practice.commands;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.license.LicenseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Comando /artixlicense - configurar e validar licença (chave em config.yml, License:).
 */
public class ArtixLicenseCommand implements CommandExecutor {

    private final Main main;

    public ArtixLicenseCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("artixlicense")) return false;

        LicenseManager license = main.getLicenseManager();
        if (license == null) {
            sender.sendMessage("§cLicense system not available.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§e/artixlicense set <KEY>");
            sender.sendMessage("§e/artixlicense validate");
            sender.sendMessage("§e/artixlicense status");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "set":
                if (args.length < 2) {
                    sender.sendMessage("§cUso: /artixlicense set ARTIX-XXXX-XXXX-XXXX-XXXX");
                    return true;
                }
                String key = args[1].trim();
                FileConfiguration config = main.getPlugin().getConfig();
                if (config != null) {
                    config.set("License", key);
                    main.getPlugin().saveConfig();
                    sender.sendMessage("§aLicense key updated in config.yml. Use /artixlicense validate to check.");
                }
                license.validateAsync(true);
                break;
            case "validate":
                sender.sendMessage("§eValidating...");
                license.validateAsync(true);
                break;
            case "status":
                sender.sendMessage("§evalidated=" + license.isValidated());
                sender.sendMessage("§elastValid=" + license.getLastSuccessAtFromCache());
                sender.sendMessage("§elastMessage=" + license.getLastMessage());
                break;
            default:
                sender.sendMessage("§e/artixlicense set <KEY> | validate | status");
                break;
        }
        return true;
    }
}
