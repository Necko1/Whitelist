package org.necko1.whitelist;

import org.necko1.whitelist.api.WhitelistAPI;
import org.necko1.whitelist.api.WhitelistAPI_impl;
import org.necko1.whitelist.cmd.WhiteListTabCompleter;
import org.necko1.whitelist.cmd.WhitelistCommand;
import org.necko1.whitelist.data.WLConfig;
import org.necko1.whitelist.data.WLJsonData;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Whitelist extends JavaPlugin {

    private WhitelistAPI api;

    public WhitelistAPI getAPI() { return this.api; }

    public static WLJsonData json;
    public static WLConfig wlConfig;
    public static final MiniMessage miniMessage = MiniMessage.miniMessage();
    public static final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    private static Whitelist instance;
    public static Whitelist getInstance() { return instance; }

    private static boolean turned;
    public static boolean isTurned() { return turned; }
    public static void setTurned(boolean turned) {
        Whitelist.turned = turned;
        wlConfig.config.set("turned", turned);
        wlConfig.save();
    }

    @Override
    public void onEnable() {
        instance = this;

        json = new WLJsonData();
        json.init();

        api = new WhitelistAPI_impl(json);

        wlConfig = new WLConfig();
        wlConfig.init();

        turned = Whitelist.wlConfig.config.getBoolean("turned", true);

        Objects.requireNonNull(getCommand("whitelist")).setExecutor(new WhitelistCommand());
        Objects.requireNonNull(getCommand("whitelist")).setTabCompleter(new WhiteListTabCompleter());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinListener(), this);
    }

    @Override
    public void onDisable() {
        json.save();
        wlConfig.save();
    }
}
