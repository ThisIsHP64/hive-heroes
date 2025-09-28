package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.Player;
import Utils.Direction;

import java.util.HashMap;

public class Bee extends Player {

    private static final int TILE = 64;      // frames are 64x64
    private static final float SCALE = 2.5f; // resize bee (2.0–3.0)

    // row mapping in BOTH sheets (0-based)
    private static final int ROW_UP    = 0;
    private static final int ROW_LEFT  = 1;
    private static final int ROW_RIGHT = 2;
    private static final int ROW_DOWN  = 3;

    public Bee(float x, float y) {
        // gutter=0 (no spacing in your sheets)
        super(new SpriteSheet(ImageLoader.load("Bee_Walk.png"), TILE, TILE, 0),
              x, y,
              "STAND_DOWN");

        // === CONTROLS: WASD ===
        MOVE_LEFT_KEY  = Key.A;
        MOVE_RIGHT_KEY = Key.D;
        MOVE_UP_KEY    = Key.W;
        MOVE_DOWN_KEY  = Key.S;

        // movement speed
        walkSpeed = 10f;
    }

    @Override
    public void update() {
        super.update();
    }

    // decide which animation to play
    @Override
    protected void handlePlayerAnimation() {
        boolean walking =
                (currentWalkingYDirection != Direction.NONE) ||
                (currentWalkingXDirection != Direction.NONE);

        if (!walking) {
            // idle: hover in last facing direction
            if (lastWalkingYDirection == Direction.UP) {
                currentAnimationName = "STAND_UP";
                facingDirection = Direction.UP;
            } else if (lastWalkingYDirection == Direction.DOWN) {
                currentAnimationName = "STAND_DOWN";
                facingDirection = Direction.DOWN;
            } else if (lastWalkingXDirection == Direction.RIGHT) {
                currentAnimationName = "STAND_RIGHT";
                facingDirection = Direction.RIGHT;
            } else if (lastWalkingXDirection == Direction.LEFT) {
                currentAnimationName = "STAND_LEFT";
                facingDirection = Direction.LEFT;
            } else {
                currentAnimationName = "STAND_DOWN";
                facingDirection = Direction.DOWN;
            }
            return;
        }

        // walking: check vertical first
        if (currentWalkingYDirection == Direction.UP) {
            currentAnimationName = "WALK_UP";
            facingDirection = Direction.UP;
        } else if (currentWalkingYDirection == Direction.DOWN) {
            currentAnimationName = "WALK_DOWN";
            facingDirection = Direction.DOWN;
        } else if (currentWalkingXDirection == Direction.RIGHT) {
            currentAnimationName = "WALK_RIGHT";
            facingDirection = Direction.RIGHT;
        } else if (currentWalkingXDirection == Direction.LEFT) {
            currentAnimationName = "WALK_LEFT";
            facingDirection = Direction.LEFT;
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet walkSheet) {
        SpriteSheet idleSheet =
            new SpriteSheet(ImageLoader.load("Bee_Idle.png"), TILE, TILE, 0);

        // hitbox; adjust if collisions feel off
        int hbX = Math.round(22 * SCALE), hbY = Math.round(42 * SCALE);
        int hbW = Math.round(20 * SCALE), hbH = Math.round(14 * SCALE);

        HashMap<String, Frame[]> map = new HashMap<>();

        // === IDLE hover (cols 0–3 for each row) ===
        map.put("STAND_UP", new Frame[] {
            new FrameBuilder(idleSheet.getSprite(ROW_UP, 0), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_UP, 1), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_UP, 2), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_UP, 3), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build()
        });
        map.put("STAND_LEFT", new Frame[] {
            new FrameBuilder(idleSheet.getSprite(ROW_LEFT, 0), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_LEFT, 1), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_LEFT, 2), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_LEFT, 3), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build()
        });
        map.put("STAND_RIGHT", new Frame[] {
            new FrameBuilder(idleSheet.getSprite(ROW_RIGHT, 0), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_RIGHT, 1), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_RIGHT, 2), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_RIGHT, 3), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build()
        });
        map.put("STAND_DOWN", new Frame[] {
            new FrameBuilder(idleSheet.getSprite(ROW_DOWN, 0), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_DOWN, 1), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_DOWN, 2), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(idleSheet.getSprite(ROW_DOWN, 3), 7).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build()
        });

        // === WALK (rows 0–3 = UP/LEFT/RIGHT/DOWN) ===
        map.put("WALK_UP", new Frame[] {
            new FrameBuilder(walkSheet.getSprite(ROW_UP, 0), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_UP, 1), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_UP, 2), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_UP, 3), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build()
        });
        map.put("WALK_LEFT", new Frame[] {
            new FrameBuilder(walkSheet.getSprite(ROW_LEFT, 0), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_LEFT, 1), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_LEFT, 2), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_LEFT, 3), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build()
        });
        map.put("WALK_RIGHT", new Frame[] {
            new FrameBuilder(walkSheet.getSprite(ROW_RIGHT, 0), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_RIGHT, 1), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_RIGHT, 2), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_RIGHT, 3), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build()
        });
        map.put("WALK_DOWN", new Frame[] {
            new FrameBuilder(walkSheet.getSprite(ROW_DOWN, 0), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_DOWN, 1), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_DOWN, 2), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build(),
            new FrameBuilder(walkSheet.getSprite(ROW_DOWN, 3), 14).withScale(SCALE).withBounds(hbX,hbY,hbW,hbH).build()
        });

        return map;
    }
}
