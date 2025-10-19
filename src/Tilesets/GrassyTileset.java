package Tilesets;

import java.util.ArrayList;


import Builders.MapTileBuilder;
import Engine.ImageLoader;
import Level.Tileset;

public class GrassyTileset extends Tileset {
    public GrassyTileset() {
        super(ImageLoader.load("snow_spritesheet.png"), 32, 32, 1.5f, 0);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();


        return mapTiles;
    }
}
