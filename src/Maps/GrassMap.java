package Maps;

import Enemies.Spider;
import Engine.GraphicsHandler;
import Level.Map;
import Level.NPC;
import NPCs.BigHive;
import NPCs.RareSunflowerwithFlowers;
import Portals.LavaPortal;
import Portals.SnowPortal;
import PowerUps.PowerUp;
import PowerUps.ShieldPowerUp;
import Scripts.SimpleTextScript;
import Scripts.TestMap.PowerUpScript;
import Tilesets.CommonTileset;
import java.util.ArrayList;


public class GrassMap extends Map {
    public GrassMap() {
        super("sprint_one_map.txt", new CommonTileset());
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
        
        // existing power-up
        PowerUp pu = new PowerUp(2000, getMapTile(70, 59).getLocation());
        pu.setInteractScript(new PowerUpScript());
        npcs.add(pu);

        // Shield powerup spawn
        ShieldPowerUp shield = new ShieldPowerUp(2001, getMapTile(76, 59).getLocation());
        npcs.add(shield);
        
        RareSunflowerwithFlowers rareSunflower = new RareSunflowerwithFlowers(4, getMapTile(67, 48).getLocation());
        npcs.add(rareSunflower);
        
        BigHive bigHive = new BigHive(4, getMapTile(67, 51).getLocation());
        npcs.add(bigHive);

        LavaPortal lavaPortal = new LavaPortal(1, getMapTile(64, 0).getLocation());
        npcs.add(lavaPortal);

        SnowPortal snowPortal = new SnowPortal(1, getMapTile(64, 122).getLocation().addY(25));
        npcs.add(snowPortal);

        // spawn multiple spiders across the map
        // spider 1 - original position near red X
        npcs.add(new Spider(1001, getMapTile(55, 62).getLocation().addY(6)));
        
        // // spider 2 - right of start
        npcs.add(new Spider(1002, getMapTile(80, 50).getLocation().addY(6)));
        
        // // spider 3 - below start
        npcs.add(new Spider(1003, getMapTile(70, 60).getLocation().addY(6)));
        
        // spider 4 - left of start
        npcs.add(new Spider(1004, getMapTile(60, 50).getLocation().addY(6)));
        
        // // spider 5 - upper area
        npcs.add(new Spider(1005, getMapTile(70, 40).getLocation().addY(6)));
        
        npcs.add(new Spider(1006, getMapTile(50, 55).getLocation().addY(6)));
        npcs.add(new Spider(1007, getMapTile(85, 45).getLocation().addY(6)));
        npcs.add(new Spider(1008, getMapTile(65, 65).getLocation().addY(6)));
        npcs.add(new Spider(1009, getMapTile(75, 55).getLocation().addY(6)));
        npcs.add(new Spider(1010, getMapTile(55, 45).getLocation().addY(6)));
        
        System.out.println("[SprintOneMap] Spawned 10 spiders across the map");
        
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
    }
}