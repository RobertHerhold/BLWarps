package com.blocklaunch.blwarps.managers.storage;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.WarpBase;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;
import jersey.repackaged.com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlatFileManager<T extends WarpBase> implements StorageManager<T> {

    private Class<T> type;
    private BLWarps plugin;
    private File file;
    private ObjectMapper mapper;

    public FlatFileManager(Class<T> type, BLWarps plugin, File file) {
        this.type = type;
        this.plugin = plugin;
        this.file = file;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<T> load() {
        Optional<List<T>> objectsOpt = readIn();
        if (!objectsOpt.isPresent()) {
            return Lists.newArrayList();
        }

        return objectsOpt.get();
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
        if (objectToRemove != null) {
            objects.remove(objectToRemove);
        }

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
        if (!this.file.exists()) {
            return Optional.absent();
        }

        try {
            JavaType type = this.mapper.getTypeFactory().constructParametricType(List.class, this.type);
            List<T> objects = this.mapper.readValue(this.file, type);
            return Optional.of(objects);
        } catch (IOException e) {
            this.plugin.getLogger().warn(Constants.ERROR_FILE_READ);
            e.printStackTrace();
            return Optional.absent();
        }
    }

    private void writeOut(List<T> objects) {
        try {
            this.file.createNewFile(); // Only creates the file if it doesn't
                                       // already
            // exist.
            this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
            this.mapper.writeValue(this.file, objects);
        } catch (IOException e) {
            this.plugin.getLogger().warn(Constants.ERROR_FILE_WRITE);
            e.printStackTrace();
        }
    }

}
