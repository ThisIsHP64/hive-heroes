package Portals;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

public class Portal extends NPC {

    public Portal(int id, Point location) {
        super(id, location.x, location.y, 
            new SpriteSheet(ImageLoader.load("portal1.png"), 
            83, 68, 0), 
            "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                        .withScale(2)
                        .withBounds(0, 0, 70, 70)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                        .withScale(2)
                        .withBounds(0, 0, 70, 70)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                        .withScale(2)
                        .withBounds(0, 0, 70, 70)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                        .withScale(2)
                        .withBounds(0, 0, 70, 70)
                        .build(),
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
