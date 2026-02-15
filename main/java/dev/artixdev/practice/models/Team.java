package dev.artixdev.practice.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import dev.artixdev.practice.enums.PlayerState;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ExpirableList;

public class Team {
    private final Set<UUID> members;
    private final Map<UUID, PlayerState> memberStates;
    private final ExpirableList<UUID> spectators;
    private final UUID teamId;
    private final UUID leaderId;
    private final long creationTime;
    
    private PlayerState teamState;
    private UUID matchId;
    private boolean isOpen;
    private boolean isSpectating;
    private boolean isInMatch;

    public Team(Player leader) {
        this.teamState = PlayerState.LOBBY;
        this.memberStates = new HashMap<>();
        this.spectators = new ExpirableList<>(TimeUnit.SECONDS, 15L);
        this.members = new HashSet<>();
        this.teamId = UUID.randomUUID();
        this.leaderId = leader.getUniqueId();
        this.creationTime = System.currentTimeMillis();
        
        // Initialize team with leader
        this.members.add(leader.getUniqueId());
        this.memberStates.put(leader.getUniqueId(), PlayerState.LOBBY);
        
        // Check if leader has permission for larger team
        if (leader.hasPermission("practice.team.large")) {
            this.spectators.setMaxSize(25);
        }
    }
    // Removed duplicate isInMatch() method to resolve compilation error.

    public Set<UUID> getMembers() {
        return this.members;
    }
    
    // Removed duplicate setTeamState(PlayerState) method

    public boolean isReady() {
        return this.teamState == PlayerState.READY && this.matchId != null;
    }

    public void setLeader(Player leader) {
        try {
            this.memberStates.put(leader.getUniqueId(), PlayerState.LEADER);
        } catch (Exception e) {
            // Handle exception
        }
    }

    public boolean isActiveTeam() {
        return this.teamState == PlayerState.FIGHTING && this.matchId != null;
    }

    public void playSound(Sound sound) {
        List<Player> players = getOnlinePlayers();
        Consumer<Player> soundConsumer = (player) -> {
            Location location = player.getEyeLocation();
            player.playSound(location, sound, 1.0F, 1.0F);
        };
        players.forEach(soundConsumer);
    }

    public UUID getMatchId() {
        return this.matchId;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Team other = (Team) obj;
        return this.teamId.equals(other.teamId);
    }

    public void setSpectating(boolean spectating) {
        this.isSpectating = spectating;
    }

    public boolean isSpectating() {
        return this.teamState != PlayerState.LOBBY && this.matchId != null;
    }

    public String getName() {
        // This method was obfuscated and needs implementation
        return "Team-" + this.teamId.toString().substring(0, 8);
    }

    // Removed duplicate getTeamState() method

    public int hashCode() {
        return this.teamId.hashCode();
    }

    public int getMemberCount(PlayerProfile profile) {
        // This method was obfuscated and needs implementation
        return this.members.size();
    }

    public List<Player> getOnlinePlayers() {
        Stream<Player> playerStream = this.members.stream().map(Bukkit::getPlayer);
        Predicate<Player> nonNullFilter = Objects::nonNull;
        
        try {
            playerStream = playerStream.filter(nonNullFilter);
        } catch (RuntimeException e) {
            // Handle exception
        }
        
        return playerStream.collect(Collectors.toList());
    }

    public boolean isSpectator(UUID uuid) {
        return this.spectators.contains(uuid);
    }

    public boolean isLeader() {
        return this.teamState != PlayerState.LOBBY && this.matchId != null;
    }

    public boolean isLeader(UUID uuid) {
        return this.leaderId.equals(uuid);
    }

    public boolean isInMatch() {
        return this.isInMatch;
    }
    
    // Removed duplicate setInMatch(boolean) method

    public boolean isOpen() {
        return this.isOpen;
    }

    public void broadcastMessage(String message) {
        List<Player> players = getOnlinePlayers();
        Consumer<Player> messageConsumer = (player) -> {
            player.sendMessage(ChatUtils.translate(message));
        };
        players.forEach(messageConsumer);
    }

    public boolean isActive() {
        if (isReady()) {
            return true;
        } else {
            return isActive() && !isSpectating();
        }
    }

    // Note: creationTime is final and cannot be modified after initialization
    // public void setCreationTime(long time) {
    //     this.creationTime = time;
    // }

    public Map<UUID, PlayerState> getMemberStates() {
        return this.memberStates;
    }

    public boolean hasMember(UUID uuid) {
        return this.members.stream().anyMatch(member -> member.equals(uuid));
    }

    public void cleanup() {
        // This method was obfuscated and needs implementation
        this.members.clear();
        this.memberStates.clear();
        this.spectators.clear();
    }

    public UUID getTeamId() {
        return this.teamId;
    }
    
    public UUID getLeaderId() {
        return this.leaderId;
    }
    
    public Player getLeader() {
        return Bukkit.getPlayer(this.leaderId);
    }
    
    public int getMaxSize() {
        return 10; // Default max size, can be configured
    }
    
    public long getCreatedAt() {
        return this.creationTime;
    }

    public void addMember(Player player) {
        // This method was obfuscated and needs implementation
        this.members.add(player.getUniqueId());
        this.memberStates.put(player.getUniqueId(), PlayerState.LOBBY);
    }

    public void addMember(UUID uuid) {
        this.members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        // This method was obfuscated and needs implementation
        this.members.remove(uuid);
        this.memberStates.remove(uuid);
    }

    public PlayerState getTeamState() {
        return this.teamState;
    }

    public void setTeamState(PlayerState teamState) {
        this.teamState = teamState;
    }

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public void setInMatch(boolean inMatch) {
        this.isInMatch = inMatch;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
        
        // Broadcast team status update
        String statusMessage = ChatUtils.translate("Team is now " + (open ? "open" : "closed"));
        broadcastMessage(statusMessage);
    }

    public String getStatus() {
        boolean isOpen = this.isOpen;
        if (!isOpen) {
            return "Team is closed";
        } else {
            return "Team is open";
        }
    }

    public void addSpectator(UUID uuid) {
        this.spectators.add(uuid);
    }
}