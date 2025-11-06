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

        currentAnimationName = "IDLE";

        setX(getX() - (SPRITE_WIDTH * SCALE / 2f));
        setY(getY() - (SPRITE_HEIGHT * SCALE));

        System.out.println(" spawned at " + getX() + ", " + getY());
    }

    @Override
    public void update(Player player) {
        if (currentAnimationName == null || getCurrentAnimation() == null)
            return;

        super.update(player);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet sheet) {
        HashMap<String, Frame[]> map = new HashMap<>();

        Frame[] idle = new Frame[] {
            new FrameBuilder(sheet.getSprite(0, 0), 14).withScale(SCALE).withBounds(7, 13, 11, 7).build(),
            new FrameBuilder(sheet.getSprite(0, 1), 14).withScale(SCALE).withBounds(7, 13, 11, 7).build(),
            new FrameBuilder(sheet.getSprite(0, 2), 14).withScale(SCALE).withBounds(7, 13, 11, 7).build(),
            new FrameBuilder(sheet.getSprite(1, 0), 14).withScale(SCALE).withBounds(7, 13, 11, 7).build(),
            new FrameBuilder(sheet.getSprite(1, 1), 14).withScale(SCALE).withBounds(7, 13, 11, 7).build(),
            new FrameBuilder(sheet.getSprite(1, 2), 14).withScale(SCALE).withBounds(7, 13, 11, 7).build(),
            new FrameBuilder(sheet.getSprite(2, 0), 14).withScale(SCALE).withBounds(7, 13, 11, 7).build(),
            new FrameBuilder(sheet.getSprite(2, 1), 14).withScale(SCALE).withBounds(7, 13, 11, 7).build(),
        };

        map.put("IDLE", idle);
        map.put("STAND_LEFT", idle);
        map.put("STAND_RIGHT", idle);
        return map;
    }
}
