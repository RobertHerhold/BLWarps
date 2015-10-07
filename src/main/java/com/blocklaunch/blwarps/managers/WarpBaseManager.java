package com.blocklaunch.blwarps.managers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.WarpBase;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

public abstract class WarpBaseManager<T extends WarpBase> {

    public BLWarps plugin;
    public StorageManager<T> storage;
    public List<T> payload;
    public List<String> names;

    public WarpBaseManager(BLWarps plugin, StorageManager<T> storage) {
        this.plugin = plugin;
        this.storage = storage;
        this.payload = new ArrayList<T>();
        this.names = new ArrayList<String>();
    }

    public Optional<T> getOne(String name) {
        for (T warpBase : this.payload) {
            if (warpBase.getName().equalsIgnoreCase(name)) {
                return Optional.of(warpBase);
            }
        }
        return Optional.empty();
    }

    public void load() {
        this.payload = this.storage.load();
    }

    public void deleteOne(T t) {
        this.payload.remove(t);
        this.storage.delete(t);
    }

    public List<T> getPayload() {
        return this.payload;
    }

    public List<String> getNames() {
        return this.names;
    }

    public abstract Optional<String> addNew(T t);

}
