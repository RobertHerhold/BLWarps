package com.blocklaunch.blwarps.commands.executors.region;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public class WarpRegionInfoExecutor implements CommandExecutor {

    private BLWarps plugin;

    public WarpRegionInfoExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        Optional<WarpRegion> optRegion = args.getOne("region");
        if (!optRegion.isPresent()) {
            source.sendMessage(Constants.WARP_NOT_FOUND_MSG);
            return CommandResult.empty();
        }

        WarpRegion region = optRegion.get();

        Optional<Warp> linkedWarpOpt = this.plugin.getWarpManager().getOne(region.getLinkedWarpName());
        Text warpName = Text.of(region.getLinkedWarpName());
        if (linkedWarpOpt.isPresent()) {
            if (Util.hasPermission(source, linkedWarpOpt.get()) == false) {
                source.sendMessage(Constants.NO_PERMISSION_MSG);
                return CommandResult.empty();
            }
            warpName = Util.warpText(linkedWarpOpt.get());
        }

        List<Text> regionInfo = Lists.newArrayList();

        regionInfo.add(Text.of(TextColors.BLUE, "---------------", region.getName(), "---------------"));
        regionInfo.add(Text.of(TextColors.BLUE, "Name: ", TextColors.WHITE, region.getName()));
        regionInfo.add(Text.of(TextColors.BLUE, "World: ", TextColors.WHITE, region.getWorld()));
        regionInfo.add(Text.of(TextColors.BLUE, "Linked Warp: ", warpName));
        regionInfo.add(Text.of(TextColors.BLUE, "Bounds: ", TextColors.WHITE, region.getMinLoc(), TextColors.BLUE, " to ", TextColors.WHITE,
                region.getMaxLoc()));

        for (Text infoLine : regionInfo) {
            source.sendMessage(infoLine);
        }

        return CommandResult.success();

    }

}
