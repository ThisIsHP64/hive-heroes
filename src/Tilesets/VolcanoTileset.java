package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class VolcanoTileset extends Tileset {


    public VolcanoTileset() {
        super(ImageLoader.load("lavatile_spritesheet.png"), 16, 16, 3, 0);
    }


    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // Lava rocks

        Frame lavaRock1Frame = new FrameBuilder(getSubImage(1, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder lavaRock1 = new MapTileBuilder(lavaRock1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(lavaRock1);


        Frame lavaRock2Frame = new FrameBuilder(getSubImage(2, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder lavaRock2 = new MapTileBuilder(lavaRock2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(lavaRock2);


        Frame lavaFrame = new FrameBuilder(getSubImage(3, 0))
            .withScale(tileScale)
            .build();
        
        MapTileBuilder lava = new MapTileBuilder(lavaFrame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(lava); 

        return mapTiles;
    }
    
}
