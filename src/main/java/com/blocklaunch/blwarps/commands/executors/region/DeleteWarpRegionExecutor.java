package com.blocklaunch.blwarps.commands.executors.region;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.region.WarpRegion;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class DeleteWarpRegionExecutor implements CommandExecutor {

    private BLWarps plugin;

    public DeleteWarpRegionExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<WarpRegion> optRegion = args.getOne("region");
        if (!optRegion.isPresent()) {
            source.sendMessage(Constants.WARP_REGION_NOT_FOUND_MSG);
            return CommandResult.empty();
        }

        WarpRegion region = optRegion.get();

        this.plugin.getWarpRegionManager().deleteOne(region);

        source.sendMessage(Text.of(Constants.SUCCESS_DELETE_WARP_REGION_MSG, TextColors.GOLD, region.getName(), TextColors.WHITE, " - ",
                Util.undoDeleteWarpRegionText(region)));
        return CommandResult.success();
    }

}
