package org.molfordan.simpleSurvival.Manager;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class messageManager {
    private final HashMap<Player, Player> lastMessaged = new HashMap<>();

    public void setLastMessaged(Player player, Player target) {
        lastMessaged.put(target, player);
    }

    public Player getLastMessagedPlayer(Player player) {
        return lastMessaged.get(player);
    }
}
