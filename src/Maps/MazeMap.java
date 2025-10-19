package Maps;

import Level.Map;
import Level.Tileset;
import Tilesets.CommonTileset;

public class MazeMap extends Map {

    public MazeMap() {
        super("maze_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
    
}
