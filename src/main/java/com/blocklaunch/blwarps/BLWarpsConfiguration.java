package com.blocklaunch.blwarps;

import java.net.URI;

import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

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

	/********************
	 * General Settings *
	 ********************/

	/**
	 * Time, in seconds, between initiating a warp and teleporting the player
	 */
	@Setting(value = "warp-delay", comment = "Time, in seconds, between initiating a warp and teleporting the player")
	private int warpDelay = 5;

	/**
	 * Whether or not to cancel a player's warp if they move or get hurt
	 */
	@Setting(value = "pvp-protect", comment = "Whether or not to cancel a player's warp if they move or get hurt")
	private boolean pvpProtect = false;

	/**
	 * The storage solution to store warps in
	 * 
	 * @see StorageType
	 */
	@Setting(value = "storage-type", comment = "The storage solution to store warps in")
	private StorageType storageType = StorageType.FLATFILE;

	@Setting(value = "rest", comment = "Connection settings for a connected REST API. Only applicable for the 'REST' storage-type")
	private RestConfiguration restConfig = new RestConfiguration();

	@Setting(value = "sql", comment = "Connection settings for a connected SQL database. Only applicable for the 'SQL' storage-type")
	private SQLConfiguration sqlConfig = new SQLConfiguration();

	@ConfigSerializable
	public static class RestConfiguration {
		/**
		 * The URL of the REST API, if that option is being used
		 */
		@Setting(value = "uri", comment = "The URI of the connected REST API (complete path with endpoint)")
		private URI RESTURI = URI.create("http://localhost:8080");

		/**
		 * The username to log into a SQL database with
		 */
		@Setting(value = "username", comment = "Username to authenticate to the REST API with (basic authentication)")
		private String RESTUsername = "root";

		/**
		 * The password to log into a SQL database with
		 */
		@Setting(value = "password", comment = "Password to authenticate to the REST API with (basic authentication)")
		private String RESTPassword = "pass";

		public URI getRESTURI() {
			return RESTURI;
		}

		public String getRESTUsername() {
			return RESTUsername;
		}

		public String getRESTPassword() {
			return RESTPassword;
		}
	}

	@ConfigSerializable
	public static class SQLConfiguration {
		/**
		 * The specific SQL database to use, Ex. MySQL, H2, SQLite, etc.
		 */
		@Setting(value = "database", comment = "The specific SQL database to use. Supported: MySQL, H2, SQLite")
		private String SQLDatabase = "MySQL";

		/**
		 * The URL of the SQL database, if that option is being used
		 */
		@Setting(value = "url", comment = "The URL of the connected SQL database")
		private String SQLURL = "localhost:3306";

		/**
		 * The name of the SQL database, if that option is being used
		 */
		@Setting(value = "database-name", comment = "The name of the SQL database, if that option is being used")
		private String SQLDatabaseName = "BLWarps";

		/**
		 * The username to log into a SQL database with
		 */
		@Setting(value = "username", comment = "Username to authenticate to the SQL database with")
		private String SQLUsername = "root";

		/**
		 * The password to log into a SQL database with
		 */
		@Setting(value = "password", comment = "Password to authenticate to the SQL database with")
		private String SQLPassword = "pass";

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

	public int getWarpDelay() {
		return warpDelay;
	}

	public boolean isPvpProtect() {
		return pvpProtect;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	public RestConfiguration getRestConfig() {
		return restConfig;
	}

	public SQLConfiguration getSQLConfig() {
		return sqlConfig;
	}

}
