package com.blocklaunch.blwarps;

import org.spongepowered.api.data.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.tileentity.SignData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandSource;

import com.google.common.base.Optional;

public class Util {
    
    private BLWarps plugin;
    public static final String WARP_SIGN_PREFIX = "[Warp]";
    
    public Util(BLWarps plugin) {
        this.plugin = plugin;
    }

    public Text formattedTextWarp(String warpName) {
        Text text =
                Texts.builder(warpName).color(TextColors.GOLD).onClick(TextActions.runCommand("/warp " + warpName))
                        .onHover(TextActions.showText(Texts.of("Warp to ", TextColors.GOLD, warpName))).build();

        return text;
    }

    public boolean hasPermission(CommandSource source, Warp warp) {
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
    
    /**
     * @param warp the warp to represent on the SignData
     * @return the formatted SignData indicating a valid warp sign
     */
    public SignData generateWarpSignData(Warp warp) {
        Optional<DataManipulatorBuilder<SignData>> builder = plugin.getGame().getRegistry().getManipulatorRegistry().getBuilder(SignData.class);
        SignData signData = builder.get().create();

        signData.setLine(0, Texts.of());
        signData.setLine(1, Texts.of(TextColors.DARK_BLUE, WARP_SIGN_PREFIX));
        signData.setLine(2, Texts.of(TextColors.GOLD, warp.getName()));
        signData.setLine(3, Texts.of());

        return signData;

    }
}
