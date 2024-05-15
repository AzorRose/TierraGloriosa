package otnose.tierra_gloriosa;

import java.sql.SQLException;


public class FactionManager {
    private final String faction_1;
    private final String faction_2;
    private final DBconnector con;
    private final PermissionManager permissionManager;

    public FactionManager(DBconnector dbcon, PermissionManager permissionManager)
    {
        this.faction_1 = "faction_1";
        this.faction_2 = "faction_2";
        this.con = dbcon;
        this.permissionManager = permissionManager;
    }

    public void setPlayerFaction(String factionName, String playerName)
    {
        try
        {
            if (faction_1.equalsIgnoreCase(factionName)) {

                if (permissionManager.checkPermission(playerName, "tg."+faction_2))
                {
                    permissionManager.removePlayerPermission(playerName, "tg."+faction_2);
                }
                if (permissionManager.checkPermission(playerName, "tg."+faction_1)) return;

                permissionManager.givePlayerPermission(playerName,"tg." + factionName);
                con.setPlayerFaction(playerName, factionName);
            }
            else if (faction_2.equalsIgnoreCase(factionName)) {
                if (permissionManager.checkPermission(playerName, "tg."+faction_1))
                {
                    permissionManager.removePlayerPermission(playerName, "tg."+faction_1);
                }
                if (permissionManager.checkPermission(playerName, "tg."+faction_2)) return;

                permissionManager.givePlayerPermission(playerName,"tg." + factionName);
                con.setPlayerFaction(playerName, factionName);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
