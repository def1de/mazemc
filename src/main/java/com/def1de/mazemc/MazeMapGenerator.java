package com.def1de.mazemc;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MazeMapGenerator {
    private final World world;
    private final boolean[][] mazeLayout;
    private final int width;
    private final int height;
    private final int offsetX;
    private final int offsetZ;
    
    public MazeMapGenerator(World world, boolean[][] mazeLayout, int width, int height) {
        this.world = world;
        this.mazeLayout = mazeLayout;
        this.width = width;
        this.height = height;
        this.offsetX = -width; // Same offset as in MazeManager
        this.offsetZ = -height;
    }
    
    public void giveMazeMapToPlayer(Player player) {
        // Create a new map
        MapView mapView = Bukkit.createMap(world);
        mapView.getRenderers().clear(); // Remove default renderer
        mapView.addRenderer(new MazeMapRenderer()); // Add static maze renderer
        mapView.addRenderer(new PlayerTrackerRenderer(player)); // Add player tracker
        
        // Create map item
        ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        if (mapMeta != null) {
            mapMeta.setMapView(mapView);
            mapMeta.setDisplayName("§6Maze Map");
            mapItem.setItemMeta(mapMeta);
        }
        
        // Give map to the player
        if (player.isOnline()) {
            player.getInventory().addItem(mapItem.clone());
            player.sendMessage("§aYou received a maze map!");
        }
    }
    
    // Custom map renderer class for static maze layout
    private class MazeMapRenderer extends MapRenderer {
        private boolean rendered = false;
        
        @Override
        public void render(MapView map, MapCanvas canvas, Player player) {
            if (rendered) return;
            rendered = true;
            
            // Clear the canvas to light gray background
            for (int x = 0; x < 128; x++) {
                for (int z = 0; z < 128; z++) {
                    canvas.setPixelColor(x, z, Color.LIGHT_GRAY);
                }
            }
            
            // Draw the maze - each maze cell (2x2 world blocks) maps to 1 pixel
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < height; z++) {
                    // Calculate the center world coordinates of this maze cell
                    int worldX = offsetX + x * 2;
                    int worldZ = offsetZ + z * 2;
                    
                    // Convert to map pixel coordinates (each 2x2 world area = 1 pixel)
                    int mapX = x; // Direct mapping since maze is 128x128 and map is 128x128
                    int mapZ = z;
                    
                    if (mapX >= 0 && mapX < 128 && mapZ >= 0 && mapZ < 128) {
                        // Check distance for ring gap
                        double dist = Math.sqrt(worldX * worldX + worldZ * worldZ);
                        
                        if (dist >= 50 && dist <= 60) {
                            // Ring gap - white color (passable)
                            canvas.setPixelColor(mapX, mapZ, Color.WHITE);
                        } else if (mazeLayout[x][z]) {
                            // Path - white color
                            canvas.setPixelColor(mapX, mapZ, Color.WHITE);
                        } else {
                            // Wall - black color
                            canvas.setPixelColor(mapX, mapZ, Color.BLACK);
                        }
                    }
                }
            }
            
            // Mark spawn area (center of map, which represents world 0,0)
            int centerMapX = (-offsetX) / 2; // Convert world 0 to map coordinates
            int centerMapZ = (-offsetZ) / 2;
            for (int dx = -3; dx <= 3; dx++) {
                for (int dz = -3; dz <= 3; dz++) {
                    if (dx*dx + dz*dz <= 9) {
                        int x = centerMapX + dx;
                        int z = centerMapZ + dz;
                        if (x >= 0 && x < 128 && z >= 0 && z < 128) {
                            canvas.setPixelColor(x, z, Color.GREEN);
                        }
                    }
                }
            }
        }
    }
    
    // Simple player tracker renderer
    private class PlayerTrackerRenderer extends MapRenderer {
        private final Player trackedPlayer;

        double worldX = -1;
        double worldZ = -1;

        int mapX = -1;
        int mapZ = -1;
        
        public PlayerTrackerRenderer(Player player) {
            this.trackedPlayer = player;
        }
        
        @Override
        public void render(MapView map, MapCanvas canvas, Player player) {
            // Clear previous player position by redrawing the maze at those locations
            if (mapX >= 0 && mapX < 128 && mapZ >= 0 && mapZ < 128) {
                canvas.setPixelColor(mapX, mapZ, getOriginalColorAt(worldX, worldZ));
            }

            // Draw current player position
            if (trackedPlayer.isOnline() && trackedPlayer.getWorld().equals(world)) {
                Location loc = trackedPlayer.getLocation();
                
                // Convert world coordinates to map coordinates
                worldX = loc.getX();
                worldZ = loc.getZ();
                
                // Map each 2x2 world block area to 1 pixel
                // Use integer division to ensure players in same 2x2 area map to same pixel
                mapX = (int)((worldX - offsetX) / 2);
                mapZ = (int)((worldZ - offsetZ) / 2);
                
                // Draw player as a single dot, but only if it's within bounds
                if (mapX >= 0 && mapX < 128 && mapZ >= 0 && mapZ < 128) {
                    // Draw player dot
                    canvas.setPixelColor(mapX, mapZ, Color.YELLOW);
                }
            }
        }
        
        private Color getOriginalColorAt(double worldX, double worldZ) {
            // Convert world coordinates to maze coordinates
            int mazeX = (int)((worldX - offsetX) / 2);
            int mazeZ = (int)((worldZ - offsetZ) / 2);
            
            // Check if coordinates are within maze bounds
            if (mazeX >= 0 && mazeX < width && mazeZ >= 0 && mazeZ < height) {
                // Check distance for ring gap
                double dist = Math.sqrt(worldX * worldX + worldZ * worldZ);
                
                if (dist >= 50 && dist <= 60) {
                    // Ring gap - cyan color
                    return Color.WHITE;
                } else if (mazeLayout[mazeX][mazeZ]) {
                    // Path - white color
                    return Color.WHITE;
                } else {
                    // Wall - black color
                    return Color.BLACK;
                }
            }
            
            // Check if it's spawn area (center)
            int centerMapX = (-offsetX) / 2; // Convert world 0 to map coordinates
            int centerMapZ = (-offsetZ) / 2;
            int mapX = (int)((worldX - offsetX) / 2);
            int mapZ = (int)((worldZ - offsetZ) / 2);
            
            int dx = mapX - centerMapX;
            int dz = mapZ - centerMapZ;
            if (dx*dx + dz*dz <= 9) {
                return Color.GREEN;
            }
            
            // Default to light gray background
            return Color.LIGHT_GRAY;
        }
    }
}