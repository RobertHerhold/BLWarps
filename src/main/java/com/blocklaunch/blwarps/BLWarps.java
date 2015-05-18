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
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;

import com.blocklaunch.blwarps.PomData;
import com.blocklaunch.blwarps.commands.DeleteWarpCommand;
import com.blocklaunch.blwarps.commands.ListWarpsCommand;
import com.blocklaunch.blwarps.commands.SetWarpCommand;
import com.blocklaunch.blwarps.commands.WarpCommand;
import com.blocklaunch.blwarps.manager.FlatFileManager;
import com.blocklaunch.blwarps.manager.RestManager;
import com.blocklaunch.blwarps.manager.SQLManager;
import com.blocklaunch.blwarps.manager.StorageManager;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.google.inject.Inject;

@Plugin(id = PomData.ARTIFACT_ID, name = PomData.NAME, version = PomData.VERSION)
public class BLWarps {

	/**
	 * Prefix to display at the beginning of messages to player, console
	 * outputs, etc.
	 */
	public static final String PREFIX = "[BLWarps]";
	public static Game game;
	public static PluginContainer plugin;
	public static SynchronousScheduler scheduler;
	public static File configFolder;
	public static BLWarpsConfiguration config;

	public static File warpsFile;
	public static StorageManager storageManager;
	/**
	 * Fallback flat file manager to save/load warps in case any of the other
	 * storage methods fail to load or save warps
	 */
	private FlatFileManager fallbackManager = new FlatFileManager(this);

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
		scheduler = game.getSyncScheduler();
		plugin = game.getPluginManager().getPlugin(PomData.ARTIFACT_ID).get();

		configFolder = configFile.getParentFile();
		warpsFile = new File(BLWarps.configFolder, "warps.json");

		// Create default config if it doesn't exist
		if (!configFile.exists()) {
			saveDefaultConfig();
		} else {
			loadConfig();
		}

		setupStorageManager();

		WarpManager.loadWarps();

		// Register commands
		CommandService cmdService = game.getCommandDispatcher();
		logger.info("Registering commands");
		cmdService.register(plugin, new SetWarpCommand(), "setwarp", "addwarp");
		cmdService.register(plugin, new WarpCommand(), "warp");
		cmdService.register(plugin, new DeleteWarpCommand(), "deletewarp", "delwarp");
		cmdService.register(plugin, new ListWarpsCommand(), "listwarps", "listwarp");
	}

	/**
	 * Reads in config values supplied from the ConfigManager. Falls back on the
	 * default configuration values in Settings
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
			logger.warn("The specified storage type could not be found. Reverting to flatfile storage. Try: "
					+ sb.toString());
		} catch (ObjectMappingException e) {
			logger.warn(PREFIX + " There was an loading the configuration." + e.getStackTrace());
		}
	}

	/**
	 * Saves a config file with default values if it does not already exist
	 * 
	 * @return true if default config was successfully created, false if the
	 *         file was not created
	 */
	public void saveDefaultConfig() {
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
		switch (config.getStorageType()) {
		case FLATFILE:
			storageManager = new FlatFileManager(this);
			break;
		case REST:
			storageManager = new RestManager(this);
			break;
		case SQL:
			storageManager = new SQLManager(this);
			break;
		default:
			storageManager = new FlatFileManager(this);
			break;
		}

	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public StorageManager getFallBackManager() {
		return fallbackManager;
	}
}