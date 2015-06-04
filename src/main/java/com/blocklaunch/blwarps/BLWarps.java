package com.blocklaunch.blwarps;

import java.io.File;
import java.io.IOException;

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
import com.blocklaunch.blwarps.managers.WarpManager;
import com.google.inject.Inject;

@Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
public class BLWarps {

    /**
     * Prefix to display at the beginning of messages to player, console outputs, etc.
     */
    public static final String PREFIX = "[BLWarps]";
    private Game game;
    private PluginContainer plugin;
    private BLWarpsConfiguration config;

    private Util util = new Util(this);
    private WarpManager warpManager = new WarpManager(this);
    private StorageManager storageManager;
    /**
     * Fallback flat file manager to save/load warps in case any of the other storage methods fail
     * to load or save warps
     */
    private FlatFileManager fallbackManager;

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

        setupStorageManager();
        storageManager.loadWarps();
        registerCommands();
        registerEventHandlers();

    }

    private void registerCommands() {
        logger.info(PREFIX + " Registering commands");
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
            logger.warn(PREFIX + " There was an loading the configuration." + e.getStackTrace());
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
                logger.info(PREFIX + " Config file successfully generated.");
            } else {
                return;
            }
        } catch (IOException exception) {
            logger.warn("The default configuration could not be created!");
        }
    }

    private void setupStorageManager() {
        File warpsFile = new File(configFile.getParentFile(), "warps.json");
        switch (config.getStorageType()) {
            case FLATFILE:
                storageManager = new FlatFileManager(warpsFile, this);
                break;
            case REST:
                storageManager = new RestManager(this);
                break;
            case SQL:
                storageManager = new SqlManager(this);
                break;
            default:
                storageManager = new FlatFileManager(warpsFile, this);
                break;
        }

        fallbackManager = new FlatFileManager(warpsFile, this);

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

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public StorageManager getFallBackManager() {
        return fallbackManager;
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
