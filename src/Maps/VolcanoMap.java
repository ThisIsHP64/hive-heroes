package Maps;

import Level.Map;
import Tilesets.CommonTileset;
import Tilesets.VolcanoTileset;

public class VolcanoMap extends Map {
    public VolcanoMap() {
        super("volcano_map.txt", new VolcanoTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }
}
