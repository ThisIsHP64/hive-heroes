package Maps;

import Level.Map;
import Tilesets.HiveTileset;

public class HiveMap extends Map {
    public HiveMap() {
        super("hive_map.txt", new HiveTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
}
