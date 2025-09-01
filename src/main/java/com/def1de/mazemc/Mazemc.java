package com.def1de.mazemc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Mazemc extends JavaPlugin {
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("MazeMC enabled");
        commandHandler = new CommandHandler();
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
                return commandHandler.handleMazeNew(sender, command, label, args);
            }
            sender.sendMessage(ChatColor.YELLOW + "Usage: /maze new");
            return true;
        }
        return false;
    }
}
