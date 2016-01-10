package com.blocklaunch.blwarps.commands.executors.group;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupInfoExecutor implements CommandExecutor {

    private BLWarps plugin;

    public GroupInfoExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<String> optGroup = args.getOne("group");

        if (!optGroup.isPresent()) {
            source.sendMessage(Constants.SPECIFY_GROUP_MSG);
            return CommandResult.empty();
        }

        String groupName = optGroup.get();

        Optional<Text> optWarpsInGroup = generateWarpList(groupName);
        if (!optWarpsInGroup.isPresent()) {
            source.sendMessage(Constants.GROUP_NOT_FOUND_MSG);
            return CommandResult.empty();
        }

        List<Text> warpInfo = Lists.newArrayList();

        warpInfo.add(Text.of(TextColors.BLUE, "---------------", groupName, "---------------"));
        warpInfo.add(Text.of(TextColors.BLUE, "Name: ", TextColors.WHITE, groupName));
        warpInfo.add(Text.of(TextColors.BLUE, "Warps: ", optWarpsInGroup.get()));

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

        List<Warp> warps = this.plugin.getWarpManager().getPayload();
        List<Warp> warpsInGroup = new ArrayList<Warp>();

        for (Warp warp : warps) {
            if (warp.getGroups().contains(groupName)) {
                warpsInGroup.add(warp);
            }
        }

        Builder builder = Text.builder();
        for (int index = 0; index < warpsInGroup.size(); index++) {
            builder.append(Util.warpText(warpsInGroup.get(index)));
            if (warpsInGroup.size() - 1 != index) {
                // Not the last warp in the list
                builder.append(Text.of(", "));
            }
        }

        return Optional.of(builder.build());
    }

}
