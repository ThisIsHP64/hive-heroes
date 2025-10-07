package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;
import java.util.HashMap;

public class RareSunflowerwithFlowers extends NPC {

    private static final int TILE = 32; // spider05.png frames are 32x32
    private static final float SCALE = 2.0f; // integer scale keeps pixels crisp

    // pick the cell we want to show for now (col,row)
    private static final int IDLE_COL = 0;
    private static final int IDLE_ROW = 0;

    // these match the FrameBuilder.withBounds(...) below so our hitbox lines up
    private static final int HBX = 4; // body box x offset in sprite pixels (pre-scale)
    private static final int HBY = 10; // body box y offset
    private static final int HBW = 7; // body box width
    private static final int HBH = 7; // body box height

    public RareSunflowerwithFlowers(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("RareSunflowerwithFlowers.png"), 16, 16),
                "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                // Animate the sunflower gently swaying
                put("STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 65) // frame 1 for 200ms
                                .withScale(3)
                                .withBounds(0, 0, 11, 7)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 65) // frame 2
                                .withScale(3)
                                .withBounds(0, 0, 11, 7)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 65) // frame 3
                                .withScale(3)
                                .withBounds(0, 0, 11, 7)
                                .build()
                });
            }
        };
    }

    public java.awt.Rectangle getHitbox() {
        int x = (int) getX();
        int y = (int) getY();
        int w = (4);
        int h = (4);
        return new java.awt.Rectangle(x, y, w, h);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {

        // uncomment to show hitbox
        Rectangle bounds = getBounds();
        float camX = map.getCamera().getX();
        float camY = map.getCamera().getY();

        int screenX = (int) (bounds.getX() - camX);
        int screenY = (int) (bounds.getY() - camY);

        graphicsHandler.drawRectangle(
                screenX,
                screenY,
                bounds.getWidth(),
                bounds.getHeight(),
                java.awt.Color.GREEN,
                5);

        super.draw(graphicsHandler);
    }
}
