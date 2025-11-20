package Maps;

import Engine.GraphicsHandler;
import Flowers.FireFlower;
import Flowers.RareSunflowerwithFlowers;
import Flowers.BlueBorah;
import Flowers.Cosmo;
import Flowers.Daffodil;
import Flowers.Daisy2;
import Flowers.Poppy;
import Flowers.Daisy;
import Level.EnhancedMapTile;
import Level.Map;
import Level.MapEntityStatus;
import Level.MapTile;
import Level.NPC;
import Level.Trigger;
import Level.TileType;
import NPCs.BlueEmerald;
import NPCs.BrokenHut;
import NPCs.BrokenTree2;
import NPCs.RedEmerald;
import NPCs.SauronEye;
import NPCs.Volcano;
import Portals.GrassPortal;
import Scripts.SimpleTextScript;
import Scripts.TestMap.LostBallScript;
import Scripts.VolcanoMap.BlueEmeraldScript;
import Scripts.VolcanoMap.RedEmeraldScript;
import Scripts.VolcanoMap.VolcanoGrassPortalScript;
import Scripts.VolcanoMap.VolcanoToGrassScript;
import Tilesets.VolcanoTileset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class VolcanoMap extends Map {

    // Random flower spawn tuning
    // toned down so the map is not overcrowded
    private static final int RANDOM_FLOWER_COUNT = 60;
    private static final float RARE_SUNFLOWER_CHANCE = 0.15f; // 15%

    // Volcano map is 80x80. Avoid edges.
    private static final int MIN_TILE_X = 4;
    private static final int MAX_TILE_X = 75;
    private static final int MIN_TILE_Y = 4;
    private static final int MAX_TILE_Y = 75;

    public VolcanoMap() {
        super("volcano_map.txt", new VolcanoTileset());
        // Bee starts near the GrassPortal (which is at 75,75)
        this.playerStartPosition = getMapTile(74, 75).getLocation();
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        return new ArrayList<>();
    }

    // Only spawn flowers on PASSABLE tiles (not NOT_PASSABLE lava/walls)
    private boolean isGoodFlowerTile(int tileX, int tileY) {
        MapTile tile = getMapTile(tileX, tileY);
        return tile.getTileType() == TileType.PASSABLE;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        Random rng = new Random();

        // Fixed important NPCs / structures

        // Grass portal back to main map - moved to (75, 75) with E key interaction
        GrassPortal grassPortal = new GrassPortal(1, getMapTile(75, 75).getLocation());
        grassPortal.setInteractScript(new VolcanoToGrassScript());

        // UPDATED: GrassPortal moved from (92, 49) to (75, 40) - near player spawn
        // GrassPortal grassPortal = new GrassPortal(1, getMapTile(75, 40).getLocation());
        // grassPortal.setInteractScript(new VolcanoToGrassScript());
        npcs.add(grassPortal);


        RedEmerald redEmerald = new RedEmerald(0, getMapTile(7, 8).getLocation());
        redEmerald.setInteractScript(new RedEmeraldScript());
        npcs.add(redEmerald);

        BlueEmerald blueEmerald = new BlueEmerald(0, getMapTile(64, 5).getLocation());
        blueEmerald.setInteractScript(new BlueEmeraldScript());
        npcs.add(blueEmerald);

        // One FireFlower near the hub area
        FireFlower hubFireFlower = new FireFlower(4000, getMapTile(60, 42).getLocation());
        npcs.add(hubFireFlower);

        // Broken huts - multiple locations (shifted 2 left, 2 up)
        npcs.add(new BrokenHut(4001, getMapTile(45, 30).getLocation()));  // Original hut
        npcs.add(new BrokenHut(4002, getMapTile(67, 66).getLocation()));  // Between trees
        npcs.add(new BrokenHut(4003, getMapTile(15, 45).getLocation()));  
        npcs.add(new BrokenHut(4004, getMapTile(22, 47).getLocation()));
        npcs.add(new BrokenHut(4005, getMapTile(37, 61).getLocation()));  // Inside rectangle
        npcs.add(new BrokenHut(4006, getMapTile(42, 62).getLocation()));  // Inside rectangle
        npcs.add(new BrokenHut(4007, getMapTile(19, 35).getLocation()));  // New (shifted up)
        npcs.add(new BrokenHut(4008, getMapTile(44, 7).getLocation()));   // New
        npcs.add(new BrokenHut(4009, getMapTile(45, 18).getLocation()));  // New

        // Sauron's Eye near the volcano
        npcs.add(new SauronEye(9001, getMapTile(48, 47).getLocation()));

        // Volcano NPC - center of map
        npcs.add(new Volcano(3001, getMapTile(41, 39).getLocation()));

        // Broken trees - strategically placed (shifted 2 left, 2 up)
        // Vertical line: X=66, Y=23 to 39 (5 trees evenly spaced)
        npcs.add(new BrokenTree2(3061, getMapTile(66, 23).getLocation()));
        npcs.add(new BrokenTree2(3062, getMapTile(66, 27).getLocation()));
        npcs.add(new BrokenTree2(3063, getMapTile(66, 31).getLocation()));
        npcs.add(new BrokenTree2(3064, getMapTile(66, 35).getLocation()));
        npcs.add(new BrokenTree2(3065, getMapTile(66, 39).getLocation()));
        
        // Vertical line: X=30, Y=27 to 36 (3 trees)
        npcs.add(new BrokenTree2(3066, getMapTile(30, 27).getLocation()));
        npcs.add(new BrokenTree2(3067, getMapTile(30, 32).getLocation()));
        npcs.add(new BrokenTree2(3068, getMapTile(30, 36).getLocation()));
        
        // Tree-Hut-Tree formation
        npcs.add(new BrokenTree2(3069, getMapTile(62, 64).getLocation()));  // Left tree
        npcs.add(new BrokenTree2(3070, getMapTile(71, 64).getLocation()));  // Right tree
        
        // Two scattered trees
        npcs.add(new BrokenTree2(3071, getMapTile(21, 57).getLocation()));
        npcs.add(new BrokenTree2(3072, getMapTile(17, 61).getLocation()));
        
        // Rectangle corners
        npcs.add(new BrokenTree2(3073, getMapTile(33, 58).getLocation()));  // Top-left
        npcs.add(new BrokenTree2(3074, getMapTile(33, 65).getLocation()));  // Bottom-left
        npcs.add(new BrokenTree2(3075, getMapTile(46, 65).getLocation()));  // Bottom-right
        npcs.add(new BrokenTree2(3076, getMapTile(46, 58).getLocation()));  // Top-right

        // Random flower field generation

        // Avoid placing flowers directly on top of these occupied tiles
        HashSet<String> blockedTiles = new HashSet<>();
        blockedTiles.add("75,75"); // portal
        blockedTiles.add("60,42"); // hub fire flower
        blockedTiles.add("48,47"); // Sauron eye
        blockedTiles.add("41,39"); // volcano
        
        // Block all hut locations (shifted)
        blockedTiles.add("45,30");
        blockedTiles.add("67,66");
        blockedTiles.add("15,45");
        blockedTiles.add("22,47");
        blockedTiles.add("37,61");
        blockedTiles.add("42,62");
        blockedTiles.add("19,35");
        blockedTiles.add("44,7");
        blockedTiles.add("45,18");
        
        // Block all tree locations (shifted)
        blockedTiles.add("66,23");
        blockedTiles.add("66,27");
        blockedTiles.add("66,31");
        blockedTiles.add("66,35");
        blockedTiles.add("66,39");
        blockedTiles.add("30,27");
        blockedTiles.add("30,32");
        blockedTiles.add("30,36");
        blockedTiles.add("62,64");
        blockedTiles.add("71,64");
        blockedTiles.add("21,57");
        blockedTiles.add("17,61");
        blockedTiles.add("33,58");
        blockedTiles.add("33,65");
        blockedTiles.add("46,65");
        blockedTiles.add("46,58");

        // Track where we already put flowers so they do not stack
        HashSet<String> usedFlowerTiles = new HashSet<>();

        int nextFlowerId = 8000; // separate id range from other NPCs

        for (int i = 0; i < RANDOM_FLOWER_COUNT; i++) {

            int tileX;
            int tileY;
            int safetyCounter = 0;

            // Find a free, passable tile
            while (true) {
                tileX = rng.nextInt(MAX_TILE_X - MIN_TILE_X + 1) + MIN_TILE_X;
                tileY = rng.nextInt(MAX_TILE_Y - MIN_TILE_Y + 1) + MIN_TILE_Y;

                String key = tileX + "," + tileY;

                if (!blockedTiles.contains(key)
                        && !usedFlowerTiles.contains(key)
                        && isGoodFlowerTile(tileX, tileY)) {

                    usedFlowerTiles.add(key);
                    break;
                }

                safetyCounter++;
                if (safetyCounter > 250) {
                    // give up if we cannot find a valid tile for this flower
                    break;
                }
            }

            // if we failed to find a spot, stop spawning more
            if (safetyCounter > 250) {
                break;
            }

            // choose flower type
            float roll = rng.nextFloat();
            NPC flower;

            // Rare sunflower
            if (roll < RARE_SUNFLOWER_CHANCE) {
                flower = new RareSunflowerwithFlowers(
                    nextFlowerId++,
                    getMapTile(tileX, tileY).getLocation()
                );

            // FireFlower – high weight
            } else if (roll < 0.55f) { // 15% rare + 40% fire
                flower = new FireFlower(
                    nextFlowerId++,
                    getMapTile(tileX, tileY).getLocation()
                );

            // BlueBorah – also common
            } else if (roll < 0.80f) { // next 25%
                flower = new BlueBorah(
                    nextFlowerId++,
                    getMapTile(tileX, tileY).getLocation()
                );

            // Other common flowers (Cosmo, Daffodil, Daisy2, Poppy, Daisy)
            } else {
                int pick = rng.nextInt(5);

                switch (pick) {
                    case 0:
                        flower = new Cosmo(
                            nextFlowerId++,
                            getMapTile(tileX, tileY).getLocation()
                        );
                        break;
                    case 1:
                        flower = new Daffodil(
                            nextFlowerId++,
                            getMapTile(tileX, tileY).getLocation()
                        );
                        break;
                    case 2:
                        flower = new Daisy2(
                            nextFlowerId++,
                            getMapTile(tileX, tileY).getLocation()
                        );
                        break;
                    case 3:
                        flower = new Poppy(
                            nextFlowerId++,
                            getMapTile(tileX, tileY).getLocation()
                        );
                        break;
                    default:
                        flower = new Daisy(
                            nextFlowerId++,
                            getMapTile(tileX, tileY).getLocation()
                        );
                        break;
                }
            }

            npcs.add(flower);
        }

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        return new ArrayList<>();
    }

    @Override
    public void loadScripts() {
        getMapTile(55, 40).setInteractScript(new SimpleTextScript("Volcanic Region"));
        // triggers.add(new Trigger(0, 0, 6000, 6000, new AddRedEmeraldScript(), "spawnedYet"));

    }
}