package NPCs;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

public class QueenBee extends NPC {

    public QueenBee(int id, Point location) {
        super(id, location.x, location.y, 
            new SpriteSheet(ImageLoader.load("queen_bee.png"), 
            64, 64, 0), 
            "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                        .withScale(3)
                        .withBounds(7, 13, 48, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                        .withScale(3)
                        .withBounds(7, 13, 48, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                        .withScale(3)
                        .withBounds(7, 13, 48, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                        .withScale(3)
                        .withBounds(7, 13, 48, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                        .withScale(3)
                        .withBounds(7, 13, 48, 40)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                        .withScale(3)
                        .withBounds(7, 13, 48, 40)
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
