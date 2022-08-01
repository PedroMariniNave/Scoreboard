package com.zpedroo.scoreboard;

import com.zpedroo.scoreboard.listeners.PlayerGeneralListeners;
import com.zpedroo.scoreboard.managers.DataManager;
import com.zpedroo.scoreboard.utils.FileUtils;
import com.zpedroo.scoreboard.utils.scoreboard.ScoreboardUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class VoltzScoreboard extends JavaPlugin {

    private static VoltzScoreboard instance;
    public static VoltzScoreboard get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);
        new DataManager();

        registerListeners();
        registerOnlinePlayersScoreboard();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }

    private void registerOnlinePlayersScoreboard() {
        Bukkit.getOnlinePlayers().forEach(ScoreboardUtils::registerPlayerScoreboard);
    }
}