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

###Flat File Storage
For flat file storage, warps are stored as JSON (serialized using [Jackson](http://wiki.fasterxml.com/JacksonHome)) in the `config/warps.json` file. A sample `warps.json` file would be similar to the following:

```json
[{"name":"warp1","world":"world_overworld","x":-190,"y":73,"z":-135},
{"name":"warp2","world":"world_overworld","x":147,"y":26,"z":97}]
```

Note the use of square brackets to indicate the presence of an array. This *is* necessary for the proper de-serialization of the JSON objects (even when there is only 1 warp).

##TODO
* Add support for REST-based warp storage
* Add support for SQL(?) based warp storage
* Add permissions
* Add support for warping across worlds
