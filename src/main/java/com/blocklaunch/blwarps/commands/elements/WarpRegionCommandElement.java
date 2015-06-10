package com.blocklaunch.blwarps.commands.elements;

import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.ArgumentParseException;
import org.spongepowered.api.util.command.args.CommandArgs;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.CommandElement;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.base.Optional;

public class WarpRegionCommandElement extends CommandElement {

    private BLWarps plugin;
    
    public WarpRegionCommandElement(Text key, BLWarps plugin) {
        super(key);
        this.plugin = plugin;
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return plugin.getWarpRegionManager().getNames();
    }

    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String regionName = args.next();
        Optional<WarpRegion> optRegion = plugin.getWarpRegionManager().getOne(regionName);
        if (!optRegion.isPresent()) {
            throw new ArgumentParseException(Constants.WARP_REGION_NOT_FOUND_MSG, regionName, 0);
        }
        return optRegion.get();
    }

}
