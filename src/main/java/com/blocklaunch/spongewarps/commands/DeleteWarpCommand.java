package com.blocklaunch.spongewarps.commands;

import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandCallable;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandSource;

import com.blocklaunch.spongewarps.SpongeWarps;
import com.blocklaunch.spongewarps.WarpManager;
import com.google.common.base.Optional;

public class DeleteWarpCommand implements CommandCallable {

	private static final String USAGE = "/warp <warp name>";
	private static final String HELP = "Teleports you to the location of the specified warp.";

	private static final Text INVALID_NUM_ARGS_MSG = Texts.of(TextColors.RED, SpongeWarps.PREFIX
			+ " There is an invalid number of arguments. Try: " + USAGE);
	private static final String ERROR_DELETE_WARP_MSG = SpongeWarps.PREFIX + " There was an error deleting the warp: ";
	private static final String SUCCESS_DELETE_WARP_MSG = SpongeWarps.PREFIX + " You successfully deleted the warp: ";

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
			source.sendMessage(INVALID_NUM_ARGS_MSG);
			return false;
		}

		// Parse the arguments into an array
		String[] args = arguments.split(" ");

		if (args.length != 1) {
			source.sendMessage(INVALID_NUM_ARGS_MSG);
		}

		Optional<String> optError = WarpManager.deleteWarp(args[0]);

		if (optError.isPresent()) {
			source.sendMessage(Texts.builder(ERROR_DELETE_WARP_MSG + optError.get()).color(TextColors.RED).build());
			return false;
		}

		source.sendMessage(Texts.builder(SUCCESS_DELETE_WARP_MSG + args[0]).color(TextColors.GREEN).build());
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
