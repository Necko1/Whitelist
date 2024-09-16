# Whitelist - [1.20.4](https://github.com/Necko1/Whitelist/blob/master/src/main/resources/plugin.yml#L4)

A whitelist plugin for Bukkit servers that uses local UUIDs, without any Microsoft/Mojang authorization.
<br>P.S. <i>The plugin can work with other server versions. I just haven't tested it yet</i>

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

## API usage

1. Add depend Whitelist to your plugin.yml
2. #### Add dependency to your pom.xml:
```xml
<dependencies>
    <dependency>
        <groupId>org.necko1</groupId>
        <artifactId>whitelist</artifactId>
        <version>1.3.4-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```
* `<version>v-SNAPSHOT</version>` <-- Replace with the [latest package release](https://github.com/Necko1/Whitelist/packages/2244918)
3. #### Add instance of the `WhitelistAPI` to your onEnable:
```java
import org.necko1.whitelist.Whitelist;
import org.necko1.whitelist.api.WhitelistAPI;


private WhitelistAPI whitelistAPI;

@Override
public void onEnable() {
    Whitelist whitelist;
    try {
        whitelist = (Whitelist) getServer().getPluginManager().getPlugin("Whitelist");
        if (whitelist == null || !whitelist.isEnabled()) {
            getLogger().info("Whitelist plugin not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    } catch (NoClassDefFoundError e) {
        getLogger().info("(NoClassDefFoundError): Whitelist plugin wasn't loaded or you are using paper-plugin.yml");
        getServer().getPluginManager().disablePlugin(this);
        return;
    }

    whitelistAPI = whitelist.getAPI();
    
    // your code...
}
```

### Examples of API usage:

* Adding a player to the whitelist for 1 hour:
```java
api.add(player, api.parseTime("1h", true));
api.save(false);
```
* Getting player's whitelist data:
```java
WhitelistData data = api.getData(api.getUUID("necko1"));
String nickname = data.getName();
UUID uuid = data.getUuid();
long expire_after = data.getGood_by();
```

### <u>Do not use spigot player's UUID instead of plugin's local UUID. Spigot UUID's differ from those created by the Whitelist plugin.</u> If you want get player's UUID for whitelisting use <code>api.getUUID(player)</code> instead</u>
