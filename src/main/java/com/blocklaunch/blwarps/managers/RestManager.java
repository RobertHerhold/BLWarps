package com.blocklaunch.blwarps.managers;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.WarpBase;

public class RestManager<T extends WarpBase> extends StorageManager<T> {

    private Class<T> type;
    WebTarget webTarget;

    public RestManager(Class<T> type, BLWarps plugin) {
        super(plugin);
        this.type = type;

        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        HttpAuthenticationFeature auth =
                HttpAuthenticationFeature.basicBuilder().nonPreemptive()
                        .credentials(plugin.getConfig().getRestConfig().getRESTUsername(), plugin.getConfig().getRestConfig().getRESTPassword())
                        .build();

        client.register(auth);
        webTarget = client.target(plugin.getConfig().getRestConfig().getRESTURI());
    }

    /**
     * Send a GET request to the REST API Attempt to map the received entity to a List<Warp>
     */
    @Override
    public void load() {
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() != 200) {
            plugin.getLogger().warn("There was an error loading from the {} storage. Error code: {}", plugin.getConfig().getStorageType(),
                    response.getStatus());
            failedLoad(type);
            return;
        }
        plugin.getWarpBaseManager(type).setPayLoad(response.readEntity(new GenericType<List<T>>() {}));
    }

    /**
     * Send a POST request to the REST API with a new Warp to save
     */
    @Override
    public void saveNew(T t) {
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(t, MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() != 201) {
            plugin.getLogger().warn("There was an error saving the warps to the {} storage. Error code: {}", plugin.getConfig().getStorageType(),
                    response.getStatus());
            failedSaveNew(t);
        }
    }

    /**
     * Send a DELETE request to the REST API with the name of the warp to delete in the path
     */
    @Override
    public void delete(T t) {
        Response response = webTarget.path(t.getName()).request(MediaType.APPLICATION_JSON_TYPE).delete();

        if (response.getStatus() != 200) {
            plugin.getLogger().warn("There was an error saving the warps to the {} storage. Error code: {}", plugin.getConfig().getStorageType(),
                    response.getStatus());
            failedDelete(t);
        }
    }

    /**
     * Send a PUT request to the REST API with the warp to replace the existing one with the same
     * name
     */
    @Override
    public void update(T t) {
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(t, MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() != 200) {
            plugin.getLogger().warn("There was an error saving the warps to the {} storage. Error code: {}", plugin.getConfig().getStorageType(),
                    response.getStatus());
            failedUpdate(t);
        }

    }

}
