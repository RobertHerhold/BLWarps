package com.blocklaunch.blwarps.commands.executors;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.WarpBase;
import com.blocklaunch.blwarps.managers.WarpBaseManager;
import com.blocklaunch.blwarps.region.WarpRegion;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class DeleteWarpBaseExecutor<T extends WarpBase> implements CommandExecutor {

    private Class<T> type;
    private BLWarps plugin;

    public DeleteWarpBaseExecutor(Class<T> type, BLWarps plugin) {
        this.type = type;
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {
        Optional<T> optWarpBase = args.getOne(type.getSimpleName().toLowerCase());
        if (!optWarpBase.isPresent()) {
            source.sendMessage(Text.of(TextColors.RED, "A " + type.getSimpleName() + " with that ID could not be found!"));
            return CommandResult.empty();
        }

        T warpBase = optWarpBase.get();

        Text denialText =
                Text.of(TextColors.RED, Constants.PREFIX + " You cannot delete the " + type.getSimpleName() + " ", TextColors.GOLD, warpBase.getId(),
                        TextColors.RED, "!");

        if (warpBase.getOwner().equalsIgnoreCase("global")) {
            String permission = type == Warp.class ? "blwarps.warp.delete-global" : "blwarps.region.delete-global";
            if (!source.hasPermission(permission)) {
                source.sendMessage(denialText);
                return CommandResult.empty();
            }
        } else {
            if (!(source instanceof Player)) {
                source.sendMessage(Text.of(TextColors.RED, Constants.PREFIX + " You must be a player owning a " + type.getSimpleName()
                        + " to delete a non-global " + type.getSimpleName() + "!"));
                return CommandResult.empty();
            }

            Player player = (Player) source;
            if (!warpBase.getOwner().equalsIgnoreCase(player.getUniqueId().toString())) {
                player.sendMessage(Text.of(TextColors.RED, Constants.PREFIX + " You do not own the " + type.getSimpleName() + " ", TextColors.GOLD,
                        warpBase.getId(), "!"));
                return CommandResult.empty();
            }
        }

        Text undoText = Text.of("Undo"); // Doesn't do anything, just
                                         // placeholder
        WarpBaseManager wbm = null;

        if (type == Warp.class) {
            undoText = Util.undoDeleteWarpText((Warp) warpBase);
            wbm = this.plugin.getWarpManager();
        } else if (type == WarpRegion.class) {
            undoText = Util.undoDeleteWarpRegionText((WarpRegion) warpBase);
            wbm = this.plugin.getWarpRegionManager();
        }

        wbm.deleteOne(warpBase);

        source.sendMessage(Text.of(TextColors.GREEN, Constants.PREFIX + " You successfully deleted the " + type.getSimpleName() + ": ",
                TextColors.GOLD, warpBase.getId(), TextColors.WHITE, " - ",
                undoText));
        return CommandResult.success();
    }
}
