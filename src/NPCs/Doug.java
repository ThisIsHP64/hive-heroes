package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;
import java.util.HashMap;

public class Doug extends NPC {

    public Doug(int id, Point location) {
        super(id, location.x, location.y,
              new SpriteSheet(ImageLoader.load("dungeon_npc3.png"), 64, 64, 0),
              "IDLE");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            // 2-frame idle animation (row 0 and row 1)
            put("IDLE", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                    .withScale(1.5f)
                    .withBounds(0, 0, 64, 64)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 14)
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