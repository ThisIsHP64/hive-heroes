package Tilesets;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Builders.MapTileBuilder;
import Engine.ImageLoader;
import Level.Tileset;

public class SnowTileset extends Tileset {

    public SnowTileset() {
        super(ImageLoader.load("snow_spritesheet.png"), 16, 16, 3f, 0);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();


        return mapTiles;
    }
    
}
