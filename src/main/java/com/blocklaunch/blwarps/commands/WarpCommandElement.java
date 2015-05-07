package com.blocklaunch.blwarps.commands;

import java.util.List;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.ArgumentParseException;
import org.spongepowered.api.util.command.args.CommandArgs;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.CommandElement;

import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.manager.WarpManager;
import com.google.common.base.Optional;

public class WarpCommandElement extends CommandElement {

	public WarpCommandElement(Text key) {
		super(key);
	}

	@Override
	protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
		if(!args.hasNext()){
			return null;
		}
		
		String warpName = args.next();
		Optional<Warp> optWarp = WarpManager.getWarp(warpName);
		if (!optWarp.isPresent()) {
			return null;
		}
		return optWarp.get();
	}

	@Override
	public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
		
		return null;
	}

}
