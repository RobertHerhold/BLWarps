package com.blocklaunch.spongewarps.manager;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;

import com.blocklaunch.spongewarps.Settings;
import com.blocklaunch.spongewarps.SpongeWarps;

public class RestManager extends StorageManager {

	WebTarget webTarget;

	public RestManager() {
		Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		webTarget = client.target(Settings.restURI);

	}

	@Override
	public boolean loadWarps() {
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
		if (response.getStatus() != 200) {
			SpongeWarps.logger.warn("There was an error loading the warps from the {} storage. Error code: {}",
					Settings.storageType, response.getStatus());
			failedLoadWarps();
			return false;
		}
		return true;
	}

	@Override
	public boolean saveWarps() {
		Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(
				Entity.entity(WarpManager.warps, MediaType.APPLICATION_JSON_TYPE));

		if (response.getStatus() != 201) {
			SpongeWarps.logger.warn("There was an error saving the warps to the {} storage. Error code: {}",
					Settings.storageType, response.getStatus());
			failedSaveWarps();
			return false;
		}
		return true;
	}

}
