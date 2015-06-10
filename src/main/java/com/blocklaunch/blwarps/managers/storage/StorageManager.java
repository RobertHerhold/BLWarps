package com.blocklaunch.blwarps.managers.storage;

import java.util.List;

import com.blocklaunch.blwarps.WarpBase;

public interface StorageManager<T extends WarpBase> {

    public List<T> load();

    public void saveNew(T t);

    public void delete(T t);

    public void update(T t);

}
