package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

// simple spider NPC using spider05.png; one idle frame + a body hitbox
public class Spider extends NPC {

    private static final int TILE  = 32;     // spider05.png frames are 32x32
    private static final float SCALE = 2.0f; // integer scale keeps pixels crisp

    // pick the cell we want to show for now (col,row)
    private static final int IDLE_COL = 0;
    private static final int IDLE_ROW = 0;

    // these match the FrameBuilder.withBounds(...) below so our hitbox lines up
    private static final int HBX = 4;   // body box x offset in sprite pixels (pre-scale)
    private static final int HBY = 10;  // body box y offset
    private static final int HBW = 7;  // body box width
    private static final int HBH = 7;  // body box height

    public Spider(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            // magenta color-key via load(...) (same as Walrus)
            new SpriteSheet(ImageLoader.load("spider05.png"), TILE, TILE),
            "IDLE");
            
        System.out.println("[Spider05] spawned at (" + location.x + "," + location.y + ")");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> animations = new HashMap<>();

        Frame idle = new FrameBuilder(spriteSheet.getSprite(IDLE_COL, IDLE_ROW))
                .withScale(SCALE)
                .withBounds(Math.round(HBX * SCALE), Math.round(HBY * SCALE),
                            Math.round(HBW * SCALE), Math.round(HBH * SCALE))
                .build();

        animations.put("IDLE", new Frame[] { idle });
        return animations;
    }

    // expose a plain Rectangle so the screen can compare against Bee's sting box
    public java.awt.Rectangle getHitbox() {
        int x = Math.round(getX()) + Math.round(HBX * SCALE);
        int y = Math.round(getY()) + Math.round(HBY * SCALE);
        int w = Math.round(HBW * SCALE);
        int h = Math.round(HBH * SCALE);
        return new java.awt.Rectangle(x, y, w, h);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
