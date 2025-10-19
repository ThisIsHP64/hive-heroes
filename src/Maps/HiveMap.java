package Maps;

import Level.Map;
import Level.NPC;
import NPCs.QueenBeeChair;
import Tilesets.HiveTileset;
import java.util.ArrayList;

public class HiveMap extends Map {
    public HiveMap() {
        super("hive_map.txt", new HiveTileset());
        this.playerStartPosition = getMapTile(66, 117).getLocation();
    } 
    @Override
    public ArrayList<NPC> loadNPCs() {
       ArrayList<NPC> npcs = new ArrayList<>();

        QueenBeeChair queenbeechair = new QueenBeeChair(1, getMapTile(66, 110).getLocation());
        npcs.add(queenbeechair);

        return npcs;
        }
}
