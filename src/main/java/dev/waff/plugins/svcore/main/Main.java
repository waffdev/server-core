package dev.waff.plugins.svcore.main;

import dev.waff.plugins.svcore.commands.CommandGamemode;
import dev.waff.plugins.svcore.plugin.PluginInfo;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/*
 * author: waffdev
 * purpose: plugin main class
 * created: 9/3/22
 */

public class Main extends JavaPlugin {

    // Config Setup
    private FileConfiguration config;
    private File configFile;

    @Override
    public void onEnable() {
        createConfig();
        getServer().getPluginCommand("gamemode").setExecutor(new CommandGamemode());
        getLogger().info(getName() + " v" + PluginInfo.VERSION + " has now been enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info(getName() + " v" + PluginInfo.VERSION + " has now been disabled");
    }

    private void createConfig() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }


}
