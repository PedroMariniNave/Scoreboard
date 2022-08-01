package com.zpedroo.scoreboard.objects;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

@Data
public class HoldItem {

    private final String type;
    private final String name;
    private final List<String> lore;
    private final List<String> nbtList;
    private final List<Enchantment> enchantments;

    public boolean isCompatible(ItemStack item) {
        if (!checkType(item.getType().toString())) return false;
        ItemMeta meta = item.getItemMeta();

        if (!checkName(meta.getDisplayName())) return false;
        if (!checkLore(meta.getLore())) return false;
        if (!checkNBTs(item)) return false;
        if (!checkEnchantments(meta.getEnchants().keySet())) return false;

        return true;
    }

    private boolean checkType(@NotNull String typeToCheck) {
        if (type == null) return true;

        return typeToCheck.contains(type);
    }

    private boolean checkName(@Nullable String nameToCheck) {
        if (name == null) return true;
        if (nameToCheck == null) return false;

        return StringUtils.equals(name, nameToCheck);
    }

    private boolean checkLore(@Nullable List<String> loreToCheck) {
        if (lore.isEmpty()) return true;
        if (loreToCheck == null) return false;

        return lore.containsAll(loreToCheck);
    }

    private boolean checkNBTs(@NotNull ItemStack item) {
        if (nbtList.isEmpty()) return true;

        NBTItem nbt = new NBTItem(item);
        for (String nbtName : nbtList) {
            if (!nbt.hasKey(nbtName)) return false;
        }

        return true;
    }

    private boolean checkEnchantments(@NotNull Set<Enchantment> enchantmentsToCheck) {
        if (enchantments.isEmpty()) return true;
        if (enchantmentsToCheck.isEmpty()) return false;

        return enchantments.containsAll(enchantmentsToCheck);
    }
}