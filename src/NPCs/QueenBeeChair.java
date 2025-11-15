package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.awt.*;
import java.util.HashMap;

// This class is for the bighive NPC
public class QueenBeeChair extends NPC {

    public QueenBeeChair(int id, Point location) {
        super(id, location.x, location.y,new FrameBuilder(ImageLoader.load("QueenBeeChair.png"))
                    .withScale(0.1f)
                    .build()
        );
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("DEFAULT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(0, 0, 43, 43)
                            .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    public Rectangle getHitbox() {
        GameObject.Rectangle bounds = getBounds();

        int w = bounds.getWidth();
        int h = bounds.getHeight();

        int x = (int) getX();
        int y = (int) getY();

        return new java.awt.Rectangle(x, y, w, h);
    }
}
