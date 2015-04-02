package com.blocklaunch.spongewarps;

import java.io.File;
import java.io.IOException;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.command.CommandService;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;
import org.spongepowered.api.util.event.Subscribe;

import com.blocklaunch.spongewarps.commands.DeleteWarpCommand;
import com.blocklaunch.spongewarps.commands.ListWarpsCommand;
import com.blocklaunch.spongewarps.commands.SetWarpCommand;
import com.blocklaunch.spongewarps.commands.WarpCommand;
import com.google.inject.Inject;

@Plugin(id = "SpongeWarps", name = "SpongeWarps", version = "1.0")
public class SpongeWarps {

	/**
	 * Prefix to display at the beginning of messages to player, console
	 * outputs, etc.
	 */
	public static final String PREFIX = "[Warps]";

	public static Game game;
	public static PluginContainer plugin;
	public static SynchronousScheduler scheduler;

	public static Logger logger = LoggerFactory.getLogger(SpongeWarps.class);
	public static File configFolder;
	public static File warpsFile;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private File configFile;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	/**
	 * Called when the server is being started. Similar to Bukkit's onEnable()
	 * function
	 * 
	 * @param event
	 */
	@Subscribe
	public void preInit(PreInitializationEvent event) {
		game = event.getGame();
		scheduler = game.getSyncScheduler();
		plugin = game.getPluginManager().getPlugin("SpongeWarps").get();

		configFolder = configFile.getParentFile();
		warpsFile = new File(SpongeWarps.configFolder, "warps.json");
		
		// Create default config if it doesn't exist
		if (!configFile.exists()) {
			saveDefaultConfig();
		} else {
			loadConfig();
		}

		// Load warps
		WarpManager.loadWarps();

		// Register commands
		CommandService cmdService = game.getCommandDispatcher();
		logger.info(PREFIX + " Registering commands");
		cmdService.register(plugin, new SetWarpCommand(), "setwarp", "addwarp");
		cmdService.register(plugin, new WarpCommand(), "warp");
		cmdService.register(plugin, new DeleteWarpCommand(), "deletewarp", "delwarp");
		cmdService.register(plugin, new ListWarpsCommand(), "listwarps", "listwarp");
	}

	private void loadConfig() {
		ConfigurationNode config = null;
		try {
			config = configManager.load();
			Settings.warpDelay = config.getNode("warp-delay").getInt();
			Settings.pvpProtect = config.getNode("pvp-protect").getBoolean();
		} catch (IOException e) {
			logger.warn(PREFIX + " The configuration could not be loaded! Using the default configuration");

		}
	}

	/**
	 * Saves a config file with default values if it does not already exist
	 * 
	 * @return true if default config was successfully created, false if the
	 *         file was not created
	 */
	public boolean saveDefaultConfig() {
		if (configFile.exists())
			return false;

		try {
			if (!configFile.exists()) {
				logger.info(PREFIX + " Generating config file...");
				configFile.createNewFile();
				ConfigurationNode config = configManager.load();

				// Populate config with default values
				config.getNode("warp-delay").setValue(Settings.warpDelay);
				config.getNode("pvp-protect").setValue(Settings.pvpProtect);

				configManager.save(config);
				logger.info(PREFIX + " Config file successfully generated.");
			}
		} catch (IOException exception) {
			logger.warn(PREFIX + " The default configuration could not be created!");
			return false;
		}
		return true;
	}
}