package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.BeeGuard;
import NPCs.QueenBeeChair;
import Scripts.GrassMap.BeeGuardScript;
import Tilesets.HiveTileset;
import java.util.ArrayList;

public class HiveMap extends Map {


    public HiveMap() {
        super("hive_map.txt", new HiveTileset());
        this.playerStartPosition = getMapTile(66, 117).getLocation();
    } 
    
    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        QueenBeeChair queenbeechair = new QueenBeeChair(1, getMapTile(66, 110).getLocation());
        npcs.add(queenbeechair);

        BeeGuard guard = new BeeGuard(1, getMapTile(50, 100).getLocation());
        guard.setInteractScript(new BeeGuardScript());
        npcs.add(guard);
        

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
