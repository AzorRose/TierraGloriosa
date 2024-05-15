package otnose.tierra_gloriosa;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static ConfigManager instance;
    private final Tierra_gloriosa plugin;
    private final FileConfiguration config;

    public ConfigManager(Tierra_gloriosa plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public static ConfigManager getInstance(Tierra_gloriosa plugin) {
        if (instance == null) {
            instance = new ConfigManager(plugin);
        }
        return instance;
    }

    public String getDbDriver() {
        return config.getString("driver");
    }

    public String getDbUrl() {
        return config.getString("url");
    }

    public String getDbUsername() {
        return config.getString("username");
    }

    public String getDbPassword() {
        return config.getString("password");
    }

    public int getPoolSize() {return config.getInt("pool_size");}

    public String getFaction1Name() {
        return config.getString("faction1_name", "name1");
    }

    public String getFaction2Name() {
        return config.getString("faction2_name", "name2");
    }
}
