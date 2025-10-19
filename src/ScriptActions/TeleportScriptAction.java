package ScriptActions;

import Maps.*;

public class TeleportScriptAction extends ScriptAction {
    TestMap testMap;
    DemoMap demoMap;
    GrassMap map;
    
    public TeleportScriptAction() {


        testMap = new TestMap();
        player.setMap(testMap);
    }

    public TeleportScriptAction(String mapName) {
        switch(mapName) {
            case "Demo Map":
                demoMap = new DemoMap();
                player.setMap(demoMap);
            case "Test Map":
                testMap = new TestMap();
                player.setMap(testMap);
            case "Sprint Map":
                map = new GrassMap();
                player.setMap(map);
        }
    }

}
