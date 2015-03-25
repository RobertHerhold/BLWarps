package com.blocklaunch.spongewarps.commands;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.world.Location;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.blocklaunch.spongewarps.WarpManager;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;

public class WarpCommand implements CommandCallable {

	private static final String USAGE = "/warp <warp name>";
	private static final String HELP = "Teleports you to the location of the specified warp.";

	private static final String MUST_BE_PLAYER_MSG = SpongeWarps.PREFIX	+ " You must be a player to send that command (not console)";
	private static final String INVALID_NUM_ARGS = SpongeWarps.PREFIX + " There is an invalid number of arguments. Try: " + USAGE;
	private static final String WARP_NOT_EXIST = SpongeWarps.PREFIX + " That warp does not exist!";
	private static final String WARP_SUCCESS = SpongeWarps.PREFIX + " You have been warped to ";

	@Override
	public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
		if (!(source instanceof Player)) {
			source.sendMessage(Texts.builder(MUST_BE_PLAYER_MSG).color(TextColors.RED).build());
			return false;
		}

		// Check if the arguments String is empty before splitting b/c in the
		// event it is empty, the split will still return an array of size 1
		// containing the empty String
		if (arguments.isEmpty()) {
			source.sendMessage(Texts.builder(INVALID_NUM_ARGS).color(TextColors.RED).build());
			return false;
		}

		// Parse the arguments into an array
		String[] args = arguments.split(" ");

		if (args.length != 1) {
			source.sendMessage(Texts.builder(INVALID_NUM_ARGS).color(TextColors.RED).build());
		}

		// Get the warp by it's name
		Optional<Warp> optWarp = WarpManager.getWarp(args[0]);
		if (!optWarp.isPresent()) {
			source.sendMessage(Texts.builder(WARP_NOT_EXIST).color(TextColors.RED).build());
		}

		Warp warp = optWarp.get();

		Player player = (Player) source;

		// Get old location of player --> insert new coordinates --> set
		// player's location
		// Does not support warping across worlds
		Location oldLoc = player.getLocation();
		Location newLoc = oldLoc.setPosition(new Vector3d(warp.getX(), warp.getY(), warp.getZ()));
		player.setLocation(newLoc);

		player.sendMessage(Texts.builder(WARP_SUCCESS + warp.getName()).color(TextColors.GREEN).build());

		return true;
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
