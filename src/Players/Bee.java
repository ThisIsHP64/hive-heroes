package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
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

    // --- attack timing ---
    private static final int ATTACK_ACTIVE_MS   = 120;  // sting window
    private static final int ATTACK_COOLDOWN_MS = 10;  // delay before next attack

    private boolean attacking = false;
    private long attackStart = 0L;
    private long lastAttackEnd = 0L;

    private boolean prevSpaceDown = false;   // edge detection

    public Bee(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Bee_Walk.png"), TILE, TILE, 0),
              x, y,
              "STAND_DOWN");

        // === CONTROLS: WASD ===
        MOVE_LEFT_KEY  = Key.A;
        MOVE_RIGHT_KEY = Key.D;
        MOVE_UP_KEY    = Key.W;
        MOVE_DOWN_KEY  = Key.S;

        walkSpeed = 10f;
    }

    @Override
    public void update() {
        handleAttackInput();
        super.update();

        // end attack window
        if (attacking && System.currentTimeMillis() - attackStart > ATTACK_ACTIVE_MS) {
            attacking = false;
            lastAttackEnd = System.currentTimeMillis();
        }
    }

    private void handleAttackInput() {
        boolean canAttack = !attacking && (System.currentTimeMillis() - lastAttackEnd > ATTACK_COOLDOWN_MS);

        boolean spaceDown = Keyboard.isKeyDown(Key.SPACE);
        boolean justPressed = spaceDown && !prevSpaceDown;

        if (justPressed && canAttack) {
            attacking = true;
            attackStart = System.currentTimeMillis();
            currentAnimationName = "ATTACK_" + facingDirection.name();
        }

        prevSpaceDown = spaceDown;
    }

    public boolean isAttacking() {
        return attacking;
    }

    // rectangle hitbox in front of the bee while attacking
    public java.awt.Rectangle getAttackHitbox() {
        int w = Math.round(22 * SCALE);
        int h = Math.round(22 * SCALE);
        int baseX = Math.round(getX());
        int baseY = Math.round(getY());

        switch (facingDirection) {
            case UP:    return new java.awt.Rectangle(baseX + Math.round(20*SCALE), baseY + Math.round(4*SCALE),  w, h);
            case DOWN:  return new java.awt.Rectangle(baseX + Math.round(20*SCALE), baseY + Math.round(44*SCALE), w, h);
            case LEFT:  return new java.awt.Rectangle(baseX + Math.round(0*SCALE),  baseY + Math.round(24*SCALE), w, h);
            case RIGHT: return new java.awt.Rectangle(baseX + Math.round(44*SCALE), baseY + Math.round(24*SCALE), w, h);
            default:    return new java.awt.Rectangle(baseX, baseY, 1, 1);
        }
    }

    @Override
    protected void handlePlayerAnimation() {
        if (attacking) {
            currentAnimationName = "ATTACK_" + facingDirection.name();
            return;
        }

        boolean walking =
                (currentWalkingYDirection != Direction.NONE) ||
                (currentWalkingXDirection != Direction.NONE);

        if (!walking) {
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

        // uncomment to show hitbox
//        Rectangle bounds = getBounds();
//
//        float camX = map.getCamera().getX();
//        float camY = map.getCamera().getY();
//
//        int screenX = (int)(bounds.getX() - camX);
//        int screenY = (int)(bounds.getY() - camY);
//
//        graphicsHandler.drawRectangle(
//                screenX,
//                screenY,
//                bounds.getWidth(),
//                bounds.getHeight(),
//                java.awt.Color.BLUE,
//                2
//        );
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet walkSheet) {
        SpriteSheet idleSheet   = new SpriteSheet(ImageLoader.load("Bee_Idle.png"),   TILE, TILE, 0);
        SpriteSheet attackSheet = new SpriteSheet(ImageLoader.load("Bee_Attack.png"), TILE, TILE, 0);

        int hbX = Math.round(10 * SCALE), hbY = Math.round(8 * SCALE);
        int hbW = Math.round(5 * SCALE), hbH = Math.round(5 * SCALE);

        HashMap<String, Frame[]> map = new HashMap<>();

        // === IDLE hover ===
        map.put("STAND_UP",    frames(idleSheet, ROW_UP,    0, 3, 7,  hbX,hbY,hbW,hbH));
        map.put("STAND_LEFT",  frames(idleSheet, ROW_LEFT,  0, 3, 7,  hbX,hbY,hbW,hbH));
        map.put("STAND_RIGHT", frames(idleSheet, ROW_RIGHT, 0, 3, 7,  hbX,hbY,hbW,hbH));
        map.put("STAND_DOWN",  frames(idleSheet, ROW_DOWN,  0, 3, 7,  hbX,hbY,hbW,hbH));

        // === WALK ===
        map.put("WALK_UP",    frames(walkSheet, ROW_UP,    0, 3, 14, hbX,hbY,hbW,hbH));
        map.put("WALK_LEFT",  frames(walkSheet, ROW_LEFT,  0, 3, 14, hbX,hbY,hbW,hbH));
        map.put("WALK_RIGHT", frames(walkSheet, ROW_RIGHT, 0, 3, 14, hbX,hbY,hbW,hbH));
        map.put("WALK_DOWN",  frames(walkSheet, ROW_DOWN,  0, 3, 14, hbX,hbY,hbW,hbH));

        // === ATTACK ===
        map.put("ATTACK_UP",    frames(attackSheet, ROW_UP,    0, 2, 6, hbX,hbY,hbW,hbH));
        map.put("ATTACK_LEFT",  frames(attackSheet, ROW_LEFT,  0, 2, 6, hbX,hbY,hbW,hbH));
        map.put("ATTACK_RIGHT", frames(attackSheet, ROW_RIGHT, 0, 2, 6, hbX,hbY,hbW,hbH));
        map.put("ATTACK_DOWN",  frames(attackSheet, ROW_DOWN,  0, 2, 6, hbX,hbY,hbW,hbH));

        return map;
    }

    private Frame[] frames(SpriteSheet sheet, int row, int colStart, int colEnd, int duration,
                           int hbX, int hbY, int hbW, int hbH) {
        int n = (colEnd - colStart) + 1;
        Frame[] out = new Frame[n];
        for (int i = 0; i < n; i++) {
            out[i] = new FrameBuilder(sheet.getSprite(row, colStart + i), duration)
                    .withScale(SCALE)
                    .withBounds(hbX, hbY, hbW, hbH)
                    .build();
        }
        return out;
    }
}
