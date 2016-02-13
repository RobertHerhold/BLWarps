package com.blocklaunch.blwarps;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.validation.constraints.Pattern;

public abstract class WarpBase {

    public String id;
    // Only allow alphanumeric names
    @Pattern(regexp = "^[A-Za-z0-9]*$") public String name;
    public String owner;
    public String world;

    // Empty constructor for Jackson
    public WarpBase() {

    }

    public WarpBase(String owner, String name, String world) {
        this.owner = owner;
        this.name = name;
        this.world = world;
        generateId();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    private void generateId() {
        try {
            UUID playerUUID = UUID.fromString(owner);
            Player player = Sponge.getServer().getPlayer(playerUUID).get();
            this.id = player.getName() + "/" + this.name;
        } catch (IllegalArgumentException | NoSuchElementException e) {
            this.id = this.owner + "/" + this.name;
        }

    }

    protected double formatDouble(double d) {
        DecimalFormat f = new DecimalFormat("##.00", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        return Double.valueOf(f.format(d));
    }

}
