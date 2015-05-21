package com.blocklaunch.blwarps;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
import org.spongepowered.api.service.config.DefaultConfig;
import org.spongepowered.api.service.scheduler.SynchronousScheduler;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import com.blocklaunch.blwarps.commands.DeleteWarpCommand;
import com.blocklaunch.blwarps.commands.GroupOperation;
import com.blocklaunch.blwarps.commands.ListWarpsCommand;
import com.blocklaunch.blwarps.commands.SetWarpCommand;
import com.blocklaunch.blwarps.commands.WarpCommand;
import com.blocklaunch.blwarps.commands.WarpCommandElement;
import com.blocklaunch.blwarps.commands.WarpGroupCommand;
import com.blocklaunch.blwarps.commands.WarpGroupCommandElement;
import com.blocklaunch.blwarps.manager.FlatFileManager;
import com.blocklaunch.blwarps.manager.RestManager;
import com.blocklaunch.blwarps.manager.SqlManager;
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
		registerCommands();

	}

	private void registerCommands() {
		logger.info(PREFIX + " Registering commands");

		HashMap<List<String>, CommandSpec> subCommands = new HashMap<>();
		
		CommandSpec createWarpSubCommand = CommandSpec.builder()
				.permission("blwarps.create")
				.description(Texts.of("Set a warp"))
				.extendedDescription(Texts.of("Sets a warp at your location, or at the specified coordinates"))
				.executor(new SetWarpCommand())
				.arguments(GenericArguments.seq(
						GenericArguments.string(Texts.of("name")),
						GenericArguments.optional(GenericArguments.vector3d(Texts.of("position")))))
				.build();
		subCommands.put(Arrays.asList("set", "add"), createWarpSubCommand);
		
		CommandSpec deleteWarpSubCommand = CommandSpec.builder()
				.permission("blwarps.delete")
				.description(Texts.of("Delete a warp"))
				.extendedDescription(Texts.of("Deletes the warp with the specified name"))
				.executor(new DeleteWarpCommand())
				.arguments(GenericArguments.string(Texts.of("name")))
				.build();
		subCommands.put(Arrays.asList("delete", "del"), deleteWarpSubCommand);
		
		CommandSpec listWarpSubCommand = CommandSpec.builder()
				.permission("blwarps.list")
				.description(Texts.of("List warps"))
				.extendedDescription(Texts.of("Lists all warps, split up into pages. Optionally, specify a page number"))
				.executor(new ListWarpsCommand())
				.arguments(GenericArguments.optional(GenericArguments.integer(Texts.of("page"))))
				.build();
		subCommands.put(Arrays.asList("list", "ls"), listWarpSubCommand);
		
		CommandSpec groupSubCommand = CommandSpec
				.builder()
				.permission("blwarps.group")
				.description(Texts.of("Manage warp groups"))
				.extendedDescription(Texts.of("Create and add warps to groups"))
				.executor(new WarpGroupCommand())
				.arguments(
						GenericArguments.enumValue(Texts.of("operation"),
								GroupOperation.class),
						GenericArguments.optional(GenericArguments
								.firstParsing(new WarpCommandElement(Texts
										.of("warp")))),
						new WarpGroupCommandElement(Texts.of("group"))).build();
		subCommands.put(Arrays.asList("group"), groupSubCommand);
		
		CommandSpec mainWarpCommand = CommandSpec.builder()
				.permission("blwarps.warp")
				.description(Texts.of("Teleport to a warp location"))
				.extendedDescription(Texts.of("Teleports you to the location of the specified warp."))
				.executor(new WarpCommand())
				.arguments(GenericArguments.firstParsing(new WarpCommandElement(Texts.of("warp"))))
				.children(subCommands)
				.build();
		
		game.getCommandDispatcher().register(plugin, mainWarpCommand, "warp");
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
			storageManager = new SqlManager(this);
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