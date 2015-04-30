package com.blocklaunch.blwarps;

import java.net.URI;

import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;

/**
 * Class that contains all the configurable options for the plugin
 */
public class BLWarpsConfiguration {
	
	public static final ObjectMapper<BLWarpsConfiguration> MAPPER;

	static {
		try {
			MAPPER = ObjectMapper.forClass(BLWarpsConfiguration.class);
		} catch (ObjectMappingException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	// ////////////////////
	// General Settings //
	// ////////////////////

	/**
	 * Time, in seconds, between initiating a warp and teleporting the player
	 */
	@Setting("warp-delay")
	private int warpDelay = 5;

	/**
	 * Whether or not to cancel a player's warp if they move or get hurt
	 */
	@Setting("pvp-protect")
	private boolean pvpProtect = false;

	/**
	 * The storage solution to store warps in
	 * 
	 * @see StorageType
	 */
	@Setting("storage-type")
	private StorageType storageType = StorageType.FLATFILE;

	///////////////////
	// REST Settings //
	///////////////////

	/**
	 * The URL of the REST API, if that option is being used
	 */
	@Setting("rest.uri")
	private URI RESTURI = URI.create("http://localhost:8080");

	/**
	 * The username to log into a SQL database with
	 */
	@Setting("rest.username")
	private String RESTUsername = "root";

	/**
	 * The password to log into a SQL database with
	 */
	@Setting("rest.password")
	private String RESTPassword = "pass";

	//////////////////
	// SQL Settings //
	//////////////////

	/**
	 * The specific SQL database to use, Ex. MySQL, H2, SQLite, etc.
	 */
	@Setting("sql.database")
	private String SQLDatabase = "MySQL";

	/**
	 * The URL of the SQL database, if that option is being used
	 */
	@Setting("sql.url")
	private String SQLURL = "localhost:3306";

	/**
	 * The name of the SQL database, if that option is being used
	 */
	@Setting("sql.database-name")
	private String SQLDatabaseName = "BLWarps";

	/**
	 * The username to log into a SQL database with
	 */
	@Setting("sql.username")
	private String SQLUsername = "root";

	/**
	 * The password to log into a SQL database with
	 */
	@Setting("sql.password")
	private String SQLPassword = "pass";

	public int getWarpDelay() {
		return warpDelay;
	}

	public boolean isPvpProtect() {
		return pvpProtect;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	public URI getRESTURI() {
		return RESTURI;
	}

	public String getRESTUsername() {
		return RESTUsername;
	}

	public String getRESTPassword() {
		return RESTPassword;
	}

	public String getSQLDatabase() {
		return SQLDatabase;
	}

	public String getSQLURL() {
		return SQLURL;
	}

	public String getSQLDatabaseName() {
		return SQLDatabaseName;
	}

	public String getSQLUsername() {
		return SQLUsername;
	}

	public String getSQLPassword() {
		return SQLPassword;
	}
}
