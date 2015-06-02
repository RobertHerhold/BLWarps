package com.blocklaunch.blwarps.commands.elements;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.ArgumentParseException;
import org.spongepowered.api.util.command.args.CommandArgs;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.CommandElement;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.google.common.base.Optional;

public class WarpCommandElement extends CommandElement {

    private BLWarps plugin;

    public WarpCommandElement(BLWarps plugin, Text key) {
        super(key);
        this.plugin = plugin;
    }

    @Override
    protected Warp parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String warpName = args.next();
        Optional<Warp> optWarp = plugin.getWarpManager().getWarp(warpName);
        if (!optWarp.isPresent()) {
            throw new ArgumentParseException(Texts.of(BLWarps.PREFIX + " A warp with that name could not be found!"), warpName, 0);
        }
        return optWarp.get();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        List<String> warpNames = new ArrayList<String>();
        for (Warp w : plugin.getWarpManager().getWarps()) {
            warpNames.add(w.getName());
        }

        return warpNames;
    }

}
