package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;
import java.util.HashMap;

public class DeadDeer extends NPC {

    private static final int SPRITE_WIDTH = 128;  // each frame width
    private static final int SPRITE_HEIGHT = 128; // each frame height
    private static final float SCALE = 1.5f;      // adjust as needed

    public DeadDeer(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("Deer1.png"), SPRITE_WIDTH, SPRITE_HEIGHT, 0),
            "DEAD"
        );

        currentAnimationName = "DEAD";

        // Adjust spawn offset so it sits on the ground properly
        setX(getX() - (SPRITE_WIDTH * SCALE / 2f));
        setY(getY() - (SPRITE_HEIGHT * SCALE / 2f));

        System.out.println("🦌 Dead Deer spawned at " + getX() + ", " + getY());
    }

    @Override
    public void update(Player player) {
        // keeps animation cycling
        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet sheet) {
        HashMap<String, Frame[]> map = new HashMap<>();

        Frame[] deadAnim = new Frame[]{
            new FrameBuilder(sheet.getSprite(0, 0), 15).withScale(SCALE).withBounds(20, 90, 90, 25).build(),
            new FrameBuilder(sheet.getSprite(1, 0), 15).withScale(SCALE).withBounds(20, 90, 90, 25).build(),
            new FrameBuilder(sheet.getSprite(2, 0), 15).withScale(SCALE).withBounds(20, 90, 90, 25).build(),
            new FrameBuilder(sheet.getSprite(0, 1), 15).withScale(SCALE).withBounds(20, 90, 90, 25).build(),
            new FrameBuilder(sheet.getSprite(1, 1), 15).withScale(SCALE).withBounds(20, 90, 90, 25).build(),
            new FrameBuilder(sheet.getSprite(2, 1), 15).withScale(SCALE).withBounds(20, 90, 90, 25).build()
        };

        map.put("DEAD", deadAnim);
        return map;
    }
}
