package com.blocklaunch.blwarps.commands.executors.region;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.base.Optional;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

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

        source.sendMessage(Texts.of(Constants.SUCCESS_DELETE_WARP_REGION_MSG, TextColors.GOLD, region.getName(), TextColors.WHITE, " - ",
                Util.undoDeleteWarpRegionText(region)));
        return CommandResult.success();
    }

}
