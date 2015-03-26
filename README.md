# SpongeWarps
SpongeWarps is a [Sponge](https://www.spongepowered.org/) plugin for easily setting locations for players to warp to. Warps are stored as JSON (serialized using [Jackson](http://wiki.fasterxml.com/JacksonHome)) in the `config/warps.json` file.

##Commands
* `/setwarp <warp name> [world name] [x] [y] [z]`
* `/warp <warp name>`
* `/delwarp <warp name>`

##TODO
* Add `/listwarp`
* Add support for REST-based warp storage
* Add support for SQL(?) based warp storage
* Add permissions
* Add support for warping across worlds
