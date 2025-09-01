package com.def1de.mazemc;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentWithLevel {
    public final Enchantment enchantment;
    public final int minLevel;
    public final int maxLevel;

    public EnchantmentWithLevel(Enchantment enchantment, int minLevel, int maxLevel) {
        this.enchantment = enchantment;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }
}