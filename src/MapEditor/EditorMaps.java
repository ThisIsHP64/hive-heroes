package MapEditor;

import Level.Map;
import Maps.*;
import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("DemoMap");
            add("HiveMap");

            add("GrassMap");
            add("VolcanoMap");
            add("MazeMap");
            add("DungeonMap");
            add("SnowMap");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "DemoMap":
                return new DemoMap();

            case "HiveMap":
                return new HiveMap();

            case "GrassMap":
                return new GrassMap();
            
            case "VolcanoMap":
                return new VolcanoMap();

            case "MazeMap":
                return new MazeMap();

            case "DungeonMap":
                return new DungeonMap();

            case "SnowMap":
                return new SnowMap();

            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
