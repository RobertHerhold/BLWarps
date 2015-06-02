package com.blocklaunch.blwarps.eventhandlers;

import org.spongepowered.api.event.EventHandler;
import org.spongepowered.api.event.message.MessageEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandSource;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.managers.PermissionUtil;
import com.blocklaunch.blwarps.managers.WarpUtil;
import com.google.common.base.Optional;

/**
 * Class to listen for chat messages being sent If there are any words in the
 * text that are the name of a warp, replace that word with a Text object which,
 * when clicked on, will warp the player to that warp. If the player does not
 * have permission to use that warp, the text will not be transformed
 */
public class MessageEventHandler implements EventHandler<MessageEvent> {
	
	private BLWarps plugin;
	
	public MessageEventHandler(BLWarps plugin) {
		this.plugin = plugin;
	}

	private static final String SPACE = " ";
	
	@Override
	public void handle(MessageEvent event) throws Exception {
		Text originalMessage = event.getMessage();
		String originalMessagePlain = Texts.toPlain(originalMessage);
		String[] originalMessageWords = originalMessagePlain.split(SPACE);
		
		for(String word : originalMessageWords) {
			if(plugin.getWarpManager().getWarpNames().contains(word.toLowerCase())) {
				Optional<Warp> optWarp = plugin.getWarpManager().getWarp(word);
				if(!optWarp.isPresent()) {
					continue;
				}
				
				Warp warp = optWarp.get();
				CommandSource source = event.getSource();
				if(!PermissionUtil.hasPermission(source, warp)) {
					continue;
				}
				
				Text text = WarpUtil.formattedTextWarp(warp);
				//TODO Replace the original text's word representing the warp with this one ^
			}
		}
		
	}
	

}
