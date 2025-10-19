package Maps;

import Level.Map;
import Tilesets.CommonTileset;
import Tilesets.VolcanoTileset;

public class SnowMap extends Map {
    public SnowMap() {
        super("snow_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
}
