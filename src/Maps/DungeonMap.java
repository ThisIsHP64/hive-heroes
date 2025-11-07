package Maps;

import Level.Map;
import Level.NPC;
import PowerUps.StingerPowerUp;
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
        
        // === Projectile powerup ===
        StingerPowerUp stingerPowerup = new StingerPowerUp(2002, getMapTile(18, 50).getLocation());
        npcs.add(stingerPowerup);
        
        return npcs;
    }

    @Override
    public void loadScripts() {
        // Add any scripts if needed
    }
}