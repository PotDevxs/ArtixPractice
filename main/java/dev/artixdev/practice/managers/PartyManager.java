package dev.artixdev.practice.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * Party Manager
 * Manages party/team operations
 */
public class PartyManager {
    
    private final Main plugin;
    private final Map<UUID, Team> playerTeams;
    private final Map<UUID, Team> teams;
    /** Pending invites: invitee UUID -> leader UUID who sent the invite */
    private final Map<UUID, UUID> pendingInvites;
    
    public PartyManager(Main plugin) {
        this.plugin = plugin;
        this.playerTeams = new ConcurrentHashMap<>();
        this.teams = new ConcurrentHashMap<>();
        this.pendingInvites = new ConcurrentHashMap<>();
    }
    
    /** Get the UUID of the leader who invited this player, or null. */
    public UUID getPendingInviteFrom(Player player) {
        return player == null ? null : pendingInvites.get(player.getUniqueId());
    }
    
    /** Remove any pending invite for this player. */
    public void removePendingInvite(Player player) {
        if (player != null) pendingInvites.remove(player.getUniqueId());
    }
    
    /**
     * Get player's party/team
     * @param playerId the player's UUID
     * @return the player's team or null
     */
    public Team getPlayerParty(UUID playerId) {
        return playerTeams.get(playerId);
    }
    
    /**
     * Create a new party/team
     * @param leader the leader player
     * @return the created team
     */
    public Team createParty(Player leader) {
        if (leader == null) {
            return null;
        }
        
        // Check if player already has a team
        Team existingTeam = getPlayerParty(leader.getUniqueId());
        if (existingTeam != null) {
            return existingTeam;
        }
        
        // Create new team
        Team team = new Team(leader);
        teams.put(team.getTeamId(), team);
        playerTeams.put(leader.getUniqueId(), team);
        
        return team;
    }
    
    /**
     * Remove a party/team
     * @param teamId the team ID
     */
    public void removeParty(UUID teamId) {
        Team team = teams.remove(teamId);
        if (team != null) {
            for (UUID memberId : team.getMembers()) {
                playerTeams.remove(memberId);
            }
            team.cleanup();
        }
    }
    
    /**
     * Add player to a team
     * @param team the team
     * @param player the player to add
     */
    public void addPlayerToTeam(Team team, Player player) {
        if (team != null && player != null) {
            team.addMember(player);
            playerTeams.put(player.getUniqueId(), team);
        }
    }
    
    /**
     * Remove player from team
     * @param playerId the player's UUID
     * @return true if successful
     */
    public boolean removePlayerFromParty(UUID playerId) {
        Team team = playerTeams.remove(playerId);
        if (team != null) {
            team.removeMember(playerId);
            if (team.getMembers().isEmpty()) {
                removeParty(team.getTeamId());
            }
            return true;
        }
        return false;
    }
    
    /**
     * Accept party invitation
     * @param player the player accepting
     * @param leader the party leader
     * @return true if successful
     */
    public boolean acceptInvitation(Player player, Player leader) {
        if (player == null || leader == null) {
            return false;
        }
        UUID from = pendingInvites.remove(player.getUniqueId());
        if (from == null || !from.equals(leader.getUniqueId())) {
            return false;
        }
        Team team = getPlayerParty(leader.getUniqueId());
        if (team != null && team.isLeader(leader.getUniqueId())) {
            addPlayerToTeam(team, player);
            return true;
        }
        return false;
    }
    
    /**
     * Send party invitation (adds to pending; target must use /party accept &lt;leader&gt; to join)
     * @param team the team
     * @param target the target player
     * @return true if invitation was sent
     */
    public boolean sendInvitation(Team team, Player target) {
        if (team == null || target == null) {
            return false;
        }
        if (getPlayerParty(target.getUniqueId()) != null) {
            return false;
        }
        UUID leaderId = team.getLeaderId();
        if (leaderId == null) return false;
        pendingInvites.put(target.getUniqueId(), leaderId);
        Player leader = team.getLeader();
        String leaderName = leader != null ? leader.getName() : "Unknown";
        target.sendMessage(ChatUtils.translate("&aYou have been invited to join &f" + leaderName + "'s &aparty. Use &f/party accept " + leaderName + " &ato join."));
        return true;
    }
    
    /**
     * Disband a party
     * @param team the team to disband
     * @return true if successful
     */
    public boolean disbandParty(Team team) {
        if (team == null) {
            return false;
        }
        removeParty(team.getTeamId());
        return true;
    }
    
    /**
     * Initialize the party manager
     */
    public void initialize() {
        // Initialization logic if needed
    }
    
    /**
     * Shutdown the party manager
     */
    public void shutdown() {
        for (Team team : teams.values()) {
            team.cleanup();
        }
        teams.clear();
        playerTeams.clear();
        pendingInvites.clear();
    }
}
