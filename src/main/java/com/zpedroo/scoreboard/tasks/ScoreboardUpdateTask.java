package com.zpedroo.scoreboard.tasks;

import com.zpedroo.scoreboard.VoltzScoreboard;
import com.zpedroo.scoreboard.objects.ScoreboardInfo;
import com.zpedroo.scoreboard.utils.placeholderapi.PlaceholderUtils;
import com.zpedroo.scoreboard.utils.scoreboard.ScoreboardLine;
import com.zpedroo.scoreboard.utils.scoreboard.ChatColorService;
import com.zpedroo.scoreboard.utils.scoreboard.ScoreboardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.zpedroo.scoreboard.utils.config.Settings.SCOREBOARD_UPDATE_TIME;

public class ScoreboardUpdateTask extends BukkitRunnable {

    private final Plugin plugin = VoltzScoreboard.get();
    private final Player player;
    private Scoreboard scoreboard;
    private ScoreboardInfo scoreboardInfo;

    public ScoreboardUpdateTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }

        if (scoreboard == null) {
            createScoreboard();
            player.setScoreboard(scoreboard);
        }

        Objective objective = getObjective();
        if (objective == null && hasValidScoreboard()) return; // other scoreboard, waiting

        updateScoreboard();
    }

    private void updateScoreboard() {
        updateTitle();
        updateLines();
    }

    private void updateTitle() {
        if (scoreboard == null) return;

        Objective objective = getObjective();
        if (objective == null || objective.getDisplayName().equals(getScoreboardInfo().getTitle())) return;

        objective.setDisplayName(getScoreboardInfo().getTitle());
    }

    private void updateLines() {
        if (scoreboard == null) return;

        Objective objective = getObjective();
        if (objective == null) return;

        ScoreboardInfo newScoreboardInfo = getScoreboardInfo();
        if (newScoreboardInfo != scoreboardInfo) {
            scoreboardInfo = newScoreboardInfo;
            scoreboard.getEntries().forEach(entry -> scoreboard.resetScores(entry));
        }

        List<String> lines = scoreboardInfo.getLines();
        for (int score = 0; score < lines.size(); ++score) {
            String line = PlaceholderUtils.replace(player, lines.get(score));
            String teamName = ChatColorService.get(score) + "" + ChatColor.RESET;

            Team team = scoreboard.getTeam(teamName);
            if (team == null) team = scoreboard.registerNewTeam(teamName);
            if (!team.hasEntry(teamName)) team.addEntry(teamName);

            ScoreboardLine scoreboardLine = new ScoreboardLine(line, score);
            if (!team.getPrefix().equals(scoreboardLine.getPrefix())) team.setPrefix(scoreboardLine.getPrefix());
            if (!team.getSuffix().equals(scoreboardLine.getSuffix())) team.setSuffix(scoreboardLine.getSuffix());

            if (!objective.getScore(teamName).isScoreSet() || objective.getScore(teamName).getScore() != score) {
                objective.getScore(teamName).setScore(score);
            }
        }
    }

    private void createScoreboard() {
        Future<Scoreboard> scoreboardFuture = Bukkit.getScheduler().callSyncMethod(plugin, () -> Bukkit.getScoreboardManager().getNewScoreboard());
        try {
            scoreboard = scoreboardFuture.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            throw new RuntimeException("Failed to update scoreboard", ex);
        }

        Objective objective = scoreboard.registerNewObjective("VoltzScoreboard", "dummy");
        objective.setDisplayName(getScoreboardInfo().getTitle());
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    
    private ScoreboardInfo getScoreboardInfo() {
        return ScoreboardUtils.getPlayerScoreboardInfo(player);
    }

    private Objective getObjective() {
        return scoreboard.getObjective("VoltzScoreboard");
    }

    private boolean hasValidScoreboard() {
        return player.getScoreboard() != null && player.getScoreboard().getEntries().size() > 0;
    }

    public void start() {
        this.runTaskTimerAsynchronously(plugin, 0L, SCOREBOARD_UPDATE_TIME);
    }
}