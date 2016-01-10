package com.blocklaunch.blwarps.commands;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.commands.elements.WarpCommandElement;
import com.blocklaunch.blwarps.commands.elements.WarpGroupCommandElement;
import com.blocklaunch.blwarps.commands.elements.WarpRegionCommandElement;
import com.blocklaunch.blwarps.commands.executors.CreateWarpExecutor;
import com.blocklaunch.blwarps.commands.executors.DeleteWarpExecutor;
import com.blocklaunch.blwarps.commands.executors.ListWarpsExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpInfoExecutor;
import com.blocklaunch.blwarps.commands.executors.WarpSignExecutor;
import com.blocklaunch.blwarps.commands.executors.group.AddWarpToGroupExecutor;
import com.blocklaunch.blwarps.commands.executors.group.DeleteGroupExecutor;
import com.blocklaunch.blwarps.commands.executors.group.GroupInfoExecutor;
import com.blocklaunch.blwarps.commands.executors.group.RemoveWarpFromGroupExecutor;
import com.blocklaunch.blwarps.commands.executors.region.CreateWarpRegionExecutor;
import com.blocklaunch.blwarps.commands.executors.region.DeleteWarpRegionExecutor;
import com.blocklaunch.blwarps.commands.executors.region.ListWarpRegionsExecutor;
import com.blocklaunch.blwarps.commands.executors.region.WarpRegionInfoExecutor;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WarpCommandGenerator {

    private BLWarps plugin;

    public WarpCommandGenerator(BLWarps plugin) {
        this.plugin = plugin;
    }

    public CommandSpec mainWarpCommand() {
        HashMap<List<String>, CommandSpec> subCommands = new HashMap<>();

        subCommands.put(Arrays.asList("set", "add", "create"), createWarpSubCommand());
        subCommands.put(Arrays.asList("delete", "del"), deleteWarpSubCommand());
        subCommands.put(Arrays.asList("list", "ls"), listWarpSubCommand());
        subCommands.put(Arrays.asList("info"), warpInfoSubCommand());
        subCommands.put(Arrays.asList("group"), groupSubCommand());
        subCommands.put(Arrays.asList("region"), regionSubCommand());
        subCommands.put(Arrays.asList("sign"), warpSignSubCommand());

        CommandSpec mainWarpCommand =
                CommandSpec.builder().permission("blwarps.warp").description(Text.of("Teleport to a warp location"))
                        .extendedDescription(Text.of("Teleports you to the location of the specified warp."))
                        .executor(new WarpExecutor(this.plugin))
                        .arguments(GenericArguments.firstParsing(new WarpCommandElement(this.plugin, Text.of("warp")))).children(subCommands)
                        .build();
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
                .description(Text.of("Set a warp"))
                .extendedDescription(Text.of("Sets a warp at your location, or at the specified coordinates"))
                .executor(new CreateWarpExecutor(this.plugin))
                .arguments(
                        GenericArguments.seq(GenericArguments.string(Text.of("name")),
                                GenericArguments.optional(GenericArguments.vector3d(Text.of("position"))))).build();
    }

    /**
     * Command: "/warp delete <warp name>"
     *
     * @return
     */
    private CommandSpec deleteWarpSubCommand() {
        return CommandSpec.builder().permission("blwarps.delete").description(Text.of("Delete a warp"))
                .extendedDescription(Text.of("Deletes the warp with the specified name")).executor(new DeleteWarpExecutor(this.plugin))
                .arguments(new WarpCommandElement(this.plugin, Text.of("warp"))).build();
    }

    /**
     * Command: "/warp list"
     *
     * @return
     */
    private CommandSpec listWarpSubCommand() {
        return CommandSpec.builder().permission("blwarps.list").description(Text.of("List warps"))
                .extendedDescription(Text.of("Lists all warps, split up into pages.")).executor(new ListWarpsExecutor(this.plugin))
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("page")))).build();
    }

    /**
     * Command: "/warp info <warp name>"
     *
     * @return
     */
    private CommandSpec warpInfoSubCommand() {
        return CommandSpec.builder().permission("blwarps.info").description(Text.of("Display information about a warp"))
                .executor(new WarpInfoExecutor()).arguments(new WarpCommandElement(this.plugin, Text.of("warp"))).build();
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

        return CommandSpec.builder().permission("blwarps.group").description(Text.of("Manage warp groups")).children(groupSubCommands).build();
    }

    /**
     * Command: "/warp group add <warp name> <group name>
     *
     * @return
     */
    private CommandSpec addWarpToGroupSubCommand() {
        return CommandSpec.builder().permission("blwarps.group.add").description(Text.of("Add a warp to a group"))
                .executor(new AddWarpToGroupExecutor(this.plugin))
                .arguments(new WarpCommandElement(this.plugin, Text.of("warp")), new WarpGroupCommandElement(this.plugin, Text.of("group")))
                .build();
    }

    /**
     * Command: "/warp group remove <warp name> <group name>"
     *
     * @return
     */
    private CommandSpec removeWarpFromGroupSubCommand() {
        return CommandSpec.builder().permission("blwarps.group.remove").description(Text.of("Remove a warp from a group"))
                .executor(new RemoveWarpFromGroupExecutor(this.plugin))
                .arguments(new WarpCommandElement(this.plugin, Text.of("warp")), new WarpGroupCommandElement(this.plugin, Text.of("group")))
                .build();
    }

    /**
     * Command: "/warp group delete <group name>"
     *
     * @return
     */
    private CommandSpec deleteGroupSubCommand() {
        return CommandSpec.builder().permission("blwarps.group.delete").description(Text.of("Delete a warp group"))
                .executor(new DeleteGroupExecutor(this.plugin)).arguments(new WarpGroupCommandElement(this.plugin, Text.of("group"))).build();
    }

    /**
     * Command: "/warp group info <group name>
     *
     * @return
     */
    private CommandSpec groupInfoSubCommand() {
        return CommandSpec.builder().permission("blwarps.group.info").description(Text.of("Display information about a warp group"))
                .executor(new GroupInfoExecutor(this.plugin)).arguments(new WarpGroupCommandElement(this.plugin, Text.of("group"))).build();
    }

    /**
     * Command: "/warp region create:delete:list:info"
     *
     * @return
     */
    private CommandSpec regionSubCommand() {
        HashMap<List<String>, CommandSpec> regionSubCommands = new HashMap<>();

        regionSubCommands.put(Arrays.asList("set", "add", "create"), createWarpRegionSubCommand());
        regionSubCommands.put(Arrays.asList("delete", "del"), deleteWarpRegionSubCommand());
        regionSubCommands.put(Arrays.asList("list", "ls"), listWarpRegionSubCommand());
        regionSubCommands.put(Arrays.asList("info"), warpRegionInfoSubCommand());

        return CommandSpec.builder().permission("blwarps.region").description(Text.of("Manage warp regions"))
                .extendedDescription(Text.of("Manage regions in which a player will be warped upon entering")).children(regionSubCommands).build();

    }

    /**
     * Command: "/warp region create"
     *
     * @return
     */
    private CommandSpec createWarpRegionSubCommand() {
        return CommandSpec
                .builder()
                .permission("blwarps.region.create")
                .description(Text.of("Display information about a warp region"))
                .executor(new CreateWarpRegionExecutor(this.plugin))
                .arguments(GenericArguments.string(Text.of("name")), new WarpCommandElement(this.plugin, Text.of("warp")),
                        GenericArguments.vector3d(Text.of("corner1")), GenericArguments.vector3d(Text.of("corner2"))).build();
    }

    /**
     * Command: "/warp region delete <region name>"
     *
     * @return
     */
    private CommandSpec deleteWarpRegionSubCommand() {
        return CommandSpec.builder().permission("blwarps.region.delete").description(Text.of("Delete a warp region"))
                .executor(new DeleteWarpRegionExecutor(this.plugin)).arguments(new WarpRegionCommandElement(Text.of("region"), this.plugin)).build();
    }

    /**
     * Command: "/warp region list"
     *
     * @return
     */
    private CommandSpec listWarpRegionSubCommand() {
        return CommandSpec.builder().permission("blwarps.region.list").description(Text.of("List warp regions"))
                .extendedDescription(Text.of("Lists all warp regions, split up into pages.")).executor(new ListWarpRegionsExecutor(this.plugin))
                .build();
    }

    /**
     * Command: "/warp region info <region name>"
     *
     * @return
     */
    private CommandSpec warpRegionInfoSubCommand() {
        return CommandSpec.builder().permission("blwarps.region.info").description(Text.of("Create a warp region"))
                .extendedDescription(Text.of("Create a region in which a player will be warped upon entering"))
                .executor(new WarpRegionInfoExecutor(this.plugin)).arguments(new WarpRegionCommandElement(Text.of("region"), this.plugin)).build();
    }

    /**
     * Command: "/warp sign <warp name>"
     *
     * @return
     */
    private CommandSpec warpSignSubCommand() {
        return CommandSpec.builder().permission("blwarps.sign").description(Text.of("Create warp signs"))
                .executor(new WarpSignExecutor())
                .arguments(new WarpCommandElement(this.plugin, Text.of("warp"))).build();
    }

}
