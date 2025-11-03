package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;
import java.util.HashMap;

public class Cat extends NPC {

    private static final int SPRITE_WIDTH  = 32;
    private static final int SPRITE_HEIGHT = 32;
    private static final float SCALE = 3f;

    public Cat(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("Cat_Idle.png"), SPRITE_WIDTH, SPRITE_HEIGHT, 0),
            "IDLE"
        );

        // which animation to play
        currentAnimationName = "IDLE";

        // OPTIONAL: nudge so it sits nicely on the tile; tweak or remove if not needed
        setX(getX() - (SPRITE_WIDTH * SCALE / 2f));
        setY(getY() - (SPRITE_HEIGHT * SCALE));

        System.out.println("🐾 Cat spawned at " + getX() + ", " + getY());
    }

    @Override
    public void update(Player player) {
        // this advances the animation frames!
        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet sheet) {
        HashMap<String, Frame[]> map = new HashMap<>();

        // Idle tail/blink loop — adjust the (row,col) indices to match your sheet
        Frame[] idle = new Frame[] {
            new FrameBuilder(sheet.getSprite(0, 0), 14).withScale(SCALE).build(),
            new FrameBuilder(sheet.getSprite(0, 1), 14).withScale(SCALE).build(),
            new FrameBuilder(sheet.getSprite(0, 2), 14).withScale(SCALE).build(),
            new FrameBuilder(sheet.getSprite(1, 0), 14).withScale(SCALE).build(),
            new FrameBuilder(sheet.getSprite(1, 1), 14).withScale(SCALE).build(),
            new FrameBuilder(sheet.getSprite(1, 2), 14).withScale(SCALE).build(),
            new FrameBuilder(sheet.getSprite(2, 0), 14).withScale(SCALE).build(),
            new FrameBuilder(sheet.getSprite(2, 1), 14).withScale(SCALE).build(),

        };

        map.put("IDLE", idle);
        return map;
    }
}
