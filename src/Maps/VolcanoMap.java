package Maps;

import Enemies.Bat;
import Enemies.Spider;
import Enemies.Skull;
import Engine.GraphicsHandler;
import Flowers.FireFlower;
import Flowers.RareSunflowerwithFlowers;
import Level.EnhancedMapTile;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.Volcano;
import Portals.GrassPortal;
import Scripts.SimpleTextScript;
import Tilesets.VolcanoTileset; // add this import
import java.util.ArrayList;

public class VolcanoMap extends Map {

    public VolcanoMap() {
        super("volcano_map.txt", new VolcanoTileset());
        // place Bee safely inside the volcanic map walls
        this.playerStartPosition = getMapTile(85, 50).getLocation();
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        GrassPortal grassPortal = new GrassPortal(1, getMapTile(92, 49).getLocation());
        npcs.add(grassPortal);

        // TEST SUNFLOWER - spawn near bee for horde testing
        RareSunflowerwithFlowers testSunflower = new RareSunflowerwithFlowers(5000, getMapTile(83, 50).getLocation());
        npcs.add(testSunflower);

        FireFlower fireFlower = new FireFlower(4, getMapTile(80, 50).getLocation());
        npcs.add(fireFlower);

        // Mirror the GrassMap style: give each spider a unique ID, use tile-based placement,
        // and nudge down a bit with addY(6) so feet align with the ground.
        npcs.add(new Spider(2001, getMapTile(55, 62).getLocation().addY(6)));
        npcs.add(new Spider(2002, getMapTile(80, 50).getLocation().addY(6)));
        npcs.add(new Spider(2003, getMapTile(70, 60).getLocation().addY(6)));
        npcs.add(new Spider(2004, getMapTile(60, 50).getLocation().addY(6)));
        npcs.add(new Spider(2005, getMapTile(70, 40).getLocation().addY(6)));

        // a few more around the lava fields—tweak as needed to avoid walls/lava tiles
        npcs.add(new Spider(2006, getMapTile(50, 55).getLocation().addY(6)));
        npcs.add(new Spider(2007, getMapTile(85, 45).getLocation().addY(6)));
        npcs.add(new Spider(2008, getMapTile(65, 65).getLocation().addY(6)));
        npcs.add(new Spider(2009, getMapTile(75, 55).getLocation().addY(6)));
        npcs.add(new Spider(2010, getMapTile(58, 46).getLocation().addY(6)));

        // spawn bats - flying enemies scattered throughout
        npcs.add(new Bat(getMapTile(52, 48).getLocation()));
        npcs.add(new Bat(getMapTile(68, 52).getLocation()));
        npcs.add(new Bat(getMapTile(78, 58).getLocation()));
        npcs.add(new Bat(getMapTile(62, 44).getLocation()));
        npcs.add(new Bat(getMapTile(72, 48).getLocation()));

        // spawn skulls - flying undead enemies
        npcs.add(new Skull(getMapTile(48, 52).getLocation()));
        npcs.add(new Skull(getMapTile(76, 46).getLocation()));
        npcs.add(new Skull(getMapTile(64, 58).getLocation()));
        npcs.add(new Skull(getMapTile(82, 52).getLocation()));
        npcs.add(new Skull(getMapTile(56, 42).getLocation()));
        npcs.add(new Skull(getMapTile(88, 48).getLocation()));

        // Volcano in the center of the map
        npcs.add(new Volcano(3001, getMapTile(41, 41).getLocation()));

        System.out.println("[VolcanoMap] Spawned 10 spiders, 5 bats, 6 skulls, and 1 TEST sunflower");

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        return new ArrayList<>();
    }

    @Override
    public void loadScripts() {
        // Optional: region label(s) or tips like in GrassMap
        getMapTile(70, 50).setInteractScript(new SimpleTextScript("Volcanic Region"));
    }
}