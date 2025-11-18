package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;
import java.util.HashMap;

public class BrokenHut extends NPC {

    private static final int SPRITE_WIDTH = 64;
    private static final int SPRITE_HEIGHT = 64;
    private static final float SCALE = 3f;

    public BrokenHut(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("BrokenHut.png"), SPRITE_WIDTH, SPRITE_HEIGHT, 0),
            "GRAVE"
        );
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> map = new HashMap<>();

        // single static frame
        map.put("GRAVE", new Frame[]{
            new FrameBuilder(spriteSheet.getSprite(0, 0), 9999)
                .withScale(SCALE)
                .withBounds(8, 20, 40, 35)
                .build()
        });

        return map;
    }

    @Override
    public void update(Player player) {
        super.update(player);
        // optional interaction or particle effect can go here
    }
}