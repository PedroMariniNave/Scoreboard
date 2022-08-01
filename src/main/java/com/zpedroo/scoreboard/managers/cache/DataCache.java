package com.zpedroo.scoreboard.managers.cache;

import com.zpedroo.scoreboard.VoltzScoreboard;
import com.zpedroo.scoreboard.enums.Types;
import com.zpedroo.scoreboard.objects.HoldItem;
import com.zpedroo.scoreboard.objects.ScoreboardInfo;
import com.zpedroo.scoreboard.utils.color.Colorize;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class DataCache {

    private final List<ScoreboardInfo> scoreboardsInfo = getScoreboardsInfoFromFolder();

    private List<ScoreboardInfo> getScoreboardsInfoFromFolder() {
        List<ScoreboardInfo> ret = new ArrayList<>();
        File folder = new File(VoltzScoreboard.get().getDataFolder(), "/scoreboards");
        File[] files = folder.listFiles((file, name) -> name.endsWith(".yml"));
        if (files == null) return ret;

        for (File fl : files) {
            FileConfiguration file = YamlConfiguration.loadConfiguration(fl);
            Types type = Types.valueOf(file.getString("Settings.type", Types.DEFAULT.name()));
            String title = Colorize.getColored(file.getString("Scoreboard.title"));
            List<String> lines = Colorize.getColored(file.getStringList("Scoreboard.lines"));
            List<String> worldsName = file.getStringList("Settings.worlds");
            List<HoldItem> holdItems = getHoldItemsFromFile(file);

            Collections.reverse(lines);
            ret.add(new ScoreboardInfo(title, lines, type, worldsName, holdItems));
        }

        return ret;
    }

    private List<HoldItem> getHoldItemsFromFile(FileConfiguration file) {
        List<HoldItem> ret = new ArrayList<>(4);
        String where = "Settings.items";
        if (!file.contains(where)) return ret;

        for (String str : file.getConfigurationSection(where).getKeys(false)) {
            String type = file.getString(where + "." + str + ".type", null);
            String name = Colorize.getColored(file.getString(where + "." + str + ".name", null));
            List<String> lore = Colorize.getColored(file.getStringList(where + "." + str + ".lore"));
            List<String> nbtList = file.getStringList(where + "." + str + ".nbts");
            List<Enchantment> enchantments = new ArrayList<>(2);
            for (String enchantmentName : file.getStringList(where + "." + str + ".enchants")) {
                enchantments.add(Enchantment.getByName(enchantmentName));
            }

            ret.add(new HoldItem(type, name, lore, nbtList, enchantments));
        }

        return ret;
    }
}