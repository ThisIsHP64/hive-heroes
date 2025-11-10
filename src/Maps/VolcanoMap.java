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
import Tilesets.VolcanoTileset;
import java.util.ArrayList;

public class VolcanoMap extends Map {

    public VolcanoMap() {
        super("volcano_map.txt", new VolcanoTileset());
        
        // UPDATED: Adjusted from (85, 50) to fit 80x80 map
        // Places bee safely inside volcanic map walls on the right side
        this.playerStartPosition = getMapTile(70, 40).getLocation();
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

        // UPDATED: GrassPortal moved from (92, 49) to (75, 40) - near player spawn
        GrassPortal grassPortal = new GrassPortal(1, getMapTile(75, 40).getLocation());
        npcs.add(grassPortal);

        // UPDATED: Test sunflower moved from (83, 50) to (68, 40)
        RareSunflowerwithFlowers testSunflower = new RareSunflowerwithFlowers(5000, getMapTile(68, 40).getLocation());
        npcs.add(testSunflower);

        // UPDATED: FireFlower moved from (80, 50) to (65, 42)
        FireFlower fireFlower = new FireFlower(4, getMapTile(65, 42).getLocation());
        npcs.add(fireFlower);

        // SPIDERS - Repositioned ones that were out of bounds
        npcs.add(new Spider(2001, getMapTile(55, 50).getLocation().addY(6)));  // was (55, 62) - moved up a bit
        npcs.add(new Spider(2002, getMapTile(65, 40).getLocation().addY(6)));  // was (80, 50) - MOVED IN
        npcs.add(new Spider(2003, getMapTile(60, 52).getLocation().addY(6)));  // was (70, 60) - kept similar
        npcs.add(new Spider(2004, getMapTile(50, 45).getLocation().addY(6)));  // was (60, 50) - moved left a bit
        npcs.add(new Spider(2005, getMapTile(58, 35).getLocation().addY(6)));  // was (70, 40) - moved in
        npcs.add(new Spider(2006, getMapTile(39, 45).getLocation().addY(6)));  // FIXED: was (42, 48) on LAVA
        npcs.add(new Spider(2007, getMapTile(68, 38).getLocation().addY(6)));  // was (85, 45) - MOVED IN
        npcs.add(new Spider(2008, getMapTile(55, 55).getLocation().addY(6)));  // was (65, 65) - moved up
        npcs.add(new Spider(2009, getMapTile(62, 48).getLocation().addY(6)));  // was (75, 55) - adjusted
        npcs.add(new Spider(2010, getMapTile(47, 41).getLocation().addY(6)));  // FIXED: was (48, 42) on LAVA

        // BATS - Most were safe, minor adjustments for better spread
        npcs.add(new Bat(getMapTile(42, 41).getLocation()));  // FIXED: was (45, 44) on LAVA
        npcs.add(new Bat(getMapTile(60, 46).getLocation()));  // was (68, 52)
        npcs.add(new Bat(getMapTile(70, 50).getLocation()));  // was (78, 58)
        npcs.add(new Bat(getMapTile(52, 40).getLocation()));  // was (62, 44)
        npcs.add(new Bat(getMapTile(64, 42).getLocation()));  // was (72, 48)

        // SKULLS - Repositioned ones that were out of bounds
        npcs.add(new Skull(getMapTile(39, 45).getLocation()));  // FIXED: was (40, 46) on LAVA
        npcs.add(new Skull(getMapTile(68, 40).getLocation()));  // was (76, 46)
        npcs.add(new Skull(getMapTile(56, 50).getLocation()));  // was (64, 58)
        npcs.add(new Skull(getMapTile(66, 46).getLocation()));  // was (82, 52) - MOVED IN
        npcs.add(new Skull(getMapTile(48, 38).getLocation()));  // was (56, 42)
        npcs.add(new Skull(getMapTile(72, 42).getLocation()));  // was (88, 48) - MOVED IN

        // Volcano NPC - SAFE at center of map
        npcs.add(new Volcano(3001, getMapTile(41, 41).getLocation()));

        System.out.println("[VolcanoMap] Spawned 10 spiders, 5 bats, 6 skulls on 80x80 map");

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        return new ArrayList<>();
    }

    @Override
    public void loadScripts() {
        // Optional: region label(s) or tips
        getMapTile(55, 40).setInteractScript(new SimpleTextScript("Volcanic Region"));
    }
}