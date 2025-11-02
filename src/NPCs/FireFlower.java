package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;
import java.util.HashMap;

public class FireFlower extends NPC {

    public FireFlower(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("FireFlower.png"), 20, 20),
                "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                // Animate the sunflower gently swaying
                put("STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 30) 
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 30) 
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 30) 
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 0), 30) 
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 1), 30) 
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 2), 30) 
                                .withScale(3)
                                .withBounds(0, 0, 20, 20)
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
        super.draw(graphicsHandler);
    }
}
