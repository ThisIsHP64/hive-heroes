package Maps;

import Level.Map;
import Level.NPC;
import NPCs.Carl;
import PowerUps.StingerPowerUp;
import Scripts.DungeonMap.Carlscript;
import Tilesets.DungeonTileset;
import java.util.ArrayList;

public class DungeonMap extends Map {
    public DungeonMap() {
        super("dungeon_map.txt", new DungeonTileset());
        
        // Bee starts here
        this.playerStartPosition = getMapTile(98, 50).getLocation();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        // === Add your existing dungeon NPCs here (spiders, portals, etc.) ===
        // Example:
        // npcs.add(new Spider(1, getMapTile(10, 10).getLocation()));
        
        // === Carl - Pest Control Guy ===
        Carl carl = new Carl(2001, getMapTile(16, 48).getLocation());
        carl.setInteractScript(new Carlscript());
        npcs.add(carl);
        
        // === Projectile powerup ===
        StingerPowerUp stingerPowerup = new StingerPowerUp(2002, getMapTile(18, 50).getLocation());
        npcs.add(stingerPowerup);
        
        return npcs;
    }

    @Override
    public void loadScripts() {
        // Scripts are now set directly on NPCs
    }
}