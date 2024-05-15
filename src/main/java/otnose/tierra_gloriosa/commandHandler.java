package otnose.tierra_gloriosa;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
public class commandHandler implements CommandExecutor{
    private final Plugin plugin;
    private final FactionManager factionManager;
    public commandHandler(Plugin plugin, FactionManager factionManager)
    {
        this.plugin = plugin;
        this.factionManager = factionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // reload command
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadPlugin(sender);
            return true;
        }
        // tg factions set procyon player
        if (args.length > 1 && args[0].equalsIgnoreCase("factions"))
        {
            if (args.length == 4 && args[1].equalsIgnoreCase("set"))
            {
                factionManager.setPlayerFaction(args[2], args[3]);
            }
        }
        return false;
    }

    private void reloadPlugin(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Reloading TG plugin...");

        try {
            plugin.onDisable();
            plugin.reloadConfig();
            plugin.onEnable();
            sender.sendMessage(ChatColor.GREEN + "TG plugin reloaded successfully.");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Failed to reload TG plugin. Check console for errors.");
            plugin.getLogger().log(Level.SEVERE, "Error reloading TG plugin", e);
        }
    }
}
