package com.blocklaunch.blwarps.commands.executors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
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

public class WarpInfoExecutor implements CommandExecutor {

    private BLWarps plugin;

    public WarpInfoExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        Optional<Warp> optWarp = args.getOne("warp");
        if (!optWarp.isPresent()) {
            source.sendMessage(Constants.WARP_NOT_FOUND_MSG);
            return CommandResult.empty();
        }

        Warp warp = optWarp.get();

        if (this.plugin.getUtil().hasPermission(source, warp) == false) {
            source.sendMessage(Constants.NO_PERMISSION_MSG);
            return CommandResult.empty();
        }

        Text warpName = Util.generateWarpText(warp);

        List<Text> warpInfo = Lists.newArrayList();

        warpInfo.add(Texts.of(TextColors.BLUE, "---------------", warpName, "---------------"));
        warpInfo.add(Texts.of(TextColors.BLUE, "Name: ", warpName));
        warpInfo.add(Texts.of(TextColors.BLUE, "World: ", TextColors.WHITE, warp.getWorld()));
        warpInfo.add(Texts.of(TextColors.BLUE, "Location: ", TextColors.WHITE, warp.getPosition()));
        warpInfo.add(Texts.of(TextColors.BLUE, "Groups: ", generateGroupList(warp)));

        for (Text infoLine : warpInfo) {
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
            builder.append(Util.generateWarpGroupInfoText(groups.get(index)));
            if (groups.size() - 1 != index) {
                // Not the last group name in the list
                builder.append(Texts.of(", "));
            }
        }

        return builder.build();
    }

}
