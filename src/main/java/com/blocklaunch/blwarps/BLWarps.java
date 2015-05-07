package com.blocklaunch.blwarps;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.PreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import com.blocklaunch.blwarps.commands.DeleteWarpCommand;
import com.blocklaunch.blwarps.commands.ListWarpsCommand;
import com.blocklaunch.blwarps.commands.SetWarpCommand;
import com.blocklaunch.blwarps.commands.WarpCommand;
import com.blocklaunch.blwarps.commands.WarpCommandElement;
import com.blocklaunch.blwarps.manager.FlatFileManager;
import com.blocklaunch.blwarps.manager.RestManager;
import com.blocklaunch.blwarps.manager.SQLManager;
import com.blocklaunch.blwarps.manager.StorageManager;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.google.inject.Inject;

@Plugin(id = "BLWarps", name = "BLWarps", version = "0.2.1")
public class BLWarps {

	/**
	 * Prefix to display at the beginning of messages to player, console
	 * outputs, etc.
	 */
	public static final String PREFIX = "[Warps]";

	public static Game game;
	public static PluginContainer plugin;
	public static SynchronousScheduler scheduler;
	public static Logger logger = LoggerFactory.getLogger(BLWarps.class);
	public static File configFolder;

	public static File warpsFile;
	public static StorageManager storageManager;
	/**
	 * Fallback flat file manager to save/load warps in case any of the other
	 * storage methods fail to load or save warps
	 */
	public static FlatFileManager fallbackManager = new FlatFileManager();

	@Inject
	@DefaultConfig(sharedRoot = false)
	private File configFile;

	@Inject
	@DefaultConfig(sharedRoot = false)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	@Subscribe
	public void preInit(PreInitializationEvent event) {
		game = event.getGame();
		scheduler = game.getSyncScheduler();
		plugin = game.getPluginManager().getPlugin("BLWarps").get();

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

		registerCommands();

	}

	private void registerCommands() {
		logger.info(PREFIX + " Registering commands");

		HashMap<List<String>, CommandSpec> subCommands = new HashMap<>();
		
		CommandSpec createWarpSubCommand = CommandSpec.builder()
				.setPermission("blwarps.create")
				.setDescription(Texts.of("Set a warp"))
				.setExtendedDescription(Texts.of("Sets a warp at your location, or at the specified coordinates"))
				.setExecutor(new SetWarpCommand())
				.setArguments(GenericArguments.seq(
						GenericArguments.string(Texts.of("name")),
						GenericArguments.optional(GenericArguments.vector3d(Texts.of("position")))))
				.build();
		subCommands.put(Arrays.asList("set", "add"), createWarpSubCommand);
		
//		CommandSpec warpSubCommand = CommandSpec.builder()
//				.setPermission("blwarps.warp")
//				.setDescription(Texts.of("Teleport to a warp location"))
//				.setExtendedDescription(Texts.of("Teleports you to the location of the specified warp."))
//				.setExecutor(wc)
//				.setArguments(GenericArguments.string(Texts.of("name")))
//				.build();
//		subCommands.put(Arrays.asList(""), warpSubCommand);
		
		CommandSpec deleteWarpSubCommand = CommandSpec.builder()
				.setPermission("blwarps.delete")
				.setDescription(Texts.of("Delete a warp"))
				.setExtendedDescription(Texts.of("Deletes the warp with the specified name"))
				.setExecutor(new DeleteWarpCommand())
				.setArguments(GenericArguments.string(Texts.of("name")))
				.build();
		subCommands.put(Arrays.asList("delete", "del"), deleteWarpSubCommand);
		
		CommandSpec listWarpSubCommand = CommandSpec.builder()
				.setPermission("blwarps.list")
				.setDescription(Texts.of("List warps"))
				.setExtendedDescription(Texts.of("Lists all warps, split up into pages. Optionally, specify a page number"))
				.setExecutor(new ListWarpsCommand())
				.setArguments(GenericArguments.optional(GenericArguments.integer(Texts.of("page"))))
				.build();
		subCommands.put(Arrays.asList("list", "ls"), listWarpSubCommand);
		
		CommandSpec mainWarpCommand = CommandSpec
				.builder()
				.setPermission("blwarps.warp")
				.setDescription(Texts.of("Teleport to a warp location"))
				.setExtendedDescription(Texts.of("Teleports you to the location of the specified warp."))
				.setExecutor(new WarpCommand())
				.setArguments(GenericArguments.firstParsing(new WarpCommandElement(Texts.of("warp"))))
				.setChildren(subCommands)
				.build();
		
		game.getCommandDispatcher().register(plugin, mainWarpCommand, "warp");
	}

	/**
	 * Reads in config values supplied from the ConfigManager. Falls back on the
	 * default configuration values in Settings
	 */
	private void loadConfig() {
		ConfigurationNode config = null;
		try {
			config = configManager.load();
			
			// GENERAL SETTINGS
			Settings.warpDelay = config.getNode("warp-delay").getInt();
			Settings.pvpProtect = config.getNode("pvp-protect").getBoolean();
			Settings.storageType = StorageType.valueOf(config.getNode("storage-type").getString().toUpperCase());
			
			// REST SETTINGS
			Settings.RESTURI = new URI(config.getNode("rest","uri").getString());
			Settings.RESTUsername = config.getNode("rest", "username").getString();
			Settings.RESTPassword = config.getNode("rest", "password").getString();
			
			// SQL SETTINGS
			Settings.SQLDatabase = config.getNode("sql", "database").getString();
			Settings.SQLURL = config.getNode("sql", "url").getString();
			Settings.SQLDatabaseName = config.getNode("sql", "database-name").getString();
			Settings.SQLUsername = config.getNode("sql", "username").getString();
			Settings.SQLPassword = config.getNode("sql", "password").getString();
		} catch (IOException e) {
			logger.warn(PREFIX + " The configuration could not be loaded! Using the default configuration");
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
			logger.warn(PREFIX + " The specified storage type could not be found. Reverting to flatfile storage. Try: "
					+ sb.toString());
		} catch (URISyntaxException e) {
			logger.warn(PREFIX + " The specified URI could not be parsed. Reverting to flatfile storage.");
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
				logger.info(PREFIX + " Generating config file...");
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				CommentedConfigurationNode config = configManager.load();

				// Populate config with default values

				// GENERAL SETTINGS
				config.getNode("warp-delay").setComment("Time, in seconds, between initiating a warp and teleporting the player");
				config.getNode("warp-delay").setValue(Settings.warpDelay);
				
				config.getNode("pvp-protect").setComment("Whether or not to cancel a player's warp if they move or get hurt");
				config.getNode("pvp-protect").setValue(Settings.pvpProtect);
				
				config.getNode("storage-type").setValue("The storage solution to store warps in");
				config.getNode("storage-type").setValue(Settings.storageType.toString());

				// REST SETTINGS
				config.getNode("rest").setComment("These settings are only applicable if the 'REST' value is selected in the storage-type field");
				config.getNode("rest", "uri").setValue(Settings.RESTURI.toString());
				config.getNode("rest", "username").setValue(Settings.RESTUsername);
				config.getNode("rest", "password").setValue(Settings.RESTPassword);

				// SQL SETTINGS
				config.getNode("sql").setComment("These settings are only applicable if the 'SQL' value is selected in the storage-type field");
				config.getNode("sql", "database").setValue(Settings.SQLDatabase);
				config.getNode("sql", "url").setValue(Settings.SQLURL);
				config.getNode("sql", "database-name").setValue(Settings.SQLDatabaseName);
				config.getNode("sql", "username").setValue(Settings.SQLUsername);
				config.getNode("sql", "password").setValue(Settings.SQLPassword);

				configManager.save(config);
				logger.info(PREFIX + " Config file successfully generated.");
			} else {
				return;
			}
		} catch (IOException exception) {
			logger.warn(PREFIX + " The default configuration could not be created!");
		}
	}

	private void setupStorageManager() {
		switch (Settings.storageType) {
		case FLATFILE:
			storageManager = new FlatFileManager();
			break;
		case REST:
			storageManager = new RestManager();
			break;
		case SQL:
			storageManager = new SQLManager();
			break;
		default:
			storageManager = new FlatFileManager();
			break;
		}

	}
}