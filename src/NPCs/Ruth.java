package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;
import java.util.HashMap;

public class Ruth extends NPC {

    public Ruth(int id, Point location) {
        super(id, location.x, location.y,
              new SpriteSheet(ImageLoader.load("dungeon_npc8.png"), 64, 64, 0),
              "STAND");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            // Single static frame
            put("STAND", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 0)
                    .withScale(1.5f)
                    .withBounds(0, 0, 64, 64)
                    .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}