package com.zpedroo.scoreboard.managers;

import com.zpedroo.scoreboard.managers.cache.DataCache;
import com.zpedroo.scoreboard.objects.ScoreboardInfo;

import java.util.List;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private final DataCache dataCache = new DataCache();

    public DataManager() {
        instance = this;
    }

    public List<ScoreboardInfo> getScoreboardsInfo() {
        return dataCache.getScoreboardsInfo();
    }

    public DataCache getCache() {
        return dataCache;
    }
}