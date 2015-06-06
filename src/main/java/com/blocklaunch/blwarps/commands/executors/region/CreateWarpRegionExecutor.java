package com.blocklaunch.blwarps.commands.executors.region;

import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;

public class CreateWarpRegionExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        if (!(source instanceof Player)) {
            source.sendMessage(Constants.MUST_BE_PLAYER_MSG);
            return CommandResult.empty();
        }
        // Make sure the source is a player before attempting to get their
        // world
        Player player = (Player) source;

        Optional<String> regionNameOpt = args.getOne("name");
        Optional<Warp> linkedWarpOpt = args.getOne("warp");
        Optional<Vector3d> corner1Opt = args.getOne("corner1");
        Optional<Vector3d> corner2Opt = args.getOne("corner2");

        if (!regionNameOpt.isPresent()) {
            source.sendMessage(Constants.SPECIFY_REGION_MSG);
            return CommandResult.empty();
        }
        if (!linkedWarpOpt.isPresent()) {
            source.sendMessage(Constants.SPECIFY_WARP_MSG);
        }
        if (!corner1Opt.isPresent() || !corner2Opt.isPresent()) {
            source.sendMessage(Constants.SPECIFY_2_CORNERS_MSG);
        }

        WarpRegion region = new WarpRegion(linkedWarpOpt.get(), regionNameOpt.get(), player.getWorld().getName(), corner1Opt.get(), corner2Opt.get());

        return null;
    }
}
