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
import Level.MapTile;
import Level.NPC;
import Level.Trigger;
import Level.TileType;
import NPCs.BrokenHut;
import NPCs.BrokenTree2;
import NPCs.SauronEye;
import NPCs.Volcano;
import Portals.GrassPortal;
import Scripts.SimpleTextScript;
import Scripts.VolcanoMap.VolcanoGrassPortalScript;
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
        grassPortal.setInteractScript(new VolcanoGrassPortalScript());
        npcs.add(grassPortal);

        // One FireFlower near the hub area
        FireFlower hubFireFlower = new FireFlower(4000, getMapTile(60, 42).getLocation());
        npcs.add(hubFireFlower);

        // Broken hut
        BrokenHut brokenHut = new BrokenHut(4001, getMapTile(66, 42).getLocation());
        npcs.add(brokenHut);

        // Sauron's Eye near the volcano
        npcs.add(new SauronEye(9001, getMapTile(48, 47).getLocation()));

        // Volcano NPC - center of map
        npcs.add(new Volcano(3001, getMapTile(41, 39).getLocation()));

        // Broken trees for decoration
        npcs.add(new BrokenTree2(3061, getMapTile(6, 50).getLocation()));
        npcs.add(new BrokenTree2(3062, getMapTile(60, 57).getLocation()));
        npcs.add(new BrokenTree2(3063, getMapTile(57, 30).getLocation()));

        // Random flower field generation

        // Avoid placing flowers directly on top of these occupied tiles
        HashSet<String> blockedTiles = new HashSet<>();
        blockedTiles.add("75,75"); // portal (updated)
        blockedTiles.add("60,42"); // hub fire flower
        blockedTiles.add("66,42"); // hut
        blockedTiles.add("48,47"); // Sauron eye
        blockedTiles.add("41,39"); // volcano
        blockedTiles.add("6,50");
        blockedTiles.add("60,57");
        blockedTiles.add("57,30");

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
    }
}