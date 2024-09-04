# Whitelist - [1.20.4](https://github.com/Necko1/Whitelist/blob/master/src/main/resources/plugin.yml#L4)

A whitelist plugin for Bukkit servers that uses local UUIDs, without any Microsoft/Mojang authorization.

## Features

* Can migrate the whitelist from your Minecraft server to this plugin.
* Uses local UUIDs, without any Microsoft authorization.
* Allows adding players for a specific amount of time.
* Provides an API for the whitelist.
* Customizable messages with MiniMessage.

## Commands

* `/whitelist add <name> [time s/m/h/d]`: Add a player to the whitelist for a certain amount of time
* `/whitelist remove <name>`: Remove a player from the whitelist
* `/whitelist list`: List all whitelisted players
* `/whitelist reload`: Reload the config
* `/whitelist help`: Show this help
* `/whitelist on`: Turn the whitelist on
* `/whitelist off`: Turn the whitelist off
* `/whitelist migrate`: migrates the whitelist from Minecraft to this plugin.

## Permissions

* `whitelist.use`: allows using the whitelist commands.
* `whitelist.*`: allows all permissions above.