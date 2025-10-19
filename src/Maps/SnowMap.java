package Maps;

import Level.Map;
import Tilesets.CommonTileset;
import Tilesets.SnowTileset;
import Tilesets.VolcanoTileset;

public class SnowMap extends Map {
    public SnowMap() {
        super("snow_map.txt", new SnowTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
}
