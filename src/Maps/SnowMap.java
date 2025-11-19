package Maps;

import Flowers.SnowFlower;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.Emerald;
import NPCs.FrostTree;
import Portals.GrassPortal;
import Scripts.DungeonMap.DungeonGrassPortalScript;
import Scripts.SnowMap.EmeraldScript;
import Tilesets.SnowTileset;
import java.util.ArrayList;

public class SnowMap extends Map {

    public SnowMap() {
        super("snow_map.txt", new SnowTileset());
        this.playerStartPosition = getMapTile(0, 0).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        // portal out
        GrassPortal grassPortal = new GrassPortal(1, getMapTile(47, 13).getLocation().addX(10).addY(15));
        grassPortal.setInteractScript(new DungeonGrassPortalScript());
        npcs.add(grassPortal);

        FrostTree frostTree = new FrostTree(1069, getMapTile(18,12).getLocation());
        npcs.add(frostTree);

        FrostTree frostTree2 = new FrostTree(1069, getMapTile(28,12).getLocation());
        npcs.add(frostTree2);

        FrostTree frostTree3 = new FrostTree(1069, getMapTile(34,12).getLocation());
        npcs.add(frostTree3);
        
        // a little flower flavor
        SnowFlower snowFlower = new SnowFlower(4, getMapTile(15, 15).getLocation());
        npcs.add(snowFlower);

        SnowFlower snowFlower2 = new SnowFlower(4, getMapTile(16, 15).getLocation());
        npcs.add(snowFlower2);

        SnowFlower snowFlower3 = new SnowFlower(4, getMapTile(20, 15).getLocation());
        npcs.add(snowFlower3);

        SnowFlower snowFlower4 = new SnowFlower(4, getMapTile(25, 15).getLocation());
        npcs.add(snowFlower4);

        SnowFlower snowFlower5 = new SnowFlower(4, getMapTile(32, 15).getLocation());
        npcs.add(snowFlower5);

        SnowFlower snowFlower6 = new SnowFlower(4, getMapTile(39, 15).getLocation());
        npcs.add(snowFlower6);

        SnowFlower snowFlower7 = new SnowFlower(4, getMapTile(41, 15).getLocation());
        npcs.add(snowFlower7);
        
        // ===== GOBLINS REMOVED - EnemySpawner handles them dynamically now! =====
        
        // ===== CRABS REMOVED - EnemySpawner handles them dynamically now! =====
        
        // ===== FROST DRAGONS REMOVED - EnemySpawner handles them dynamically now! =====

        Emerald emerald = new Emerald(1, getMapTile(40, 13).getLocation());
        emerald.setInteractScript(new EmeraldScript());
        npcs.add(emerald);

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        return triggers;
    }

    @Override
    public void loadScripts() {
        // no scripts yet
    }
}