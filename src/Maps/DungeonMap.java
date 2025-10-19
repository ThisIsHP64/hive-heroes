package Maps;

import Level.Map;
import Level.Tileset;
import Tilesets.CommonTileset;
import Tilesets.DungeonTileset;

public class DungeonMap extends Map {

    public DungeonMap() {
        super("dungeon_map.txt", new DungeonTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
    
}
