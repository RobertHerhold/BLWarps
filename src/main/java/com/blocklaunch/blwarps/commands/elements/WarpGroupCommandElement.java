package com.blocklaunch.blwarps.commands.elements;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.ArgumentParseException;
import org.spongepowered.api.util.command.args.CommandArgs;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.CommandElement;

import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.managers.WarpManager;

public class WarpGroupCommandElement extends CommandElement {

    public WarpGroupCommandElement(Text key) {
        super(key);
    }

    @Override
    protected String parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        if (!args.hasNext()) {
            return null;
        }

        String groupName = args.next();

        // Don't want to do any warp lookups --> Let the appropriate executor
        // determine behavior

        return groupName;
    }

    @Override
    public List<String> complete(CommandSource source, CommandArgs args, CommandContext context) {
        List<String> groupNames = new ArrayList<String>();
        for (Warp w : WarpManager.warps) {
            for (String group : w.getGroups()) {
                if (!groupNames.contains(group)) {
                    groupNames.add(group);
                }
            }
        }

        return groupNames;
    }

}
