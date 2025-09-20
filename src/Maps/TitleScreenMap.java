package Maps;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.ScreenManager;
import GameObject.ImageEffect;
import GameObject.Sprite;
import Level.Map;
import Tilesets.CommonTileset;
import Utils.Colors;
import Utils.Point;

// Represents the map that is used as a background for the main menu and credits menu screen
public class TitleScreenMap extends Map {

    private Sprite cat;

    public TitleScreenMap() {
        super("sprint_one_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(50, 50).getLocation();
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // we gotta center the title screen on the hive
        Point hiveLocation = getMapTile(63, 63).getLocation();

        // center the camera there
        int camX = (int) (hiveLocation.x - (ScreenManager.getScreenWidth() / 2));
        int camY = (int) (hiveLocation.y- (ScreenManager.getScreenHeight() / 2));
        camera.moveX(camX - camera.getX());
        camera.moveY(camY - camera.getY());

        super.draw(graphicsHandler);
    }
}
