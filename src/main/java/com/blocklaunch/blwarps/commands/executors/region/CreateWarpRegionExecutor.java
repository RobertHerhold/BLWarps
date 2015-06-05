package com.blocklaunch.blwarps.commands.executors.region;

import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;

public class CreateWarpRegionExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<String> regionNameOpt = args.getOne("name");
        Optional<Warp> linkedWarpOpt = args.getOne("warp");
        Optional<Vector3d> corner1Opt = args.getOne("corner1");
        Optional<Vector3d> corner2Opt = args.getOne("corner2");
        
        if(!regionNameOpt.isPresent()) {
            source.sendMessage(Constants.SPECIFY_REGION_MSG);
            return CommandResult.empty();
        }
        
        return null;
    }

}
