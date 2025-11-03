package Maps;

import Enemies.Spider;
import Engine.GraphicsHandler;
import Flowers.*;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.BigHive;
<<<<<<< HEAD
=======
import NPCs.Cat;
import NPCs.Hut;
import NPCs.RareSunflowerwithFlowers;
>>>>>>> 5af4132 (in progress - blue tunic)
import Portals.LavaPortal;
import Portals.Portal;
import Portals.ReversePortal;
import Portals.SnowPortal;
import PowerUps.PowerUp;
import PowerUps.ShieldPowerUp;
import Scripts.GrassMap.GrassToDungeonScript;
import Scripts.GrassMap.GrassToMazeScript;
import Scripts.GrassMap.GrassToSnowScript;
import Scripts.GrassMap.GrassToVolcanoScript;
import Scripts.SimpleTextScript;
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
        npcs.add(pu);

        // Shield powerup spawn
        ShieldPowerUp shield = new ShieldPowerUp(2001, getMapTile(76, 59).getLocation());
        npcs.add(shield);
        
        RareSunflowerwithFlowers rareSunflower = new RareSunflowerwithFlowers(4, getMapTile(67, 48).getLocation());
        npcs.add(rareSunflower);

        BlueBorah blueBorah = new BlueBorah(4, getMapTile(50, 55).getLocation());
        npcs.add(blueBorah);

        Cosmo cosmo = new Cosmo(4, getMapTile(52, 55).getLocation());
        npcs.add(cosmo);

        Daffodil daffodil = new Daffodil(4, getMapTile(45, 57).getLocation());
        npcs.add(daffodil);

        Daisy2 daisy2 = new Daisy2(4, getMapTile(46,57).getLocation());
        npcs.add(daisy2);

        Rose rose = new Rose(4, getMapTile(43, 54).getLocation());
        npcs.add(rose);

        Yarrow yarrow = new Yarrow(4, getMapTile(90, 50).getLocation());
        npcs.add(yarrow);

        Poppy poppy = new Poppy(4, getMapTile(45, 40).getLocation());
        npcs.add(poppy);

        Daisy daisy = new Daisy(4, getMapTile(35, 35).getLocation());
        npcs.add(daisy);
        
        BigHive bigHive = new BigHive(4, getMapTile(67, 51).getLocation());
        npcs.add(bigHive);

        LavaPortal lavaPortal = new LavaPortal(1, getMapTile(64, 0).getLocation());
        lavaPortal.setInteractScript(new GrassToVolcanoScript());
        npcs.add(lavaPortal);

        SnowPortal snowPortal = new SnowPortal(1, getMapTile(64, 122).getLocation().addY(25));
        snowPortal.setInteractScript(new GrassToSnowScript());
        npcs.add(snowPortal);

        Portal dungeonPortal = new Portal(1, getMapTile(1, 63).getLocation());
        dungeonPortal.setInteractScript(new GrassToDungeonScript());
        npcs.add(dungeonPortal);

        ReversePortal mazePortal = new ReversePortal(1, getMapTile(121, 63).getLocation());
        mazePortal.setInteractScript(new GrassToMazeScript());
        npcs.add(mazePortal);

        // spawn multiple spiders across the map
        // spider 1 - original position near red X
        npcs.add(new Spider(1001, getMapTile(55, 62).getLocation().addY(6)));
        
        // spider 2 - right of start
        npcs.add(new Spider(1002, getMapTile(80, 50).getLocation().addY(6)));
        
        // spider 3 - below start
        npcs.add(new Spider(1003, getMapTile(70, 60).getLocation().addY(6)));
        
        // spider 4 - left of start
        npcs.add(new Spider(1004, getMapTile(60, 50).getLocation().addY(6)));
        
        // spider 5 - upper area
        npcs.add(new Spider(1005, getMapTile(70, 40).getLocation().addY(6)));
        
        npcs.add(new Spider(1006, getMapTile(50, 55).getLocation().addY(6)));
        npcs.add(new Spider(1007, getMapTile(85, 45).getLocation().addY(6)));
        npcs.add(new Spider(1008, getMapTile(65, 65).getLocation().addY(6)));
        npcs.add(new Spider(1009, getMapTile(75, 55).getLocation().addY(6)));
        npcs.add(new Spider(1010, getMapTile(55, 45).getLocation().addY(6)));
        
        System.out.println("[SprintOneMap] Spawned 10 spiders across the map");

        npcs.add(new Hut(4000, getMapTile(84, 70).getLocation()));

        npcs.add(new Cat(5000, getMapTile(83, 71).getLocation()));
        
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

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        
        // vertical triggers
        // triggers.add(new Trigger(0, 0, 6000, 6000, 
        //     new LevelUpgradeScript(), "isLevel1"));

        // triggers.add(new Trigger(0, 0, 6000, 6000, 
        //     new LevelUpgradeScript(), "isLevel2"));
        
        // triggers.add(new Trigger(0, 0, 6000, 6000, 
        //     new LevelUpgradeScript(), "isLevel3"));
        
        // triggers.add(new Trigger(0, 0, 6000, 6000, 
        //     new LevelUpgradeScript(), "isLevel4"));
        return triggers;
    }
}