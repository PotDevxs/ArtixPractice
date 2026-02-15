package dev.artixdev.practice.models;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;
import dev.artixdev.practice.utils.cuboid.Cuboid;
import dev.artixdev.practice.utils.other.Callback;
import dev.artixdev.practice.enums.ArenaType;
import dev.artixdev.practice.enums.KitType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class Arena {
    
    private static final Logger logger = LogManager.getLogger(Arena.class);
    
    @SerializedName("id")
    private UUID id;
    
    @SerializedName("_id")
    private String name;
    
    @SerializedName("enabled")
    private boolean enabled;
    
    @SerializedName("displayName")
    private String displayName;
    
    @SerializedName("displayIcon")
    private ItemStack displayIcon;
    
    @SerializedName("type")
    private ArenaType type;
    
    @SerializedName("spawn1")
    private Location spawn1;
    
    @SerializedName("spawn2")
    private Location spawn2;
    
    @SerializedName("spectatorSpawn")
    private Location spectatorSpawn;
    
    @SerializedName("bounds")
    private Cuboid bounds;
    
    @SerializedName("kits")
    private final Set<String> kits;
    
    @SerializedName("kitType")
    private KitType kitType;
    
    @SerializedName("ranked")
    private boolean ranked;
    
    @SerializedName("gridIndex")
    private int gridIndex;
    
    @SerializedName("buildHeight")
    private int buildHeight;
    
    @SerializedName("deathHeight")
    private int deathHeight;
    
    @SerializedName("portalRadius")
    private int portalRadius;
    
    @SerializedName("redSpecial")
    private List<Location> redSpecial;
    
    @SerializedName("blueSpecial")
    private List<Location> blueSpecial;
    
    @SerializedName("arenaSchematic")
    private SchematicData arenaSchematic;
    
    private transient boolean isSetup;

    public Arena(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.type = ArenaType.DUEL; // Default type
        this.gridIndex = 0; // Default grid index
        this.displayName = "&7" + name;
        this.kits = Sets.newConcurrentHashSet();
        this.redSpecial = new ArrayList<>();
        this.blueSpecial = new ArrayList<>();
        this.displayIcon = new ItemStack(Material.PAPER);
        this.deathHeight = -1;
        this.portalRadius = 3;
        
        if (this.displayIcon.hasItemMeta()) {
            ItemMeta meta = this.displayIcon.getItemMeta();
            meta.setDisplayName(this.displayName);
            this.displayIcon.setItemMeta(meta);
        }
    }
    
    public Arena(String name, ArenaType type, int gridIndex) {
        this.id = UUID.randomUUID(); // Generate new UUID
        this.name = name;
        this.type = type;
        this.gridIndex = gridIndex;
        this.displayName = "&7" + name;
        this.kits = Sets.newConcurrentHashSet();
        this.redSpecial = new ArrayList<>();
        this.blueSpecial = new ArrayList<>();
        this.displayIcon = new ItemStack(Material.PAPER);
        this.deathHeight = -1;
        this.portalRadius = 3;
        
        if (this.displayIcon.hasItemMeta()) {
            ItemMeta meta = this.displayIcon.getItemMeta();
            meta.setDisplayName(this.displayName);
            this.displayIcon.setItemMeta(meta);
        }
    }

    public Arena() {
        this.id = UUID.randomUUID(); // Generate new UUID
        this.kits = Sets.newConcurrentHashSet();
        this.redSpecial = new ArrayList<>();
        this.blueSpecial = new ArrayList<>();
        this.displayIcon = new ItemStack(Material.PAPER);
        this.deathHeight = -1;
        this.portalRadius = 3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ItemStack getDisplayIcon() {
        return displayIcon;
    }

    public void setDisplayIcon(ItemStack displayIcon) {
        this.displayIcon = displayIcon;
    }

    public ArenaType getType() {
        return type;
    }

    public void setType(ArenaType type) {
        this.type = type;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public void setSpawn1(Location spawn1) {
        this.spawn1 = spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public void setSpawn2(Location spawn2) {
        this.spawn2 = spawn2;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public void setSpectatorSpawn(Location spectatorSpawn) {
        this.spectatorSpawn = spectatorSpawn;
    }

    public Cuboid getBounds() {
        return bounds;
    }

    public void setBounds(Cuboid bounds) {
        this.bounds = bounds;
    }

    public Set<String> getKits() {
        return ImmutableSet.copyOf(kits);
    }

    public void addKit(Kit kit) {
        this.kits.add(kit.getName().toLowerCase());
    }

    public void removeKit(Kit kit) {
        this.kits.remove(kit.getName().toLowerCase());
    }

    public boolean hasKit(Kit kit) {
        return this.kits.contains(kit.getName().toLowerCase());
    }

    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

    public boolean isRanked() {
        return ranked;
    }

    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    public int getGridIndex() {
        return gridIndex;
    }

    public void setGridIndex(int gridIndex) {
        this.gridIndex = gridIndex;
    }

    public int getBuildHeight() {
        if (buildHeight == 0) {
            Preconditions.checkNotNull(spawn1, "Spawn 1 cannot be null");
            Preconditions.checkNotNull(spawn2, "Spawn 2 cannot be null");
            
            double maxY = Math.max(spawn1.getY(), spawn2.getY());
            return (int) (maxY + 10.0);
        }
        return buildHeight;
    }

    public void setBuildHeight(int buildHeight) {
        this.buildHeight = buildHeight;
    }

    public int getDeathHeight() {
        return deathHeight;
    }

    public void setDeathHeight(int deathHeight) {
        this.deathHeight = deathHeight;
    }

    public int getPortalRadius() {
        return portalRadius;
    }

    public void setPortalRadius(int portalRadius) {
        this.portalRadius = portalRadius;
    }

    public List<Location> getRedSpecial() {
        return redSpecial;
    }

    public void setRedSpecial(List<Location> redSpecial) {
        this.redSpecial = redSpecial;
    }

    public List<Location> getBlueSpecial() {
        return blueSpecial;
    }

    public void setBlueSpecial(List<Location> blueSpecial) {
        this.blueSpecial = blueSpecial;
    }

    public SchematicData getArenaSchematic() {
        return arenaSchematic;
    }

    public void setArenaSchematic(SchematicData arenaSchematic) {
        Preconditions.checkArgument(type != ArenaType.SUMO, "Sumo arenas cannot have schematics");
        Preconditions.checkArgument(type != ArenaType.BRIDGES, "Bridges arenas cannot have schematics");
        
        this.arenaSchematic = arenaSchematic;
    }

    public boolean isSetup() {
        return isSetup;
    }

    public void setSetup(boolean setup) {
        this.isSetup = setup;
    }

    public void setup() {
        this.setSetup(true);
    }

    public boolean isValid() {
        if (spawn1 != null && spawn2 != null && bounds != null) {
            if (enabled && !isSetup) {
                if (type == ArenaType.SUMO) {
                    return true;
                }
                if (type == ArenaType.BRIDGES) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean isReady() {
        return spawn1 != null && spawn2 != null && bounds != null;
    }

    public Location getSpawn1(boolean clone) {
        if (spawn1 == null) {
            return null;
        }
        return clone ? spawn1.clone() : spawn1;
    }

    public Location getSpawn2(boolean clone) {
        if (spawn2 == null) {
            return null;
        }
        return clone ? spawn2.clone() : spawn2;
    }

    public Location getSpawn1Clone() {
        return spawn1 == null ? null : spawn1.clone();
    }

    public Location getSpawn2Clone() {
        return spawn2 == null ? null : spawn2.clone();
    }

    public void forEachChunk(Consumer<Chunk> callback) {
        Preconditions.checkNotNull(bounds, "Bounds cannot be null");
        List<Chunk> chunks = bounds.getChunks();
        Objects.requireNonNull(callback);
        chunks.forEach(callback);
    }

    @Override
    public String toString() {
        return "Arena{" +
                "name='" + name + '\'' +
                ", enabled=" + enabled +
                ", displayName='" + displayName + '\'' +
                ", type=" + type +
                ", gridIndex=" + gridIndex +
                ", buildHeight=" + buildHeight +
                ", deathHeight=" + deathHeight +
                ", portalRadius=" + portalRadius +
                ", isSetup=" + isSetup +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return enabled == arena.enabled &&
                gridIndex == arena.gridIndex &&
                buildHeight == arena.buildHeight &&
                deathHeight == arena.deathHeight &&
                portalRadius == arena.portalRadius &&
                isSetup == arena.isSetup &&
                Objects.equals(name, arena.name) &&
                Objects.equals(displayName, arena.displayName) &&
                Objects.equals(displayIcon, arena.displayIcon) &&
                type == arena.type &&
                Objects.equals(spawn1, arena.spawn1) &&
                Objects.equals(spawn2, arena.spawn2) &&
                Objects.equals(spectatorSpawn, arena.spectatorSpawn) &&
                Objects.equals(bounds, arena.bounds) &&
                Objects.equals(kits, arena.kits) &&
                Objects.equals(redSpecial, arena.redSpecial) &&
                Objects.equals(blueSpecial, arena.blueSpecial) &&
                Objects.equals(arenaSchematic, arena.arenaSchematic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, enabled, displayName, displayIcon, type, spawn1, spawn2, spectatorSpawn, bounds, kits, gridIndex, buildHeight, deathHeight, portalRadius, redSpecial, blueSpecial, arenaSchematic, isSetup);
    }

    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }

    public void setMin(Location min) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMin'");
    }

    public void setMax(Location max) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMax'");
    }

    public Object getMin() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMin'");
    }

    public Object getMax() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMax'");
    }
}