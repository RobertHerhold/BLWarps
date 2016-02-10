package com.blocklaunch.blwarps;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.List;

public class Constants {

    /**
     * Prefix to display at the beginning of messages to player, console
     * outputs, etc.
     */
    public static final String PREFIX = "[BLWarps]";
    public static final String WARP_SIGN_PREFIX = "[Warp]";

    public static final List<String> FORBIDDEN_NAMES = Arrays.asList("set", "add", "create", "delete", "del", "list", "ls", "info", "group",
            "region", "sign");
    public static final Text CANNOT_USE_FORBIDDEN_NAME_MSG = Text.of(TextColors.RED, PREFIX, " You cannot use that name!");

    public static final Text SUCCESS_CREATE_WARP_MSG = Text.of(TextColors.GREEN, PREFIX + " You have successfully created a warp: ");
    public static final String ERROR_CREATE_WARP_MSG = PREFIX + " There was an error creating the warp: ";
    public static final Text SUCCESS_DELETE_WARP_MSG = Text.of(TextColors.GREEN, PREFIX + " You successfully deleted the warp: ");

    public static final Text SUCCESS_CREATE_WARP_REGION_MSG = Text.of(TextColors.GREEN, PREFIX + " You have successfully created a warp region: ");
    public static final String ERROR_CREATE_WARP_REGION_MSG = PREFIX + " There was an error creating the warp region: ";
    public static final Text SUCCESS_DELETE_WARP_REGION_MSG = Text.of(TextColors.GREEN, PREFIX + " You successfully deleted the warp region: ");

    public static final Text SPECIFY_WARP_MSG = Text.of(TextColors.RED, PREFIX + " You must specify a warp!");
    public static final Text SPECIFY_REGION_MSG = Text.of(TextColors.RED, PREFIX, " You must specify a region!");
    public static final Text SPECIFY_2_CORNERS_MSG = Text.of(TextColors.RED, PREFIX, " You must specify two locations!");

    public static final Text WARP_NOT_FOUND_MSG = Text.of(TextColors.RED, PREFIX + " A warp with that name could not be found!");
    public static final Text WARP_REGION_NOT_FOUND_MSG = Text.of(TextColors.RED, PREFIX + " A warp region with that name could not be found!");

    public static final String WARP_NAME_EXISTS = "A warp with that name already exists!";
    public static final String WARP_LOCATION_EXISTS = "A warp at that location already exists!";
    public static final String WARP_REGION_NAME_EXISTS = "A warp region with that name already exists!";
    public static final String WARP_REGION_LOCATION_EXISTS = "A warp region in that area already exists!";

    public static final String ERROR_FILE_WRITE = "There was an error writing to the file!";
    public static final String ERROR_FILE_READ = "There was an error reading the warps file!";

    public static final String ERROR_WARPING_MSG = PREFIX + " There was an error scheduling your warp: ";
    public static final Text NO_WARPS_MSG = Text.of(TextColors.GREEN, PREFIX + " There are no warps to display.");
    public static final Text NO_WARP_REGIONS_MSG = Text.of(TextColors.GREEN, PREFIX + " There are no warp regions to display.");
    public static final Text MUST_BE_PLAYER_MSG = Text.of(TextColors.RED, PREFIX + " You must be a player to send that command (not console)");
    public static final Text NO_PERMISSION_MSG = Text.of(TextColors.RED, PREFIX + " You do not have permission to use that warp!");
    public static final Text INVENTORY_FULL_MSG = Text.of(TextColors.RED, PREFIX + " Your inventory is full! Please clear a slot and try again.");
    public static final Text NO_WARPS_AFFECTED_MSG = Text.of(TextColors.RED, PREFIX + " No warps were affected!");
    public static final Text WORLD_NOT_FOUND_MSG = Text.of(TextColors.RED, PREFIX + " The world you requested to be warped to could not be found!");
    public static final Text DONT_MOVE_MSG = Text.of(TextColors.GREEN, Constants.PREFIX + " Do not move or get hurt or your warp will be canceled!");
}
