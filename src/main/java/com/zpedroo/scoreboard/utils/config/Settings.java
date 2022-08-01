package com.zpedroo.scoreboard.utils.config;

import com.zpedroo.scoreboard.utils.FileUtils;

public class Settings {

    public static final int SCOREBOARD_UPDATE_TIME = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.scoreboard-update-time");
}