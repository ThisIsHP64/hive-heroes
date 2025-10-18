package Maps;

import Level.Map;
import Level.NPC;
import NPCs.QueenBeeChair;
import Tilesets.HiveTileset;
import java.util.ArrayList;

public class HiveMap extends Map {
    public HiveMap() {
        super("hive_map.txt", new HiveTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    } 
    @Override
    public ArrayList<NPC> loadNPCs() {
       ArrayList<NPC> npcs = new ArrayList<>();

        QueenBeeChair queenbeechair = new QueenBeeChair(1, getMapTile(70, 59).getLocation());
        npcs.add(queenbeechair);

        return npcs;
        }
}
