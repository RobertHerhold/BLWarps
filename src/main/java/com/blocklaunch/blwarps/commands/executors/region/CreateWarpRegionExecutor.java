package com.blocklaunch.blwarps.commands.executors.region;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class CreateWarpRegionExecutor implements CommandExecutor {

    private BLWarps plugin;

    public CreateWarpRegionExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

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

        Warp linkedWarp = linkedWarpOpt.get();

        // Is a global warp accesible to everyone w/ permission or a private
        // warp
        boolean isGlobal = (boolean) args.getOne("g").orElse(false);
        String expectedWarpOwner = "";
        if (isGlobal) {
            // Check that the owner of the warp is "global"
            expectedWarpOwner = "global";
        } else {
            // Check that the owner of the warp is the one trying to create the
            // region
            expectedWarpOwner = player.getUniqueId().toString();
        }
        
        if (!linkedWarp.getOwner().equalsIgnoreCase(expectedWarpOwner)) {
            source.sendMessage(Text.of(TextColors.RED, Constants.ERROR_CREATE_WARP_REGION_MSG, " You must own the warp you are linking to a region!"));
            return CommandResult.empty();
        }

        WarpRegion region =
                new WarpRegion(linkedWarp.getOwner(), regionNameOpt.get(), linkedWarp.getId(), player.getWorld().getName(),
                        corner1Opt.get(), corner2Opt.get());

        Optional<String> optError = this.plugin.getWarpRegionManager().addNew(region);
        if (optError.isPresent()) {
            source.sendMessage(Text.of(Constants.ERROR_CREATE_WARP_REGION_MSG, optError.get()));
            return CommandResult.empty();
        }

        source.sendMessage(Text.of(Constants.SUCCESS_CREATE_WARP_REGION_MSG, Util.warpRegionInfoText(region)));
        return CommandResult.success();
    }
}
