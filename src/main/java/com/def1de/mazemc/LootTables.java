package com.def1de.mazemc;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.*;

public class LootTables {
     private static final List<LootEntry> innerLootTable = Arrays.asList(
            new LootEntry(new ItemStack(Material.LEATHER_HELMET, 1), 0.2).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.LEATHER_CHESTPLATE, 1), 0.2).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.LEATHER_LEGGINGS, 1), 0.2).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.LEATHER_BOOTS, 1), 0.2).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.CHAINMAIL_BOOTS, 1), 0.08).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), 0.08).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), 0.08).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.CHAINMAIL_BOOTS, 1), 0.08).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.WOODEN_SWORD, 1), 0.35),
            new LootEntry(new ItemStack(Material.POTATO, 2), 0.5),
            new LootEntry(new ItemStack(Material.BREAD, 3), 0.5),
            new LootEntry(new ItemStack(Material.CARROT, 3), 0.5),
            new LootEntry(new ItemStack(Material.SALMON, 1), 0.5)
    );

    private static final List<LootEntry> outerLootTable = Arrays.asList(
            new LootEntry(new ItemStack(Material.IRON_HELMET, 1), 0.2).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.IRON_CHESTPLATE, 1), 0.2).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.IRON_LEGGINGS, 1), 0.2).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.IRON_BOOTS, 1), 0.2).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 2),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 1),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.DIAMOND_BOOTS, 1), 0.05).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 3),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), 0.05).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 3),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.DIAMOND_LEGGINGS, 1), 0.05).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 3),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.DIAMOND_BOOTS, 1), 0.05).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.BLAST_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.FIRE_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.PROJECTILE_PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.PROTECTION, 1, 4),
                    new EnchantmentWithLevel(Enchantment.THORNS, 1, 3),
                    new EnchantmentWithLevel(Enchantment.BINDING_CURSE , 1, 1)
            ),
            new LootEntry(new ItemStack(Material.IRON_SWORD, 1), 0.15).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.SHARPNESS, 2, 5),
                    new EnchantmentWithLevel(Enchantment.SMITE, 2, 5),
                    new EnchantmentWithLevel(Enchantment.FIRE_ASPECT, 1, 2),
                    new EnchantmentWithLevel(Enchantment.KNOCKBACK, 1, 2)
            ),
            new LootEntry(new ItemStack(Material.DIAMOND_SWORD, 1), 0.05).addRandomEnchant(
                    new EnchantmentWithLevel(Enchantment.SHARPNESS, 2, 5),
                    new EnchantmentWithLevel(Enchantment.SMITE, 2, 5),
                    new EnchantmentWithLevel(Enchantment.FIRE_ASPECT, 1, 2),
                    new EnchantmentWithLevel(Enchantment.KNOCKBACK, 1, 2)
            ),
            new LootEntry(new ItemStack(Material.GOLDEN_APPLE, 1), 0.07),
            new LootEntry(new ItemStack(Material.BREAD, 15), 0.35),
            new LootEntry(new ItemStack(Material.GOLDEN_CARROT, 8), 0.12),
            new LootEntry(createPotion(PotionType.INVISIBILITY), 0.5),
            new LootEntry(createSplashPotion(PotionType.INVISIBILITY), 0.5),
            new LootEntry(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1), 0.001)
    );

    public static List<LootEntry> getInnerLootTable() {
         return innerLootTable;
     }

     public static List<LootEntry> getOuterLootTable() {
         return outerLootTable;
     }

    /**
     * Helper method to create a potion with specific type
     */
    private static ItemStack createPotion(PotionType potionType) {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(potionType);
        potion.setItemMeta(meta);
        return potion;
    }

    /**
     * Helper method to create a splash potion with specific type
     */
        private static ItemStack createSplashPotion(PotionType potionType) {
            ItemStack potion = new ItemStack(Material.SPLASH_POTION, 1);
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            meta.setBasePotionType(potionType);
            potion.setItemMeta(meta);
            return potion;
        }
    }
