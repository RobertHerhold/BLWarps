# BLWarps [![Build Status](https://travis-ci.org/BlockLaunch/BLWarps.svg?branch=master)](https://travis-ci.org/BlockLaunch/BLWarps)
BLWarps is a [Sponge](https://www.spongepowered.org/) plugin for easily setting locations for players to warp to.

##Commands
Note: In the following commands, square brackets (`[]`) indicate optional arguments, and angle brackets (`<>`) indicate required arguments.
* `/warp set <warp name> [x] [y] [z]`
 * Create a new warp with name `warp name` and optional coordinates (`x`, `y`, `z`).
* `/warp <warp name>`
 * Warp to the warp with name `warp name`.
* `/warp delete <warp name>`
 * Delete the warp with name `warp name`.
* `/warp list`
 * List all of the currently active warps.
 * Only the warps that the `CommandSource` has permission to use will be displayed to them
* `/warp info <warp name>`
 * Displays information about the specified warp
* `/warp group add <warp name> <group name>`
 * Add the warp with name `warp name` to the group `group name`.
* `/warp group remove <warp name> <group name>`
 * Remove the warp with name `warp name` from the group `group name`
* `/warp group removeall <group name>`
 * Remove all warps from the group with name `group name`
* `/warp group info <group name>`
 * Displays information about the specified warp group

##Permissions
Permissions can be required for either individual warps, or warp groups, as shown below:
 * `blwarps.warp.<warp name>`
 * `blwarps.warp.group.<group name>`
 * `blwarps.warp.*` (wildcard --> warp to anywhere)

##Warp Signs
Warp signs can be created for any previously established warp. To do this, simply place a sign anywhere in the world, and write the following text on each line:

1. [Warp]
2. `<warp name>`
3. Doesn't matter
4. Doesn't matter

If you have correctly placed the sign, the text on the sign will be colored and reformatted. To use the sign to warp, simply right click on the sign.

##Storage
Regardless of the storage solution, each warp has 5 properties:
* name - the name of the warp
* world - the name of the world that contains the warp
* x - the x coordinate of the warp (stored as a double)
* y - the y coordinate of the warp (stored as a double)
* z - the z coordinate of the warp (stored as a double)

And optional properties:
* groups - the groups of which the warp belongs to

###Flat File Storage
For flat file storage, warps are stored as JSON (serialized using [Jackson](http://wiki.fasterxml.com/JacksonHome)) in the `config/BLWarps/warps.json` file. A sample `warps.json` file would be similar to the following:

```json
[ {
    "name" : "warp1",
    "world" : "world",
    "x" : 40.3,
    "y" : 41.0,
    "z" : 72.07,
    "groups" : [ "group", "group2" ]
}, {
    "name" : "warp2",
    "world" : "DIM-1",
    "x" : 7.63,
    "y" : 64.0,
    "z" : 11.79
} ]
```
###SQL Storage
Any [JDBC](http://www.oracle.com/technetwork/java/overview-141217.html)-compatible database is a valid option for storing warps. The JDBC connection URL must be specified in the `sql.connection-url`. By default, the Warps will be stored in the `warps` table. Currently, the name of the table cannot be changed. BLWarps uses [JDBI](http://jdbi.org/) for executing queries and mapping results to a [Warp](https://github.com/BlockLaunch/BLWarps/blob/master/src/main/java/com/blocklaunch/blwarps/Warp.java) object.

###[REST API](http://en.wikipedia.org/wiki/Representational_state_transfer) Storage
For REST-based storage, HTTP requests are sent using the  [Jersey Client](https://jersey.java.net/documentation/latest/client.html) to the URI specified by the `rest-uri` field in the configuration. Currently, only basic authentication is available, which uses the `rest.username` and `rest.password` credentials (by default, they are `root` and `pass`, respectively). The plugin will send the following requests, and will expect the `application/json` media type as part of the response:
* `GET` when loading warps - must receive a List of [Warps](https://github.com/BlockLaunch/BLWarps/blob/master/src/main/java/com/blocklaunch/blwarps/Warp.java)
* `POST` when saving a new warp
* `DELETE` when deleting a warp. The warp's name will be a path parameter.
 * Ex: `http://localhost:8080/warps/deletethiswarp`
* `PUT` when updating a warp (adding/removing groups)

##Building BLWarps
BLWarps uses Maven as a dependency manager and as a build tool. To build the plugin from source, make sure [Maven](https://maven.apache.org/download.cgi) is installed, and run `mvn clean install`. The newly built plugin will be in `target/bl-warps-{version}.jar`. For developing, run the Maven build once, then add the `target/generated-sources/java-templates` directory as a source folder (see graphic below for how to do this in Eclipse).

![Add generated sources as a source folder](mvn_build_tutorial.png)
