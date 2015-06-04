package com.blocklaunch.blwarps.commands.executors.group;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class GroupInfoExecutor implements CommandExecutor {

    private static final Text SPECIFY_GROUP_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX + " You must specify a group!");

    private BLWarps plugin;

    public GroupInfoExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<String> optGroup = args.getOne("group");

        if (!optGroup.isPresent()) {
            source.sendMessage(SPECIFY_GROUP_MSG);
            return CommandResult.empty();
        }

        String groupName = optGroup.get();

        Optional<Text> optWarpsInGroup = generateWarpList(groupName);
        if (!optWarpsInGroup.isPresent()) {
            source.sendMessage(Texts.of(TextColors.RED, BLWarps.PREFIX, " That group does not exist!"));
            return CommandResult.empty();
        }

        List<Text> warpInfo = Lists.newArrayList();

        warpInfo.add(Texts.of(TextColors.BLUE, "---------------", groupName, "---------------"));
        warpInfo.add(Texts.of(TextColors.BLUE, "Name: ", TextColors.WHITE, groupName));
        warpInfo.add(Texts.of(TextColors.BLUE, "Warps: ", optWarpsInGroup.get()));

        for (Text groupInfoLine : warpInfo) {
            source.sendMessage(groupInfoLine);
        }

        return CommandResult.success();
    }

    /**
     * Iterate through all warps, and check for the specified group
     * 
     * @param groupName
     * @return the concatenated list of all warps with the specified group
     */
    private Optional<Text> generateWarpList(String groupName) {

        List<Warp> warps = plugin.getWarpManager().getWarps();
        List<Warp> warpsInGroup = new ArrayList<Warp>();

        for (Warp warp : warps) {
            if (warp.getGroups().contains(groupName)) {
                warpsInGroup.add(warp);
            }
        }

        TextBuilder builder = Texts.builder();
        for (int index = 0; index < warpsInGroup.size(); index++) {
            builder.append(plugin.getUtil().generateWarpText(warpsInGroup.get(index)));
            if (warpsInGroup.size() - 1 != index) {
                // Not the last warp in the list
                builder.append(Texts.of(", "));
            }
        }

        return Optional.of(builder.build());
    }

}
