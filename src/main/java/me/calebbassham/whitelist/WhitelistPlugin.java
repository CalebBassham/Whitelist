package me.calebbassham.whitelist;

import org.bukkit.plugin.java.JavaPlugin;

public class WhitelistPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        WhitelistCmd.register();
    }
}
