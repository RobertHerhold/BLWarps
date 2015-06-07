package com.blocklaunch.blwarps.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.WarpBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;

public class FlatFileManager<T extends WarpBase> extends StorageManager<T> {

    private BLWarps plugin;
    private File file;
    private ObjectMapper mapper;
    private Class<T> type;

    public FlatFileManager(Class<T> type, BLWarps plugin, File file) {
        super(plugin);
        this.type = type;
        this.plugin = plugin;
        this.file = file;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void load() {
        Optional<List<T>> objectsOpt = readIn();
        if (!objectsOpt.isPresent()) {
            return;
        }

        plugin.getWarpBaseManager(type).setPayLoad(objectsOpt.get());

    }

    @Override
    public void saveNew(T t) {
        Optional<List<T>> objectsOpt = readIn();

        List<T> currentlySavedObjects = new ArrayList<T>();
        if (objectsOpt.isPresent()) {
            currentlySavedObjects = objectsOpt.get();
        }
        currentlySavedObjects.add(t);

        writeOut(currentlySavedObjects);

    }

    @Override
    public void delete(T t) {
        Optional<List<T>> objectsOpt = readIn();

        if (!objectsOpt.isPresent()) {
            return;
        }
        List<T> objects = objectsOpt.get();

        // Temporary warp for avoiding ConcurrentModificationException
        T objectToRemove = null;
        for (T object : objects) {
            if (object.getName().equalsIgnoreCase(t.getName())) {
                objectToRemove = object;
            }
        }
        if (objectToRemove != null)
            objects.remove(objectToRemove);

        writeOut(objects);

    }

    @Override
    public void update(T updatedObject) {
        Optional<List<T>> objectsOpt = readIn();

        if (!objectsOpt.isPresent()) {
            return;
        }
        List<T> objects = objectsOpt.get();

        // Temporary warp for avoiding ConcurrentModificationException
        T objectToUpdate = null;
        for (T t : objects) {
            if (t.getName().equalsIgnoreCase(updatedObject.getName())) {
                objectToUpdate = t;
            }
        }
        if (objectToUpdate != null) {
            objects.remove(objectToUpdate);
            objects.add(updatedObject);
        }
        writeOut(objects);
    }

    private Optional<List<T>> readIn() {
        if (!file.exists()) {
            return Optional.absent();
        }

        try {
            List<T> objects = mapper.readValue(file, new TypeReference<List<T>>() {});
            return Optional.of(objects);
        } catch (IOException e) {
            plugin.getLogger().warn(Constants.ERROR_FILE_READ);
            e.printStackTrace();
            return Optional.absent();
        }
    }

    private void writeOut(List<T> objects) {
        try {
            file.createNewFile(); // Only creates the file if it doesn't already exist.
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(file, objects);
        } catch (IOException e) {
            plugin.getLogger().warn(Constants.ERROR_FILE_WRITE);
            e.printStackTrace();
        }
    }

}
