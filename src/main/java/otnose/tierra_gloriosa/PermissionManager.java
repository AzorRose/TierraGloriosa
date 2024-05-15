package otnose.tierra_gloriosa;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PermissionManager {

    public boolean checkPermission(String playerName, String permission)
    {
        LuckPerms luckPerms = LuckPermsProvider.get();

        // Получение пользователя по имени
        Player player = Bukkit.getPlayer(playerName);
        User user = getUser(player, luckPerms);
        if (user == null) return false;

        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();

    }

    public void removePlayerPermission(String playerName, String permission)
    {
        LuckPerms luckPerms = LuckPermsProvider.get();

        // Получение пользователя по имени
        Player player = Bukkit.getPlayer(playerName);
        User user = getUser(player, luckPerms);
        if (user == null) return;

        user.data().remove(Node.builder(permission).build());

        luckPerms.getUserManager().saveUser(user);
    }


    public void givePlayerPermission(String playerName, String permission) {
        // Получение экземпляра LuckPerms
        LuckPerms luckPerms = LuckPermsProvider.get();
        Player player = Bukkit.getPlayer(playerName);
        User user = getUser(player, luckPerms);
        if (user == null) return;

            // Add the permission
        user.data().add(Node.builder(permission).build());

            // Now we need to save changes.
        luckPerms.getUserManager().saveUser(user);


    }

    public User getUser(Player player, LuckPerms luckPerms)
    {
        // Получение пользователя по имени

        if (player == null) {
            // Игрок не найден или не подключен
            return null;
        }

        // Получение User объекта для игрока
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        return user;
    }

}