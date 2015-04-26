# SpongeWarps
SpongeWarps is a [Sponge](https://www.spongepowered.org/) plugin for easily setting locations for players to warp to.

##Commands
* `/setwarp <warp name> [world name] [x] [y] [z]`
* `/warp <warp name>`
* `/delwarp <warp name>`
* `/listwarps [page number]`

##Storage
Regardless of the storage solution, each warp has 5 properties:
* name - the name of the warp
* world - the name of the world that contains the warp
* x - the x coordinate of the warp (stored as a double)
* y - the y coordinate of the warp (stored as a double)
* z - the z coordinate of the warp (stored as a double)

Note: If an attempt to save warps with any storage method fails, the plugin will revert to a flat file to store the warps.

###Flat File Storage
For flat file storage, warps are stored as JSON (serialized using [Jackson](http://wiki.fasterxml.com/JacksonHome)) in the `config/SpongeWarps/warps.json` file. A sample `warps.json` file would be similar to the following:

```json
[ {
    "name" : "warp1",
    "world" : "world",
    "x" : 40.3,
    "y" : 41.0,
    "z" : 72.07
}, {
    "name" : "warp2",
    "world" : "DIM-1",
    "x" : 7.63,
    "y" : 64.0,
    "z" : 11.79
} ]
```
###SQL Storage
Any SQL database supported by [JDBC](http://www.oracle.com/technetwork/java/overview-141217.html) is a valid option for storing warps. This plugin uses [JDBI](http://jdbi.org/) for executing queries and mapping results to a [Warp](https://github.com/BlockLaunch/SpongeWarps/blob/master/src/main/java/com/blocklaunch/spongewarps/Warp.java) object. By default, the plugin will attempt to connect to `localhost:3306` with the `SpongeWarps` database (these can be changed with the `sql.url` and `sql.database` fields, respectively, in the configuration). A table named `warps` will be created by default. Currently, the name of the table cannot be changed.

###[REST API](http://en.wikipedia.org/wiki/Representational_state_transfer) Storage
For REST-based storage, HTTP requests are sent using the  [Jersey Client](https://jersey.java.net/documentation/latest/client.html) to the URI specified by the `rest-uri` field in the configuration. The plugin will send a `GET` request (when loading warps) or a `POST` request (when saving warps). It is up to the REST API to return a List of [Warps](https://github.com/BlockLaunch/SpongeWarps/blob/master/src/main/java/com/blocklaunch/spongewarps/Warp.java) with the `application/json` media type upon receiving a `GET` request, and to appropriately save the warps upon receiving a `POST` request containing the serialized warps


##TODO
* Add permissions
* Add sign warps
* Add authentication for REST
