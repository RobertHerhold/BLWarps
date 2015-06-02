package com.blocklaunch.blwarps.managers;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.blocklaunch.blwarps.Warp;

public class WarpUtil {

    public static Text formattedTextWarp(Warp warp) {
        Text text =
                Texts.builder().color(TextColors.GOLD).onClick(TextActions.runCommand("warp" + warp.getName()))
                        .onHover(TextActions.showText(Texts.of("Warp to ", TextColors.GOLD, warp.getName()))).build();

        return text;
    }
}
