package com.def1de.mazemc;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {
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

        // Start area (circle radius 5 at 0,0)
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
    }







}
