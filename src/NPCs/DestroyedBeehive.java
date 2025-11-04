package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;
import java.util.HashMap;

public class DestroyedBeehive extends NPC {

    private static final int TILE_W = 64;
    private static final int TILE_H = 64;
    private static final float SCALE = 2f;

    public DestroyedBeehive(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("DesBeehive.png"), TILE_W, TILE_H, 0),
            "HIVE"
        );
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> map = new HashMap<>();
        map.put("HIVE", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(0, 0), 9999)
                .withScale(SCALE)
                .withBounds(8, 20, 45, 35) // adjust for your hitbox size
                .build()
        });
        return map;
    }

    @Override
    public void update(Player player) {
        // Optional: Add interaction or dialogue later
    }
}
