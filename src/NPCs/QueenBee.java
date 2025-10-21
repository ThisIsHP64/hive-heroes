package NPCs;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

public class QueenBee extends NPC {

    public QueenBee(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("queenBee.png"), 64, 64, 0), "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                        .withScale(3)
                        .withBounds(7, 13, 10, 10)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                        .withScale(3)
                        .withBounds(7, 13, 10, 10)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                        .withScale(3)
                        .withBounds(7, 13, 10, 10)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                        .withScale(3)
                        .withBounds(7, 13, 10, 10)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                        .withScale(3)
                        .withBounds(7, 13, 10, 10)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 5), 8)
                        .withScale(3)
                        .withBounds(7, 13, 10, 10)
                        .build(),
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
    
}
