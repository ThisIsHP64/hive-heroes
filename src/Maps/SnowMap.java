package Maps;

import Enemies.Crab;
import Enemies.Goblin;
import Enemies.FrostDragon;
import Flowers.SnowFlower;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import Portals.GrassPortal;
import Tilesets.SnowTileset;
import java.util.ArrayList;

public class SnowMap extends Map {
    public SnowMap() {
        super("snow_map.txt", new SnowTileset());
        this.playerStartPosition = getMapTile(0, 0).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // portal out
        GrassPortal grassPortal = new GrassPortal(1, getMapTile(47, 13).getLocation().addX(10).addY(15));
        npcs.add(grassPortal);

        // a little flower flavor
        SnowFlower snowFlower = new SnowFlower(4, getMapTile(15, 15).getLocation());
        npcs.add(snowFlower);

        // goblins
        npcs.add(new Goblin(getMapTile(16, 3).getLocation()));
        npcs.add(new Goblin(getMapTile(13, 10).getLocation()));
        npcs.add(new Goblin(getMapTile(20, 15).getLocation()));
        npcs.add(new Goblin(getMapTile(28, 1).getLocation()));
        npcs.add(new Goblin(getMapTile(40, 6).getLocation()));

        // crabs
        npcs.add(new Crab(getMapTile(1, 14).getLocation()));
        npcs.add(new Crab(getMapTile(3, 9).getLocation()));
        npcs.add(new Crab(getMapTile(43, 15).getLocation()));
        npcs.add(new Crab(getMapTile(20, 8).getLocation()));
        npcs.add(new Crab(getMapTile(48, 8).getLocation()));

        // frost dragons — existing
        npcs.add(new FrostDragon(getMapTile(1, 6).getLocation()));
        npcs.add(new FrostDragon(getMapTile(2, 8).getLocation()));

        // frost dragons — more spawns (spread out)
        npcs.add(new FrostDragon(getMapTile(6, 2).getLocation()));
        npcs.add(new FrostDragon(getMapTile(10, 4).getLocation()));
        npcs.add(new FrostDragon(getMapTile(18, 6).getLocation()));
        npcs.add(new FrostDragon(getMapTile(22, 12).getLocation()));
        npcs.add(new FrostDragon(getMapTile(35, 3).getLocation()));
        npcs.add(new FrostDragon(getMapTile(44, 10).getLocation()));

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        return triggers;
    }

    @Override
    public void loadScripts() {
        // no scripts yet
    }
}
