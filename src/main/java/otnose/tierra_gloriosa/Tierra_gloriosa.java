package otnose.tierra_gloriosa;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public final class Tierra_gloriosa extends JavaPlugin implements Listener{

    private ConfigManager configManager;
    private DBconnector dbcon;
    private FactionManager factionManager;
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Enabling TG...");

        configManager = ConfigManager.getInstance(this);

        try {
            getLogger().info("Elo System trying to connect db");
            dbcon = new DBconnector(configManager, configManager.getPoolSize());
            dbcon.createFactionTable();
            getLogger().info("Success");
        } catch (SQLException e) {
            getLogger().info("Error while trying to connect db: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception ex) {
            getLogger().info("Unexpected error: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        permissionManager = new PermissionManager();

        factionManager = new FactionManager(dbcon, permissionManager);

        Placeholders placeholderExpansion = new Placeholders(this, dbcon);

        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        // Регистрация команды tg set
        this.getCommand("tg").setExecutor(new commandHandler(this, factionManager));

        // Регистрация плейсхолдер менеджера
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholderExpansion.register();
        }

        getLogger().info("TG has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            dbcon.closeAllConnections();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getLogger().info("TG has been disabled!");
    }

    public String getFaction(String playerName) throws SQLException {
        return dbcon.getPlayerFaction(playerName);
    }
}
