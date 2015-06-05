package com.blocklaunch.blwarps.commands.executors.group;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;
import com.google.common.base.Optional;

public class DeleteGroupExecutor implements CommandExecutor {
    private BLWarps plugin;

    public DeleteGroupExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<String> optGroup = args.getOne("group");

        if (!optGroup.isPresent()) {
            source.sendMessage(Constants.SPECIFY_GROUP_MSG);
            return CommandResult.empty();
        }

        String group = optGroup.get();

        int affectedCounter = 0;
        for (Warp w : plugin.getWarpManager().getWarps()) {
            if (w.getGroups().contains(group)) {
                plugin.getWarpManager().removeWarpFromGroup(w, group);
                affectedCounter++;
            }
        }

        if (affectedCounter == 0) {
            source.sendMessage(Constants.NO_WARPS_AFFECTED_MSG);
            return CommandResult.empty();
        }

        source.sendMessage(Texts.of(TextColors.GREEN, Constants.PREFIX + " The ", TextColors.GOLD, group, TextColors.GREEN,
                " group tag was removed from ", TextColors.GOLD, affectedCounter, TextColors.GREEN, " warps."));

        return CommandResult.builder().successCount(affectedCounter).build();
    }

}
