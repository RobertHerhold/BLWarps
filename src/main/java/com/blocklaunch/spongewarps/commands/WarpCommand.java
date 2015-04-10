package com.blocklaunch.spongewarps.commands;

import java.util.List;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.Warp;
import com.blocklaunch.spongewarps.WarpManager;
import com.google.common.base.Optional;

public class WarpCommand implements CommandCallable {

	private static final String USAGE = "/warp <warp name>";
	private static final Text HELP = Texts.of("Teleports you to the location of the specified warp.");
	private static final String SHORT_DESC = "Teleport to a warp location";

	private static final Text MUST_BE_PLAYER_MSG = Texts.of(TextColors.RED, SpongeWarps.PREFIX
			+ " You must be a player to send that command (not console)");
	private static final Text INVALID_NUM_ARGS_MSG = Texts.of(TextColors.RED, SpongeWarps.PREFIX
			+ " There is an invalid number of arguments. Try: " + USAGE);
	private static final Text WARP_NOT_EXIST_MSG = Texts.of(TextColors.RED, SpongeWarps.PREFIX
			+ " That warp does not exist!");
	private static final String ERROR_WARPING_MSG = SpongeWarps.PREFIX + " There was an error scheduling your warp: ";

	@Override
	public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean call(CommandSource source, String arguments, List<String> parents) throws CommandException {
		if (!(source instanceof Player)) {
			source.sendMessage(MUST_BE_PLAYER_MSG);
			return false;
		}

		// Check if the arguments String is empty before splitting b/c in the
		// event it is empty, the split will still return an array of size 1
		// containing the empty String
		if (arguments.isEmpty()) {
			source.sendMessage(INVALID_NUM_ARGS_MSG);
			return false;
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
		}

		Warp warp = optWarp.get();

		Player player = (Player) source;

		Optional<String> optError = WarpManager.scheduleWarp(player, warp);

		if(optError.isPresent()){
			player.sendMessage(Texts.of(TextColors.RED, ERROR_WARPING_MSG + optError.get()));
			return true;
		}

		return true;
	}

	@Override
	public Text getHelp(CommandSource arg0) {
		return HELP;
	}

	@Override
	public String getShortDescription(CommandSource arg0) {
		return SHORT_DESC;
	}

	@Override
	public String getUsage(CommandSource arg0) {
		return USAGE;
	}

	@Override
	public boolean testPermission(CommandSource source) {
		// TODO Auto-generated method stub
		return false;
	}

}
