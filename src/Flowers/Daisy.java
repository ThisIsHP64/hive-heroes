package Flowers;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Utils.Point;
import java.util.HashMap;

public class Daisy extends Flower {

    public Daisy(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("PoppyAndDaisy.png"), 20, 20),
                "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                // Animate the sunflower gently swaying
                put("STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(1, 0), 30) // frame 1 for 200ms
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 1), 30) // frame 2
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 2), 30) // frame 3
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
                                .build()
                });
            }
        };
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
