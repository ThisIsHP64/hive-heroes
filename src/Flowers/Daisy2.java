package Flowers;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Utils.Point;

public class Daisy2 extends Flower {

    public Daisy2(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("DaisyOUTLINED.png"), 
                32, 32), "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                put("STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0)) // frame 1 for 200ms
                                .withScale(1.5f)
                                .withBounds(0, 0, 32, 32)
                                .build(),
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
