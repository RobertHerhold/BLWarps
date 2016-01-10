package com.blocklaunch.blwarps.commands.executors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

public class WarpExecutor implements CommandExecutor {

    private BLWarps plugin;

    public WarpExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(Constants.MUST_BE_PLAYER_MSG);
            return CommandResult.empty();
        }
        Player player = (Player) source;

        Optional<Warp> optWarp = args.getOne("warp");
        if (!optWarp.isPresent()) {
            source.sendMessage(Constants.WARP_NOT_FOUND_MSG);
            return CommandResult.empty();
        }

        Warp warp = optWarp.get();

        if (Util.hasPermission(player, warp) == false) {
            player.sendMessage(Constants.NO_PERMISSION_MSG);
            return CommandResult.empty();
        }

        this.plugin.getWarpManager().scheduleWarp(player, warp);

        return CommandResult.success();
    }

}
