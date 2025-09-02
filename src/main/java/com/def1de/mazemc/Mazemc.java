package com.def1de.mazemc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Mazemc extends JavaPlugin {
    private static Mazemc instance;
    private MazeManager mazeManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("MazeMC enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        File root = Bukkit.getWorldContainer();
        File[] files = root.listFiles((dir, name) -> name.matches("world_\\d+"));
        if (files != null) {
            for (File file : files) {
                try {
                    deleteDirectory(file);
                    getLogger().info("Deleted maze world: " + file.getName());
                } catch (Exception e) {
                    getLogger().warning("Failed to delete maze world: " + file.getName() + ", reason: " + e.getMessage());
                }
            }
        }
    }

    public static Mazemc getInstance() {
        return instance;
    }

    private void deleteDirectory(File file) throws java.io.IOException {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectory(entry);
                }
            }
        }
        if (!file.delete()) {
            throw new java.io.IOException("Failed to delete " + file);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("maze")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("new")) {
                mazeManager = new MazeManager(sender);
                return true;
            } else if(args.length == 1 && args[0].equalsIgnoreCase("start")) {
                mazeManager.startGame();
                return true;
            } else if (command.getName().equalsIgnoreCase("hub")) {
                if (sender instanceof org.bukkit.entity.Player) {
                    org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;

                    // Get the default world (usually "world")
                    org.bukkit.World defaultWorld = Bukkit.getWorld("world");
                    if (defaultWorld == null) {
                        // Fallback to the first world if "world" doesn't exist
                        defaultWorld = Bukkit.getWorlds().get(0);
                    }

                    // Teleport to spawn location of default world
                    org.bukkit.Location spawnLocation = defaultWorld.getSpawnLocation();
                    player.teleport(spawnLocation);
                    player.sendMessage("§aWelcome back to the hub!");

                    // Reset player state
                    player.setGameMode(org.bukkit.GameMode.ADVENTURE);
                    player.setWalkSpeed(0.2f);
                    player.setFlySpeed(0.1f);
                    player.setAllowFlight(false);

                } else {
                    sender.sendMessage("§cOnly players can use this command!");
                }
                return true;
            }
        }
        return false;
    }
}
