package com.def1de.mazemc;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandHandler {
    private final List<LootEntry> innerLootTable = Arrays.asList(
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

    private final List<LootEntry> outerLootTable = Arrays.asList(
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
            new LootEntry(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1), 0.001)
    );

    public boolean handleMazeNew(CommandSender sender, Command command, String label, String[] args) {
        String worldName = "world_" + System.currentTimeMillis();
        WorldCreator wc = new WorldCreator(worldName);
        wc.type(WorldType.FLAT);
        wc.generatorSettings("{\"layers\":[{\"block\":\"air\",\"height\":1}],\"biome\":\"the_void\"}");
        World world = wc.createWorld();
        if (world != null) {
            createMaze(world, 150, 150);
            sender.sendMessage(ChatColor.GREEN + "Empty world '" + worldName + "' created with a stone block at (0, 100, 0)!");
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location loc = new Location(world, 0.5, 101, 0.5);
                world.getChunkAt(loc).load();
                player.teleport(loc);
                player.sendMessage(ChatColor.AQUA + "You have been teleported to the new world!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Failed to create world.");
        }
        return true;
    }

    private void createMaze(World world, int width, int height) {
        // Ensure odd numbers
        int mazeWidth = (width % 2 == 0) ? width + 1 : width;
        int mazeHeight = (height % 2 == 0) ? height + 1 : height;

        boolean[][] maze = new boolean[mazeWidth][mazeHeight]; // true = path
        java.util.Stack<int[]> stack = new java.util.Stack<>();
        java.util.Random random = new java.util.Random();

        int startX = 1, startY = 1;
        maze[startX][startY] = true;
        stack.push(new int[]{startX, startY});

        int[][] directions = {{2, 0}, {-2, 0}, {0, 2}, {0, -2}};

        // DFS recursive backtracker
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int cx = current[0], cy = current[1];

            java.util.List<int[]> neighbors = new java.util.ArrayList<>();
            for (int[] d : directions) {
                int nx = cx + d[0], ny = cy + d[1];
                if (nx > 0 && ny > 0 && nx < mazeWidth - 1 && ny < mazeHeight - 1 && !maze[nx][ny]) {
                    neighbors.add(new int[]{nx, ny, d[0], d[1]});
                }
            }

            if (!neighbors.isEmpty()) {
                int[] chosen = neighbors.get(random.nextInt(neighbors.size()));
                int nx = chosen[0], ny = chosen[1];
                int mx = cx + chosen[2] / 2, my = cy + chosen[3] / 2; // wall between
                maze[mx][my] = true;
                maze[nx][ny] = true;
                stack.push(new int[]{nx, ny});
            } else {
                stack.pop();
            }
        }

        // Add ~5% loops by carving extra walls
        int extraLoops = (mazeWidth * mazeHeight) / 20; // ~5%
        for (int i = 0; i < extraLoops; i++) {
            int x = 2 + random.nextInt(mazeWidth - 3);
            int z = 2 + random.nextInt(mazeHeight - 3);
            if (!maze[x][z]) {
                maze[x][z] = true;
            }
        }

        // Materials
        Material[] wallBlocks = {
                Material.STONE,
                Material.COBBLESTONE,
                Material.STONE_BRICKS,
                Material.MOSSY_COBBLESTONE,
                Material.MOSSY_STONE_BRICKS
        };

        Material[] groundBlocks = {
                Material.GRASS_BLOCK,
                Material.DIRT,
                Material.COARSE_DIRT,
                Material.PODZOL,
                Material.MOSS_BLOCK
        };

        // Build maze in world (centered at 0,0)
        int baseY = 100;
        int offsetX = -mazeWidth;
        int offsetZ = -mazeHeight;

        for (int x = 0; x < mazeWidth; x++) {
            for (int z = 0; z < mazeHeight; z++) {
                for (int dx = 0; dx < 2; dx++) {
                    for (int dz = 0; dz < 2; dz++) {
                        int wx = offsetX + x * 2 + dx;
                        int wz = offsetZ + z * 2 + dz;

                        // Distance from center determines stage
                        double dist = Math.sqrt(wx * wx + wz * wz);

                        if (dist >= 50 && dist <= 60) {
                            // Ring gap (just styled floor, no walls)
                            world.getBlockAt(wx, baseY, wz)
                                    .setType(groundBlocks[random.nextInt(groundBlocks.length)]);
                            for (int y = 1; y <= 35; y++) {
                                world.getBlockAt(wx, baseY + y, wz).setType(Material.AIR);
                            }
                            continue;
                        }

                        if (maze[x][z]) {
                            // Path floor
                            world.getBlockAt(wx, baseY, wz)
                                    .setType(groundBlocks[random.nextInt(groundBlocks.length)]);
                            int clearHeight = (dist < 50) ? 10 : 30;
                            for (int y = 1; y <= clearHeight; y++) {
                                world.getBlockAt(wx, baseY + y, wz).setType(Material.AIR);
                            }
                        } else {
                            // Walls
                            int wallHeight = (dist < 50) ? 10 : 30;
                            for (int y = 0; y <= wallHeight; y++) {
                                Block block = world.getBlockAt(wx, baseY + y, wz);
                                block.setType(wallBlocks[random.nextInt(wallBlocks.length)]);
                            }
                        }
                    }
                }
            }
        }

        double chestChance = 0.05; // 5% chance to spawn a chest at a corner
        for (int x = 1; x < mazeWidth - 1; x++) {
            for (int z = 1; z < mazeHeight - 1; z++) {
                if (!maze[x][z]) continue; // Only consider path cells

                // Check for corner: two adjacent path neighbors at a right angle
                boolean bottom = maze[x][z - 1];
                boolean top = maze[x][z + 1];
                boolean right = maze[x + 1][z];
                boolean left = maze[x - 1][z];

                boolean isCorner = (bottom && right && !top && !left) ||
                        (bottom && left && !top && !right) ||
                        (top && right && !bottom && !left) ||
                        (top && left && !bottom && !right);

                if (isCorner && random.nextDouble() < chestChance) {
                    // Place chest at the center of this path cell
                    int wx = offsetX + x * 2;
                    int wz = offsetZ + z * 2;
                    int chestY = baseY + 1; // Place chest above the ground

                    if (bottom && right) {
                        wz += 1;
                    } else if (bottom && left) {
                        wx += 1;
                        wz += 1;
                    } else if (top && right) {
                        // keep
                    } else if (top && left) {
                        wx += 1;
                    }

                    createChest(world, wx, chestY, wz);
                }
            }
        }

        // Start area (circle radius 8 at 0,0)
        int radius = 8;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= radius * radius) {
                    int wx = dx;
                    int wz = dz;
                    world.getBlockAt(wx, baseY, wz)
                            .setType(groundBlocks[random.nextInt(groundBlocks.length)]);
                    for (int y = 1; y <= 25; y++) {
                        world.getBlockAt(wx, baseY + y, wz).setType(Material.AIR);
                    }
                }
            }
        }

        // Crate a camp in the start area
        world.getBlockAt(0, baseY + 1, 0).setType(Material.CAMPFIRE);

        // Set wooden logs around the campfire as seating
        int[][] logPositions = {
                {-1, 3}, {0, 3}, {1, 3},      // North
                {3, -1}, {3, 0}, {3, 1},      // East
                {-3, -1}, {-3, 0}, {-3, 1},   // West
                {-1, -3}, {0, -3}, {1, -3}    // South
        };

        for (int[] pos : logPositions) {
            int x = pos[0];
            int z = pos[1];
            Block log = world.getBlockAt(x, baseY + 1, z);
            log.setType(Material.OAK_LOG);
            org.bukkit.block.data.Orientable orientable = (org.bukkit.block.data.Orientable) log.getBlockData();

            // Determine axis: logs on N/S face Z, logs on E/W face X
            if (x == 3 || x == -3) {
                orientable.setAxis(org.bukkit.Axis.Z); // East-West logs
            } else if (z == 3 || z == -3) {
                orientable.setAxis(org.bukkit.Axis.X); // North-South logs
            } else {
                // Diagonal logs: choose axis based on which is further from center
                orientable.setAxis(Math.abs(x) > Math.abs(z) ? org.bukkit.Axis.X : org.bukkit.Axis.Z);
            }
            log.setBlockData(orientable);
        }

    }


    private void createChest(World world, int x, int y, int z) {
        Block block = world.getBlockAt(x, y, z);
        double dist = Math.sqrt(x * x + z * z);
        if (dist >= 50 && dist <= 60) {
            return; // No chests in the ring gap
        }
        block.setType(Material.CHEST);
        // Fill the chest with loot here
        Bukkit.getScheduler().runTaskLater(Mazemc.getInstance(), () -> {
            if (block.getState() instanceof Chest) {
                Chest chest = (Chest) block.getState();
                Inventory inv = chest.getBlockInventory();

                List<LootEntry> lootTable = (dist < 50) ? innerLootTable : outerLootTable;

                Set<Integer> usedSlots = new HashSet<>();
                Random random = new Random();
                for (LootEntry entry : lootTable) {
                    if (random.nextDouble() < entry.spawnRate) {
                        ItemStack loot = entry.item.clone();
                        if (!entry.possibleEnchants.isEmpty()) {
                            EnchantmentWithLevel chosen = entry.possibleEnchants.get(random.nextInt(entry.possibleEnchants.size()));
                            int level = chosen.minLevel + random.nextInt(chosen.maxLevel - chosen.minLevel + 1);
                            loot.addUnsafeEnchantment(chosen.enchantment, level);
                        }
                        int slot;
                        do {
                            slot = random.nextInt(inv.getSize());
                        } while (usedSlots.contains(slot));
                        inv.setItem(slot, loot);
                        usedSlots.add(slot);
                    }
                }
            }
        }, 2L);
    }

}
