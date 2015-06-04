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
import com.blocklaunch.blwarps.commands.executors.WarpInfoExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpRegionExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpSignExecutor;
import com.blocklaunch.blwarps.commands.executors.group.AddWarpToGroupExecutor;
import com.blocklaunch.blwarps.commands.executors.group.DeleteGroupExecutor;
import com.blocklaunch.blwarps.commands.executors.group.GroupInfoExecutor;
import com.blocklaunch.blwarps.commands.executors.group.RemoveWarpFromGroupExecutor;

public class WarpCommandBuilder {

    private BLWarps plugin;

    public WarpCommandBuilder(BLWarps plugin) {
        this.plugin = plugin;
    }

    public CommandSpec mainWarpCommand() {
        HashMap<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("set", "add", "create"), createWarpSubCommand());
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

    /**
     * Command: "/warp set <warp name> [x] [y] [z]"
     * 
     * @return
     */
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

    /**
     * Command: "/warp delete <warp name>"
     * 
     * @return
     */
    private CommandSpec deleteWarpSubCommand() {
        return CommandSpec.builder().permission("blwarps.delete").description(Texts.of("Delete a warp"))
                .extendedDescription(Texts.of("Deletes the warp with the specified name")).executor(new DeleteWarpExecutor(plugin))
                .arguments(new WarpCommandElement(plugin, Texts.of("warp"))).build();
    }

    /**
     * Command: "/warp list"
     * 
     * @return
     */
    private CommandSpec listWarpSubCommand() {
        return CommandSpec.builder().permission("blwarps.list").description(Texts.of("List warps"))
                .extendedDescription(Texts.of("Lists all warps, split up into pages.")).executor(new ListWarpsExecutor(plugin))
                .arguments(GenericArguments.optional(GenericArguments.integer(Texts.of("page")))).build();
    }

    /**
     * Command: "/warp info <warp name>"
     * 
     * @return
     */
    private CommandSpec warpInfoSubCommand() {
        return CommandSpec.builder().permission("blwarps.info").description(Texts.of("Display information about a warp"))
                .executor(new WarpInfoExecutor(plugin)).arguments(new WarpCommandElement(plugin, Texts.of("warp"))).build();
    }

    /**
     * Command: "/warp group add:remove:delete:info
     * 
     * @return
     */
    private CommandSpec groupSubCommand() {
        HashMap<List<String>, CommandSpec> groupSubCommands = new HashMap<>();

        groupSubCommands.put(Arrays.asList("add"), addWarpToGroupSubCommand());
        groupSubCommands.put(Arrays.asList("remove"), removeWarpFromGroupSubCommand());
        groupSubCommands.put(Arrays.asList("removeall", "delete"), deleteGroupSubCommand());
        groupSubCommands.put(Arrays.asList("info"), groupInfoSubCommand());

        return CommandSpec.builder().permission("blwarps.group").description(Texts.of("Manage warp groups")).children(groupSubCommands).build();
    }

    /**
     * Command: "/warp group add <warp name> <group name>
     * 
     * @return
     */
    private CommandSpec addWarpToGroupSubCommand() {
        return CommandSpec.builder().permission("blwarps.group.add").description(Texts.of("Add a warp to a group"))
                .executor(new AddWarpToGroupExecutor(plugin))
                .arguments(new WarpCommandElement(plugin, Texts.of("warp")), new WarpGroupCommandElement(plugin, Texts.of("group"))).build();
    }

    /**
     * Command: "/warp group remove <warp name> <group name>"
     * 
     * @return
     */
    private CommandSpec removeWarpFromGroupSubCommand() {
        return CommandSpec.builder().permission("blwarps.group.remove").description(Texts.of("Remove a warp from a group"))
                .executor(new RemoveWarpFromGroupExecutor(plugin))
                .arguments(new WarpCommandElement(plugin, Texts.of("warp")), new WarpGroupCommandElement(plugin, Texts.of("group"))).build();
    }

    /**
     * Command: "/warp group delete <group name>"
     * 
     * @return
     */
    private CommandSpec deleteGroupSubCommand() {
        return CommandSpec.builder().permission("blwarps.group.delete").description(Texts.of("Delete a warp group"))
                .executor(new DeleteGroupExecutor(plugin)).arguments(new WarpGroupCommandElement(plugin, Texts.of("group"))).build();
    }

    /**
     * Command: "/warp group info <group name>
     * 
     * @return
     */
    private CommandSpec groupInfoSubCommand() {
        return CommandSpec.builder().permission("blwarps.group.info").description(Texts.of("Display information about a warp group"))
                .executor(new GroupInfoExecutor(plugin)).arguments(new WarpGroupCommandElement(plugin, Texts.of("group"))).build();
    }

    /**
     * Command: "/warp region create"
     * 
     * @return
     */
    private CommandSpec warpRegionSubCommand() {
        return CommandSpec.builder().permission("blwarps.region.create").description(Texts.of("Manage warp regions"))
                .extendedDescription(Texts.of("Manage regions in which a player will be warped upon entering")).executor(new WarpRegionExecutor())
                .arguments(GenericArguments.enumValue(Texts.of("operation"), WarpRegionOperation.class)).build();
    }

    /**
     * Command: "/warp sign <warp name>"
     * 
     * @return
     */
    private CommandSpec warpSignSubCommand() {
        return CommandSpec.builder().permission("blwarps.sign").description(Texts.of("Create warp signs")).executor(new WarpSignExecutor(plugin))
                .arguments(new WarpCommandElement(plugin, Texts.of("warp"))).build();
    }

}
