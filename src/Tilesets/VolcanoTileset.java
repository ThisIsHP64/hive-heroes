package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

public class VolcanoTileset extends Tileset {


    public VolcanoTileset() {
        // uses an updated method (that supports a float scale) to load the image (48 by 48px)
        super(ImageLoader.load("volcano_spritesheet.png"), 32, 32, 1.5f, 0);
        
        // this utilzes the same spritesheet as above, but shrunk so that the tiles are 16x16 (with a scale of 3f)
        // deferred because the resized spritesheet contains tiles of lower resolution quality 
        // super(ImageLoader.load("resized_volcano_tiles.png"), 16, 16, 3.0f, 0);

        // other spritesheet for the lava
        // super(ImageLoader.load("lavatile_spritesheet.png"), 16, 16, 3f, 0);
    }


    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
            
        /*
        NOTES: 
            I CAN ALSO FLIP THEM ABOUT THE X OR Y AXIS FOR MORE FLEXIBILITY.
            Need to credit the creators of the original spritesheets in both the credits section of the game 
                                                                                   and on the readME.md file
        */

        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        Frame tile_0_0Frame = new FrameBuilder(getSubImage(0, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_0 = new MapTileBuilder(tile_0_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_0);

        Frame tile_0_1Frame = new FrameBuilder(getSubImage(0, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_1 = new MapTileBuilder(tile_0_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_1);

        Frame tile_0_2Frame = new FrameBuilder(getSubImage(0, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_2 = new MapTileBuilder(tile_0_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_2);

        Frame tile_0_3Frame = new FrameBuilder(getSubImage(0, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_3 = new MapTileBuilder(tile_0_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_3);

        Frame tile_0_4Frame = new FrameBuilder(getSubImage(0, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_4 = new MapTileBuilder(tile_0_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_4);

        Frame tile_0_5Frame = new FrameBuilder(getSubImage(0, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_5 = new MapTileBuilder(tile_0_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_5);

        Frame tile_0_6Frame = new FrameBuilder(getSubImage(0, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_6 = new MapTileBuilder(tile_0_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_6);

        Frame tile_0_7Frame = new FrameBuilder(getSubImage(0, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_7 = new MapTileBuilder(tile_0_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_7);

        Frame tile_0_8Frame = new FrameBuilder(getSubImage(0, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_8 = new MapTileBuilder(tile_0_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_8);

        Frame tile_0_9Frame = new FrameBuilder(getSubImage(0, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_9 = new MapTileBuilder(tile_0_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_9);

        Frame tile_0_10Frame = new FrameBuilder(getSubImage(0, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_10 = new MapTileBuilder(tile_0_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_10);

        Frame tile_0_11Frame = new FrameBuilder(getSubImage(0, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_11 = new MapTileBuilder(tile_0_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_11);

        Frame tile_0_12Frame = new FrameBuilder(getSubImage(0, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_12 = new MapTileBuilder(tile_0_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_12);

        Frame tile_0_13Frame = new FrameBuilder(getSubImage(0, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_13 = new MapTileBuilder(tile_0_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_13);

        Frame tile_0_14Frame = new FrameBuilder(getSubImage(0, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_14 = new MapTileBuilder(tile_0_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_14);

        Frame tile_0_15Frame = new FrameBuilder(getSubImage(0, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_15 = new MapTileBuilder(tile_0_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_15);

        Frame tile_0_16Frame = new FrameBuilder(getSubImage(0, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_16 = new MapTileBuilder(tile_0_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_16);

        Frame tile_0_17Frame = new FrameBuilder(getSubImage(0, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_0_17 = new MapTileBuilder(tile_0_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_0_17);

        Frame tile_1_0Frame = new FrameBuilder(getSubImage(1, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_0 = new MapTileBuilder(tile_1_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_0);

        Frame tile_1_1Frame = new FrameBuilder(getSubImage(1, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_1 = new MapTileBuilder(tile_1_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_1);

        Frame tile_1_2Frame = new FrameBuilder(getSubImage(1, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_2 = new MapTileBuilder(tile_1_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_2);

        Frame tile_1_3Frame = new FrameBuilder(getSubImage(1, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_3 = new MapTileBuilder(tile_1_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_3);

        Frame tile_1_4Frame = new FrameBuilder(getSubImage(1, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_4 = new MapTileBuilder(tile_1_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_4);

        Frame tile_1_5Frame = new FrameBuilder(getSubImage(1, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_5 = new MapTileBuilder(tile_1_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_5);

        Frame tile_1_6Frame = new FrameBuilder(getSubImage(1, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_6 = new MapTileBuilder(tile_1_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_6);

        Frame tile_1_7Frame = new FrameBuilder(getSubImage(1, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_7 = new MapTileBuilder(tile_1_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_7);

        Frame tile_1_8Frame = new FrameBuilder(getSubImage(1, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_8 = new MapTileBuilder(tile_1_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_8);

        Frame tile_1_9Frame = new FrameBuilder(getSubImage(1, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_9 = new MapTileBuilder(tile_1_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_9);

        Frame tile_1_10Frame = new FrameBuilder(getSubImage(1, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_10 = new MapTileBuilder(tile_1_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_10);

        Frame tile_1_11Frame = new FrameBuilder(getSubImage(1, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_11 = new MapTileBuilder(tile_1_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_11);

        Frame tile_1_12Frame = new FrameBuilder(getSubImage(1, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_12 = new MapTileBuilder(tile_1_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_12);

        Frame tile_1_13Frame = new FrameBuilder(getSubImage(1, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_13 = new MapTileBuilder(tile_1_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_13);

        Frame tile_1_14Frame = new FrameBuilder(getSubImage(1, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_14 = new MapTileBuilder(tile_1_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_14);

        Frame tile_1_15Frame = new FrameBuilder(getSubImage(1, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_15 = new MapTileBuilder(tile_1_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_15);

        Frame tile_1_16Frame = new FrameBuilder(getSubImage(1, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_16 = new MapTileBuilder(tile_1_16Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_1_16);

        Frame tile_1_17Frame = new FrameBuilder(getSubImage(1, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_1_17 = new MapTileBuilder(tile_1_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_1_17);

        Frame tile_2_0Frame = new FrameBuilder(getSubImage(2, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_0 = new MapTileBuilder(tile_2_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_0);

        Frame tile_2_1Frame = new FrameBuilder(getSubImage(2, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_1 = new MapTileBuilder(tile_2_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_1);

        Frame tile_2_2Frame = new FrameBuilder(getSubImage(2, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_2 = new MapTileBuilder(tile_2_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_2);

        Frame tile_2_3Frame = new FrameBuilder(getSubImage(2, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_3 = new MapTileBuilder(tile_2_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_3);

        Frame tile_2_4Frame = new FrameBuilder(getSubImage(2, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_4 = new MapTileBuilder(tile_2_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_4);

        Frame tile_2_5Frame = new FrameBuilder(getSubImage(2, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_5 = new MapTileBuilder(tile_2_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_5);

        Frame tile_2_6Frame = new FrameBuilder(getSubImage(2, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_6 = new MapTileBuilder(tile_2_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_6);

        Frame tile_2_7Frame = new FrameBuilder(getSubImage(2, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_7 = new MapTileBuilder(tile_2_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_7);

        Frame tile_2_8Frame = new FrameBuilder(getSubImage(2, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_8 = new MapTileBuilder(tile_2_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_8);

        Frame tile_2_9Frame = new FrameBuilder(getSubImage(2, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_9 = new MapTileBuilder(tile_2_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_9);

        Frame tile_2_10Frame = new FrameBuilder(getSubImage(2, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_10 = new MapTileBuilder(tile_2_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_10);

        Frame tile_2_11Frame = new FrameBuilder(getSubImage(2, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_11 = new MapTileBuilder(tile_2_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_11);

        Frame tile_2_12Frame = new FrameBuilder(getSubImage(2, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_12 = new MapTileBuilder(tile_2_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_12);

        Frame tile_2_13Frame = new FrameBuilder(getSubImage(2, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_13 = new MapTileBuilder(tile_2_13Frame)
            .withTileType(TileType.NOT_PASSABLE);
        
        mapTiles.add(tile_2_13);

        Frame tile_2_14Frame = new FrameBuilder(getSubImage(2, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_14 = new MapTileBuilder(tile_2_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_14);

        Frame tile_2_15Frame = new FrameBuilder(getSubImage(2, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_15 = new MapTileBuilder(tile_2_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_15);

        Frame tile_2_16Frame = new FrameBuilder(getSubImage(2, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_16 = new MapTileBuilder(tile_2_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_16);

        Frame tile_2_17Frame = new FrameBuilder(getSubImage(2, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_17 = new MapTileBuilder(tile_2_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_2_17);

        Frame tile_3_0Frame = new FrameBuilder(getSubImage(3, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_0 = new MapTileBuilder(tile_3_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_0);

        Frame tile_3_1Frame = new FrameBuilder(getSubImage(3, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_1 = new MapTileBuilder(tile_3_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_1);

        Frame tile_3_2Frame = new FrameBuilder(getSubImage(3, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_2 = new MapTileBuilder(tile_3_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_2);

        Frame tile_3_3Frame = new FrameBuilder(getSubImage(3, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_3 = new MapTileBuilder(tile_3_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_3);

        Frame tile_3_4Frame = new FrameBuilder(getSubImage(3, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_4 = new MapTileBuilder(tile_3_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_4);

        Frame tile_3_5Frame = new FrameBuilder(getSubImage(3, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_5 = new MapTileBuilder(tile_3_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_5);

        Frame tile_3_6Frame = new FrameBuilder(getSubImage(3, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_6 = new MapTileBuilder(tile_3_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_6);

        Frame tile_3_7Frame = new FrameBuilder(getSubImage(3, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_7 = new MapTileBuilder(tile_3_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_7);

        Frame tile_3_8Frame = new FrameBuilder(getSubImage(3, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_8 = new MapTileBuilder(tile_3_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_8);

        Frame tile_3_9Frame = new FrameBuilder(getSubImage(3, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_9 = new MapTileBuilder(tile_3_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_9);

        Frame tile_3_10Frame = new FrameBuilder(getSubImage(3, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_10 = new MapTileBuilder(tile_3_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_10);

        Frame tile_3_11Frame = new FrameBuilder(getSubImage(3, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_11 = new MapTileBuilder(tile_3_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_11);

        Frame tile_3_12Frame = new FrameBuilder(getSubImage(3, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_12 = new MapTileBuilder(tile_3_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_12);

        Frame tile_3_13Frame = new FrameBuilder(getSubImage(3, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_13 = new MapTileBuilder(tile_3_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_13);

        Frame tile_3_14Frame = new FrameBuilder(getSubImage(3, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_14 = new MapTileBuilder(tile_3_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_14);

        Frame tile_3_15Frame = new FrameBuilder(getSubImage(3, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_15 = new MapTileBuilder(tile_3_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_15);

        Frame tile_3_16Frame = new FrameBuilder(getSubImage(3, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_16 = new MapTileBuilder(tile_3_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_16);

        Frame tile_3_17Frame = new FrameBuilder(getSubImage(3, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_3_17 = new MapTileBuilder(tile_3_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_3_17);

        Frame tile_4_0Frame = new FrameBuilder(getSubImage(4, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_0 = new MapTileBuilder(tile_4_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_0);

        Frame tile_4_1Frame = new FrameBuilder(getSubImage(4, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_1 = new MapTileBuilder(tile_4_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_1);

        Frame tile_4_2Frame = new FrameBuilder(getSubImage(4, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_2 = new MapTileBuilder(tile_4_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_2);

        Frame tile_4_3Frame = new FrameBuilder(getSubImage(4, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_3 = new MapTileBuilder(tile_4_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_3);

        Frame tile_4_4Frame = new FrameBuilder(getSubImage(4, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_4 = new MapTileBuilder(tile_4_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_4);

        Frame tile_4_5Frame = new FrameBuilder(getSubImage(4, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_5 = new MapTileBuilder(tile_4_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_5);

        Frame tile_4_6Frame = new FrameBuilder(getSubImage(4, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_6 = new MapTileBuilder(tile_4_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_6);

        Frame tile_4_7Frame = new FrameBuilder(getSubImage(4, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_7 = new MapTileBuilder(tile_4_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_7);

        Frame tile_4_8Frame = new FrameBuilder(getSubImage(4, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_8 = new MapTileBuilder(tile_4_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_8);

        Frame tile_4_9Frame = new FrameBuilder(getSubImage(4, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_9 = new MapTileBuilder(tile_4_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_9);

        Frame tile_4_10Frame = new FrameBuilder(getSubImage(4, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_10 = new MapTileBuilder(tile_4_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_10);

        Frame tile_4_11Frame = new FrameBuilder(getSubImage(4, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_11 = new MapTileBuilder(tile_4_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_11);

        Frame tile_4_12Frame = new FrameBuilder(getSubImage(4, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_12 = new MapTileBuilder(tile_4_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_12);

        Frame tile_4_13Frame = new FrameBuilder(getSubImage(4, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_13 = new MapTileBuilder(tile_4_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_13);

        Frame tile_4_14Frame = new FrameBuilder(getSubImage(4, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_14 = new MapTileBuilder(tile_4_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_14);

        Frame tile_4_15Frame = new FrameBuilder(getSubImage(4, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_15 = new MapTileBuilder(tile_4_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_15);

        Frame tile_4_16Frame = new FrameBuilder(getSubImage(4, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_16 = new MapTileBuilder(tile_4_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_16);

        Frame tile_4_17Frame = new FrameBuilder(getSubImage(4, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_17 = new MapTileBuilder(tile_4_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_4_17);

        Frame tile_5_0Frame = new FrameBuilder(getSubImage(5, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_0 = new MapTileBuilder(tile_5_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_0);

        Frame tile_5_1Frame = new FrameBuilder(getSubImage(5, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_1 = new MapTileBuilder(tile_5_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_1);

        Frame tile_5_2Frame = new FrameBuilder(getSubImage(5, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_2 = new MapTileBuilder(tile_5_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_2);

        Frame tile_5_3Frame = new FrameBuilder(getSubImage(5, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_3 = new MapTileBuilder(tile_5_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_3);

        Frame tile_5_4Frame = new FrameBuilder(getSubImage(5, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_4 = new MapTileBuilder(tile_5_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_4);

        Frame tile_5_5Frame = new FrameBuilder(getSubImage(5, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_5 = new MapTileBuilder(tile_5_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_5);

        Frame tile_5_6Frame = new FrameBuilder(getSubImage(5, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_6 = new MapTileBuilder(tile_5_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_6);

        Frame tile_5_7Frame = new FrameBuilder(getSubImage(5, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_7 = new MapTileBuilder(tile_5_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_7);

        Frame tile_5_8Frame = new FrameBuilder(getSubImage(5, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_8 = new MapTileBuilder(tile_5_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_8);

        Frame tile_5_9Frame = new FrameBuilder(getSubImage(5, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_9 = new MapTileBuilder(tile_5_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_9);

        Frame tile_5_10Frame = new FrameBuilder(getSubImage(5, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_10 = new MapTileBuilder(tile_5_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_10);

        Frame tile_5_11Frame = new FrameBuilder(getSubImage(5, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_11 = new MapTileBuilder(tile_5_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_11);

        Frame tile_5_12Frame = new FrameBuilder(getSubImage(5, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_12 = new MapTileBuilder(tile_5_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_12);

        Frame tile_5_13Frame = new FrameBuilder(getSubImage(5, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_13 = new MapTileBuilder(tile_5_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_13);

        Frame tile_5_14Frame = new FrameBuilder(getSubImage(5, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_14 = new MapTileBuilder(tile_5_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_14);

        Frame tile_5_15Frame = new FrameBuilder(getSubImage(5, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_15 = new MapTileBuilder(tile_5_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_15);

        Frame tile_5_16Frame = new FrameBuilder(getSubImage(5, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_16 = new MapTileBuilder(tile_5_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_16);

        Frame tile_5_17Frame = new FrameBuilder(getSubImage(5, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_5_17 = new MapTileBuilder(tile_5_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_5_17);

        Frame tile_6_0Frame = new FrameBuilder(getSubImage(6, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_0 = new MapTileBuilder(tile_6_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_0);

        Frame tile_6_1Frame = new FrameBuilder(getSubImage(6, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_1 = new MapTileBuilder(tile_6_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_1);

        Frame tile_6_2Frame = new FrameBuilder(getSubImage(6, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_2 = new MapTileBuilder(tile_6_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_2);

        Frame tile_6_3Frame = new FrameBuilder(getSubImage(6, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_3 = new MapTileBuilder(tile_6_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_3);

        Frame tile_6_4Frame = new FrameBuilder(getSubImage(6, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_4 = new MapTileBuilder(tile_6_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_4);

        Frame tile_6_5Frame = new FrameBuilder(getSubImage(6, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_5 = new MapTileBuilder(tile_6_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_5);

        Frame tile_6_6Frame = new FrameBuilder(getSubImage(6, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_6 = new MapTileBuilder(tile_6_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_6);

        Frame tile_6_7Frame = new FrameBuilder(getSubImage(6, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_7 = new MapTileBuilder(tile_6_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_7);

        Frame tile_6_8Frame = new FrameBuilder(getSubImage(6, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_8 = new MapTileBuilder(tile_6_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_8);

        Frame tile_6_9Frame = new FrameBuilder(getSubImage(6, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_9 = new MapTileBuilder(tile_6_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_9);

        Frame tile_6_10Frame = new FrameBuilder(getSubImage(6, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_10 = new MapTileBuilder(tile_6_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_10);

        Frame tile_6_11Frame = new FrameBuilder(getSubImage(6, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_11 = new MapTileBuilder(tile_6_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_11);

        Frame tile_6_12Frame = new FrameBuilder(getSubImage(6, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_12 = new MapTileBuilder(tile_6_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_12);

        Frame tile_6_13Frame = new FrameBuilder(getSubImage(6, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_13 = new MapTileBuilder(tile_6_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_13);

        Frame tile_6_14Frame = new FrameBuilder(getSubImage(6, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_14 = new MapTileBuilder(tile_6_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_14);

        Frame tile_6_15Frame = new FrameBuilder(getSubImage(6, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_15 = new MapTileBuilder(tile_6_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_15);

        Frame tile_6_16Frame = new FrameBuilder(getSubImage(6, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_16 = new MapTileBuilder(tile_6_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_16);

        Frame tile_6_17Frame = new FrameBuilder(getSubImage(6, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_6_17 = new MapTileBuilder(tile_6_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_6_17);

        Frame tile_7_0Frame = new FrameBuilder(getSubImage(7, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_0 = new MapTileBuilder(tile_7_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_0);

        Frame tile_7_1Frame = new FrameBuilder(getSubImage(7, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_1 = new MapTileBuilder(tile_7_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_1);

        Frame tile_7_2Frame = new FrameBuilder(getSubImage(7, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_2 = new MapTileBuilder(tile_7_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_2);

        Frame tile_7_3Frame = new FrameBuilder(getSubImage(7, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_3 = new MapTileBuilder(tile_7_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_3);

        Frame tile_7_4Frame = new FrameBuilder(getSubImage(7, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_4 = new MapTileBuilder(tile_7_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_4);

        Frame tile_7_5Frame = new FrameBuilder(getSubImage(7, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_5 = new MapTileBuilder(tile_7_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_5);

        Frame tile_7_6Frame = new FrameBuilder(getSubImage(7, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_6 = new MapTileBuilder(tile_7_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_6);

        Frame tile_7_7Frame = new FrameBuilder(getSubImage(7, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_7 = new MapTileBuilder(tile_7_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_7);

        Frame tile_7_8Frame = new FrameBuilder(getSubImage(7, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_8 = new MapTileBuilder(tile_7_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_8);

        Frame tile_7_9Frame = new FrameBuilder(getSubImage(7, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_9 = new MapTileBuilder(tile_7_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_9);

        Frame tile_7_10Frame = new FrameBuilder(getSubImage(7, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_10 = new MapTileBuilder(tile_7_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_10);

        Frame tile_7_11Frame = new FrameBuilder(getSubImage(7, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_11 = new MapTileBuilder(tile_7_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_11);

        Frame tile_7_12Frame = new FrameBuilder(getSubImage(7, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_12 = new MapTileBuilder(tile_7_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_12);

        Frame tile_7_13Frame = new FrameBuilder(getSubImage(7, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_13 = new MapTileBuilder(tile_7_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_13);

        Frame tile_7_14Frame = new FrameBuilder(getSubImage(7, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_14 = new MapTileBuilder(tile_7_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_14);

        Frame tile_7_15Frame = new FrameBuilder(getSubImage(7, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_15 = new MapTileBuilder(tile_7_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_15);

        Frame tile_7_16Frame = new FrameBuilder(getSubImage(7, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_16 = new MapTileBuilder(tile_7_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_16);

        Frame tile_7_17Frame = new FrameBuilder(getSubImage(7, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_7_17 = new MapTileBuilder(tile_7_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_7_17);

        Frame tile_8_0Frame = new FrameBuilder(getSubImage(8, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_0 = new MapTileBuilder(tile_8_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_0);

        Frame tile_8_1Frame = new FrameBuilder(getSubImage(8, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_1 = new MapTileBuilder(tile_8_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_1);

        Frame tile_8_2Frame = new FrameBuilder(getSubImage(8, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_2 = new MapTileBuilder(tile_8_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_2);

        Frame tile_8_3Frame = new FrameBuilder(getSubImage(8, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_3 = new MapTileBuilder(tile_8_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_3);

        Frame tile_8_4Frame = new FrameBuilder(getSubImage(8, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_4 = new MapTileBuilder(tile_8_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_4);

        Frame tile_8_5Frame = new FrameBuilder(getSubImage(8, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_5 = new MapTileBuilder(tile_8_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_5);

        Frame tile_8_6Frame = new FrameBuilder(getSubImage(8, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_6 = new MapTileBuilder(tile_8_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_6);

        Frame tile_8_7Frame = new FrameBuilder(getSubImage(8, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_7 = new MapTileBuilder(tile_8_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_7);

        Frame tile_8_8Frame = new FrameBuilder(getSubImage(8, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_8 = new MapTileBuilder(tile_8_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_8);

        Frame tile_8_9Frame = new FrameBuilder(getSubImage(8, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_9 = new MapTileBuilder(tile_8_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_9);

        Frame tile_8_10Frame = new FrameBuilder(getSubImage(8, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_10 = new MapTileBuilder(tile_8_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_10);

        Frame tile_8_11Frame = new FrameBuilder(getSubImage(8, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_11 = new MapTileBuilder(tile_8_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_11);

        Frame tile_8_12Frame = new FrameBuilder(getSubImage(8, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_12 = new MapTileBuilder(tile_8_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_12);

        Frame tile_8_13Frame = new FrameBuilder(getSubImage(8, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_13 = new MapTileBuilder(tile_8_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_13);

        Frame tile_8_14Frame = new FrameBuilder(getSubImage(8, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_14 = new MapTileBuilder(tile_8_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_14);

        Frame tile_8_15Frame = new FrameBuilder(getSubImage(8, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_15 = new MapTileBuilder(tile_8_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_15);

        Frame tile_8_16Frame = new FrameBuilder(getSubImage(8, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_16 = new MapTileBuilder(tile_8_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_16);

        Frame tile_8_17Frame = new FrameBuilder(getSubImage(8, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_17 = new MapTileBuilder(tile_8_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_8_17);

        Frame tile_9_0Frame = new FrameBuilder(getSubImage(9, 0))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_0 = new MapTileBuilder(tile_9_0Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_0);

        Frame tile_9_1Frame = new FrameBuilder(getSubImage(9, 1))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_1 = new MapTileBuilder(tile_9_1Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_1);

        Frame tile_9_2Frame = new FrameBuilder(getSubImage(9, 2))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_2 = new MapTileBuilder(tile_9_2Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_2);

        Frame tile_9_3Frame = new FrameBuilder(getSubImage(9, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_3 = new MapTileBuilder(tile_9_3Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_3);

        Frame tile_9_4Frame = new FrameBuilder(getSubImage(9, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_4 = new MapTileBuilder(tile_9_4Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_4);

        Frame tile_9_5Frame = new FrameBuilder(getSubImage(9, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_5 = new MapTileBuilder(tile_9_5Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_5);

        Frame tile_9_6Frame = new FrameBuilder(getSubImage(9, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_6 = new MapTileBuilder(tile_9_6Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_6);

        Frame tile_9_7Frame = new FrameBuilder(getSubImage(9, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_7 = new MapTileBuilder(tile_9_7Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_7);

        Frame tile_9_8Frame = new FrameBuilder(getSubImage(9, 8))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_8 = new MapTileBuilder(tile_9_8Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_8);

        Frame tile_9_9Frame = new FrameBuilder(getSubImage(9, 9))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_9 = new MapTileBuilder(tile_9_9Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_9);

        Frame tile_9_10Frame = new FrameBuilder(getSubImage(9, 10))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_10 = new MapTileBuilder(tile_9_10Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_10);

        Frame tile_9_11Frame = new FrameBuilder(getSubImage(9, 11))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_11 = new MapTileBuilder(tile_9_11Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_11);

        Frame tile_9_12Frame = new FrameBuilder(getSubImage(9, 12))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_12 = new MapTileBuilder(tile_9_12Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_12);

        Frame tile_9_13Frame = new FrameBuilder(getSubImage(9, 13))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_13 = new MapTileBuilder(tile_9_13Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_13);

        Frame tile_9_14Frame = new FrameBuilder(getSubImage(9, 14))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_14 = new MapTileBuilder(tile_9_14Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_14);

        Frame tile_9_15Frame = new FrameBuilder(getSubImage(9, 15))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_15 = new MapTileBuilder(tile_9_15Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_15);

        Frame tile_9_16Frame = new FrameBuilder(getSubImage(9, 16))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_16 = new MapTileBuilder(tile_9_16Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_16);

        Frame tile_9_17Frame = new FrameBuilder(getSubImage(9, 17))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_17 = new MapTileBuilder(tile_9_17Frame)
            .withTileType(TileType.PASSABLE);

        mapTiles.add(tile_9_17);

        return mapTiles;
    }
    
}
