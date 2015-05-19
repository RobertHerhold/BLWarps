package com.blocklaunch.blwarps.commands;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.google.common.base.Optional;

public class WarpCommand implements CommandExecutor {

	private static final Text MUST_BE_PLAYER_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
			+ " You must be a player to send that command (not console)");
	private static final Text WARP_NOT_EXIST_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
			+ " That warp does not exist!");
	private static final String ERROR_WARPING_MSG = BLWarps.PREFIX + " There was an error scheduling your warp: ";

	@Override
	public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
		if (!(source instanceof Player)) {
			source.sendMessage(MUST_BE_PLAYER_MSG);
			return CommandResult.empty();
		}

		// Get the warp by it's name
		Optional<Warp> optWarp = args.getOne("warp");
		if (!optWarp.isPresent()) {
			source.sendMessage(WARP_NOT_EXIST_MSG);
			return CommandResult.empty();
		}

		Warp warp = optWarp.get();

		Player player = (Player) source;

		Optional<String> optError = WarpManager.scheduleWarp(player, warp);

		if (optError.isPresent()) {
			player.sendMessage(Texts.of(TextColors.RED, ERROR_WARPING_MSG + optError.get()));
			return CommandResult.empty();
		}

		return CommandResult.success();
	}

}
