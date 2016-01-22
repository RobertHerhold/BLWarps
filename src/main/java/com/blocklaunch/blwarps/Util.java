package com.blocklaunch.blwarps;

import com.blocklaunch.blwarps.region.WarpRegion;
import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableSignData;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class Util {

    //
    // Warp Text
    //

    public static Text warpText(Warp warp) {
        return Text.builder(warp.getName()).color(TextColors.GOLD).onClick(TextActions.runCommand("/warp " + warp.getName()))
                .onHover(TextActions.showText(Text.of("Warp to ", TextColors.GOLD, warp.getName()))).build();

    }

    public static Text deleteWarpText(Warp warp) {
        return Text.builder("[X]").color(TextColors.RED).onClick(TextActions.runCommand("/warp delete " + warp.getName()))
                .onHover(TextActions.showText(Text.of(TextColors.RED, "Delete ", TextColors.GOLD, warp.getName()))).build();
    }

    public static Text undoDeleteWarpText(Warp warp) {
        return Text.builder("Undo").color(TextColors.RED)
                .onClick(TextActions.runCommand("/warp set " + warp.getName() + " " + Util.vector3dToCommandFriendlyString(warp.getPosition())))
                .onHover(TextActions.showText(Text.of(TextColors.RED, "Undo delete warp ", TextColors.GOLD, warp.getName()))).build();
    }

    //
    // Warp Group Text
    //

    public static Text warpGroupInfoText(String groupName) {
        return Text.builder(groupName).color(TextColors.GOLD).onClick(TextActions.runCommand("/warp group info " + groupName))
                .onHover(TextActions.showText(Text.of("Show ", TextColors.GOLD, groupName, TextColors.WHITE, " info."))).build();
    }

    //
    // Warp Region Text
    //

    public static Text warpRegionInfoText(WarpRegion region) {
        return Text.builder(region.getName()).color(TextColors.GOLD).onClick(TextActions.runCommand("/warp region info " + region.getName()))
                .onHover(TextActions.showText(Text.of("Show ", TextColors.GOLD, region.getName(), TextColors.WHITE, " info."))).build();
    }

    public static Text deleteWarpRegionText(WarpRegion region) {
        return Text.builder("[X]").color(TextColors.RED).onClick(TextActions.runCommand("/warp region delete " + region.getName()))
                .onHover(TextActions.showText(Text.of(TextColors.RED, "Delete warp region ", TextColors.GOLD, region.getName()))).build();
    }

    public static Text undoDeleteWarpRegionText(WarpRegion region) {
        return Text
                .builder("Undo")
                .color(TextColors.RED)
                .onClick(
                        TextActions.runCommand("/warp region add " + region.getName() + " " + region.getLinkedWarpName() + " "
                                + Util.vector3dToCommandFriendlyString(region.getMinLoc())
                                + " " + Util.vector3dToCommandFriendlyString(region.getMaxLoc())))
                .onHover(TextActions.showText(Text.of(TextColors.RED, "Undo delete warp region ", TextColors.GOLD, region.getName()))).build();
    }

    public static String vector3dToCommandFriendlyString(Vector3d vector) {
        StringBuilder sb = new StringBuilder();
        sb.append(vector.getX());
        sb.append(",");
        sb.append(vector.getY());
        sb.append(",");
        sb.append(vector.getZ());

        return sb.toString();
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

    /**
     * @param warp the warp to represent on the SignData
     * @return the formatted SignData indicating a valid warp sign
     */
    public static SignData generateWarpSignData(Warp warp) {
        Optional<DataManipulatorBuilder<SignData, ImmutableSignData>> builder =
                Sponge.getDataManager().getManipulatorBuilder(SignData.class);
        SignData signData = builder.get().create();
        ListValue<Text> lines = signData.lines();

        lines.set(0, Text.of());
        lines.set(1, Text.of(TextColors.DARK_BLUE, Constants.WARP_SIGN_PREFIX));
        lines.set(2, Text.of(TextColors.GOLD, warp.getName()));
        lines.set(3, Text.of());

        signData.set(lines);

        return signData;

    }

}
