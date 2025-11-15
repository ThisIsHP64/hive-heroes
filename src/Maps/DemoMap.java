package Maps;

import Engine.GraphicsHandler;
import Flowers.RareSunflowerwithFlowers;
import Level.Map;
import Level.NPC;
import NPCs.BigHive;
import Enemies.Spider;
import PowerUps.PowerUp;
import Scripts.SimpleTextScript;
import Tilesets.CommonTileset;
import java.util.ArrayList; // add spider

public class DemoMap extends Map {

    public DemoMap() {
        super("demo_map.txt", new CommonTileset());
        // Bee starts here
        this.playerStartPosition = getMapTile(70, 50).getLocation();
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // --- existing power-up ---
        PowerUp pu = new PowerUp(2000, getMapTile(37, 55).getLocation());
        npcs.add(pu);

        RareSunflowerwithFlowers rareSunflower = new RareSunflowerwithFlowers(4, getMapTile(40, 55).getLocation());
        npcs.add(rareSunflower);

        BigHive bigHive = new BigHive(4, getMapTile(67, 51).getLocation());
        npcs.add(bigHive);

        // spawn multiple spiders across the map
        npcs.add(new Spider(1001, getMapTile(55, 62).getLocation().addY(6)));
        npcs.add(new Spider(1002, getMapTile(80, 50).getLocation().addY(6)));
        npcs.add(new Spider(1003, getMapTile(70, 60).getLocation().addY(6)));
        npcs.add(new Spider(1004, getMapTile(60, 50).getLocation().addY(6)));
        npcs.add(new Spider(1005, getMapTile(70, 40).getLocation().addY(6)));
        npcs.add(new Spider(1006, getMapTile(43, 55).getLocation().addY(6)));
        npcs.add(new Spider(1007, getMapTile(50, 45).getLocation().addY(6)));
        npcs.add(new Spider(1008, getMapTile(85, 55).getLocation().addY(6)));
        npcs.add(new Spider(1009, getMapTile(65, 65).getLocation().addY(6)));
        npcs.add(new Spider(1010, getMapTile(75, 45).getLocation().addY(6)));

        System.out.println("[DemoMap] Spawned 10 spiders across the map");

        return npcs;
    }

    @Override
    public void loadScripts() {
        // Region labels
        
        getMapTile(71, 50).setInteractScript(new SimpleTextScript("The Hive"));
        getMapTile(72, 50).setInteractScript(new SimpleTextScript("The Hive"));

        getMapTile(0, 0).setInteractScript(new SimpleTextScript("Arctic Region?"));
        getMapTile(1, 0).setInteractScript(new SimpleTextScript("Arctic Region?"));

        getMapTile(123, 0).setInteractScript(new SimpleTextScript("Windy Region?"));
        getMapTile(124, 0).setInteractScript(new SimpleTextScript("Windy Region?"));

        getMapTile(123, 124).setInteractScript(new SimpleTextScript("Grassy Region?"));
        getMapTile(124, 124).setInteractScript(new SimpleTextScript("Grassy Region?"));

        getMapTile(0, 124).setInteractScript(new SimpleTextScript("Volcanic Region?"));
        getMapTile(1, 124).setInteractScript(new SimpleTextScript("Volcanic Region?"));

        // getMapTile(0, 63).setInteractScript(new TeleportScript());
    }
}