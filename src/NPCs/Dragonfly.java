package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import java.util.HashMap;

public class Dragonfly extends NPC {
    private int totalAmountMoved = 0;
    private Direction direction = Direction.RIGHT;
    private float speed = 1;
    
    public Dragonfly(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("DragonFly.png"), 24, 24), "WALK_RIGHT");
    }

    // this code makes the bug npc walk back and forth (left to right)
    @Override
    public void performAction(Player player) {
        // if bug has not yet moved 90 pixels in one direction, move bug forward
        float amountMoved = 0;
        switch (direction) {
            case LEFT:
            amountMoved = moveXHandleCollision(-speed);
            break;
            case RIGHT:
            amountMoved = moveXHandleCollision(speed);
            break;
        }

        totalAmountMoved += Math.abs(amountMoved);

        // else if bug has already moved 90 pixels in one direction, flip the bug's direction
        if (totalAmountMoved >= 5000) {
            totalAmountMoved = 0;
            switch (direction) {
                case RIGHT:
                    direction = Direction.RIGHT;
                    break;
                case LEFT:
                    direction = Direction.LEFT;
                    break;
            }
        }

        // based off of the bugs current walking direction, set its animation to match
            switch (direction) {
                case RIGHT:
                    currentAnimationName = "WALK_RIGHT";
                    break;
                case LEFT:
                    currentAnimationName = "WALK_LEFT";
                    break;
            }
        }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("STAND_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(2)
                    .withBounds(0,0, 24, 24)
                    .build()
            });
            put("STAND_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                    .withScale(2)
                    .withBounds(0,0, 24, 24)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .build()
           });
            put("WALK_LEFT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                        .withScale(2)
                    .withBounds(0,0, 24, 24)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                        .withScale(2)
                    .withBounds(0,0, 24, 24)
                        .build()
            });
            put("WALK_RIGHT", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                        .withScale(2)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0,0, 24, 24)
                        .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                        .withScale(2)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(0,0, 24, 24)
                        .build()
            });
        }};
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
