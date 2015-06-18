package com.blocklaunch.blwarps.commands.elements;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Warp;
import com.google.common.base.Optional;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.ArgumentParseException;
import org.spongepowered.api.util.command.args.CommandArgs;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.CommandElement;

import java.util.List;

public class WarpCommandElement extends CommandElement {

    private BLWarps plugin;

    public WarpCommandElement(BLWarps plugin, Text key) {
        super(key);
        this.plugin = plugin;
    }

    @Override
    protected Warp parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String warpName = args.next();
        Optional<Warp> optWarp = this.plugin.getWarpManager().getOne(warpName);
        if (!optWarp.isPresent()) {
            throw new ArgumentParseException(Constants.WARP_NOT_FOUND_MSG, warpName, 0);
        }
        return optWarp.get();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return this.plugin.getWarpManager().getNames();
    }

}
