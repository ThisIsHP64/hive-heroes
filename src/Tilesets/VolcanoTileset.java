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
        // uses an updated method (that supports a float scale) to load the image (48 by 48px)
        // super(ImageLoader.load("volcano_tiles.png"), 32, 32, 1.5f, 0);
        
        // this utilzes the same spritesheet as above, but shrunk so that the tiles are 16x16 (with a scale of 3f)
        // deferred because the resized spritesheet contains tiles of lower resolution quality 
        // super(ImageLoader.load("resized_volcano_tiles.png"), 16, 16, 3.0f, 0);

        // other spritesheet for the lava
        super(ImageLoader.load("lavatile_spritesheet.png"), 16, 16, 3f, 0);
    }


    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // Lava rocks 
        /*
        NOTES: 
            I CAN ALSO FLIP THEM ABOUT THE X OR Y AXIS FOR MORE FLEXIBILITY.
            For blocks that the bee walks on, I can take them from the pillars.
            Need to credit the creators of the original spritesheets in both the credits section of the game 
                                                                                   and on the readME.md file

        */
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
