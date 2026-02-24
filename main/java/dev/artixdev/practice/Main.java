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
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
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
import dev.artixdev.practice.managers.AchievementsManager;
import dev.artixdev.practice.managers.KnockbackProfileManager;
import dev.artixdev.practice.managers.DailyRewardManager;
import dev.artixdev.practice.managers.SeasonManager;
import dev.artixdev.practice.managers.FFAManager;
import dev.artixdev.practice.managers.ShopManager;
import dev.artixdev.practice.expansions.hologram.HologramManager;
import dev.artixdev.practice.listeners.FFAListener;
import dev.artixdev.practice.license.LicenseManager;
import dev.artixdev.practice.commands.ArtixLicenseCommand;
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
    private AchievementsManager achievementsManager;
    private KnockbackProfileManager knockbackProfileManager;
    private DailyRewardManager dailyRewardManager;
    private SeasonManager seasonManager;
    private FFAManager ffaManager;
    private ShopManager shopManager;
    private LicenseManager licenseManager;

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

    /** Construtor sem argumentos: Main é o plugin ArtixPractice (carregado pelo Spigot). */
    public Main() {
        instance = this;
        this.plugin = this;
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

    @Override
    public void onEnable() {
        initialize();
    }

    @Override
    public void onDisable() {
        shutdown();
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
            // License validation (config.yml "License:") - must run first; invalid = message + disable plugin
            this.licenseManager = new LicenseManager(this);
            if (!this.licenseManager.runOnEnable()) {
                LOGGER.error("[ArtixPractice] Key Invalida. O plugin sera desativado.");
                if (this.plugin != null) {
                    org.bukkit.Bukkit.getPluginManager().disablePlugin(this.plugin);
                }
                return;
            }
            if (this.plugin != null) {
                registerAllCommands();
            }

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
            // Start announcement broadcast task (every 60 seconds)
            new dev.artixdev.practice.tasks.AnnouncementTask().startTask(this, 20L * 60, 20L * 60);
            // Start filter task for players/arenas/matches (every 5 seconds)
            dev.artixdev.practice.tasks.FilterTask filterTask = new dev.artixdev.practice.tasks.FilterTask(this);
            filterTask.start(this, 100L, 100L);

            // Initialize hologram manager (expansions)
            this.hologramManager = new HologramManager(this);

            // New feature managers
            this.achievementsManager = new AchievementsManager(this);
            this.knockbackProfileManager = new KnockbackProfileManager(this);
            this.dailyRewardManager = new DailyRewardManager(this);
            this.seasonManager = new SeasonManager(this);
            this.ffaManager = new FFAManager(this);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.MainListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new FFAListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.HotbarInteractListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.MatchListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.StatisticsListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.GameplayListener(this.playerManager), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.InventoryClickListener(), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.WorldProtectionListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.WorldListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.CommandListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.EntityDamageListener(), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.EventListener(), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.PotionEffectListener(), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.ProjectileListener(this), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.DamageListener(), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.VelocityListener(), this.plugin);
            this.plugin.getServer().getPluginManager().registerEvents(new dev.artixdev.practice.listeners.EntityPortalListener(), this.plugin);
            this.shopManager = new ShopManager(this);

            LOGGER.info("Artix plugin initialized successfully!");
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Artix plugin!", e);
            throw new RuntimeException("Plugin initialization failed", e);
        }
    }

    /**
     * Register all plugin commands via CommandMap (nothing needed in plugin.yml commands section).
     * All practice-related commands use ArtixCommandArg; artixlicense uses ArtixLicenseCommand.
     */
    private void registerAllCommands() {
        try {
            if (!(Bukkit.getServer().getPluginManager() instanceof SimplePluginManager)) {
                LOGGER.warn("PluginManager is not SimplePluginManager, cannot register commands dynamically.");
                return;
            }
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());
            String fallback = plugin.getName().toLowerCase(java.util.Locale.ROOT);

            dev.artixdev.practice.commands.ArtixCommandArg artixCommands = new dev.artixdev.practice.commands.ArtixCommandArg(this);
            ArtixLicenseCommand licenseCommand = new ArtixLicenseCommand(this);

            registerCommand(commandMap, fallback, "practice", artixCommands, Arrays.asList("artixpractice"), "Main practice command");
            registerCommand(commandMap, fallback, "party", artixCommands, Arrays.asList("p"), "Party management");
            registerCommand(commandMap, fallback, "accept", artixCommands, Collections.emptyList(), "Accept duel/party");
            registerCommand(commandMap, fallback, "shop", artixCommands, Collections.emptyList(), "Open shop");
            registerCommand(commandMap, fallback, "cosmetics", artixCommands, Collections.emptyList(), "Cosmetics menu");
            registerCommand(commandMap, fallback, "queue", artixCommands, Collections.emptyList(), "Queue");
            registerCommand(commandMap, fallback, "silent", artixCommands, Collections.emptyList(), "Toggle silent mode");
            registerCommand(commandMap, fallback, "hologram", artixCommands, Collections.emptyList(), "Hologram management");
            registerCommand(commandMap, fallback, "botkb", artixCommands, Arrays.asList("botknockback"), "Bot knockback profiles");
            registerCommand(commandMap, fallback, "togglev", artixCommands, Arrays.asList("visibility", "tv", "togglevisibility", "toggleplayers"), "Toggle player visibility");
            registerCommand(commandMap, fallback, "toggleduels", artixCommands, Arrays.asList("tdl", "tduels", "tdr", "toggleduelrequests"), "Toggle duel requests");
            registerCommand(commandMap, fallback, "postmatch", artixCommands, Arrays.asList("matchinv"), "Post-match inventory");
            registerCommand(commandMap, fallback, "artixlicense", licenseCommand, Collections.emptyList(), "License (config)");

            registerCommand(commandMap, fallback, "config", new dev.artixdev.practice.commands.ConfigCommand(this), Collections.emptyList(), "Configuration");
            registerCommand(commandMap, fallback, "animatedtitle", new dev.artixdev.practice.commands.AnimatedTitleCommand(this), Collections.emptyList(), "Scoreboard title animation");

            LOGGER.info("Registered all commands (ArtixCommandArg, Config, AnimatedTitle, License).");
        } catch (Exception e) {
            LOGGER.error("Failed to register commands dynamically", e);
        }
    }

    private void registerCommand(CommandMap commandMap, String fallback, String name,
                                 org.bukkit.command.CommandExecutor executor,
                                 java.util.List<String> aliases, String description) {
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            PluginCommand cmd = c.newInstance(name, plugin);
            cmd.setExecutor(executor);
            cmd.setAliases(aliases);
            cmd.setDescription(description != null ? description : "");
            cmd.setUsage("/" + name);
            commandMap.register(fallback, cmd);
        } catch (Exception e) {
            LOGGER.error("Failed to register command /" + name, e);
        }
    }

    /**
     * Shutdown all managers
     */
    public void shutdown() {
        try {
            if (licenseManager != null) {
                licenseManager.onDisable();
            }
            // Shutdown managers in reverse order
            if (scoreboardManager != null) {
                scoreboardManager.shutdown();
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

            SCHEDULER.shutdown();
            try {
                if (!SCHEDULER.awaitTermination(3, java.util.concurrent.TimeUnit.SECONDS)) {
                    SCHEDULER.shutdownNow();
                }
            } catch (InterruptedException e) {
                SCHEDULER.shutdownNow();
                Thread.currentThread().interrupt();
            }
            
            LOGGER.info("Artix plugin shutdown complete!");
            
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

    /** Shared scheduled executor for async/repeating tasks. Shut down on plugin disable. */
    public static ScheduledExecutorService getScheduler() {
        return SCHEDULER;
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

    public AchievementsManager getAchievementsManager() {
        return achievementsManager;
    }

    public KnockbackProfileManager getKnockbackProfileManager() {
        return knockbackProfileManager;
    }

    public DailyRewardManager getDailyRewardManager() {
        return dailyRewardManager;
    }

    public SeasonManager getSeasonManager() {
        return seasonManager;
    }

    public FFAManager getFFAManager() {
        return ffaManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public LicenseManager getLicenseManager() {
        return licenseManager;
    }
}