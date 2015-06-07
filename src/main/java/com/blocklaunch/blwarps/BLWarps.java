package com.blocklaunch.blwarps;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.block.tileentity.SignChangeEvent;
import org.spongepowered.api.event.entity.player.PlayerChatEvent;
import org.spongepowered.api.event.entity.player.PlayerInteractBlockEvent;
import org.spongepowered.api.event.entity.player.PlayerMoveEvent;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.event.EventManager;

import com.blocklaunch.blwarps.commands.WarpCommandBuilder;
import com.blocklaunch.blwarps.eventhandlers.PlayerChatEventHandler;
import com.blocklaunch.blwarps.eventhandlers.PlayerInteractBlockEventHandler;
import com.blocklaunch.blwarps.eventhandlers.PlayerMoveEventHandler;
import com.blocklaunch.blwarps.eventhandlers.SignChangeEventHandler;
import com.blocklaunch.blwarps.managers.FlatFileManager;
import com.blocklaunch.blwarps.managers.RestManager;
import com.blocklaunch.blwarps.managers.SqlManager;
import com.blocklaunch.blwarps.managers.StorageManager;
import com.blocklaunch.blwarps.managers.WarpBaseManager;
import com.blocklaunch.blwarps.managers.WarpManager;
import com.blocklaunch.blwarps.managers.WarpRegionManager;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.inject.Inject;

@Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
public class BLWarps {

    /**
     * Prefix to display at the beginning of messages to player, console outputs, etc.
     */
    private Game game;
    private PluginContainer plugin;
    private BLWarpsConfiguration config;

    private Util util = new Util(this);

    private Map<Class<? extends WarpBase>, WarpBaseManager<? extends WarpBase>> warpBaseManagers =
            new HashMap<Class<? extends WarpBase>, WarpBaseManager<? extends WarpBase>>();
    private Map<Class<? extends WarpBase>, StorageManager<? extends WarpBase>> storageManagers =
            new HashMap<Class<? extends WarpBase>, StorageManager<? extends WarpBase>>();
    private Map<Class<? extends WarpBase>, FlatFileManager<? extends WarpBase>> fallbackManagers =
            new HashMap<Class<? extends WarpBase>, FlatFileManager<? extends WarpBase>>();

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File configFile;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    @Subscribe
    public void preInit(PreInitializationEvent event) {
        game = event.getGame();
        plugin = game.getPluginManager().getPlugin(PomData.ARTIFACT_ID).get();

        // Create default config if it doesn't exist
        if (!configFile.exists()) {
            saveDefaultConfig();
        } else {
            loadConfig();
        }

        setupWarpBaseManagers();
        setupStorageManagers();

        storageManagers.get(Warp.class).load();
        storageManagers.get(WarpRegion.class).load();

        registerCommands();
        registerEventHandlers();

    }

    private void registerCommands() {
        logger.info("Registering commands");
        game.getCommandDispatcher().register(plugin, new WarpCommandBuilder(this).mainWarpCommand(), "warp");
    }

    /**
     * Reads in config values supplied from the ConfigManager. Falls back on the default
     * configuration values in Settings
     */
    private void loadConfig() {
        ConfigurationNode rawConfig = null;
        try {
            rawConfig = configLoader.load();
            config = BLWarpsConfiguration.MAPPER.bindToNew().populate(rawConfig);
        } catch (IOException e) {
            logger.warn("The configuration could not be loaded! Using the default configuration");
        } catch (IllegalArgumentException e) {
            // Everything after this is only for stringifying the array of all
            // StorageType values
            StringBuilder sb = new StringBuilder();
            StorageType[] storageTypes = StorageType.values();
            for (int i = 0; i < storageTypes.length; i++) {
                sb.append(storageTypes[i].toString());
                if (i + 1 != storageTypes.length) {
                    sb.append(", ");
                }
            }
            logger.warn("The specified storage type could not be found. Reverting to flatfile storage. Try: " + sb.toString());
        } catch (ObjectMappingException e) {
            logger.warn("There was an loading the configuration." + e.getStackTrace());
        }
    }

    /**
     * Saves a config file with default values if it does not already exist
     * 
     * @return true if default config was successfully created, false if the file was not created
     */
    private void saveDefaultConfig() {
        try {
            if (!configFile.exists()) {
                logger.info("Generating config file...");
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                CommentedConfigurationNode rawConfig = configLoader.load();

                try {
                    // Populate config with default values
                    config = BLWarpsConfiguration.MAPPER.bindToNew().populate(rawConfig);
                    BLWarpsConfiguration.MAPPER.bind(config).serialize(rawConfig);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }

                configLoader.save(rawConfig);
                logger.info("Config file successfully generated.");
            } else {
                return;
            }
        } catch (IOException exception) {
            logger.warn("The default configuration could not be created!");
        }
    }

    private void setupWarpBaseManagers() {
        warpBaseManagers.put(Warp.class, new WarpManager(this));
        warpBaseManagers.put(WarpRegion.class, new WarpRegionManager(this));
    }

    private void setupStorageManagers() {
        File warpsFile = new File(configFile.getParentFile(), "warps.json");
        File warpRegionFile = new File(configFile.getParentFile(), "warp-regions.json");

        switch (config.getStorageType()) {
            case FLATFILE:
                storageManagers.put(Warp.class, new FlatFileManager<Warp>(Warp.class, this, warpsFile));
                storageManagers.put(WarpRegion.class, new FlatFileManager<WarpRegion>(WarpRegion.class, this, warpRegionFile));
                break;
            case REST:
                storageManagers.put(Warp.class, new RestManager<Warp>(Warp.class, this));
                storageManagers.put(WarpRegion.class, new RestManager<WarpRegion>(WarpRegion.class, this));
                break;
            case SQL:
                storageManagers.put(Warp.class, new SqlManager<Warp>(Warp.class, this));
                storageManagers.put(WarpRegion.class, new SqlManager<WarpRegion>(WarpRegion.class, this));
                break;
            default:
                storageManagers.put(Warp.class, new FlatFileManager<Warp>(Warp.class, this, warpsFile));
                storageManagers.put(WarpRegion.class, new FlatFileManager<WarpRegion>(WarpRegion.class, this, warpRegionFile));
                break;
        }

        fallbackManagers.put(Warp.class, new FlatFileManager<Warp>(Warp.class, this, warpsFile));
        fallbackManagers.put(WarpRegion.class, new FlatFileManager<WarpRegion>(WarpRegion.class, this, warpsFile));

    }

    private void registerEventHandlers() {
        EventManager eventManager = game.getEventManager();
        // Filter chat & replace warp names in chat w/ clickable links
        eventManager.register(this, PlayerChatEvent.class, new PlayerChatEventHandler(this));
        // Watch for players right-clicking warp signs
        eventManager.register(this, PlayerInteractBlockEvent.class, new PlayerInteractBlockEventHandler(this));
        // Watch for warp signs being created
        eventManager.register(this, SignChangeEvent.class, new SignChangeEventHandler(this));
        // Watch for player movement (warp regions, cancelling warps)
        eventManager.register(this, PlayerMoveEvent.class, new PlayerMoveEventHandler(this));
    }

    public Logger getLogger() {
        return logger;
    }

    public WarpBaseManager<? extends WarpBase> getWarpBaseManager(Class<? extends WarpBase> clazz) {
        return warpBaseManagers.get(clazz);
    }

    public StorageManager<? extends WarpBase> getStorageManager(Class<? extends WarpBase> clazz) {
        return storageManagers.get(clazz);
    }

    public FlatFileManager<? extends WarpBase> getFallBackManager(Class<? extends WarpBase> clazz) {
        return fallbackManagers.get(clazz);
    }

    public BLWarpsConfiguration getConfig() {
        return config;
    }

    public Game getGame() {
        return game;
    }

    public Util getUtil() {
        return util;
    }

}
