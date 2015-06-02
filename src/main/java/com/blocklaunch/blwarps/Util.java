package com.blocklaunch.blwarps;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandSource;

public class Util {

    public static Text formattedTextWarp(String warpName) {
        Text text =
                Texts.builder(warpName).color(TextColors.GOLD).onClick(TextActions.runCommand("/warp " + warpName))
                        .onHover(TextActions.showText(Texts.of("Warp to ", TextColors.GOLD, warpName))).build();

        return text;
    }

    public static boolean hasPermission(CommandSource source, Warp warp) {
        String warpPermission = "blwarps.warp." + warp.getName();
        String groupPermissionBase = "blwarps.warp.group.";
        String wildCardPermission = "blwarps.warp.*";

        boolean playerIsValid = false;

        // Check permission for individual warp or wildcard
        if (source.hasPermission(warpPermission) || source.hasPermission(wildCardPermission)) {
            playerIsValid = true;
        }

        // Check permission for warp groups
        for (String groupName : warp.getGroups()) {
            String permission = groupPermissionBase + groupName;
            if (source.hasPermission(permission)) {
                playerIsValid = true;
            }
        }

        return playerIsValid;
    }
}
