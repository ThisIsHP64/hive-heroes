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
        this.playerStartPosition = getMapTile(63, 63).getLocation();
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
        getMapTile(63, 63).setInteractScript(new SimpleTextScript("The Hive"));

        getMapTile(0, 0).setInteractScript(new SimpleTextScript("(0,0) Arctic Region"));

        getMapTile(124, 0).setInteractScript(new SimpleTextScript("(124, 0) Arctic Region?"));

        getMapTile(124, 124).setInteractScript(new SimpleTextScript("(124, 124) Grassy Region?"));

        getMapTile(0, 124).setInteractScript(new SimpleTextScript("(0, 124) Volcanic Region?"));
    }
}