package Tilesets;

import java.util.ArrayList;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;

public class MazeTileset extends Tileset {

    public MazeTileset() {
        super(ImageLoader.load("maze_spritesheet.png"), 32, 32, 1.5f, 0);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {

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
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_2_2);

        Frame tile_2_3Frame = new FrameBuilder(getSubImage(2, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_2_3 = new MapTileBuilder(tile_2_3Frame)
            .withTileType(TileType.NOT_PASSABLE);

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
            .withTileType(TileType.NOT_PASSABLE);

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
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_4_2);

        Frame tile_4_3Frame = new FrameBuilder(getSubImage(4, 3))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_3 = new MapTileBuilder(tile_4_3Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_4_3);

        Frame tile_4_4Frame = new FrameBuilder(getSubImage(4, 4))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_4 = new MapTileBuilder(tile_4_4Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_4_4);

        Frame tile_4_5Frame = new FrameBuilder(getSubImage(4, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_4_5 = new MapTileBuilder(tile_4_5Frame)
            .withTileType(TileType.NOT_PASSABLE);

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
            .withTileType(TileType.NOT_PASSABLE);

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
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_8_4);

        Frame tile_8_5Frame = new FrameBuilder(getSubImage(8, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_5 = new MapTileBuilder(tile_8_5Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_8_5);

        Frame tile_8_6Frame = new FrameBuilder(getSubImage(8, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_6 = new MapTileBuilder(tile_8_6Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_8_6);

        Frame tile_8_7Frame = new FrameBuilder(getSubImage(8, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_8_7 = new MapTileBuilder(tile_8_7Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_8_7);

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
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_9_4);

        Frame tile_9_5Frame = new FrameBuilder(getSubImage(9, 5))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_5 = new MapTileBuilder(tile_9_5Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_9_5);

        Frame tile_9_6Frame = new FrameBuilder(getSubImage(9, 6))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_6 = new MapTileBuilder(tile_9_6Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_9_6);

        Frame tile_9_7Frame = new FrameBuilder(getSubImage(9, 7))
            .withScale(tileScale)
            .build();

        MapTileBuilder tile_9_7 = new MapTileBuilder(tile_9_7Frame)
            .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(tile_9_7);

        return mapTiles;
    }
    
}
