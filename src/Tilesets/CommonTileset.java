package Tilesets;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import Level.TileType;
import Level.Tileset;
import java.util.ArrayList;

// This class represents a "common" tileset of standard tiles defined in the CommonTileset.png file
public class CommonTileset extends Tileset {

    public CommonTileset() {
        super(ImageLoader.load("Sprint1tiles.png"), 16, 16, 3f, 1);
    }

    @Override
    public ArrayList<MapTileBuilder> defineTiles() {
        ArrayList<MapTileBuilder> mapTiles = new ArrayList<>();

        // full flower 
        Frame fullFlowerFrame = new FrameBuilder(getSubImage(0, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder fullFlowerTile = new MapTileBuilder(fullFlowerFrame);

        mapTiles.add(fullFlowerTile);

        // bear sign 1
        Frame bearSign1Frame = new FrameBuilder(getSubImage(3, 0))
                .withScale(tileScale)
                .withBounds(1, 2, 14, 14)
                .build();

        MapTileBuilder bearSign1Tile = new MapTileBuilder(bearSign1Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(bearSign1Tile);

        // path1
        Frame path1Frame = new FrameBuilder(getSubImage(0, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder path1Tile = new MapTileBuilder(path1Frame);

        mapTiles.add(path1Tile);

        // rock
        Frame rockFrame = new FrameBuilder(getSubImage(3, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder rockTile = new MapTileBuilder(rockFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(rockTile);

        // path 2
        Frame path2Frame = new FrameBuilder(getSubImage(2, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder path2Tile = new MapTileBuilder(path2Frame);       

        mapTiles.add(path2Tile);

        // left end branch
        Frame leftEndBranchFrame = new FrameBuilder(getSubImage(2, 4))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder leftEndBranchTile = new MapTileBuilder(leftEndBranchFrame)
                .withTopLayer(leftEndBranchFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(leftEndBranchTile);

        // right end branch
        Frame rightEndBranchFrame = new FrameBuilder(getSubImage(2, 4))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .build();

        MapTileBuilder rightEndBranchTile = new MapTileBuilder(rightEndBranchFrame)
                .withTopLayer(rightEndBranchFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(rightEndBranchTile);
        
        // tree trunk
        Frame treeTrunkFrame = new FrameBuilder(getSubImage(1, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder treeTrunkTile = new MapTileBuilder(treeTrunkFrame)
                .withTopLayer(treeTrunkFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(treeTrunkTile);

        // tree top leaves with apples
        Frame treeTopAppleleavesFrame = new FrameBuilder(getSubImage(1, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder treeTopAppleleavesTile = new MapTileBuilder(treeTopAppleleavesFrame)
                .withTopLayer(treeTopAppleleavesFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(treeTopAppleleavesTile);
        
        // lavender
        Frame[] lavenderFrames = new Frame[] {
                new FrameBuilder(getSubImage(1, 2), 65)
                    .withScale(tileScale)
                    .build(),
                new FrameBuilder(getSubImage(1, 3), 65)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(1, 2), 65)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(1, 4), 65)
                        .withScale(tileScale)
                        .build()
        };

        MapTileBuilder lavenderTile = new MapTileBuilder(lavenderFrames);

        mapTiles.add(lavenderTile);

        // sunflower
        Frame[] sunflowerFrames = new Frame[] {
                new FrameBuilder(getSubImage(0, 2), 65)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(0, 3), 65)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(0, 2), 65)
                        .withScale(tileScale)
                        .build(),
                new FrameBuilder(getSubImage(0, 4), 65)
                        .withScale(tileScale)
                        .build()
        };

        MapTileBuilder sunflowerTile = new MapTileBuilder(sunflowerFrames);

        mapTiles.add(sunflowerTile);

        // middle branch
        Frame middleBranchFrame = new FrameBuilder(getSubImage(2, 3))
                .withScale(tileScale)
                .withBounds(0, 6, 16, 4)
                .build();

        MapTileBuilder middleBranchTile = new MapTileBuilder(middleBranchFrame)
                .withTopLayer(middleBranchFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(middleBranchTile);

        // tree trunk bottom
        Frame treeTrunkBottomFrame = new FrameBuilder(getSubImage(2, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder treeTrunkBottomTile = new MapTileBuilder(treeTrunkBottomFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(treeTrunkBottomTile);

        // mushrooms
        Frame mushroomFrame = new FrameBuilder(getSubImage(2, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder mushroomTile = new MapTileBuilder(mushroomFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(mushroomTile);


        // grey rock
        Frame greyRockFrame = new FrameBuilder(getSubImage(3, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder greyRockTile = new MapTileBuilder(greyRockFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(greyRockTile);

        // bush
        Frame bushFrame = new FrameBuilder(getSubImage(3, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder bushTile = new MapTileBuilder(bushFrame)
                .withTileType(TileType.PASSABLE);

        mapTiles.add(bushTile);

        // branch with bee hive
        Frame branchBeeHiveFrame = new FrameBuilder(getSubImage(3, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder branchBeeHiveTile = new MapTileBuilder(branchBeeHiveFrame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(branchBeeHiveTile);

        // bear sign 2
        Frame bearSign2Frame = new FrameBuilder(getSubImage(4, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder bearSign2Tile = new MapTileBuilder(bearSign2Frame)
                .withTopLayer(bearSign2Frame)
                .withTileType(TileType.NOT_PASSABLE);

        mapTiles.add(bearSign2Tile);

        // path 3
        Frame path3Frame = new FrameBuilder(getSubImage(4, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder path3Tile = new MapTileBuilder(path3Frame)
                .withTopLayer(path3Frame);

        mapTiles.add(path3Tile);

        // path field 2
        Frame pathField2Frame = new FrameBuilder(getSubImage(4, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder pathField2Tile = new MapTileBuilder(pathField2Frame);

        mapTiles.add(pathField2Tile);

        // path field 3
        Frame pathField3Frame = new FrameBuilder(getSubImage(4, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder pathField3Tile = new MapTileBuilder(pathField3Frame);

        mapTiles.add(pathField3Tile);

        // path field 4
        Frame pathField4Frame = new FrameBuilder(getSubImage(4, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder pathField4Tile = new MapTileBuilder(pathField4Frame);

        mapTiles.add(pathField4Tile);

        // dark green plain grass
        Frame darkGreenplainGrassFrame = new FrameBuilder(getSubImage(5, 0))
                .withScale(tileScale)
                .build();

        MapTileBuilder darkGreenplainGrassTile = new MapTileBuilder(darkGreenplainGrassFrame);

        mapTiles.add(darkGreenplainGrassTile);

        // path 4
        Frame path4Frame = new FrameBuilder(getSubImage(5, 1))
                .withScale(tileScale)
                .build();

        MapTileBuilder path4Tile = new MapTileBuilder(path4Frame);

        mapTiles.add(path4Tile);

        // field path 1
        Frame fieldPath1Frame = new FrameBuilder(getSubImage(5, 2))
                .withScale(tileScale)
                .build();

        MapTileBuilder fieldPath1Tile = new MapTileBuilder(fieldPath1Frame);

        mapTiles.add(fieldPath1Tile);

        // plain grass 2
        Frame plainGrass2Frame = new FrameBuilder(getSubImage(5, 3))
                .withScale(tileScale)
                .build();

        MapTileBuilder plainGrass2Tile = new MapTileBuilder(plainGrass2Frame);

        mapTiles.add(plainGrass2Tile);

        // field grass 5
        Frame fieldGrass5Frame = new FrameBuilder(getSubImage(5, 4))
                .withScale(tileScale)
                .build();

        MapTileBuilder fieldGrass5Tile = new MapTileBuilder(fieldGrass5Frame);

        mapTiles.add(fieldGrass5Tile);
        


        return mapTiles;
    }
}
