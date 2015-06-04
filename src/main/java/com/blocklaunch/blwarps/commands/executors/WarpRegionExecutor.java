package com.blocklaunch.blwarps.commands.executors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.commands.WarpRegionOperation;
import com.google.common.base.Optional;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class WarpRegionExecutor implements CommandExecutor {

    private static final Text NO_OPERATION_MSG = Texts.of(TextColors.RED, BLWarps.PREFIX + " You need to specify an operation: CREATE");
    private static final Text NO_PERMISSION = Texts.of(TextColors.RED, BLWarps.PREFIX + " You do not have permission!");

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<WarpRegionOperation> optOperation = args.getOne("operation");
        if (!optOperation.isPresent()) {
            source.sendMessage(NO_OPERATION_MSG);
            return CommandResult.empty();
        }

        switch (optOperation.get()) {
            case CREATE:
                if (!source.hasPermission("blwarps.group.add")) {
                    source.sendMessage(NO_PERMISSION);
                    return CommandResult.empty();
                }
                return new CreateWarpRegionExecutor().execute(source, args);
            default:
                return CommandResult.empty();
        }
    }

    class CreateWarpRegionExecutor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
            
            
            return null;
        }

    }

}
