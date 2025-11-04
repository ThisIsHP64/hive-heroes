package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;
import java.util.HashMap;

public class Hut extends NPC {

    private static final int TILE_W = 512;
    private static final int TILE_H = 512;

    private static final float SCALE = 1f;

    public Hut(int id, Point location) {
        super(
                id,
                location.x,
                location.y,
                new SpriteSheet(ImageLoader.load("Hut.png"), TILE_W, TILE_H, 0),
                "HUT"
        );
        System.out.println("Hut spawned at " + location.x + ", " + location.y);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> map = new HashMap<>();
        map.put("HUT", new Frame[]{
                new FrameBuilder(spriteSheet.getSprite(0, 0), 9999)
                    .withBounds(60, 180, 380, 300)
                    .withScale(SCALE)
                    .build()
        });
        return map;
    }

    @Override
    public void update(Player player) {
    }
}
