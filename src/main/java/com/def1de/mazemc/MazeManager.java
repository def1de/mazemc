package com.def1de.mazemc;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class MazeManager {
    private static int width = 128;
    private static int height = 128;
    private static int baseY = 100;
    Random random = new Random();

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

    private List<Player> playersInMaze = new ArrayList<>();
    private BukkitTask freezeTask;

    private List<LootEntry> innerLootTable = LootTables.getInnerLootTable();
    private List<LootEntry> outerLootTable = LootTables.getOuterLootTable();

    private World world;
    private Player worldOwner;

    private boolean[][] mazeLayout;
    private MazeMapGenerator mapGenerator;

    private List<Watcher> watchers = new ArrayList<>();

    public MazeManager(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can create a maze world.");
            return;
        }
        this.worldOwner = (Player) sender;
        String worldName = "world_" + System.currentTimeMillis();
        WorldCreator wc = new WorldCreator(worldName);
        wc.type(WorldType.FLAT);
        wc.generatorSettings("{\"layers\":[{\"block\":\"air\",\"height\":1}],\"biome\":\"the_void\"}");
        world = wc.createWorld();

        if (world != null) {
            createMaze();
            worldOwner.sendMessage("§2Created a new maze world: §7" + worldName);
            
            // Teleport all the players to the new world spawn
                Location[] spawnLocations = {
                    new Location(world, -1.5, baseY + 1, -0.5),
                    new Location(world, 1.5, baseY + 1, 1.5),
                    new Location(world, -0.5, baseY + 1, 2.5),
                    new Location(world, 1.5, baseY + 1, 2.5),
                    new Location(world, 2.5, baseY + 1, 1.5),
                    new Location(world, 2.5, baseY + 1, -0.5),
                    new Location(world, 1.5, baseY + 1, -1.5),
                    new Location(world, -0.5, baseY + 1, -1.5)  
                };

                Location spawnLocation;

                int playerIndex = 0;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (playerIndex >= spawnLocations.length) {
                        worldOwner.sendMessage("§eNot enough predefined spawn locations for all players!");
                        break; // No more predefined spawn locations
                    }
                
                    spawnLocation = spawnLocations[playerIndex];
                    world.getChunkAt(spawnLocation).load();

                    player.teleport(spawnLocation);

                    // Freeze the player using speed restriction
                    player.setWalkSpeed(0f);
                    player.setFlySpeed(0f);
                    player.setAllowFlight(false);

                    // Set game mode to adventure
                    player.setGameMode(GameMode.ADVENTURE);
                    // Reset player state
                    player.getInventory().clear();
                    player.setHealth(player.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getDefaultValue());
                    player.setFoodLevel(20);
                    player.setSaturation(20f);
                    player.setExhaustion(0f);
                    player.setFireTicks(0);
                    player.setExp(0);
                    player.setLevel(0);
                    player.setTotalExperience(0);
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        player.removePotionEffect(effect.getType());
                    }

                    // Store players
                    playersInMaze.add(player);

                    player.sendMessage("§3You have been teleported to the new maze world!");
                    playerIndex++;
                }

                // Start a repeating task to keep players frozen
                freezeTask = Bukkit.getScheduler().runTaskTimer(Mazemc.getInstance(), () -> {

                    for (int i = 0; i < playersInMaze.size(); i++) {
                        Player player = playersInMaze.get(i);
                        Location frozenLocation = spawnLocations[i];

                        if (player.isOnline() && player.getLocation().distance(frozenLocation) > 0.1) {
                            player.teleport(frozenLocation);
                        }
                    }
                }, 0L, 5L);

                // Send summary message to command sender
                worldOwner.sendMessage("§aTeleported " + playersInMaze.size() + " player(s) to the new maze world.");
                startGame();
        } else {
            worldOwner.sendMessage("§cFailed to create world.");
        }
    }

    public void startGame() {
        // Unfreeze players
        if (freezeTask != null) {
            freezeTask.cancel();
        }
        if (mapGenerator == null) {
            worldOwner.sendMessage("§cMaze map generator is not initialized.");
            return;
        }
        for (Player player : playersInMaze) {
            if (player.isOnline()) {
                player.setWalkSpeed(0.2f);
                player.setFlySpeed(0.1f);
                player.setAllowFlight(false);
                player.sendMessage("§aThe maze has started! Good luck!");
                // Play start sound
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.UI, 1.0f, 1.2f);
                mapGenerator.giveMazeMapToPlayer(player);
            }
        }

        watchers = Watcher.createWatchers(world, new ArrayList<>(playersInMaze), mazeLayout);
        worldOwner.sendMessage("§6" + watchers.size() + " Guardian watchers have been deployed!");
    }

    private void createMaze() {
        // Ensure odd numbers
        width = (width % 2 == 0) ? width + 1 : width;
        height = (height % 2 == 0) ? height + 1 : height;

        boolean[][] maze = new boolean[width][height]; // true = path
        this.mazeLayout = maze;
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
                if (nx > 0 && ny > 0 && nx < width - 1 && ny < height - 1 && !maze[nx][ny]) {
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
        int extraLoops = (width * height) / 20; // ~5%
        for (int i = 0; i < extraLoops; i++) {
            int x = 2 + random.nextInt(width - 3);
            int z = 2 + random.nextInt(height - 3);
            if (!maze[x][z]) {
                maze[x][z] = true;
            }
        }

        generateMazeInWorld(maze);
        generateChestsInWorld(maze);
        createSpawnArea();

        this.mapGenerator = new MazeMapGenerator(world, mazeLayout, width, height);;
    }

    private void generateMazeInWorld(boolean[][] maze) {
        // Build maze in world (centered at 0,0)
        int baseY = 100;
        int offsetX = -width;
        int offsetZ = -height;

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
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
    }

    private void generateChestsInWorld(boolean[][] maze) {
        double chestChance = 0.05; // 5% chance to spawn a chest at a corner
        for (int x = 1; x < width - 1; x++) {
            for (int z = 1; z < height - 1; z++) {
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
                    int wx = -width + x * 2;
                    int wz = -height + z * 2;
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

    private void createSpawnArea() {
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
}
