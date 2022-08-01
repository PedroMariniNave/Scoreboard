package com.zpedroo.scoreboard.objects;

import com.zpedroo.scoreboard.enums.Types;
import lombok.Data;

import java.util.List;

@Data
public class ScoreboardInfo {

    private final String title;
    private final List<String> lines;
    private final Types type;
    private final List<String> worldsName;
    private final List<HoldItem> holdItems;
}