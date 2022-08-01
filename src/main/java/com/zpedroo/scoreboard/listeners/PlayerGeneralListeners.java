package com.zpedroo.scoreboard.listeners;

import com.zpedroo.scoreboard.utils.scoreboard.ScoreboardUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerGeneralListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ScoreboardUtils.registerPlayerScoreboard(event.getPlayer());
    }
}