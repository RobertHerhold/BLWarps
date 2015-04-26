package com.blocklaunch.spongewarps;

import java.net.URI;

/**
 * Class that contains all the configurable options for the plugin
 */
public class Settings {

	/**
	 * Time, in seconds, between initiating a warp and teleporting the player
	 */
	public static int warpDelay = 5;

	/**
	 * Whether or not to cancel a player's warp if they move or get hurt
	 */
	public static boolean pvpProtect = false;
	
	/**
	 * The storage solution to store warps in
	 * @see StorageType
	 */
	public static StorageType storageType = StorageType.FLATFILE;
	
	
	/**
	 * The URL of the REST API, if that option is being used
	 */
	public static URI restURI = URI.create("http://localhost:8080");
	
	/**
	 * The specific SQL database to use, Ex. MySQL, H2, SQLite, etc.
	 */
	public static String SQLDatabase = "MySQL";
	
	/**
	 * The URL of the SQL database, if that option is being used
	 */
	public static String SQLURL = "localhost:3306";
	
	/**
	 * The name of the SQL database, if that option is being used
	 */
	public static String SQLDatabaseName = "SpongeWarps";
	
	
	/**
	 * The username to log into a SQL database with
	 */
	public static String SQLUsername = "root";
	
	/**
	 * The password to log into a SQL database with
	 */
	public static String SQLPassword = "pass";
}
