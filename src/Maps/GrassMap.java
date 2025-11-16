package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Level.NPC;
import Level.Trigger;
import NPCs.BigHive;
import NPCs.BrokenTree;
import NPCs.Butterfly;
import NPCs.Cat;
import NPCs.Cat3;
import NPCs.DeadDeer;
import NPCs.DestroyedBeehive;
import NPCs.Dragonfly;
import NPCs.Ghost;
import NPCs.Grave;
import NPCs.Hut;
import NPCs.Hut2;
import NPCs.LadyBug;
import NPCs.SkelBee;
import Portals.LavaPortal;
import Portals.Portal;
import Portals.ReversePortal;
import Portals.SnowPortal;
import PowerUps.PowerUp;
import PowerUps.ShieldPowerUp;
import Scripts.GrassMap.*;
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
        
        // RareSunflowerwithFlowers rareSunflower = new RareSunflowerwithFlowers(4, getMapTile(67, 48).getLocation());
        // npcs.add(rareSunflower);

        // BlueBorah blueBorah = new BlueBorah(4, getMapTile(50, 55).getLocation());
        // npcs.add(blueBorah);

        // Cosmo cosmo = new Cosmo(4, getMapTile(52, 55).getLocation());
        // npcs.add(cosmo);

        // Daffodil daffodil = new Daffodil(4, getMapTile(45, 57).getLocation());
        // npcs.add(daffodil);

        // Daisy2 daisy2 = new Daisy2(4, getMapTile(46,57).getLocation());
        // npcs.add(daisy2);

        // Rose rose = new Rose(4, getMapTile(43, 54).getLocation());
        // npcs.add(rose);

        // Yarrow yarrow = new Yarrow(4, getMapTile(90, 50).getLocation());
        // npcs.add(yarrow);

        // Poppy poppy = new Poppy(4, getMapTile(45, 40).getLocation());
        // npcs.add(poppy);

        // Daisy daisy = new Daisy(4, getMapTile(35, 35).getLocation());
        // npcs.add(daisy);

        LadyBug ladyBug = new LadyBug(4, getMapTile(5, 122).getLocation());
        ladyBug.setInteractScript(new LadyBugScript());
        npcs.add(ladyBug);

        Butterfly butterfly = new Butterfly(4, getMapTile(55, 120).getLocation());
        butterfly.setInteractScript(new ButterflyScript());
        npcs.add(butterfly);

        Dragonfly dragonfly = new Dragonfly(4, getMapTile(60, 68).getLocation());
        dragonfly.setInteractScript(new DragonflyScript());
        npcs.add(dragonfly);
        
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

        // ===== SPIDERS REMOVED - EnemySpawner handles them dynamically now! =====

        npcs.add(new Hut(4000, getMapTile(84, 70).getLocation()));

        Cat cat = new Cat(5000, getMapTile(83, 71).getLocation());
        cat.setInteractScript(new CatScript());
        npcs.add(cat);

        DestroyedBeehive hive = new DestroyedBeehive(5001, getMapTile(108, 16).getLocation());
        hive.setInteractScript(new DestroyedBeehiveScript());
        npcs.add(hive);

        Cat3 cat3 = new Cat3(5079, getMapTile(110, 84).getLocation());
        cat3.setInteractScript(new Cat3Script());
        npcs.add(cat3);

        Hut2 hut2 = new Hut2(4001, getMapTile(100, 100).getLocation());
        hut2.setInteractScript(new Hut2Script());
        npcs.add(hut2);

        npcs.add(new Grave(5002, getMapTile(11, 10).getLocation()));
        
        Ghost ghost = new Ghost(5007, getMapTile(10, 11).getLocation());
        ghost.setInteractScript(new GhostScript());
        npcs.add(ghost);

        DeadDeer deer = new DeadDeer(5003, getMapTile(15, 55).getLocation());
        deer.setInteractScript(new DeadDeerScript());
        npcs.add(deer);

        npcs.add(new SkelBee(5030, getMapTile(9, 98).getLocation()));
        npcs.add(new SkelBee(5030, getMapTile(8, 96).getLocation()));
        npcs.add(new SkelBee(5030, getMapTile(16, 98).getLocation()));
        npcs.add(new SkelBee(5030, getMapTile(20, 98).getLocation()));
        npcs.add(new SkelBee(5030, getMapTile(30, 96).getLocation()));
        npcs.add(new SkelBee(5030, getMapTile(40, 90).getLocation()));
        npcs.add(new SkelBee(5030, getMapTile(43, 87).getLocation()));

        npcs.add(new BrokenTree(5069, getMapTile(100, 10).getLocation()));


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
        
        // triggers.add(new Trigger(0, 0, 6000, 6000, new RandomEventScript()));

        return triggers;
    }
}