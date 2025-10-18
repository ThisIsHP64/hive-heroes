package MapEditor;

import Level.Map;
import Maps.*;
import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("SprintOneMap");
            add("DemoMap");
            add("VolcanoMap");
            add("HiveMap");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "SprintOneMap":
                return new SprintOneMap();
            case "DemoMap":
                return new DemoMap();
            case "VolcanoMap":
                return new VolcanoMap();
            case "HiveMap":
                return new HiveMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
