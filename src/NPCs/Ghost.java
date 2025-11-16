package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;
import java.util.HashMap;

public class Ghost extends NPC {

    private static final int SPRITE_WIDTH = 32;
    private static final int SPRITE_HEIGHT = 32;
    private static final float SCALE = 2f;

    public Ghost(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("ghostSheet.png"), SPRITE_WIDTH, SPRITE_HEIGHT, 0),
            "IDLE"
        );

        currentAnimationName = "IDLE";

        // Adjust position so it’s centered nicely on the tile
        setX(getX() - (SPRITE_WIDTH * SCALE / 2f));
        setY(getY() - (SPRITE_HEIGHT * SCALE));

        System.out.println("Ghost spawned at " + getX() + ", " + getY());
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
            new FrameBuilder(sheet.getSprite(0, 0), 15).withScale(SCALE).withBounds(8, 18, 16, 10).build(),
            new FrameBuilder(sheet.getSprite(1, 0), 15).withScale(SCALE).withBounds(8, 18, 16, 10).build(),
            new FrameBuilder(sheet.getSprite(0, 1), 15).withScale(SCALE).withBounds(8, 18, 16, 10).build(),
            new FrameBuilder(sheet.getSprite(1, 1), 15).withScale(SCALE).withBounds(8, 18, 16, 10).build()
        };

        map.put("IDLE", idle);
        map.put("STAND_LEFT", idle);
        map.put("STAND_RIGHT", idle);
        return map;
    }
}
