package com.blocklaunch.blwarps;

import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.net.URI;

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
    private SqlConfiguration sqlConfig = new SqlConfiguration();

    @ConfigSerializable
    public static class RestConfiguration {
        /**
         * The URL of the REST API, if that option is being used
         */
        @Setting(value = "uri", comment = "The URI of the connected REST API (complete path with endpoint)")
        private URI restUri = URI.create("http://localhost:8080");

        /**
         * The username to log into a SQL database with
         */
        @Setting(value = "username", comment = "Username to authenticate to the REST API with (basic authentication)")
        private String restUsername = "root";

        /**
         * The password to log into a SQL database with
         */
        @Setting(value = "password", comment = "Password to authenticate to the REST API with (basic authentication)")
        private String restPassword = "jdbc:mysql://localhost:3306/?user=root";

        public URI getRESTURI() {
            return restUri;
        }

        public String getRESTUsername() {
            return restUsername;
        }

        public String getRESTPassword() {
            return restPassword;
        }
    }

    @ConfigSerializable
    public static class SqlConfiguration {
        
        /**
         * The JDBC connection url to access the SQL database with
         */
        @Setting(value = "connection-url", comment = "JDBC Connection URL to access the SQL database with")
        private String jdbcConnectionUrl = "jdbc:mysql://localhost:3306/BLWarps?user=root";

        public String getConnectionUrl() {
            return jdbcConnectionUrl;
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

    public SqlConfiguration getSqlConfig() {
        return sqlConfig;
    }

}
