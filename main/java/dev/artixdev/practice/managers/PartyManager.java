package dev.artixdev.practice.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Team;

/**
 * Party Manager
 * Manages party/team operations
 */
public class PartyManager {
    
    private final Main plugin;
    private final Map<UUID, Team> playerTeams;
    private final Map<UUID, Team> teams;
    
    public PartyManager(Main plugin) {
        this.plugin = plugin;
        this.playerTeams = new ConcurrentHashMap<>();
        this.teams = new ConcurrentHashMap<>();
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
        
        Team team = getPlayerParty(leader.getUniqueId());
        if (team != null && team.isLeader(leader.getUniqueId())) {
            addPlayerToTeam(team, player);
            return true;
        }
        return false;
    }
    
    /**
     * Send party invitation
     * @param team the team
     * @param target the target player
     * @return true if successful
     */
    public boolean sendInvitation(Team team, Player target) {
        if (team == null || target == null) {
            return false;
        }
        // For now, just add the player directly
        // In a full implementation, this would manage an invitation system
        addPlayerToTeam(team, target);
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
    }
}
