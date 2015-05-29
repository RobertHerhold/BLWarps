package com.blocklaunch.blwarps.commands;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.google.common.base.Optional;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class WarpGroupCommand implements CommandExecutor {

    private static final Text NO_OPERATION_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX
            + " You need to specify an operation: ADD, REMOVE, REMOVEALL");
    private static final Text SPECIFY_WARP_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX + " You must specify a warp!");
    private static final Text SPECIFY_GROUP_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX + " You must specify a group!");
    private static final Text NO_WARPS_AFFECTED = Texts.of(TextColors.RED, BLWarps.PREFIX + " No warps were affected!");
    private static final Text NO_PERMISSION = Texts.of(TextColors.RED, BLWarps.PREFIX + " You do not have permission!");

    private BLWarps plugin;

    public WarpGroupCommand(BLWarps plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<GroupOperation> optOperation = args.getOne("operation");
        if (!optOperation.isPresent()) {
            // This should never be called because of the argument not being
            // optional, but this is just a fail-safe
            source.sendMessage(NO_OPERATION_MSG);
            return CommandResult.empty();
        }

        switch (optOperation.get()) {
            case ADD:
                if (!source.hasPermission("blwarps.group.add")) {
                    source.sendMessage(NO_PERMISSION);
                    return CommandResult.empty();
                }
                return new AddWarpToGroupExecutor().execute(source, args);
            case REMOVE:
                if (!source.hasPermission("blwarps.group.remove")) {
                    source.sendMessage(NO_PERMISSION);
                    return CommandResult.empty();
                }
                return new RemoveWarpFromGroupExecutor().execute(source, args);
            case REMOVEALL:
                if (!source.hasPermission("blwarps.group.removeall")) {
                    source.sendMessage(NO_PERMISSION);
                    return CommandResult.empty();
                }
                return new DeleteGroupExecutor().execute(source, args);
            default:
                return CommandResult.empty();
        }
    }

    /**
     * Removes the specified group from all warps
     */
    class DeleteGroupExecutor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
            Optional<String> optGroup = args.getOne("group");

            if (!optGroup.isPresent()) {
                source.sendMessage(SPECIFY_GROUP_MSG);
                return CommandResult.empty();
            }

            String group = optGroup.get();

            int affectedCounter = 0;
            for (Warp w : WarpManager.warps) {
                if (w.getGroups().contains(group)) {
                    plugin.getWarpManager().removeWarpFromGroup(w, group);
                    affectedCounter++;
                }
            }

            if (affectedCounter == 0) {
                source.sendMessage(NO_WARPS_AFFECTED);
                return CommandResult.empty();
            }

            source.sendMessage(Texts.of(TextColors.GREEN, BLWarps.PREFIX + " The ", TextColors.GOLD, group, TextColors.GREEN,
                    " group tag was removed from ", TextColors.GOLD, affectedCounter, TextColors.GREEN, " warps."));

            return CommandResult.builder().successCount(affectedCounter).build();
        }

    }

    /**
     * Adds the specified group tag to a warp
     */
    class AddWarpToGroupExecutor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
            // Need both a warp and a group for this
            Optional<Warp> optWarp = args.getOne("warp");
            Optional<String> optGroup = args.getOne("group");

            if (!optWarp.isPresent()) {
                source.sendMessage(SPECIFY_WARP_MSG);
                return CommandResult.empty();
            }

            if (!optGroup.isPresent()) {
                source.sendMessage(SPECIFY_GROUP_MSG);
                return CommandResult.empty();
            }

            Warp warp = optWarp.get();
            String group = optGroup.get();

            plugin.getWarpManager().addWarpToGroup(warp, group);

            source.sendMessage(Texts.of(TextColors.GREEN, BLWarps.PREFIX + " You successfully added ", TextColors.GOLD, warp.getName(),
                    TextColors.GREEN, " to ", TextColors.GOLD, group, TextColors.GREEN, "."));

            return CommandResult.success();
        }

    }

    /**
     * Remove the specified group tag from a warp
     */
    class RemoveWarpFromGroupExecutor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
            // Need both a warp and a group for this
            Optional<Warp> optWarp = args.getOne("warp");
            Optional<String> optGroup = args.getOne("group");

            if (!optWarp.isPresent()) {
                source.sendMessage(SPECIFY_WARP_MSG);
                return CommandResult.empty();
            }

            if (!optGroup.isPresent()) {
                source.sendMessage(SPECIFY_GROUP_MSG);
                return CommandResult.empty();
            }

            Warp warp = optWarp.get();
            String group = optGroup.get();

            if (!warp.getGroups().contains(group)) {
                source.sendMessage(Texts.of(TextColors.RED, BLWarps.PREFIX + " ", TextColors.GOLD, warp.getName(), TextColors.RED,
                        " is not in the group ", TextColors.GOLD, group, TextColors.RED, "."));
                return CommandResult.empty();
            }

            plugin.getWarpManager().removeWarpFromGroup(warp, group);

            source.sendMessage(Texts.of(TextColors.GREEN, BLWarps.PREFIX + " You have successfully removed ", TextColors.GOLD, warp.getName(),
                    TextColors.GREEN, " from ", TextColors.GOLD, group, TextColors.GREEN, "."));

            return CommandResult.success();
        }

    }

}
