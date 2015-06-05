package com.blocklaunch.blwarps;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class Constants {

    public static final String PREFIX = "[BLWarps]";

    public static final Text SUCCESS_CREATE_WARP_MSG = Texts.of(TextColors.GREEN, PREFIX + " You have successfully created a warp: ");
    public static final String ERROR_CREATE_WARP_MSG = PREFIX + " There was an error creating the warp: ";
    public static final Text SUCCESS_DELETE_WARP_MSG = Texts.of(TextColors.GREEN, PREFIX + " You successfully deleted the warp: ");

    public static final Text SPECIFY_WARP_MSG = Texts.of(TextColors.RED, PREFIX + " You must specify a warp!");
    public static final Text SPECIFY_GROUP_MSG = Texts.of(TextColors.RED, PREFIX + " You must specify a group!");
    public static final Text SPECIFY_REGION_MSG = Texts.of(TextColors.RED, PREFIX, " You must specify a region!");

    public static final Text WARP_NOT_FOUND_MSG = Texts.of(TextColors.RED, PREFIX + " A warp with that name could not be found!");
    public static final Text GROUP_NOT_FOUND_MSG = Texts.of(TextColors.RED, Constants.PREFIX, " That group with that name could not be found!");

    public static final String WARP_SUCCESS_MSG = PREFIX + " You have been warped to ";
    public static final String ERROR_WARPING_MSG = PREFIX + " There was an error scheduling your warp: ";
    public static final Text NO_WARPS_MSG = Texts.of(TextColors.GREEN, PREFIX + " There were no warps to display.");
    public static final Text MUST_BE_PLAYER_MSG = Texts.of(TextColors.RED, PREFIX + " You must be a player to send that command (not console)");
    public static final Text NO_PERMISSION_MSG = Texts.of(TextColors.RED, PREFIX + " You do not have permission to use that warp!");
    public static final Text INVENTORY_FULL_MSG = Texts.of(TextColors.RED, PREFIX + " Your inventory is full! Please clear a slot and try again.");
    public static final Text NO_WARPS_AFFECTED_MSG = Texts.of(TextColors.RED, PREFIX + " No warps were affected!");
    public static final Text WORLD_NOT_FOUND_MSG = Texts.of(TextColors.RED, PREFIX + " The world you requested to be warped to could not be found!");

}
