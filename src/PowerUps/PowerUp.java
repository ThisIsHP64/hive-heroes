package PowerUps;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;
import java.util.HashMap;

// This class is for the walrus NPC
public class PowerUp extends NPC {

    public PowerUp(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Powerup.png"), 24, 24), "IDLE");
        this.isUncollidable = true;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("IDLE", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3)
                            .withBounds(6, 6, 12, 12)
                            .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
