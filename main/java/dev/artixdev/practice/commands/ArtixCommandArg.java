package dev.artixdev.practice.commands;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.EventType;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.models.CosmeticSettings;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.menus.CosmeticsMenu;
import dev.artixdev.practice.menus.ShopMenu;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.Messages;

/**
 * Executor único de todos os comandos do plugin (practice, party, accept, shop, cosmetics, queue, silent).
 * Registrado para cada comando no Main.
 */
public class ArtixCommandArg implements CommandExecutor {

    private final Main plugin;

    public ArtixCommandArg(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName().toLowerCase(Locale.ROOT);
        boolean consoleAllowed = "hologram".equals(cmd) || "botkb".equals(cmd) || "botknockback".equals(cmd);
        if (!(sender instanceof Player) && !consoleAllowed) {
            sender.sendMessage(Messages.getPrefix() + " §cApenas jogadores podem usar este comando.");
            return true;
        }
        Player player = sender instanceof Player ? (Player) sender : null;

        switch (cmd) {
            case "practice":
            case "artixpractice":
                handlePractice(player, args);
                break;
            case "party":
            case "p":
                handleParty(player, args);
                break;
            case "accept":
                handleAccept(player, args);
                break;
            case "shop":
                handleShop(player);
                break;
            case "cosmetics":
                handleCosmetics(player, args);
                break;
            case "queue":
                handleQueueCommand(player, args);
                break;
            case "silent":
                handleSilent(player);
                break;
            case "hologram":
                handleHologram(sender, args);
                break;
            case "botkb":
            case "botknockback":
                handleBotKb(sender, args);
                break;
            case "togglev":
            case "visibility":
            case "tv":
            case "togglevisibility":
            case "toggleplayers":
                handleToggleVisibility(player, args);
                break;
            case "toggleduels":
            case "tdl":
            case "tduels":
            case "tdr":
            case "toggleduelrequests":
                handleToggleDuels(player, args);
                break;
            case "postmatch":
            case "matchinv":
                handlePostMatch(player, args);
                break;
            default:
                return false;
        }
        return true;
    }

    // ---------- PRACTICE ----------

    private void handlePractice(Player player, String[] args) {
        if (!player.hasPermission("artixpractice.use")) {
            player.sendMessage(Messages.getPrefix() + " §cSem permissão.");
            return;
        }
        String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "";
        String[] subArgs = args.length > 1 ? java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];

        switch (sub) {
            case "queue":
                handleQueue(player, subArgs);
                break;
            case "arena":
            case "arenas":
                handleArena(player);
                break;
            case "kit":
            case "kits":
                handleKit(player);
                break;
            case "stats":
            case "statistics":
                handleStats(player);
                break;
            case "help":
                handleHelp(player);
                break;
            case "reload":
                handleReload(player);
                break;
            case "version":
            case "ver":
            case "v":
                handleVersion(player);
                break;
            case "":
            default:
                handleDefault(player);
                break;
        }
    }

    private void handleDefault(Player player) {
        player.sendMessage(msg("PRACTICE.HELP_TITLE"));
        player.sendMessage(msg("PRACTICE.HELP_USE"));
        player.sendMessage(msg("PRACTICE.HELP_AVAILABLE"));
        player.sendMessage(msg("PRACTICE.HELP_QUEUE"));
        player.sendMessage(msg("PRACTICE.HELP_ARENA"));
        player.sendMessage(msg("PRACTICE.HELP_KIT"));
        player.sendMessage(msg("PRACTICE.HELP_STATS"));
        player.sendMessage(msg("PRACTICE.HELP_HELP"));
    }

    private void handleQueue(Player player, String[] args) {
        if (plugin.getQueueManager().isPlayerInQueue(player)) {
            player.sendMessage(msg("PRACTICE.ALREADY_IN_QUEUE"));
            return;
        }
        if (plugin.getMatchManager().isPlayerInMatch(player)) {
            player.sendMessage(msg("PRACTICE.CANNOT_QUEUE_IN_MATCH"));
            return;
        }
        EventType eventType = EventType.UNRANKED;
        KitType kitType = KitType.DIAMOND;
        if (args.length >= 1) {
            try {
                eventType = EventType.valueOf(args[0].toUpperCase(Locale.ROOT));
            } catch (Exception ignored) {}
        }
        if (args.length >= 2) {
            try {
                kitType = KitType.valueOf(args[1].toUpperCase(Locale.ROOT).replace(" ", "_"));
            } catch (Exception ignored) {}
        }
        boolean success = plugin.getQueueManager().addPlayerToQueue(player, eventType, kitType);
        if (success) {
            player.sendMessage(Messages.get("PRACTICE.JOINED_QUEUE", "type", eventType.getDisplayName(), "kit", kitType.getName()));
            player.sendMessage(Messages.get("PRACTICE.PLAYERS_IN_QUEUE", "count", String.valueOf(plugin.getQueueManager().getQueueSize(eventType, kitType))));
        } else {
            player.sendMessage(msg("PRACTICE.FAILED_JOIN_QUEUE"));
        }
    }

    private void handleArena(Player player) {
        player.sendMessage(msg("PRACTICE.ARENA_MANAGEMENT_TITLE"));
        player.sendMessage(Messages.get("PRACTICE.ARENA_COUNT", "count", String.valueOf(plugin.getArenaManager().getArenas().size())));
        player.sendMessage(msg("PRACTICE.ARENA_USE"));
    }

    private void handleKit(Player player) {
        player.sendMessage(msg("PRACTICE.KIT_MANAGEMENT_TITLE"));
        player.sendMessage(Messages.get("PRACTICE.KIT_COUNT", "count", String.valueOf(plugin.getKitManager().getKits().size())));
        player.sendMessage(msg("PRACTICE.KIT_USE"));
    }

    private void handleStats(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(msg("PRACTICE.PROFILE_LOAD_FAILED"));
            return;
        }
        player.sendMessage(msg("PRACTICE.STATS_TITLE"));
        player.sendMessage(Messages.get("PRACTICE.STATS_WINS", "wins", String.valueOf(profile.getWins())));
        player.sendMessage(Messages.get("PRACTICE.STATS_LOSSES", "losses", String.valueOf(profile.getLosses())));
        player.sendMessage(Messages.get("PRACTICE.STATS_KILLS", "kills", String.valueOf(profile.getKills())));
        player.sendMessage(Messages.get("PRACTICE.STATS_DEATHS", "deaths", String.valueOf(profile.getDeaths())));
        player.sendMessage(Messages.get("PRACTICE.STATS_ELO", "elo", String.valueOf(profile.getElo())));
        player.sendMessage(Messages.get("PRACTICE.STATS_LEVEL", "level", String.valueOf(profile.getLevel())));
    }

    private void handleHelp(Player player) {
        player.sendMessage(msg("PRACTICE.HELP_TITLE"));
        player.sendMessage(msg("PRACTICE.HELP_COMMANDS"));
        player.sendMessage(msg("PRACTICE.HELP_PRACTICE"));
        player.sendMessage(msg("PRACTICE.HELP_QUEUE_FULL"));
        player.sendMessage(msg("PRACTICE.HELP_ARENA_CMD"));
        player.sendMessage(msg("PRACTICE.HELP_KIT_CMD"));
        player.sendMessage(msg("PRACTICE.HELP_STATS_CMD"));
        player.sendMessage(msg("PRACTICE.HELP_HELP_CMD"));
        player.sendMessage(msg("PRACTICE.HELP_BLANK"));
        player.sendMessage(msg("PRACTICE.EVENT_TYPES"));
        for (EventType type : EventType.values()) {
            if (type != EventType.NONE)
                player.sendMessage(Messages.get("PRACTICE.EVENT_TYPE_LINE", "name", type.getDisplayName(), "description", type.getDescription()));
        }
        player.sendMessage(msg("PRACTICE.HELP_BLANK"));
        player.sendMessage(msg("PRACTICE.KIT_TYPES"));
        for (KitType type : KitType.values()) {
            if (type != KitType.CUSTOM)
                player.sendMessage(Messages.get("PRACTICE.KIT_TYPE_LINE", "name", type.getDisplayName(), "description", type.getDescription()));
        }
    }

    private void handleReload(Player player) {
        if (!player.hasPermission("artixpractice.reload")) {
            player.sendMessage(msg("GENERAL.NO_PERMISSION_RELOAD"));
            return;
        }
        plugin.reloadConfig();
        plugin.onDisable();
        plugin.onEnable();
        player.sendMessage(msg("GENERAL.RELOAD_SUCCESS"));
    }

    private void handleVersion(Player player) {
        player.sendMessage(msg("GENERAL.PLUGIN_INFO_TITLE"));
        player.sendMessage(Messages.get("GENERAL.PLUGIN_VERSION", "version", plugin.getDescription().getVersion()));
        player.sendMessage(Messages.get("GENERAL.PLUGIN_AUTHOR", "authors", String.valueOf(plugin.getDescription().getAuthors())));
        player.sendMessage(Messages.get("GENERAL.PLUGIN_DESCRIPTION", "description", plugin.getDescription().getDescription() != null ? plugin.getDescription().getDescription() : ""));
    }

    // ---------- PARTY ----------

    private void handleParty(Player player, String[] args) {
        String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "";
        String[] subArgs = args.length > 1 ? java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];

        switch (sub) {
            case "create":
                partyCreate(player);
                break;
            case "invite":
                partyInvite(player, subArgs);
                break;
            case "accept":
                partyAccept(player, subArgs);
                break;
            case "leave":
                partyLeave(player);
                break;
            case "disband":
                partyDisband(player);
                break;
            case "kick":
                partyKick(player, subArgs);
                break;
            case "info":
                partyInfo(player);
                break;
            case "roster":
            case "classes":
                partyRoster(player);
                break;
            case "":
            default:
                player.sendMessage(ChatUtils.colorize("&6&lParty &7- Use &f/party create &7| &finvite &7| &faccept &7| &fleave &7| &fdisband &7| &fkick &7| &finfo &7| &froster"));
                break;
        }
    }

    private void partyCreate(Player player) {
        if (plugin.getPartyManager().getPlayerParty(player.getUniqueId()) != null) {
            player.sendMessage(ChatUtils.colorize("&cYou are already in a party!"));
            return;
        }
        Team party = plugin.getPartyManager().createParty(player);
        if (party != null) {
            player.sendMessage(ChatUtils.colorize("&aParty created successfully!"));
            player.sendMessage(ChatUtils.colorize("&7Use &f/party invite <player> &7to invite members."));
        } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to create party!"));
        }
    }

    private void partyInvite(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatUtils.colorize("&cUsage: /party invite <player>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(ChatUtils.colorize("&cPlayer not found."));
            return;
        }
        Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
        }
        if (!party.getLeaderId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&cOnly the party leader can invite players!"));
            return;
        }
        if (plugin.getPartyManager().getPlayerParty(target.getUniqueId()) != null) {
            player.sendMessage(ChatUtils.colorize("&c" + target.getName() + " is already in a party!"));
            return;
        }
        if (party.getMembers().size() >= party.getMaxSize()) {
            player.sendMessage(ChatUtils.colorize("&cYour party is full!"));
            return;
        }
        boolean success = plugin.getPartyManager().sendInvitation(party, target);
        if (success) {
            player.sendMessage(ChatUtils.colorize("&aInvitation sent to " + target.getName() + "!"));
            target.sendMessage(ChatUtils.colorize("&aYou have been invited to " + player.getName() + "'s party!"));
            target.sendMessage(ChatUtils.colorize("&7Use &f/party accept " + player.getName() + " &7to join."));
        } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to send invitation!"));
        }
    }

    private void partyAccept(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatUtils.colorize("&cUsage: /party accept <leader>"));
            return;
        }
        Player leader = Bukkit.getPlayerExact(args[0]);
        if (leader == null) {
            player.sendMessage(ChatUtils.colorize("&cPlayer not found."));
            return;
        }
        if (plugin.getPartyManager().getPlayerParty(player.getUniqueId()) != null) {
            player.sendMessage(ChatUtils.colorize("&cYou are already in a party!"));
            return;
        }
        boolean success = plugin.getPartyManager().acceptInvitation(player, leader);
        if (success) {
            player.sendMessage(ChatUtils.colorize("&aYou joined " + leader.getName() + "'s party!"));
            leader.sendMessage(ChatUtils.colorize("&a" + player.getName() + " joined your party!"));
        } else {
            player.sendMessage(ChatUtils.colorize("&cNo invitation found from " + leader.getName() + "!"));
        }
    }

    private void partyLeave(Player player) {
        Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
        }
        if (party.getLeaderId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&cYou cannot leave your own party! Use &f/party disband &cto disband it."));
            return;
        }
        boolean success = plugin.getPartyManager().removePlayerFromParty(player.getUniqueId());
        if (success) {
            player.sendMessage(ChatUtils.colorize("&aYou left the party!"));
            for (UUID memberId : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberId);
                if (member != null && !member.equals(player)) {
                    member.sendMessage(ChatUtils.colorize("&c" + player.getName() + " left the party!"));
                }
            }
        } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to leave party!"));
        }
    }

    private void partyDisband(Player player) {
        Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
        }
        if (!party.getLeaderId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&cOnly the party leader can disband the party!"));
            return;
        }
        boolean success = plugin.getPartyManager().disbandParty(party);
        if (success) {
            player.sendMessage(ChatUtils.colorize("&aParty disbanded successfully!"));
            for (UUID memberId : party.getMembers()) {
                Player member = Bukkit.getPlayer(memberId);
                if (member != null && !member.equals(player)) {
                    member.sendMessage(ChatUtils.colorize("&cThe party has been disbanded!"));
                }
            }
        } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to disband party!"));
        }
    }

    private void partyKick(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatUtils.colorize("&cUsage: /party kick <player>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(ChatUtils.colorize("&cPlayer not found."));
            return;
        }
        Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
        }
        if (!party.getLeaderId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&cOnly the party leader can kick players!"));
            return;
        }
        if (!party.getMembers().contains(target.getUniqueId())) {
            player.sendMessage(ChatUtils.colorize("&c" + target.getName() + " is not in your party!"));
            return;
        }
        if (target.equals(player)) {
            player.sendMessage(ChatUtils.colorize("&cYou cannot kick yourself! Use &f/party disband &cto disband the party."));
            return;
        }
        boolean success = plugin.getPartyManager().removePlayerFromParty(target.getUniqueId());
        if (success) {
            player.sendMessage(ChatUtils.colorize("&a" + target.getName() + " has been kicked from the party!"));
            target.sendMessage(ChatUtils.colorize("&cYou have been kicked from the party!"));
        } else {
            player.sendMessage(ChatUtils.colorize("&cFailed to kick player!"));
        }
    }

    private void partyInfo(Player player) {
        Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
        }
        Player leader = party.getLeader();
        player.sendMessage(ChatUtils.colorize("&6&lParty Information"));
        player.sendMessage(ChatUtils.colorize("&7Leader: &f" + (leader != null ? leader.getName() : "Unknown")));
        player.sendMessage(ChatUtils.colorize("&7Members: &f" + party.getMembers().size() + "/" + party.getMaxSize()));
        player.sendMessage(ChatUtils.colorize("&7Created: &f" + timeAgo(party.getCreatedAt())));
        player.sendMessage(ChatUtils.colorize("&7Members:"));
        for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
                String role = memberId.equals(party.getLeaderId()) ? "&cLeader" : "&7Member";
                String status = member.isOnline() ? "&aOnline" : "&cOffline";
                player.sendMessage(ChatUtils.colorize("&7- " + role + " &f" + member.getName() + " &7(" + status + "&7)"));
            }
        }
    }

    private void partyRoster(Player player) {
        Team party = plugin.getPartyManager().getPlayerParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(ChatUtils.colorize("&cYou are not in a party!"));
            return;
        }
        Player leader = party.getLeader();
        player.sendMessage(ChatUtils.colorize("&6&lParty Roster"));
        player.sendMessage(ChatUtils.colorize("&7Leader: &f" + (leader != null ? leader.getName() : "Unknown")));
        player.sendMessage(ChatUtils.colorize("&7Members: &f" + party.getMembers().size() + "/" + party.getMaxSize()));
        for (UUID memberId : party.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null) {
                String role = memberId.equals(party.getLeaderId()) ? "&cLeader" : "&7Member";
                player.sendMessage(ChatUtils.colorize("&7- " + role + " &f" + member.getName()));
            }
        }
    }

    private static String timeAgo(long timestamp) {
        long diff = System.currentTimeMillis() - timestamp;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        if (days > 0) return days + " day(s) ago";
        if (hours > 0) return hours + " hour(s) ago";
        if (minutes > 0) return minutes + " minute(s) ago";
        return "Just now";
    }

    // ---------- ACCEPT (duel) ----------

    private void handleAccept(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatUtils.colorize("&cUsage: /accept <player>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(ChatUtils.colorize("&cPlayer not found."));
            return;
        }
        if (player.equals(target)) {
            player.sendMessage(ChatUtils.colorize("&cYou cannot accept a duel from yourself."));
            return;
        }
        PlayerProfile playerProfile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        PlayerProfile targetProfile = plugin.getPlayerManager().getPlayerProfile(target.getUniqueId());
        if (playerProfile == null || targetProfile == null) {
            player.sendMessage(ChatUtils.colorize("&cPlayer profiles not loaded."));
            return;
        }
        if (playerProfile.getState() != dev.artixdev.practice.enums.PlayerState.LOBBY) {
            player.sendMessage(ChatUtils.colorize("&cYou must be in the lobby to accept duels."));
            return;
        }
        if (targetProfile.getState() != dev.artixdev.practice.enums.PlayerState.LOBBY) {
            player.sendMessage(ChatUtils.colorize("&cTarget player is not in the lobby."));
            return;
        }
        player.sendMessage(ChatUtils.colorize("&aDuel accept logic: use your duel/request system here."));
        plugin.getLogger().info("Duel accept: " + player.getName() + " accepted from " + target.getName());
    }

    // ---------- SHOP ----------

    private void handleShop(Player player) {
        try {
            MenuHandler.getInstance().openMenu(new ShopMenu(), player);
        } catch (Exception e) {
            player.sendMessage(ChatUtils.colorize("&cFailed to open shop!"));
            plugin.getLogger().warning("Failed to open shop for " + player.getName() + ": " + e.getMessage());
        }
    }

    // ---------- COSMETICS ----------

    private void handleCosmetics(Player player, String[] args) {
        String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "";
        String[] subArgs = args.length > 1 ? java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];

        switch (sub) {
            case "":
            case "help":
                try {
                    MenuHandler.getInstance().openMenu(new CosmeticsMenu(), player);
                    player.sendMessage(ChatUtils.colorize("&aOpening cosmetics menu..."));
                } catch (Exception e) {
                    player.sendMessage(ChatUtils.colorize("&cFailed to open cosmetics menu!"));
                }
                break;
            case "killeffect":
                cosmeticsKillEffect(player, subArgs);
                break;
            case "killmessage":
                cosmeticsKillMessage(player, subArgs);
                break;
            case "trail":
                cosmeticsTrail(player, subArgs);
                break;
            case "reset":
                cosmeticsReset(player);
                break;
            case "list":
                cosmeticsList(player);
                break;
            default:
                player.sendMessage(ChatUtils.colorize("&7/cosmetics [help|killEffect|killMessage|trail|reset|list]"));
                break;
        }
    }

    private void cosmeticsKillEffect(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatUtils.colorize("&cUsage: /cosmetics killEffect <effect>"));
            return;
        }
        try {
            CosmeticSettings.KillEffect effect = CosmeticSettings.KillEffect.valueOf(args[0].toUpperCase(Locale.ROOT));
            PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
            if (profile == null) {
                player.sendMessage(ChatUtils.colorize("&cYour profile is not loaded!"));
                return;
            }
            CosmeticSettings cosmetics = profile.getCosmeticSettings();
            if (cosmetics == null) cosmetics = new CosmeticSettings();
            cosmetics.setKillEffect(effect);
            profile.setCosmeticSettings(cosmetics);
            plugin.getPlayerManager().savePlayerProfile(profile);
            player.sendMessage(ChatUtils.colorize("&aKill effect set to: &f" + effect.name()));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatUtils.colorize("&cInvalid kill effect. Available: " + java.util.Arrays.toString(CosmeticSettings.KillEffect.values())));
        }
    }

    private void cosmeticsKillMessage(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatUtils.colorize("&cUsage: /cosmetics killMessage <message>"));
            return;
        }
        try {
            CosmeticSettings.KillMessage msg = CosmeticSettings.KillMessage.valueOf(args[0].toUpperCase(Locale.ROOT));
            PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
            if (profile == null) {
                player.sendMessage(ChatUtils.colorize("&cYour profile is not loaded!"));
                return;
            }
            CosmeticSettings cosmetics = profile.getCosmeticSettings();
            if (cosmetics == null) cosmetics = new CosmeticSettings();
            cosmetics.setKillMessage(msg);
            profile.setCosmeticSettings(cosmetics);
            plugin.getPlayerManager().savePlayerProfile(profile);
            player.sendMessage(ChatUtils.colorize("&aKill message set to: &f" + msg.name()));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatUtils.colorize("&cInvalid kill message. Available: " + java.util.Arrays.toString(CosmeticSettings.KillMessage.values())));
        }
    }

    private void cosmeticsTrail(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatUtils.colorize("&cUsage: /cosmetics trail <trail>"));
            return;
        }
        try {
            CosmeticSettings.Trail trail = CosmeticSettings.Trail.valueOf(args[0].toUpperCase(Locale.ROOT));
            PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
            if (profile == null) {
                player.sendMessage(ChatUtils.colorize("&cYour profile is not loaded!"));
                return;
            }
            CosmeticSettings cosmetics = profile.getCosmeticSettings();
            if (cosmetics == null) cosmetics = new CosmeticSettings();
            cosmetics.setTrail(trail);
            profile.setCosmeticSettings(cosmetics);
            plugin.getPlayerManager().savePlayerProfile(profile);
            player.sendMessage(ChatUtils.colorize("&aTrail set to: &f" + trail.name()));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatUtils.colorize("&cInvalid trail. Available: " + java.util.Arrays.toString(CosmeticSettings.Trail.values())));
        }
    }

    private void cosmeticsReset(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(ChatUtils.colorize("&cYour profile is not loaded!"));
            return;
        }
        CosmeticSettings cosmetics = profile.getCosmeticSettings();
        if (cosmetics == null) cosmetics = new CosmeticSettings();
        cosmetics.resetAll();
        profile.setCosmeticSettings(cosmetics);
        plugin.getPlayerManager().savePlayerProfile(profile);
        player.sendMessage(ChatUtils.colorize("&aAll cosmetics have been reset!"));
    }

    private void cosmeticsList(Player player) {
        player.sendMessage(ChatUtils.colorize("&6&lAvailable Cosmetics"));
        player.sendMessage(ChatUtils.colorize("&cKill Effects: " + java.util.Arrays.toString(CosmeticSettings.KillEffect.values())));
        player.sendMessage(ChatUtils.colorize("&eKill Messages: " + java.util.Arrays.toString(CosmeticSettings.KillMessage.values())));
        player.sendMessage(ChatUtils.colorize("&dTrails: " + java.util.Arrays.toString(CosmeticSettings.Trail.values())));
    }

    // ---------- QUEUE (comando /queue) ----------

    private void handleQueueCommand(Player player, String[] args) {
        String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "";
        String[] subArgs = args.length > 1 ? java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];

        switch (sub) {
            case "join":
                if (subArgs.length >= 2) {
                    try {
                        EventType et = EventType.valueOf(subArgs[0].toUpperCase(Locale.ROOT));
                        KitType kt = KitType.valueOf(subArgs[1].toUpperCase(Locale.ROOT).replace(" ", "_"));
                        handleQueue(player, new String[] { et.name(), kt.name() });
                    } catch (Exception e) {
                        player.sendMessage(ChatUtils.colorize("&cUsage: /queue join <type> <kit>"));
                    }
                } else {
                    handleQueue(player, new String[0]);
                }
                break;
            case "leave":
                if (!plugin.getQueueManager().isPlayerInQueue(player)) {
                    player.sendMessage(ChatUtils.colorize("&cYou are not in a queue!"));
                    return;
                }
                boolean left = plugin.getQueueManager().removePlayerFromQueue(player);
                player.sendMessage(left ? ChatUtils.colorize("&aLeft the queue!") : ChatUtils.colorize("&cFailed to leave queue!"));
                break;
            case "list":
                player.sendMessage(ChatUtils.colorize("&6&lAvailable Queues:"));
                for (EventType et : EventType.values()) {
                    if (et == EventType.NONE) continue;
                    for (KitType kt : KitType.values()) {
                        if (kt == KitType.CUSTOM) continue;
                        int size = plugin.getQueueManager().getQueueSize(et, kt);
                        String status = size > 0 ? "&a" + size + " players" : "&7Empty";
                        player.sendMessage(ChatUtils.colorize("&7- " + et.getDisplayName() + " " + kt.getDisplayName() + ": " + status));
                    }
                }
                break;
            case "forcequeue":
                if (!player.hasPermission("artixpractice.admin")) {
                    player.sendMessage(ChatUtils.colorize("&cNo permission."));
                    return;
                }
                if (subArgs.length < 3) {
                    player.sendMessage(ChatUtils.colorize("&cUsage: /queue forceQueue <target> <type> <kit>"));
                    return;
                }
                Player target = Bukkit.getPlayerExact(subArgs[0]);
                if (target == null) {
                    player.sendMessage(ChatUtils.colorize("&cPlayer not found."));
                    return;
                }
                try {
                    EventType et = EventType.valueOf(subArgs[1].toUpperCase(Locale.ROOT));
                    KitType kt = KitType.valueOf(subArgs[2].toUpperCase(Locale.ROOT).replace(" ", "_"));
                    if (plugin.getQueueManager().addPlayerToQueue(target, et, kt)) {
                        player.sendMessage(ChatUtils.colorize("&aForced " + target.getName() + " into queue."));
                        target.sendMessage(ChatUtils.colorize("&aYou were forced into a queue."));
                    } else {
                        player.sendMessage(ChatUtils.colorize("&cCould not force into queue."));
                    }
                } catch (Exception e) {
                    player.sendMessage(ChatUtils.colorize("&cInvalid type or kit."));
                }
                break;
            case "clear":
                if (!player.hasPermission("artixpractice.admin")) {
                    player.sendMessage(ChatUtils.colorize("&cNo permission."));
                    return;
                }
                int cleared = plugin.getQueueManager().clearAllQueues();
                player.sendMessage(ChatUtils.colorize("&aCleared " + cleared + " queues!"));
                break;
            case "info":
                if (subArgs.length < 2) {
                    player.sendMessage(ChatUtils.colorize("&cUsage: /queue info <type> <kit>"));
                    return;
                }
                try {
                    EventType et = EventType.valueOf(subArgs[0].toUpperCase(Locale.ROOT));
                    KitType kt = KitType.valueOf(subArgs[1].toUpperCase(Locale.ROOT).replace(" ", "_"));
                    int size = plugin.getQueueManager().getQueueSize(et, kt);
                    player.sendMessage(ChatUtils.colorize("&6&lQueue: &f" + et.getDisplayName() + " " + kt.getDisplayName() + " &7- &f" + size + " players"));
                } catch (Exception e) {
                    player.sendMessage(ChatUtils.colorize("&cInvalid type or kit."));
                }
                break;
            default:
                player.sendMessage(ChatUtils.colorize("&7/queue join [type] [kit] &7| &fleave &7| &flist &7| &finfo <type> <kit>"));
                break;
        }
    }

    // ---------- SILENT ----------

    private void handleSilent(Player player) {
        if (!player.hasPermission("artix.profile.silent")) {
            player.sendMessage(ChatUtils.colorize("&cNo permission."));
            return;
        }
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(Messages.get("SILENT.PROFILE_NOT_LOADED"));
            return;
        }
        boolean current = profile.isSilentMode();
        profile.setSilentMode(!current);
        player.sendMessage(current ? Messages.get("SILENT.DISABLED") : Messages.get("SILENT.ENABLED"));
    }

    // ---------- HOLOGRAM ----------

    private void handleHologram(CommandSender sender, String[] args) {
        if (!sender.hasPermission("artix.holo.admin")) {
            sender.sendMessage(ChatUtils.colorize("&cSem permissao."));
            return;
        }
        String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "";
        String[] subArgs = args.length > 1 ? java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];
        switch (sub) {
            case "":
            case "help":
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_TITLE"));
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_CREATE"));
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_DELETE"));
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_LIST"));
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_TELEPORT"));
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_MOVE"));
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_UPDATE"));
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_RELOAD"));
                break;
            case "create":
                if (subArgs.length < 2) {
                    sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
                    return;
                }
                sender.sendMessage(Messages.get("HOLOGRAM.CREATED", "name", subArgs[0]));
                sender.sendMessage(Messages.get("HOLOGRAM.CREATED_TYPE", "type", subArgs[1]));
                break;
            case "delete":
            case "remove":
                if (subArgs.length < 1) {
                    sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
                    return;
                }
                sender.sendMessage(Messages.get("HOLOGRAM.DELETED", "name", subArgs[0]));
                break;
            case "list":
                if (plugin.getHologramManager() != null && plugin.getHologramManager().getAllHolograms() != null && !plugin.getHologramManager().getAllHolograms().isEmpty()) {
                    for (dev.artixdev.practice.models.HologramInterface h : plugin.getHologramManager().getAllHolograms()) {
                        sender.sendMessage(ChatUtils.colorize("&7- &f" + h.getName()));
                    }
                } else {
                    sender.sendMessage(Messages.get("HOLOGRAM.LIST_EMPTY"));
                }
                break;
            case "teleport":
            case "tp":
                if (subArgs.length < 1 || !(sender instanceof Player)) {
                    sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
                    return;
                }
                sender.sendMessage(Messages.get("HOLOGRAM.NOT_FOUND", "name", subArgs[0]));
                break;
            case "move":
                if (subArgs.length < 1 || !(sender instanceof Player)) {
                    sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
                    return;
                }
                sender.sendMessage(Messages.get("HOLOGRAM.MOVED", "name", subArgs[0]));
                break;
            case "update":
                if (subArgs.length < 1) {
                    sender.sendMessage(Messages.get("HOLOGRAM.INVALID_PARAMETERS"));
                    return;
                }
                sender.sendMessage(Messages.get("HOLOGRAM.UPDATED", "name", subArgs[0]));
                break;
            case "reload":
                sender.sendMessage(Messages.get("HOLOGRAM.RELOAD_SUCCESS"));
                break;
            default:
                sender.sendMessage(Messages.get("HOLOGRAM.HELP_TITLE"));
                break;
        }
    }

    // ---------- BOT KNOCKBACK ----------

    private void handleBotKb(CommandSender sender, String[] args) {
        if (!sender.hasPermission("artix.bot.knockback")) {
            sender.sendMessage(ChatUtils.colorize("&cSem permissao."));
            return;
        }
        if (plugin.getKnockbackProfileManager() == null) {
            sender.sendMessage(ChatUtils.colorize("&cSistema de knockback nao disponivel."));
            return;
        }
        String sub = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "";
        String[] subArgs = args.length > 1 ? java.util.Arrays.copyOfRange(args, 1, args.length) : new String[0];
        switch (sub) {
            case "list":
                java.util.List<String> names = plugin.getKnockbackProfileManager().getProfileNames();
                sender.sendMessage(Messages.get("BOTKB.LIST_HEADER", "list", String.join(", ", names != null ? names : java.util.Collections.emptyList())));
                break;
            case "create":
            case "new":
                if (subArgs.length < 1) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_CREATE"));
                    return;
                }
                if (plugin.getKnockbackProfileManager().createProfile(subArgs[0])) {
                    sender.sendMessage(Messages.get("BOTKB.PROFILE_CREATED", "name", subArgs[0]));
                } else {
                    sender.sendMessage(Messages.get("BOTKB.PROFILE_EXISTS"));
                }
                break;
            case "delete":
            case "remove":
                if (subArgs.length < 1) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_DELETE"));
                    return;
                }
                if (plugin.getKnockbackProfileManager().deleteProfile(subArgs[0])) {
                    sender.sendMessage(Messages.get("BOTKB.PROFILE_DELETED", "profile", subArgs[0]));
                } else {
                    sender.sendMessage(Messages.get("BOTKB.PROFILE_NOT_FOUND_DEFAULT"));
                }
                break;
            case "set":
                if (subArgs.length < 3) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_SET"));
                    return;
                }
                plugin.getKnockbackProfileManager().setProperty(subArgs[0], subArgs[1], subArgs[2]);
                sender.sendMessage(Messages.get("BOTKB.SET_SUCCESS", "profile", subArgs[0], "property", subArgs[1], "value", subArgs[2]));
                break;
            case "get":
                if (subArgs.length < 2) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_GET"));
                    return;
                }
                String val = plugin.getKnockbackProfileManager().getProperty(subArgs[0], subArgs[1]);
                if (val != null) {
                    sender.sendMessage(Messages.get("BOTKB.GET_SUCCESS", "profile", subArgs[0], "property", subArgs[1], "value", val));
                } else {
                    sender.sendMessage(Messages.get("BOTKB.GET_NOT_FOUND"));
                }
                break;
            case "apply":
                if (subArgs.length < 1) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_APPLY"));
                    return;
                }
                if (plugin.getKnockbackProfileManager().getProfile(subArgs[0]) == null) {
                    sender.sendMessage(Messages.get("BOTKB.APPLY_NOT_FOUND", "profile", subArgs[0]));
                    return;
                }
                if (subArgs.length >= 2) {
                    Player target = Bukkit.getPlayerExact(subArgs[1]);
                    sender.sendMessage(Messages.get("BOTKB.APPLY_SUCCESS", "profile", subArgs[0], "player", target != null ? target.getName() : subArgs[1]));
                } else {
                    sender.sendMessage(Messages.get("BOTKB.APPLY_AVAILABLE", "profile", subArgs[0]));
                }
                break;
            case "reset":
                if (subArgs.length < 1) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_RESET"));
                    return;
                }
                plugin.getKnockbackProfileManager().resetProfile(subArgs[0]);
                sender.sendMessage(Messages.get("BOTKB.RESET_SUCCESS", "profile", subArgs[0]));
                break;
            case "copy":
                if (subArgs.length < 2) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_COPY"));
                    return;
                }
                if (plugin.getKnockbackProfileManager().copyProfile(subArgs[0], subArgs[1])) {
                    sender.sendMessage(Messages.get("BOTKB.COPY_SUCCESS", "source", subArgs[0], "destination", subArgs[1]));
                } else {
                    sender.sendMessage(Messages.get("BOTKB.COPY_FAIL"));
                }
                break;
            case "rename":
                if (subArgs.length < 2) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_RENAME"));
                    return;
                }
                if (plugin.getKnockbackProfileManager().renameProfile(subArgs[0], subArgs[1])) {
                    sender.sendMessage(Messages.get("BOTKB.RENAME_SUCCESS", "profile", subArgs[0], "newName", subArgs[1]));
                } else {
                    sender.sendMessage(Messages.get("BOTKB.RENAME_FAIL"));
                }
                break;
            case "info":
                if (subArgs.length < 1) {
                    sender.sendMessage(Messages.get("BOTKB.USAGE_INFO"));
                    return;
                }
                dev.artixdev.practice.models.KnockbackProfile p = plugin.getKnockbackProfileManager().getProfile(subArgs[0]);
                if (p == null) {
                    sender.sendMessage(Messages.get("BOTKB.INFO_NOT_FOUND"));
                    return;
                }
                sender.sendMessage(Messages.get("BOTKB.INFO_HEADER", "profile", p.getName()));
                sender.sendMessage(Messages.get("BOTKB.INFO_LINE_1", "horizontal", String.valueOf(p.getHorizontal()), "vertical", String.valueOf(p.getVertical())));
                sender.sendMessage(Messages.get("BOTKB.INFO_LINE_2", "verticalLimit", String.valueOf(p.getVerticalLimit()), "friction", String.valueOf(p.getFriction())));
                sender.sendMessage(Messages.get("BOTKB.INFO_LINE_3", "extraHorizontal", String.valueOf(p.getExtraHorizontal()), "extraVertical", String.valueOf(p.getExtraVertical()), "delay", String.valueOf(p.getDelay())));
                break;
            default:
                sender.sendMessage(ChatUtils.colorize("&7/botkb list|create|delete|set|get|apply|reset|copy|rename|info"));
                break;
        }
    }

    // ---------- TOGGLE VISIBILITY ----------

    private void handleToggleVisibility(Player player, String[] args) {
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(ChatUtils.colorize("&cSeu perfil nao foi carregado."));
            return;
        }
        boolean current = profile.isVisibilityEnabled();
        profile.setVisibilityEnabled(!current);
        if (profile.isVisibilityEnabled()) {
            player.sendMessage(ChatUtils.colorize("&aVisibilidade ativada. Outros jogadores podem te ver."));
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(player)) other.showPlayer(player);
            }
        } else {
            player.sendMessage(ChatUtils.colorize("&cVisibilidade desativada. Outros jogadores nao podem te ver."));
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (!other.equals(player)) other.hidePlayer(player);
            }
        }
    }

    // ---------- TOGGLE DUELS ----------

    private void handleToggleDuels(Player player, String[] args) {
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage(ChatUtils.colorize("&cSeu perfil nao foi carregado."));
            return;
        }
        boolean current = profile.isDuelsEnabled();
        profile.setDuelsEnabled(!current);
        player.sendMessage(current
            ? ChatUtils.colorize("&cSolicitacoes de duelos desativadas.")
            : ChatUtils.colorize("&aSolicitacoes de duelos ativadas. Voce pode receber convites de duelos."));
    }

    // ---------- POST MATCH ----------

    private void handlePostMatch(Player player, String[] args) {
        if (!player.hasPermission("artix.command.postmatch")) {
            player.sendMessage(ChatUtils.colorize("&cSem permissao."));
            return;
        }
        if (args.length < 1) {
            player.sendMessage(ChatUtils.colorize("&7Uso: /postmatch <jogador|uuid>"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            try {
                UUID uuid = UUID.fromString(args[0]);
                target = Bukkit.getPlayer(uuid);
            } catch (Exception ignored) {}
        }
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatUtils.colorize("&cJogador nao encontrado ou offline."));
            return;
        }
        PlayerProfile targetProfile = plugin.getPlayerManager().getPlayerProfile(target.getUniqueId());
        if (targetProfile == null) {
            player.sendMessage(ChatUtils.colorize("&cPerfil do jogador nao carregado."));
            return;
        }
        try {
            dev.artixdev.practice.menus.PostMatchInventoryMenu menu = new dev.artixdev.practice.menus.PostMatchInventoryMenu(target);
            MenuHandler.getInstance().openMenu(menu, player);
            player.sendMessage(ChatUtils.colorize("&aMenu post-match aberto para &f" + target.getName()));
        } catch (Exception e) {
            player.sendMessage(ChatUtils.colorize("&cErro ao abrir menu: " + e.getMessage()));
        }
    }

    private static String msg(String key) {
        return Messages.get(key);
    }
}
