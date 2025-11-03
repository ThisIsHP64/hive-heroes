package Flowers;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Utils.Point;
import java.util.HashMap;

// This class is for the walrus NPC
public class Yarrow extends Flower {

    public Yarrow(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Yarrow.png"), 20, 20), "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(0, 0, 20, 20)
                            .build()
            });
        }};
    }

    public java.awt.Rectangle getHitbox() {
        Rectangle bounds = getBounds();
        
        int w = bounds.getWidth();
        int h = bounds.getHeight();

        int x = (int) getX();
        int y = (int) getY();
        
        return new java.awt.Rectangle(x, y, w, h);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
