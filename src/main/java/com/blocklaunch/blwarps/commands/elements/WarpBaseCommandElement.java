package com.blocklaunch.blwarps.commands.elements;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.WarpBase;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Optional;

public class WarpBaseCommandElement<T extends WarpBase> extends CommandElement {

    private BLWarps plugin;
    private Class<T> type;

    public WarpBaseCommandElement(Class<T> type, BLWarps plugin, Text key) {
        super(key);
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    protected T parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String givenIdentifier = args.next();
        // If the source is not a player and they have not provided a full warp
        // id, they must be referring to a global warp
        String owner = source instanceof Player ? ((Player) source).getName() : "global";
        String id = "";
        if (givenIdentifier.indexOf('/') != -1) {
            id = givenIdentifier;
        } else {
            id = owner + "/" + givenIdentifier;
        }

        Optional<? extends WarpBase> optWarpBase = this.plugin.getWarpBaseManagers().get(type).getOne(id);
        if (!optWarpBase.isPresent()) {
            throw new ArgumentParseException(Constants.WARP_NOT_FOUND_MSG, id, 0);
        }
        return (T) optWarpBase.get();
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        List<T> all = (List<T>) this.plugin.getWarpBaseManagers().get(type).getPayload();
        List<String> autoCompleteNames = Lists.newArrayList();
        for (T warpBase : all) {
            // Check if is a Warp or WarpRegion. If it is a Warp, check for
            // permission. If it is a WarpRegion, check for permission to it's
            // linked Warp
            if (type == Warp.class) {
                Warp warp = (Warp) warpBase;
                if (Util.hasPermission(src, warp)) {
                    autoCompleteNames.add(warp.getId());
                }
            } else if (type == WarpRegion.class) {
                WarpRegion warpRegion = (WarpRegion) warpBase;
                String linkedWarpId = warpRegion.getLinkedWarpId();
                Optional<Warp> warpOpt = plugin.getWarpManager().getOne(linkedWarpId);
                if (warpOpt.isPresent() && Util.hasPermission(src, warpOpt.get())) {
                    autoCompleteNames.add(linkedWarpId);
                }
            }
        }
        return autoCompleteNames;
    }
}
