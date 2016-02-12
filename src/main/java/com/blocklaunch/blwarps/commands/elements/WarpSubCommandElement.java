package com.blocklaunch.blwarps.commands.elements;

import jersey.repackaged.com.google.common.collect.Lists;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WarpSubCommandElement extends CommandElement {

    private Map<List<String>, CommandSpec> subCommands;

    public WarpSubCommandElement(Map<List<String>, CommandSpec> subCommands, Text key) {
        super(key);
        this.subCommands = subCommands;
    }

    @Override
    protected CommandExecutor parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String subCommand = args.next();

        Iterator<List<String>> keySetIterator = subCommands.keySet().iterator();
        while (keySetIterator.hasNext()) {
            List<String> subCommandAliases = keySetIterator.next();
            for (String subCommandAlias : subCommandAliases) {
                if (subCommandAlias.equalsIgnoreCase(subCommand)) {
                    return subCommands.get(subCommandAliases).getExecutor();
                }
            }
        }
        throw args.createError(Text.of("'%s' did not match any subcommands", subCommand));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return Lists.newArrayList();
    }

}
