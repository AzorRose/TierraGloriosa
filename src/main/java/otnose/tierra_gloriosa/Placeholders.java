package otnose.tierra_gloriosa;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class Placeholders extends PlaceholderExpansion{

    private final Tierra_gloriosa plugin;
    private final DBconnector con;

    public Placeholders(Tierra_gloriosa plugin, DBconnector con) {
        this.plugin = plugin;
        this.con = con;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "tg";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("faction")) {
            String faction = null;
            try {
                faction = plugin.getFaction(player.getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return String.valueOf(faction);
        }
        return "---";
    }
}
