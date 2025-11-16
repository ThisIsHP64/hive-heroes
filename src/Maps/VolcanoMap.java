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
import NPCs.SauronEye;
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

        // ===== SPIDERS REMOVED - EnemySpawner handles them dynamically now! =====

        // ===== BATS REMOVED - EnemySpawner handles them dynamically now! =====

        // ===== SKULLS REMOVED - EnemySpawner handles them dynamically now! =====

        // Easter egg: Sauron's Eye near the volcano
        npcs.add(new SauronEye(9001, getMapTile(48, 47).getLocation()));
        
        // Volcano NPC - SAFE at center of map
        npcs.add(new Volcano(3001, getMapTile(41, 39).getLocation()));

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