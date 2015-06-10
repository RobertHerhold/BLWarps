package com.blocklaunch.blwarps.managers;

import java.util.ArrayList;
import java.util.List;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.WarpBase;
import com.blocklaunch.blwarps.managers.storage.StorageManager;
import com.google.common.base.Optional;

public abstract class WarpBaseManager<T extends WarpBase> {
    
    public BLWarps plugin;
    public StorageManager<T> storage;
    public List<T> payload;
    public List<String> names;
    
    public WarpBaseManager (BLWarps plugin, StorageManager<T> storage) {
        this.plugin = plugin;
        this.storage = storage;
        this.payload = new ArrayList<T>();
        this.names = new ArrayList<String>();
    }
    
    public Optional<T> getOne(String name) {
        for (T warpBase : payload) {
            if (warpBase.getName().equalsIgnoreCase(name)) {
                return Optional.of(warpBase );
            }
        }
        return Optional.absent();
    }

    public void load() {
        this.payload = storage.load();
    }
    
    public void deleteOne(T t) {
        payload.remove(t);
    }
    
    public List<T> getPayload() {
        return payload;
    }
    
    public List<String> getNames() {
        return names;
    }
    
    public abstract Optional<String> addNew(T t);

}
