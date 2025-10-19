package Maps;

import Level.Map;
import Level.Tileset;
import Tilesets.CommonTileset;

public class DungeonMap extends Map {

    public DungeonMap() {
        super("dungeon_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
    
}
