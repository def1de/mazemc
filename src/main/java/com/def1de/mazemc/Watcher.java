package com.def1de.mazemc;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Guardian-based watcher system that acts as hallway traps in the maze.
 * Watchers are strategically placed at intersections and important corridors
 * to detect and alert hunters about runner positions.
 */
public class Watcher {
    private Guardian guardian;
    private Location position;
    private BoundingBox watchArea;
    private boolean isActive;
    private World world;
    
    private static final int WATCH_RADIUS = 8;
    private static final double DETECTION_HEIGHT = 3.0;
    
    public Watcher(World world, Location position, BoundingBox watchArea) {
        this.world = world;
        this.position = position;
        this.watchArea = watchArea;
        this.isActive = true;
        
        spawnGuardian();
        startWatching();
    }
    
    /**
     * Spawns a Guardian at the watcher position with appropriate configuration
     */
    private void spawnGuardian() {
        // Find the closest air block and position watcher to face it
        Location airBlock = findClosestAirBlock();
        if (airBlock == null) {
            return; // Skip if no air block found
        }
        
        // Calculate direction from wall to air block
        double dx = airBlock.getX() - position.getX();
        double dz = airBlock.getZ() - position.getZ();
        
        // Move guardian forward from the wall towards the air block (but not all the way)
        double moveDistance = 0.7; // Move 0.7 blocks forward from wall
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length > 0) {
            dx /= length;
            dz /= length;
        }
        
        Location spawnLocation = position.clone().add(dx * moveDistance, 0, dz * moveDistance);
        guardian = (Guardian) world.spawnEntity(spawnLocation, EntityType.GUARDIAN);
        
        // Configure Guardian as a static watcher - ADD MORE PERSISTENCE OPTIONS
        guardian.setAI(false);
        guardian.setInvulnerable(true);
        guardian.setSilent(true);
        guardian.setGravity(false);
        guardian.setGlowing(false);
        guardian.setPersistent(true);  // prevents natural despawning
        guardian.setRemoveWhenFarAway(false);  // prevents distance-based despawning
        guardian.setCustomName("WATCHER");
        guardian.setCustomNameVisible(false);
        
        // Make Guardian face the air block and pitch down
        Location guardianLoc = guardian.getLocation();
        
        // Calculate yaw to face the air block
        double angle = Math.atan2(dz, dx) * 180.0 / Math.PI;
        guardianLoc.setYaw((float) (angle - 90)); // Adjust for Minecraft's coordinate system
        guardianLoc.setPitch(30.0f); // Pitch down 30 degrees to observe hallway
        
        guardian.teleport(guardianLoc);
    }
    
    /**
     * Finds the closest air block to this watcher's position
     */
    private Location findClosestAirBlock() {
        Location closest = null;
        double closestDistance = Double.MAX_VALUE;
        
        // Check surrounding blocks for air
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (dx == 0 && dz == 0) continue; // Skip the watcher's own position
                
                Location checkLoc = position.clone().add(dx, 0, dz);
                if (checkLoc.getBlock().getType() == Material.AIR) {
                    double distance = position.distance(checkLoc);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closest = checkLoc;
                    }
                }
            }
        }
        
        return closest;
    }
    
    /**
     * Creates a strategic watcher deployment system for the maze
     */
    public static List<Watcher> createWatchers(World world, List<Player> players, boolean[][] mazeLayout) {
        List<Watcher> watchers = new ArrayList<>();
        List<Location> trapLocations = findHallwayTraps(world, mazeLayout);
        
        // Create watchers at strategic locations
        for (Location trapLocation : trapLocations) {
            BoundingBox watchArea = createWatchArea(trapLocation);
            Watcher watcher = new Watcher(world, trapLocation, watchArea);
            watchers.add(watcher);
        }
        
        return watchers;
    }
    
    /**
     * Finds optimal hallway locations for placing watcher traps
     */
    private static List<Location> findHallwayTraps(World world, boolean[][] mazeLayout) {
        List<Location> trapLocations = new ArrayList<>();
        int width = mazeLayout.length;
        int height = mazeLayout[0].length;
        
        // Scan for intersections and important corridors
        for (int x = 1; x < width - 1; x++) {
            for (int z = 1; z < height - 1; z++) {
                if (mazeLayout[x][z]) { // If this is a path
                    int adjacentPaths = countAdjacentPaths(mazeLayout, x, z);
                    
                    // Place watchers at intersections (3+ adjacent paths)
                    // or at strategic corridor positions
                    if (adjacentPaths >= 3 || isStrategicCorridor(mazeLayout, x, z)) {
                        // Find wall positions around this path cell
                        List<Location> wallPositions = findWallPositions(world, mazeLayout, x, z);
                        trapLocations.addAll(wallPositions);
                    }
                }
            }
        }
        
        // Limit the number of watchers to prevent overcrowding
        Collections.shuffle(trapLocations);
        List<Location> selectedTraps = new ArrayList<>();
        for (Location loc : trapLocations) {
            // Remove from the list if location is less than 65 blocks from center
            if (loc.distance(new Location(world, 0, 115, 0)) >= 65 && selectedTraps.size() < 250) {
                selectedTraps.add(loc);
            }
        }
        return selectedTraps;
    }
    
    /**
     * Finds wall positions around a path cell for embedding watchers
     */
    private static List<Location> findWallPositions(World world, boolean[][] mazeLayout, int pathX, int pathZ) {
        List<Location> wallPositions = new ArrayList<>();
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}}; // N, E, S, W
        
        for (int[] dir : directions) {
            int wallX = pathX + dir[0];
            int wallZ = pathZ + dir[1];
            
            // Check if this position is a wall (not a path)
            if (wallX >= 0 && wallX < mazeLayout.length && 
                wallZ >= 0 && wallZ < mazeLayout[0].length && 
                !mazeLayout[wallX][wallZ]) {
                
                // Convert maze coordinates to world coordinates
                double worldX = (wallX * 2) - 128 + 0.5;
                double worldZ = (wallZ * 2) - 128 + 0.5;
                
                Location wallLocation = new Location(world, worldX, 115, worldZ);
                wallPositions.add(wallLocation);
            }
        }
        
        return wallPositions;
    }
    
    /**
     * Counts adjacent path cells to identify intersections
     */
    private static int countAdjacentPaths(boolean[][] mazeLayout, int x, int z) {
        int count = 0;
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newZ = z + dir[1];
            
            if (newX >= 0 && newX < mazeLayout.length && 
                newZ >= 0 && newZ < mazeLayout[0].length && 
                mazeLayout[newX][newZ]) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Identifies strategic corridor positions for watcher placement
     */
    private static boolean isStrategicCorridor(boolean[][] mazeLayout, int x, int z) {
        // Check for long straight corridors or bottleneck positions
        int adjacentPaths = countAdjacentPaths(mazeLayout, x, z);
        
        // Strategic if it's a 2-path corridor with specific patterns
        if (adjacentPaths == 2) {
            // Check for T-junctions nearby or corridor bends
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    int checkX = x + dx;
                    int checkZ = z + dz;
                    
                    if (checkX >= 0 && checkX < mazeLayout.length && 
                        checkZ >= 0 && checkZ < mazeLayout[0].length && 
                        mazeLayout[checkX][checkZ]) {
                        
                        if (countAdjacentPaths(mazeLayout, checkX, checkZ) >= 3) {
                            return true; // Strategic if near an intersection
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Creates a watching area around the trap location
     */
    private static BoundingBox createWatchArea(Location center) {
        // Create watch area at the maze level (Y=100-120) not high in the air
        return BoundingBox.of(
            center.clone().add(-WATCH_RADIUS, -15, -WATCH_RADIUS), // Y=115-15=100
            center.clone().add(WATCH_RADIUS, 5, WATCH_RADIUS)      // Y=115+5=120
        );
    }
    
    /**
     * Starts the watching task for this watcher
     */
    private void startWatching() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isActive || guardian == null || guardian.isDead()) {
                    this.cancel();
                    return;
                }
                
                checkForPlayers();
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Mazemc"), 0L, 20L); // Check every second
    }
    
    /**
     * Checks for players in the watching area and alerts the hunter system
     */
    private void checkForPlayers() {        
        for (Player player : world.getPlayers()) {
            // Check for detection within watch area
            if (watchArea.contains(player.getLocation().toVector())) {
                // Calculate detection range based on player state
                double detectionRange = calculateDetectionRange(player);
                
                // Check line of sight - watcher must be able to see the player
                if (hasLineOfSight(guardian.getLocation(), player.getLocation(), detectionRange)) {
                    alertHunterSystem(player);
                }
            }
        }
    }
    
    /**
     * Calculates detection range based on player state
     */
    private double calculateDetectionRange(Player player) {
        double baseRange = WATCH_RADIUS;
        
        // Reduce range if player is crouching
        if (player.isSneaking()) {
            baseRange *= 0.5; // 50% detection range when crouching
        }
        
        // Further reduce range if player has invisibility effect
        if (player.hasPotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY)) {
            baseRange *= 0.25; // 25% detection range when invisible
        }
        
        return baseRange;
    }
    
    /**
     * Checks if there's a clear line of sight between two locations
     */
    private boolean hasLineOfSight(Location from, Location to, double range) {
        // Calculate only horizontal distance (ignore Y difference)
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        
        // If target is beyond detection range horizontally, can't see it
        if (horizontalDistance > range) {
            return false;
        }
        
        // Simple line of sight check using rayTrace (still uses 3D for wall checking)
        try {
            double fullDistance = from.distance(to);
            return from.getWorld().rayTraceBlocks(from, to.toVector().subtract(from.toVector()), 
                fullDistance) == null;
        } catch (Exception e) {
            // Fallback to horizontal distance check if rayTrace fails
            return horizontalDistance <= range;
        }
    }
    
    /**
     * Alerts the hunter system about a detected player
     */
    private void alertHunterSystem(Player detectedPlayer) {
        // For now, broadcast to all players - implement hunter-specific logic later
        String alertMessage = String.format(
            "§c§l[WATCHER ALERT]§r §7Player §e%s§7 detected at §f%d, %d§7!",
            detectedPlayer.getName(),
            (int) detectedPlayer.getLocation().getX(),
            (int) detectedPlayer.getLocation().getZ()
        );

        world.spawnParticle(Particle.ELDER_GUARDIAN, detectedPlayer.getLocation().add(0, 1, 0), 1);
        detectedPlayer.playSound(detectedPlayer.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.UI, 1.0f, 0.5f);
        // Spawn a title to the detected player
        detectedPlayer.sendTitle("§c§lWATCHER ALERT!", "§7You have been detected!", 0, 40, 20);

        guardian.setGlowing(true);
        isActive = false;
        Bukkit.getScheduler().runTaskLater(Mazemc.getInstance(), () -> {
            guardian.setGlowing(false);

            // destroy the watcher after alerting
            deactivate();
        }, 60L); // Glow for 3 seconds
        
        // Send alert to all players (hunters)
        for (Player hunter : world.getPlayers()) {
            hunter.sendMessage(alertMessage);
        }
    }


    /**
     * Deactivates the watcher and removes the guardian
     */
    public void deactivate() {
        isActive = false;
        if (guardian != null && !guardian.isDead()) {
            guardian.remove();
        }
    }
    
    /**
     * Cleanup method to remove all watchers
     */
    public static void removeAllWatchers(List<Watcher> watchers) {
        for (Watcher watcher : watchers) {
            watcher.deactivate();
        }
        watchers.clear();
    }
}
