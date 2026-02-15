package dev.artixdev.practice;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import me.drizzy.api.chunk.IChunkAdapter;
import me.drizzy.api.hider.EntityHider;
import me.drizzy.api.nms.INMSImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEventsAPI;
import dev.artixdev.libs.com.google.gson.Gson;
import dev.artixdev.libs.com.google.gson.GsonBuilder;
import dev.artixdev.libs.com.google.gson.LongSerializationPolicy;
import dev.artixdev.practice.managers.ArenaManager;
import dev.artixdev.practice.managers.PlayerManager;
import dev.artixdev.practice.managers.BotManager;
import dev.artixdev.practice.managers.DatabaseManager;
import dev.artixdev.practice.managers.MenuManager;
import dev.artixdev.practice.managers.HotbarManager;
import dev.artixdev.practice.managers.PartyManager;
import dev.artixdev.practice.managers.QueueManager;
import dev.artixdev.practice.managers.MatchManager;
import dev.artixdev.practice.managers.KitManager;
import dev.artixdev.practice.managers.RankManager;
import dev.artixdev.practice.managers.AnnouncementManager;
import dev.artixdev.practice.expansions.hologram.HologramManager;
import dev.artixdev.practice.utils.TablistManager;
import dev.artixdev.practice.utils.ScoreboardManager;
import dev.artixdev.practice.storage.StorageManager;
import dev.artixdev.practice.configs.SettingsConfig;

public class Main extends JavaPlugin {
    
    private static Main instance;
    protected static final String VERSION;
    private final JavaPlugin plugin;
    private IChunkAdapter chunkAdapter;
    private boolean isPacketEventsEnabled;
    private EntityHider entityHider;
    public static final Gson GSON;
    private INMSImpl nmsHandler;
    private static final Logger LOGGER;
    private static final ScheduledExecutorService SCHEDULER;
    public static final int PROTOCOL_VERSION;
    private StorageManager storageManager;
    private ArenaManager arenaManager;
    private PlayerManager playerManager;
    private BotManager botManager;
    private DatabaseManager databaseManager;
    private MenuManager menuManager;
    private HotbarManager hotbarManager;
    private TablistManager tablistManager;
    private ScoreboardManager scoreboardManager;
    private ConfigManager configManager;
    private SettingsConfig settingsConfig;
    private KitManager kitManager;
    private RankManager rankManager;
    private AnnouncementManager announcementManager;
    private HologramManager hologramManager;

    static {
        LOGGER = LogManager.getLogger(Main.class);
        Server server = Bukkit.getServer();
        Class<?> serverClass = server.getClass();
        String packageName = serverClass.getPackage().getName();
        VERSION = packageName.split("\\.")[3];
        PROTOCOL_VERSION = Integer.parseInt(VERSION.split("_")[1]);
        SCHEDULER = Executors.newSingleThreadScheduledExecutor();
        GsonBuilder builder = new GsonBuilder();
        builder = builder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        builder = builder.setPrettyPrinting().serializeNulls();
        GSON = builder.create();
    }

    public Main(JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;
    }

    public static Main getInstance() {
        return instance;
    }

    public IChunkAdapter getChunkAdapter() {
        return chunkAdapter;
    }

    public boolean isPacketEventsEnabled() {
        return isPacketEventsEnabled;
    }

    public boolean isEntityHiderRequired() {
        // Check if entity hider is required based on server version
        return PROTOCOL_VERSION <= 8;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void initializeChunkAdapter() {
        // Initialize chunk adapter based on server version
        // This would need to be implemented based on the actual server version
        // For now, we'll leave it null and implement version detection later
        this.chunkAdapter = null;
    }

    public void initializeNMS() {
        // Initialize NMS handler based on server version
        // This would need to be implemented based on the actual server version
        // For now, we'll leave it null and implement version detection later
        this.nmsHandler = null;
    }

    public INMSImpl getNMSHandler() {
        return nmsHandler;
    }


    public void initializeEntityHider() throws IllegalArgumentException {
        // Initialize entity hider if required
        if (isEntityHiderRequired()) {
            this.entityHider = new EntityHider(this.plugin, this.nmsHandler);
        }
    }

    public EntityHider getEntityHider() {
        return entityHider;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public void setArenaManager(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public void setSettingsConfig(SettingsConfig settingsConfig) {
        this.settingsConfig = settingsConfig;
    }
    
    /**
     * Initialize all managers and configurations
     */
    public void initialize() {
        try {
            // Initialize configuration manager
            this.configManager = new ConfigManager(this);
            this.configManager.initializeConfigs();
            
            // Validate configurations
            if (!this.configManager.validateConfigs()) {
                LOGGER.warn("Some configurations have validation issues!");
            }
            
            // Initialize database manager
            this.databaseManager = new DatabaseManager(this);
            this.databaseManager.initialize();
            
            // Initialize storage manager
            this.storageManager = new StorageManager(this);
            
            // Initialize player manager
            this.playerManager = new PlayerManager(this);
            
            // Initialize arena manager
            this.arenaManager = new ArenaManager(this);
            this.arenaManager.initialize();
            
            // Initialize bot manager
            this.botManager = new BotManager(this);
            this.botManager.initialize();
            
            // Initialize menu manager
            this.menuManager = new MenuManager(this);
            this.menuManager.initialize();
            
            // Initialize hotbar manager
            this.hotbarManager = new HotbarManager(this);
            this.hotbarManager.initialize();
            
            // Initialize tablist manager
            this.tablistManager = new TablistManager(this);
            this.tablistManager.initialize();
            
            // Initialize scoreboard manager
            this.scoreboardManager = new ScoreboardManager(this);
            this.scoreboardManager.initialize();
            
            // Initialize party manager
            this.partyManager = new PartyManager(this);
            this.partyManager.initialize();
            
            // Initialize queue manager
            this.queueManager = new QueueManager(this);
            this.queueManager.initialize();
            
            // Initialize match manager
            this.matchManager = new MatchManager(this);
            this.matchManager.initialize();
            
            // Initialize kit manager
            this.kitManager = new KitManager(this);
            
            // Initialize rank manager
            this.rankManager = new RankManager();
            
            // Initialize announcement manager
            this.announcementManager = new AnnouncementManager(this);
            this.announcementManager.initialize();

            // Initialize hologram manager (expansions)
            this.hologramManager = new HologramManager(this);
            
            LOGGER.info("Bolt plugin initialized successfully!");
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Bolt plugin!", e);
            throw new RuntimeException("Plugin initialization failed", e);
        }
    }
    
    /**
     * Shutdown all managers
     */
    public void shutdown() {
        try {
            // Shutdown managers in reverse order
            if (scoreboardManager != null) {
                // ScoreboardManager cleanup if needed
            }
            
            if (tablistManager != null) {
                tablistManager.shutdown();
            }
            
            if (hotbarManager != null) {
                hotbarManager.shutdown();
            }
            
            if (menuManager != null) {
                menuManager.shutdown();
            }
            
            if (botManager != null) {
                botManager.shutdown();
            }
            
            if (arenaManager != null) {
                arenaManager.shutdown();
            }
            
            if (playerManager != null) {
                playerManager.shutdown();
            }
            
            if (storageManager != null) {
                // StorageManager cleanup if needed
            }
            
            if (databaseManager != null) {
                databaseManager.shutdown();
            }
            
            LOGGER.info("Bolt plugin shutdown complete!");
            
        } catch (Exception e) {
            LOGGER.error("Error during plugin shutdown!", e);
        }
    }
    
    // Getters for managers
    public BotManager getBotManager() {
        return botManager;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public MenuManager getMenuManager() {
        return menuManager;
    }
    
    public HotbarManager getHotbarManager() {
        return hotbarManager;
    }
    
    public TablistManager getTablistManager() {
        return tablistManager;
    }
    
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    private QueueManager queueManager;
    private MatchManager matchManager;
    
    public QueueManager getQueueManager() {
        return queueManager;
    }
    
    public void setQueueManager(QueueManager queueManager) {
        this.queueManager = queueManager;
    }
    
    public MatchManager getMatchManager() {
        return matchManager;
    }
    
    public void setMatchManager(MatchManager matchManager) {
        this.matchManager = matchManager;
    }
    
    public KitManager getKitManager() {
        return kitManager;
    }
    
    public void setKitManager(KitManager kitManager) {
        this.kitManager = kitManager;
    }
    
    private PartyManager partyManager;
    
    public PartyManager getPartyManager() {
        return partyManager;
    }
    
    public void setPartyManager(PartyManager partyManager) {
        this.partyManager = partyManager;
    }
    
    /**
     * Get RankManager
     * @return RankManager instance
     */
    public RankManager getRankManager() {
        if (rankManager == null) {
            rankManager = new RankManager();
        }
        return rankManager;
    }
    
    /**
     * Get Gson instance
     * @return Gson instance
     */
    public Gson getGson() {
        return GSON;
    }
    
    /**
     * Get AnnouncementManager
     * @return AnnouncementManager instance
     */
    public AnnouncementManager getAnnouncementManager() {
        return announcementManager;
    }

    /**
     * Get HologramManager (expansions).
     * @return HologramManager instance
     */
    public HologramManager getHologramManager() {
        return hologramManager;
    }
}