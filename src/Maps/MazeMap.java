package Maps;

import java.util.ArrayList;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Portals.GrassPortal;
import Tilesets.MazeTileset;

public class MazeMap extends Map {

    public MazeMap() {
        super("maze_map.txt", new MazeTileset());
        this.playerStartPosition = getMapTile(5, 10).getLocation();
    }
    
    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        GrassPortal grassPortal = new GrassPortal(1, getMapTile(19, 21).getLocation().addX(35).addY(20));
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
