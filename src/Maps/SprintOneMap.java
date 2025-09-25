package Maps;

import Engine.GraphicsHandler;
import Level.Map;
import Level.NPC;
import PowerUps.PowerUp;
import Scripts.SimpleTextScript;
import Scripts.TestMap.PowerUpScript;
import Tilesets.CommonTileset;
import java.util.ArrayList;

public class SprintOneMap extends Map {


    public SprintOneMap() {
        super("sprint_one_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(70, 50).getLocation();
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        PowerUp pu = new PowerUp(2000, getMapTile(5, 10).getLocation().subtractY(40));
        pu.setInteractScript(new PowerUpScript());
        npcs.add(pu);


        return npcs;
    }

    @Override
    public void loadScripts() {

        // simple signs and markers to indicate the relative locations of the Hive and other regions
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