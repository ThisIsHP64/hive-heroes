package Maps;

import java.util.ArrayList;

import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Scripts.GrassMap.GrassToDungeonScript;
import Scripts.HiveMap.GrassPortalScript;
import Scripts.MazeMap.MazeToGrassScript;
import Scripts.MazeMap.RedTunicScript;
import Tilesets.MazeTileset;
import NPCs.OneRing;
import Portals.GrassPortal;
import NPCs.FireTunic;

public class MazeMap extends Map {

    public MazeMap() {
        super("maze_map.txt", new MazeTileset());
        // Bee starts at X:1, Y:1 (top-left area of maze)
        this.playerStartPosition = getMapTile(1, 1).getLocation();
    }
    
    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        
        // No enhanced map tiles for now
        
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // Hidden Easter Egg - One Ring at X:2, Y:28 (bottom-left, hidden)
        OneRing oneRing = new OneRing(5000, getMapTile(3, 28).getLocation());
        npcs.add(oneRing);

        // FINISH - Red Tunic at X:19, Y:29 (center-bottom - the goal!)
        FireTunic redTunic = new FireTunic(5001, getMapTile(38, 28).getLocation());
        redTunic.setInteractScript(new RedTunicScript());
        npcs.add(redTunic);

        GrassPortal grassPortal = new GrassPortal(1, getMapTile(32, 26).getLocation().addY(20).addX(35));
        grassPortal.setInteractScript(new MazeToGrassScript());
        npcs.add(grassPortal);

        System.out.println("[MazeMap] START at (1,1) | ONE RING at (2,28) | RED TUNIC (finish) at (19,29)");

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