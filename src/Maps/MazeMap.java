package Maps;

import java.util.ArrayList;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Tilesets.MazeTileset;

public class MazeMap extends Map {

    public MazeMap() {
        super("maze_map.txt", new MazeTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
    
    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();



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
