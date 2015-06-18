package com.blocklaunch.blwarps.commands.executors.region;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.List;

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
        Text warpName = Texts.of(region.getLinkedWarpName());
        if (linkedWarpOpt.isPresent()) {
            if (this.plugin.getUtil().hasPermission(source, linkedWarpOpt.get()) == false) {
                source.sendMessage(Constants.NO_PERMISSION_MSG);
                return CommandResult.empty();
            }
            warpName = Util.generateWarpText(linkedWarpOpt.get());
        }

        List<Text> regionInfo = Lists.newArrayList();

        regionInfo.add(Texts.of(TextColors.BLUE, "---------------", region.getName(), "---------------"));
        regionInfo.add(Texts.of(TextColors.BLUE, "Name: ", TextColors.WHITE, region.getName()));
        regionInfo.add(Texts.of(TextColors.BLUE, "World: ", TextColors.WHITE, region.getWorld()));
        regionInfo.add(Texts.of(TextColors.BLUE, "Linked Warp: ", warpName));
        regionInfo.add(Texts.of(TextColors.BLUE, "Bounds: ", TextColors.WHITE, region.getMinLoc(), TextColors.BLUE, " to ", TextColors.WHITE,
                region.getMaxLoc()));

        for (Text infoLine : regionInfo) {
            source.sendMessage(infoLine);
        }

        return CommandResult.success();

    }

    private Text generateGroupList(Warp warp) {
        List<String> groups = warp.getGroups();
        if (groups.isEmpty()) {
            return Texts.of("none");
        }

        TextBuilder builder = Texts.builder();
        for (int index = 0; index < groups.size(); index++) {
            builder.append(this.plugin.getUtil().generateWarpGroupInfoText(groups.get(index)));
            if (groups.size() - 1 != index) {
                // Not the last group name in the list
                builder.append(Texts.of(", "));
            }
        }

        return builder.build();
    }

}
