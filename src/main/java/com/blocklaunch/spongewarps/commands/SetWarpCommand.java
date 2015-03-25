package com.blocklaunch.spongewarps.commands;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.blocklaunch.spongewarps.WarpManager;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;

public class SetWarpCommand implements CommandCallable {

	private static final String USAGE = "/setwarp <warp name> [world name] [x] [y] [z]";
	private static final String HELP = "Sets a warp at your location, or at the specified coordinates";

	private static final String MUST_BE_PLAYER_MSG = SpongeWarps.PREFIX	+ " You must be a player to send that command (not console)";
	private static final String SUCCESSFULLY_CREATED_WARP_MSG = SpongeWarps.PREFIX + " You have successfully created a warp: ";
	private static final String ERROR_CREATING_WARP_MSG = SpongeWarps.PREFIX + " There was an error creating the warp: ";
	private static final String INVALID_NUM_ARGS = SpongeWarps.PREFIX + " There is an invalid number of arguments. Try: " + USAGE;
	private static final String ERROR_PARSING_NUMBER = SpongeWarps.PREFIX + " There was an error parsing the warp coordinates.";

	@Override
	public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
		// Check if the arguments String is empty before splitting b/c in the
		// event it is empty, the split will still return an array of size 1
		// containing the empty String
		if (arguments.isEmpty()) {
			source.sendMessage(Texts.builder(INVALID_NUM_ARGS).color(TextColors.RED).build());
			return false;
		}

		// Parse the arguments into an array
		String[] args = arguments.split(" ");

		Player player = null;
		// Make sure the source is a player before attempting to get their
		// location

		Warp newWarp = null;

		// One argument --> Set warp at the player's location with the given
		// name
		if (args.length == 1) {
			if (source instanceof Player) {
				player = (Player) source;
			} else {
				source.sendMessage(Texts.builder(MUST_BE_PLAYER_MSG).color(TextColors.RED).build());
				return false;
			}
			Vector3d location = player.getLocation().getPosition();

			String worldName = player.getWorld().getName();
			String warpName = args[0];
			double warpX = location.getX();
			double warpY = location.getY();
			double warpZ = location.getZ();
			newWarp = new Warp(warpName, worldName, warpX, warpY, warpZ);

			// If there are 4 arguments --> set warp at the given
			// coordinates, but the command must still be sent from a player to
			// get the world they are in
		} else if (args.length == 4) {
			if (source instanceof Player) {
				player = (Player) source;
			} else {
				source.sendMessage(Texts.builder(MUST_BE_PLAYER_MSG).color(TextColors.RED).build());
				return false;
			}

			String warpName = args[0];
			String worldName = player.getWorld().getName();
			double warpX, warpY, warpZ;
			try {
				warpX = Integer.parseInt(args[1]);
				warpY = Integer.parseInt(args[2]);
				warpZ = Integer.parseInt(args[3]);
			} catch (Exception e) {
				source.sendMessage(Texts.builder(ERROR_PARSING_NUMBER).color(TextColors.RED).build());
				return false;
			}

			newWarp = new Warp(warpName, worldName, warpX, warpY, warpZ);
			// If there are 5 arguments --> set warp at the given
			// coordinates and world name. Only command that can be sent from
			// the console
		} else if (args.length == 5) {
			String warpName = args[0];
			String worldName = args[1];
			double warpX, warpY, warpZ;
			try {
				warpX = Integer.parseInt(args[2]);
				warpY = Integer.parseInt(args[3]);
				warpZ = Integer.parseInt(args[4]);
			} catch (Exception e) {
				source.sendMessage(Texts.builder(ERROR_PARSING_NUMBER).color(TextColors.RED).build());
				return false;
			}
			newWarp = new Warp(warpName, worldName, warpX, warpY, warpZ);

		} else {
			source.sendMessage(Texts.builder(INVALID_NUM_ARGS).color(TextColors.RED).build());
			return false;
		}

		Optional<String> error = WarpManager.addWarp(newWarp);
		if (error.isPresent()) {
			source.sendMessage(Texts.builder(ERROR_CREATING_WARP_MSG + error.get()).color(TextColors.RED).build());
			return false;
		} else {
			source.sendMessage(Texts.builder(SUCCESSFULLY_CREATED_WARP_MSG + newWarp.toString())
					.color(TextColors.GREEN).build());
			return true;
		}

	}

	@Override
	public boolean testPermission(CommandSource source) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<String> getShortDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getHelp() {
		return Optional.of(HELP);
	}

	@Override
	public String getUsage() {
		return USAGE;
	}

}
