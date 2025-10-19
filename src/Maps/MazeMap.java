package Maps;

import Level.Map;
import Level.Tileset;
import Tilesets.CommonTileset;
import Tilesets.MazeTileset;

public class MazeMap extends Map {

    public MazeMap() {
        super("maze_map.txt", new MazeTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
    
}
