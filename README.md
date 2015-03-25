# SpongeWarps
SpongeWarps is a [Sponge](https://www.spongepowered.org/) plugin for easily setting locations for players to warp to. Warps are stored as JSON (serialized using [Jackson](http://wiki.fasterxml.com/JacksonHome)) in the `config/warps.json` file.

##Commands
`/setwarp <warp name> [world name] [x] [y] [z]`

##TODO
1. Add `/warp` and `/delwarp`
1. Add support for REST-based warp storage
1. Add support for SQL(?) based warp storage
1. Add permissions
