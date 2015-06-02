package com.blocklaunch.blwarps.commands.executors;

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
import com.blocklaunch.blwarps.managers.PermissionUtil;
import com.google.common.base.Optional;

public class WarpCommand implements CommandExecutor {

    private static final Text MUST_BE_PLAYER_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
            + " You must be a player to send that command (not console)");
    private static final Text WARP_NOT_EXIST_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX + " That warp does not exist!");
    private static final String ERROR_WARPING_MSG = BLWarps.PREFIX + " There was an error scheduling your warp: ";
    private static final Text NO_PERMISSION = Texts.of(TextColors.RED, BLWarps.PREFIX + " You do not have permission to use that warp!");

    private BLWarps plugin;

    public WarpCommand(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(MUST_BE_PLAYER_MSG);
            return CommandResult.empty();
        }
        Player player = (Player) source;

        Optional<Warp> optWarp = args.getOne("warp");
        if (!optWarp.isPresent()) {
            source.sendMessage(WARP_NOT_EXIST_MSG);
            return CommandResult.empty();
        }

        Warp warp = optWarp.get();

        if (PermissionUtil.hasPermission(player, warp) == false) {
            player.sendMessage(NO_PERMISSION);
            return CommandResult.empty();
        }

        Optional<String> optError = plugin.getWarpManager().scheduleWarp(player, warp);

        if (optError.isPresent()) {
            player.sendMessage(Texts.of(TextColors.RED, ERROR_WARPING_MSG + optError.get()));
            return CommandResult.empty();
        }

        return CommandResult.success();
    }

}
