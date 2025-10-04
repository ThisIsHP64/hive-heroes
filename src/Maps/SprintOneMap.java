package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Level.NPC;
import PowerUps.PowerUp;
import Scripts.SimpleTextScript;
import Scripts.TestMap.PowerUpScript;
import Tilesets.CommonTileset;

import java.util.ArrayList;

import NPCs.RareSunflowerwithFlowers;
import NPCs.Spider;      // add spider
import Utils.Point;     // for positions

public class SprintOneMap extends Map {

    public SprintOneMap() {
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

        // --- existing power-up ---
        PowerUp pu = new PowerUp(2000, getMapTile(5, 10).getLocation().subtractY(40));
        pu.setInteractScript(new PowerUpScript());
        npcs.add(pu);

        RareSunflowerwithFlowers rareSunflower = new RareSunflowerwithFlowers(4, getMapTile(70, 70).getLocation());
        npcs.add(rareSunflower);
        // --- SPIDER: place it down the path near the red X ---
        // Tiles on this map are grid-based; use tile coords for easy nudging.
        // Start is (70,50). The red X area is roughly ~12 tiles down and a hair right.
        final int TX = 55;  // tile X (move right: +1; left: -1)
        final int TY = 62;  // tile Y (move down: +1; up: -1)

        // Grab the tile's pixel location and give a tiny Y offset so it sits nicely
        Point spiderPos = getMapTile(TX, TY).getLocation().addY(6);
        npcs.add(new Spider(1001, spiderPos));

        System.out.println("[SprintOneMap] Spider at tile (" + TX + "," + TY + ") -> px("
                + spiderPos.x + "," + spiderPos.y + ")");

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
