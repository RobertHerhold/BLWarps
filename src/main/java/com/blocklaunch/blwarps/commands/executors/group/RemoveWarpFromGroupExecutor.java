package com.blocklaunch.blwarps.commands.executors.group;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class RemoveWarpFromGroupExecutor implements CommandExecutor {

    private BLWarps plugin;

    public RemoveWarpFromGroupExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        // Need both a warp and a group for this
        Optional<Warp> optWarp = args.getOne("warp");
        Optional<String> optGroup = args.getOne("group");

        if (!optWarp.isPresent()) {
            source.sendMessage(Constants.SPECIFY_WARP_MSG);
            return CommandResult.empty();
        }

        if (!optGroup.isPresent()) {
            source.sendMessage(Constants.SPECIFY_GROUP_MSG);
            return CommandResult.empty();
        }

        Warp warp = optWarp.get();
        String group = optGroup.get();

        if (!warp.getGroups().contains(group)) {
            source.sendMessage(Text.of(TextColors.RED, Constants.PREFIX + " ", TextColors.GOLD, warp.getName(), TextColors.RED,
                    " is not in the group ", TextColors.GOLD, group, TextColors.RED, "."));
            return CommandResult.empty();
        }

        this.plugin.getWarpManager().removeWarpFromGroup(warp, group);

        source.sendMessage(Text.of(TextColors.GREEN, Constants.PREFIX + " You have successfully removed ", TextColors.GOLD, warp.getName(),
                TextColors.GREEN, " from ", TextColors.GOLD, group, TextColors.GREEN, "."));

        return CommandResult.success();
    }

}
