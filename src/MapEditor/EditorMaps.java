package MapEditor;

import Level.Map;
import Maps.SprintOneMap;
import Maps.TestMap;
import java.util.ArrayList;

public class EditorMaps {
    public static ArrayList<String> getMapNames() {
        return new ArrayList<String>() {{
            add("TestMap");
            add("TitleScreen");
            add("SprintOneMap");
        }};
    }

    public static Map getMapByName(String mapName) {
        switch(mapName) {
            case "TestMap":
                return new TestMap();
            case "SprintOneMap":
                return new SprintOneMap();
            default:
                throw new RuntimeException("Unrecognized map name");
        }
    }
}
