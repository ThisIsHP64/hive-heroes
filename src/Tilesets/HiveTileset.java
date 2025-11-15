package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

// This class represents a "common" tileset of standard tiles defined in the CommonTileset.png file
public class HiveTileset extends Tileset {

    public HiveTileset() {
        super(ImageLoader.load("HiveTiles.png"), 16, 16, 3f, 1);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // hive wall 1 
        Frame hiveWall1Frame = new FrameBuilder(getSubImage(0, 0))
        .withScale(tileScale)
        .build();

        MapTileBuilder hivewall1Tile = new MapTileBuilder(hiveWall1Frame);

        mapTiles.add(hivewall1Tile);

        // hive wall 2
        Frame hiveWall2Frame = new FrameBuilder(getSubImage(0, 1))
        .withScale(tileScale)
        .build();

        MapTileBuilder hivewall2Tile = new MapTileBuilder(hiveWall2Frame);

        mapTiles.add(hivewall2Tile);

        // hive wall 3
        Frame hiveWall3Frame = new FrameBuilder(getSubImage(0, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall3Tile = new MapTileBuilder(hiveWall3Frame);

        mapTiles.add(hivewall3Tile);

        // path 1
        Frame path1Frame = new FrameBuilder(getSubImage(0, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder path1Tile = new MapTileBuilder(path1Frame);

        mapTiles.add(path1Tile);

        // path 2
        Frame path2Frame = new FrameBuilder(getSubImage(0, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder path2Tile = new MapTileBuilder(path2Frame);       

        mapTiles.add(path2Tile);

        // hive wall 4
        Frame hiveWall4Frame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall4Tile = new MapTileBuilder(hiveWall4Frame);

        mapTiles.add(hivewall4Tile);

        // hive wall 5
        Frame hiveWall5Frame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall5Tile = new MapTileBuilder(hiveWall5Frame);

        mapTiles.add(hivewall5Tile);
        
        // hive wall 6
        Frame hiveWall6Frame = new FrameBuilder(getSubImage(1, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall6Tile = new MapTileBuilder(hiveWall6Frame);

        mapTiles.add(hivewall6Tile);

        // path 3
        Frame path3Frame = new FrameBuilder(getSubImage(1, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder path3Tile = new MapTileBuilder(path3Frame);

        mapTiles.add(path3Tile);

        // path 4
        Frame path4Frame = new FrameBuilder(getSubImage(1, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder path4Tile = new MapTileBuilder(path4Frame);       

        mapTiles.add(path4Tile);

        // hive wall 7
        Frame hiveWall7Frame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall7Tile = new MapTileBuilder(hiveWall7Frame);

        mapTiles.add(hivewall7Tile);

        // hive wall 8
        Frame hiveWall8Frame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall8Tile = new MapTileBuilder(hiveWall8Frame);

        mapTiles.add(hivewall8Tile);
        
        // hive wall 9
        Frame hiveWall9Frame = new FrameBuilder(getSubImage(2, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall9Tile = new MapTileBuilder(hiveWall9Frame);

        mapTiles.add(hivewall9Tile);

        // entrance 1
        Frame entrance1Frame = new FrameBuilder(getSubImage(2, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder entrance1Tile = new MapTileBuilder(entrance1Frame);

        mapTiles.add(entrance1Tile);

        // entrance 2
        Frame entrance2Frame = new FrameBuilder(getSubImage(2, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder entrance2Tile = new MapTileBuilder(entrance2Frame);       

        mapTiles.add(entrance2Tile);

        // hive wall 10
        Frame hiveWall10Frame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall10Tile = new MapTileBuilder(hiveWall10Frame);

        mapTiles.add(hivewall10Tile);

        // hive wall 11
        Frame hiveWall11Frame = new FrameBuilder(getSubImage(3, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall11Tile = new MapTileBuilder(hiveWall11Frame);

        mapTiles.add(hivewall11Tile);
        
        // hive wall 12
        Frame hiveWall12Frame = new FrameBuilder(getSubImage(3, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder hivewall12Tile = new MapTileBuilder(hiveWall12Frame);

        mapTiles.add(hivewall12Tile);

        // left boarder 1
        Frame leftBoarder1Frame = new FrameBuilder(getSubImage(3, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftBoarder1Tile = new MapTileBuilder(leftBoarder1Frame);

        mapTiles.add(leftBoarder1Tile);

        // right boarder 1
        Frame rightBoarder1Frame = new FrameBuilder(getSubImage(3, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightBoarder1Tile = new MapTileBuilder(rightBoarder1Frame);       

        mapTiles.add(rightBoarder1Tile);

        // chair spot 1
        Frame chairSpot1Frame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder chairSpot1Tile = new MapTileBuilder(chairSpot1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(chairSpot1Tile);

        // chair spot 2
        Frame chairSpot2Frame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder chairSpot2Tile = new MapTileBuilder(chairSpot2Frame);

        mapTiles.add(chairSpot2Tile);

        // right boarder 4
        Frame rightBoarder4Frame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightBoarder4Tile = new MapTileBuilder(rightBoarder4Frame);

        mapTiles.add(rightBoarder4Tile);

        // left boarder 2
        Frame leftBoarder2Frame = new FrameBuilder(getSubImage(4, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftBoarder2Tile = new MapTileBuilder(leftBoarder2Frame);

        mapTiles.add(leftBoarder2Tile);

        // right boarder 2
        Frame rightBoarder2Frame = new FrameBuilder(getSubImage(4, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightBoarder2Tile = new MapTileBuilder(rightBoarder2Frame);       

        mapTiles.add(rightBoarder2Tile);

        // right boarder 7
        Frame rightBoarder7Frame = new FrameBuilder(getSubImage(5, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightBoarder7Tile = new MapTileBuilder(rightBoarder7Frame);

        mapTiles.add(rightBoarder7Tile);

        // right boarder 6
        Frame rightBoarder6Frame = new FrameBuilder(getSubImage(5, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightBoarder6Tile = new MapTileBuilder(rightBoarder6Frame);

        mapTiles.add(rightBoarder6Tile);
        
        // right boarder 5
        Frame rightBoarder5Frame = new FrameBuilder(getSubImage(5, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightBoarder5Tile = new MapTileBuilder(rightBoarder5Frame);

        mapTiles.add(rightBoarder5Tile);

        // left boarder 3
        Frame leftBoarder3Frame = new FrameBuilder(getSubImage(5, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder leftBoarder3Tile = new MapTileBuilder(leftBoarder3Frame);

        mapTiles.add(leftBoarder3Tile);

        // right boarder 3
        Frame rightBoarder3Frame = new FrameBuilder(getSubImage(5, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder rightBoarder3Tile = new MapTileBuilder(rightBoarder3Frame);       

        mapTiles.add(rightBoarder3Tile);
        


        return mapTiles;
    }
}
