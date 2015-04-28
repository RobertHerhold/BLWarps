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
Any SQL database supported by [JDBC](http://www.oracle.com/technetwork/java/overview-141217.html) whose connection URL takes the form `jdbc:engine://[username[:password]@]host/database` is a valid option for storing warps. This plugin uses [JDBI](http://jdbi.org/) for executing queries and mapping results to a [Warp](https://github.com/BlockLaunch/SpongeWarps/blob/master/src/main/java/com/blocklaunch/spongewarps/Warp.java) object. By default, the plugin will attempt to connect to `localhost:3306` on the `SpongeWarps` database with the `root` username and `pass` password (these can be changed with the `sql.url`, `sql.database`, `sql.username`, and `sql.password` fields, respectively, in the configuration). A table named `warps` will be created by default. Currently, the name of the table cannot be changed.

###[REST API](http://en.wikipedia.org/wiki/Representational_state_transfer) Storage
For REST-based storage, HTTP requests are sent using the  [Jersey Client](https://jersey.java.net/documentation/latest/client.html) to the URI specified by the `rest-uri` field in the configuration. Currently, only basic authentication is available, which uses the `rest.username` and `rest.password` credentials (by default, they are `root` and `pass`, respectively). The plugin will send the following requests, and will expect the `application/json` media type as part of the response:
* `GET` when loading warps - must receive a List of [Warps](https://github.com/BlockLaunch/SpongeWarps/blob/master/src/main/java/com/blocklaunch/spongewarps/Warp.java)
* `POST` when saving warps
* `DELETE` when deleting a warp. The warp's name will be a path parameter. Ex: `http://localhost:8080/warps/deletethiswarp`

##Building SpongeWarps
SpongeWarps uses Maven as a dependency manager and as a build tool. To build the plugin from source, make sure [Maven](https://maven.apache.org/download.cgi) is installed, and run `mvn clean install`. The newly built plugin will be in `target/sponge-warps-1.0.jar`

##TODO
* Add permissions
* Add sign warps
* Add authentication for REST
