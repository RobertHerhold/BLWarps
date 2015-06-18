package com.blocklaunch.blwarps;

import javax.validation.constraints.Size;

public abstract class WarpBase {

    // Max of 15 chars b/c a sign only fits 15 chars on a line
    @Size(min = 1, max = 15) public String name;
    public String world;

    // Empty constructor for Jackson
    public WarpBase() {

    }

    public WarpBase(String name, String world) {
        this.name = name;
        this.world = world;
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

}
