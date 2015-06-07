package com.blocklaunch.blwarps.managers;

import java.util.List;

import com.blocklaunch.blwarps.WarpBase;
import com.google.common.base.Optional;

public abstract class WarpBaseManager<T extends WarpBase> {
    
    private List<? extends WarpBase> payload;
    
    public List<? extends WarpBase> getPayLoad() {
        return payload;
    }
    
    public void setPayLoad(List<? extends WarpBase> payload) {
        this.payload = payload;
    }

    public abstract Optional<String> addNew(T t);

    public abstract void deleteOne(T t);

    public abstract Optional<T> getOne(String name);

}
