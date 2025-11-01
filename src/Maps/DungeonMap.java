package Maps;

import java.util.ArrayList;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Portals.GrassPortal;
import Tilesets.DungeonTileset;

public class DungeonMap extends Map {

    public DungeonMap() {
        super("dungeon_map.txt", new DungeonTileset());
        this.playerStartPosition = getMapTile(91, 50).getLocation();
    }
    
    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // Portal portal = new Portal(1, getMapTile(95, 49).getLocation());
        // npcs.add(portal);

        GrassPortal grassPortal = new GrassPortal(1, getMapTile(95, 49).getLocation());
        npcs.add(grassPortal);

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        


        return triggers;
    }

    @Override
    public void loadScripts() {
        

        
    }
}
