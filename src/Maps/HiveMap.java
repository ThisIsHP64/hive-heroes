package Maps;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.BeeGuard;
import NPCs.Portal;
import NPCs.QueenBeeChair;
import Scripts.HiveMap.BeeGuardScript;
import Scripts.HiveMap.QueenBeeScript;
import Tilesets.HiveTileset;
import java.util.ArrayList;
import NPCs.QueenBee;

public class HiveMap extends Map {


    public HiveMap() {
        super("hive_map.txt", new HiveTileset());
        this.playerStartPosition = getMapTile(10, 20).getLocation();
    } 
    
    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        QueenBeeChair queenbeechair = new QueenBeeChair(1, getMapTile(9, 10).getLocation());
        npcs.add(queenbeechair);

        BeeGuard guard = new BeeGuard(1, getMapTile(15, 20).getLocation());
        guard.setInteractScript(new BeeGuardScript());
        npcs.add(guard);

        QueenBee queenBee = new QueenBee(1, getMapTile(12, 9).getLocation());
        queenBee.setInteractScript(new QueenBeeScript());
        npcs.add(queenBee);

        Portal portal = new Portal(1, getMapTile(3, 20).getLocation());
        npcs.add(portal);

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
