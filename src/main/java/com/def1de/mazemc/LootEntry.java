// LootEntry.java
package com.def1de.mazemc;

import org.bukkit.inventory.ItemStack;
import java.util.*;

public class LootEntry {
    public final ItemStack item;
    public final double spawnRate;
    public final List<EnchantmentWithLevel> possibleEnchants;

    public LootEntry(ItemStack item, double spawnRate) {
        this.item = item;
        this.spawnRate = spawnRate;
        this.possibleEnchants = new ArrayList<>();
    }

    public LootEntry addRandomEnchant(EnchantmentWithLevel... enchants) {
        this.possibleEnchants.addAll(Arrays.asList(enchants));
        return this;
    }
}