package com.zpedroo.scoreboard.utils.scoreboard;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class ChatColorService {

    private static final List<ChatColor> COLORS = Arrays.asList(ChatColor.values());

    public static String get(int index) {
        String color = "";
        if (index > 15) {
            if (index < 32) {
                color += ChatColor.BOLD;
            } else if (index < 47) {
                color += ChatColor.ITALIC;
            } else if (index < 63) {
                color += ChatColor.STRIKETHROUGH;
            } else if (index < 79) {
                color += ChatColor.UNDERLINE;
            } else if (index < 95) {
                color += ChatColor.MAGIC;
            }
        }

        color += COLORS.get(index);
        return color;
    }
}