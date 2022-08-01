package com.zpedroo.scoreboard.utils.scoreboard;

import com.zpedroo.scoreboard.enums.Types;
import com.zpedroo.scoreboard.managers.DataManager;
import com.zpedroo.scoreboard.objects.HoldItem;
import com.zpedroo.scoreboard.objects.ScoreboardInfo;
import com.zpedroo.scoreboard.tasks.ScoreboardUpdateTask;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScoreboardUtils {

    public static void registerPlayerScoreboard(Player player) {
        ScoreboardUpdateTask task = new ScoreboardUpdateTask(player);
        task.start();
    }

    @Nullable
    public static ScoreboardInfo getPlayerScoreboardInfo(Player player) {
        List<ScoreboardInfo> scoreboardsInfo = DataManager.getInstance().getScoreboardsInfo();
        for (ScoreboardInfo scoreboardInfo : scoreboardsInfo) {
            switch (scoreboardInfo.getType()) {
                case WORLD:
                    if (scoreboardInfo.getWorldsName().contains(player.getWorld().getName())) return scoreboardInfo;
                case REGION:
                    break;
                case HOLD_ITEM:
                    ItemStack item = player.getItemInHand();
                    if (item == null || item.getType().equals(Material.AIR)) break;

                    for (HoldItem holdItem : scoreboardInfo.getHoldItems()) {
                        if (holdItem.isCompatible(item)) return scoreboardInfo;
                    }
                    break;
            }
        }

        return scoreboardsInfo.stream().filter(scoreboardInfo -> scoreboardInfo.getType() == Types.DEFAULT).findFirst().orElse(null);
    }
}