package com.blocklaunch.blwarps.manager;

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
import com.blocklaunch.blwarps.Warp;

public class RestManager extends StorageManager {

	WebTarget webTarget;

	public RestManager() {
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		HttpAuthenticationFeature auth = HttpAuthenticationFeature.basicBuilder().nonPreemptive()
				.credentials(BLWarps.config.getRestConfig().getRESTUsername(), BLWarps.config.getRestConfig().getRESTPassword()).build();
		
		client.register(auth);
		webTarget = client.target(BLWarps.config.getRestConfig().getRESTURI());
	}

	@Override
	public boolean loadWarps() {
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
		if (response.getStatus() != 200) {
			BLWarps.logger.warn("There was an error loading the warps from the {} storage. Error code: {}",
					BLWarps.config.getStorageType(), response.getStatus());
			failedLoadWarps();
			return false;
		}
		WarpManager.warps = response.readEntity(new GenericType<List<Warp>>() {
		});
		return true;
	}

	@Override
	boolean saveNewWarp(Warp warp) {
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(
				Entity.entity(warp, MediaType.APPLICATION_JSON_TYPE));

		if (response.getStatus() != 201) {
			BLWarps.logger.warn("There was an error saving the warps to the {} storage. Error code: {}",
					BLWarps.config.getStorageType(), response.getStatus());
			failedSaveNewWarp(warp);
			return false;
		}
		return true;
	}

	@Override
	boolean deleteWarp(Warp warp) {
		Response response = webTarget.path(warp.getName()).request(MediaType.APPLICATION_JSON_TYPE).delete();

		if (response.getStatus() != 200) {
			BLWarps.logger.warn("There was an error saving the warps to the {} storage. Error code: {}",
					BLWarps.config.getStorageType(), response.getStatus());
			failedSaveNewWarp(warp);
			return false;
		}
		return true;
	}

}
