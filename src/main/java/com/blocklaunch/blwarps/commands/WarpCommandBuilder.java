package com.blocklaunch.blwarps.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.commands.elements.WarpCommandElement;
import com.blocklaunch.blwarps.commands.elements.WarpGroupCommandElement;
import com.blocklaunch.blwarps.commands.executors.DeleteWarpExecutor;
import com.blocklaunch.blwarps.commands.executors.ListWarpsExecutor;
import com.blocklaunch.blwarps.commands.executors.SetWarpExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpGroupExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpInfoExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpRegionExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpSignExecutor;

public class WarpCommandBuilder {

    private BLWarps plugin;

    public WarpCommandBuilder(BLWarps plugin) {
        this.plugin = plugin;
    }

    public CommandSpec mainWarpCommand() {
        HashMap<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("set", "add"), createWarpSubCommand());
        subCommands.put(Arrays.asList("delete", "del"), deleteWarpSubCommand());
        subCommands.put(Arrays.asList("list", "ls"), listWarpSubCommand());
        subCommands.put(Arrays.asList("info"), warpInfoSubCommand());
        subCommands.put(Arrays.asList("group"), groupSubCommand());
        subCommands.put(Arrays.asList("region"), warpRegionSubCommand());
        subCommands.put(Arrays.asList("sign"), warpSignSubCommand());

        CommandSpec mainWarpCommand =
                CommandSpec.builder().permission("blwarps.warp").description(Texts.of("Teleport to a warp location"))
                        .extendedDescription(Texts.of("Teleports you to the location of the specified warp.")).executor(new WarpExecutor(plugin))
                        .arguments(GenericArguments.firstParsing(new WarpCommandElement(plugin, Texts.of("warp")))).children(subCommands).build();
        return mainWarpCommand;
    }

    private CommandSpec createWarpSubCommand() {
        return CommandSpec
                .builder()
                .permission("blwarps.warp.create")
                .description(Texts.of("Set a warp"))
                .extendedDescription(Texts.of("Sets a warp at your location, or at the specified coordinates"))
                .executor(new SetWarpExecutor(plugin))
                .arguments(
                        GenericArguments.seq(GenericArguments.string(Texts.of("name")),
                                GenericArguments.optional(GenericArguments.vector3d(Texts.of("position"))))).build();
    }

    private CommandSpec deleteWarpSubCommand() {
        return CommandSpec.builder().permission("blwarps.delete").description(Texts.of("Delete a warp"))
                .extendedDescription(Texts.of("Deletes the warp with the specified name")).executor(new DeleteWarpExecutor(plugin))
                .arguments(new WarpCommandElement(plugin, Texts.of("warp"))).build();
    }

    private CommandSpec listWarpSubCommand() {
        return CommandSpec.builder().permission("blwarps.list").description(Texts.of("List warps"))
                .extendedDescription(Texts.of("Lists all warps, split up into pages.")).executor(new ListWarpsExecutor(plugin))
                .arguments(GenericArguments.optional(GenericArguments.integer(Texts.of("page")))).build();
    }

    private CommandSpec warpInfoSubCommand() {
        return CommandSpec.builder().permission("blwarps.info").description(Texts.of("Display information about a warp"))
                .executor(new WarpInfoExecutor(plugin)).arguments(new WarpCommandElement(plugin, Texts.of("warp"))).build();
    }

    private CommandSpec groupSubCommand() {
        return CommandSpec
                .builder()
                .permission("blwarps.group")
                .description(Texts.of("Manage warp groups"))
                .extendedDescription(Texts.of("Create and add warps to groups"))
                .executor(new WarpGroupExecutor(plugin))
                .arguments(GenericArguments.enumValue(Texts.of("operation"), GroupOperation.class),
                        GenericArguments.optional(GenericArguments.firstParsing(new WarpCommandElement(plugin, Texts.of("warp")))),
                        new WarpGroupCommandElement(plugin, Texts.of("group"))).build();
    }

    private CommandSpec warpRegionSubCommand() {
        return CommandSpec.builder().permission("blwarps.region.create").description(Texts.of("Manage warp regions"))
                .extendedDescription(Texts.of("Manage regions in which a player will be warped upon entering")).executor(new WarpRegionExecutor())
                .arguments(GenericArguments.enumValue(Texts.of("operation"), WarpRegionOperation.class)).build();
    }

    private CommandSpec warpSignSubCommand() {
        return CommandSpec.builder().permission("blwarps.sign").description(Texts.of("Create warp signs")).executor(new WarpSignExecutor(plugin))
                .arguments(new WarpCommandElement(plugin, Texts.of("warp"))).build();
    }

}
