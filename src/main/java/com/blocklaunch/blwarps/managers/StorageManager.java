package com.blocklaunch.blwarps.managers;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.WarpBase;

public abstract class StorageManager<T extends WarpBase> {

    public BLWarps plugin;

    public StorageManager(BLWarps plugin) {
        this.plugin = plugin;
    }

    public void failedLoad(Class<? extends WarpBase> clazz) {
        plugin.getFallBackManager(clazz).load();
    }

    public void failedSaveNew(T t) {
        plugin.getFallBackManager(t.getClass()).saveNew(t);
    }

    public void failedDelete(T t) {
        plugin.getFallBackManager(t.getClass()).delete(t);
    }
    
    public void failedUpdate(T t) {
        plugin.getFallBackManager(t.getClass()).update(t);
    }

    public abstract void load();

    public abstract void saveNew(T t);

    public abstract void delete(T t);

    public abstract void update(T t);

}
