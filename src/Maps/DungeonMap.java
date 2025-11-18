package Maps;

import Level.Map;
import Level.NPC;
import NPCs.Carl;
import NPCs.Doug;
import NPCs.Earl;
import NPCs.Frank;
import NPCs.FrostTunic;
import NPCs.Gene;
import NPCs.Hank;
import NPCs.Ruth;
import NPCs.Walter;
import Portals.GrassPortal;
import PowerUps.StingerPowerUp;
import Scripts.DungeonMap.*;
import Tilesets.DungeonTileset;
import java.util.ArrayList;

public class DungeonMap extends Map {
    public DungeonMap() {
        super("dungeon_map.txt", new DungeonTileset());
        
        // Bee starts here
        this.playerStartPosition = getMapTile(90, 50).getLocation();
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();
        
        // === Add your existing dungeon NPCs here (spiders, portals, etc.) ===
        // Example:
        // npcs.add(new Spider(1, getMapTile(10, 10).getLocation()));
        
        // === Carl - Pest Control Guy ===
        Carl carl = new Carl(2001, getMapTile(16, 48).getLocation());
        carl.setInteractScript(new CarlScript());
        npcs.add(carl);
        
        // === Frank - The Bat ===
        Frank frank = new Frank(2003, getMapTile(91, 52).getLocation());
        frank.setInteractScript(new FrankScript());
        npcs.add(frank);
        
        // === Doug - Frog #1 ===
        Doug doug = new Doug(2004, getMapTile(41, 27).getLocation());
        doug.setInteractScript(new DougScript());
        npcs.add(doug);
        
        // === Earl - Frog #2 ===
        Earl earl = new Earl(2005, getMapTile(41, 14).getLocation());
        earl.setInteractScript(new EarlScript());
        npcs.add(earl);
        
        // === Hank - The Ghost ===
        Hank hank = new Hank(2006, getMapTile(79, 19).getLocation());
        hank.setInteractScript(new HankScript());
        npcs.add(hank);
        
        // === Walter ===
        Walter walter = new Walter(2007, getMapTile(81, 87).getLocation());
        walter.setInteractScript(new WalterScript());
        npcs.add(walter);
        
        // === Gene - The Horse ===
        Gene gene = new Gene(2008, getMapTile(40, 81).getLocation());
        gene.setInteractScript(new GeneScript());
        npcs.add(gene);
        
        // === Ruth - The Snake ===
        Ruth ruth = new Ruth(2009, getMapTile(40, 91).getLocation());
        ruth.setInteractScript(new RuthScript());
        npcs.add(ruth);
        
        // === Grass Portal - Teleport to Grasslands ===
        GrassPortal grassPortal = new GrassPortal(2010, getMapTile(35, 87).getLocation());
        grassPortal.setInteractScript(new DungeonGrassPortalScript());
        npcs.add(grassPortal);
        
        // === Frost Tunic - Blue Tunic Powerup ===
        FrostTunic frostTunic = new FrostTunic(2011, getMapTile(40, 87).getLocation());
        frostTunic.setInteractScript(new FrostTunicScript());
        npcs.add(frostTunic);
        
        // === Projectile powerup ===
        StingerPowerUp stingerPowerup = new StingerPowerUp(2002, getMapTile(79, 87).getLocation());
        npcs.add(stingerPowerup);
        
        return npcs;
    }

    @Override
    public void loadScripts() {
        
    }
}