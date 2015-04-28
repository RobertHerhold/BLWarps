package com.blocklaunch.blwarps.commands;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.google.common.base.Optional;

public class WarpCommand implements CommandCallable {

	private static final Text USAGE = Texts.of("/warp <warp name>");
	private static final Text HELP = Texts.of("Teleports you to the location of the specified warp.");
	private static final Text SHORT_DESC = Texts.of("Teleport to a warp location");

	private static final Text MUST_BE_PLAYER_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
			+ " You must be a player to send that command (not console)");
	private static final Text INVALID_NUM_ARGS_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
			+ " There is an invalid number of arguments. Try: " + USAGE);
	private static final Text WARP_NOT_EXIST_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
			+ " That warp does not exist!");
	private static final String ERROR_WARPING_MSG = BLWarps.PREFIX + " There was an error scheduling your warp: ";

	@Override
	public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<CommandResult> process(CommandSource source, String arguments) throws CommandException {
		if (!(source instanceof Player)) {
			source.sendMessage(MUST_BE_PLAYER_MSG);
			return Optional.of(CommandResult.empty());
		}

		// Check if the arguments String is empty before splitting b/c in the
		// event it is empty, the split will still return an array of size 1
		// containing the empty String
		if (arguments.isEmpty()) {
			source.sendMessage(INVALID_NUM_ARGS_MSG);
			return Optional.of(CommandResult.empty());
		}

		// Parse the arguments into an array
		String[] args = arguments.split(" ");

		if (args.length != 1) {
			source.sendMessage(INVALID_NUM_ARGS_MSG);
		}

		// Get the warp by it's name
		Optional<Warp> optWarp = WarpManager.getWarp(args[0]);
		if (!optWarp.isPresent()) {
			source.sendMessage(WARP_NOT_EXIST_MSG);
			return Optional.of(CommandResult.empty());
		}

		Warp warp = optWarp.get();

		Player player = (Player) source;

		Optional<String> optError = WarpManager.scheduleWarp(player, warp);

		if(optError.isPresent()){
			player.sendMessage(Texts.of(TextColors.RED, ERROR_WARPING_MSG + optError.get()));
			return Optional.of(CommandResult.empty());
		}

		return Optional.of(CommandResult.success());
	}

	@Override
	public Optional<Text> getHelp(CommandSource arg0) {
		return Optional.of(HELP);
	}

	@Override
	public Optional<Text> getShortDescription(CommandSource arg0) {
		return Optional.of(SHORT_DESC);
	}

	@Override
	public Text getUsage(CommandSource arg0) {
		return USAGE;
	}

	@Override
	public boolean testPermission(CommandSource source) {
		// TODO Auto-generated method stub
		return false;
	}

}
