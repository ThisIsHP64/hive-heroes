package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import java.util.HashMap;

public class Butterfly extends NPC {
    private int totalAmountMoved = 0;
    private Direction direction = Direction.UP;
    private float speed = 1;
    
    public Butterfly(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("Butterfly.png"), 24, 24), "WALK_UP");
    }

    // this code makes the bug npc walk back and forth (left to right)
    @Override
    public void performAction(Player player) {
        // if bug has not yet moved 90 pixels in one direction, move bug forward
        float amountMoved = 0;
        switch (direction) {
            case UP:
                amountMoved = moveYHandleCollision(-speed);
                break;
            case DOWN:
                amountMoved = moveYHandleCollision(speed);
                break;
        }

        totalAmountMoved += Math.abs(amountMoved);

        // else if bug has already moved 90 pixels in one direction, flip the bug's direction
        if (totalAmountMoved >= 5000) {
            totalAmountMoved = 0;
            switch (direction) {
                case UP:
                    direction = Direction.DOWN;
                    break;
                case DOWN:
                    direction = Direction.UP;
                    break;
            }
        }

        // based off of the bugs current walking direction, set its animation to match
            switch (direction) {
                case UP:
                    currentAnimationName = "WALK_UP";
                    break;
                case DOWN:
                    currentAnimationName = "WALK_DOWN";
                    break;
            }
        }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("WALK_DOWN", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                        .withScale(2)
                        .withBounds(3, 5, 18, 7)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                        .withScale(2)
                        .withBounds(3, 5, 18, 7)
                        .build()
            });
            put("WALK_UP", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                        .withScale(2)
                        .withBounds(3, 5, 18, 7)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                        .withScale(2)
                        .withBounds(3, 5, 18, 7)
                        .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
