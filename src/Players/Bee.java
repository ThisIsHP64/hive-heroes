package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard; // using this to read SPACE
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.Player;
import Utils.Direction;

import java.util.HashMap;

public class Bee extends Player {

    // frames are 64x64 in all Bee sheets
    private static final int TILE  = 64;
    // scale up the sprite a bit (tweak 2.0–3.0)
    private static final float SCALE = 2.5f;

    // row mapping in ALL Bee sheets (0-based)
    private static final int ROW_UP    = 0;
    private static final int ROW_LEFT  = 1;
    private static final int ROW_RIGHT = 2;
    private static final int ROW_DOWN  = 3;

    // === Attack tuning ===
    // how long the sting is “live” after you press SPACE (in ms)
    // short window keeps melee snappy but not always-on
    private static final int ATTACK_ACTIVE_MS = 100;

    // simple “am I attacking right now” flag + timer
    private boolean attacking = false;
    private long attackStart  = 0L;

    // for edge-triggering the key (so one press = one jab)
    private boolean prevSpaceDown = false;

    public Bee(float x, float y) {
        // gutter=0 (no spacing between frames)
        super(new SpriteSheet(ImageLoader.load("Bee_Walk.png"), TILE, TILE, 0),
              x, y,
              "STAND_DOWN");

        // === Controls: WASD ===
        MOVE_LEFT_KEY  = Key.A;
        MOVE_RIGHT_KEY = Key.D;
        MOVE_UP_KEY    = Key.W;
        MOVE_DOWN_KEY  = Key.S;

        // feels good with our tiles; tweak if needed
        walkSpeed = 10f;
    }

    @Override
    public void update() {
        // check SPACE and flip on the attack window if pressed
        handleAttackInput();

        // base movement/physics/animation pipeline
        super.update();

        // auto-stop attack after the short window
        if (attacking && System.currentTimeMillis() - attackStart > ATTACK_ACTIVE_MS) {
            attacking = false; // hitbox off again
        }
    }

    // SPACE = jab. No cooldown — every press should go off.
    private void handleAttackInput() {
        boolean spaceDown   = Keyboard.isKeyDown(Key.SPACE);
        boolean justPressed = spaceDown && !prevSpaceDown; // edge detect

        if (justPressed) {
            attacking   = true;
            attackStart = System.currentTimeMillis();

            // lock into the attack anim that matches our facing
            currentAnimationName = "ATTACK_" + facingDirection.name();
        }

        // remember for next frame
        prevSpaceDown = spaceDown;
    }

    // other classes can ask if we’re in the “sting is live” window
    public boolean isAttacking() { return attacking; }

    // small rectangle in front of the Bee while attacking
    // (numbers feel good with SCALE=2.5; tweak to taste)
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

    // decide which animation to play
    @Override
    protected void handlePlayerAnimation() {
        // if we’re jabbing, keep the attack anim no matter what
        if (attacking) {
            currentAnimationName = "ATTACK_" + facingDirection.name();
            return;
        }

        // normal idle/walk logic
        boolean walking =
                (currentWalkingYDirection != Direction.NONE) ||
                (currentWalkingXDirection != Direction.NONE);

        if (!walking) {
            // idle in last facing direction
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

        // walking gets priority by axis (vertical first)
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
        // Debug tip: uncomment to see the active sting area
        // if (attacking) graphicsHandler.drawRect(getAttackHitbox(), java.awt.Color.RED, 2);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet walkSheet) {
        // all sheets = 64x64, 4 rows in this order: UP / LEFT / RIGHT / DOWN
        SpriteSheet idleSheet   = new SpriteSheet(ImageLoader.load("Bee_Idle.png"),   TILE, TILE, 0);
        SpriteSheet attackSheet = new SpriteSheet(ImageLoader.load("Bee_Attack.png"), TILE, TILE, 0);

        // collision box anchored inside the sprite — feels right with this art
        int hbX = Math.round(22 * SCALE), hbY = Math.round(42 * SCALE);
        int hbW = Math.round(20 * SCALE), hbH = Math.round(14 * SCALE);

        HashMap<String, Frame[]> map = new HashMap<>();

        // === IDLE hover (cols 0–3) ===
        map.put("STAND_UP",    frames(idleSheet,   ROW_UP,    0, 3, 7,  hbX,hbY,hbW,hbH));
        map.put("STAND_LEFT",  frames(idleSheet,   ROW_LEFT,  0, 3, 7,  hbX,hbY,hbW,hbH));
        map.put("STAND_RIGHT", frames(idleSheet,   ROW_RIGHT, 0, 3, 7,  hbX,hbY,hbW,hbH));
        map.put("STAND_DOWN",  frames(idleSheet,   ROW_DOWN,  0, 3, 7,  hbX,hbY,hbW,hbH));

        // === WALK (cols 0–3) ===
        map.put("WALK_UP",     frames(walkSheet,   ROW_UP,    0, 3, 14, hbX,hbY,hbW,hbH));
        map.put("WALK_LEFT",   frames(walkSheet,   ROW_LEFT,  0, 3, 14, hbX,hbY,hbW,hbH));
        map.put("WALK_RIGHT",  frames(walkSheet,   ROW_RIGHT, 0, 3, 14, hbX,hbY,hbW,hbH));
        map.put("WALK_DOWN",   frames(walkSheet,   ROW_DOWN,  0, 3, 14, hbX,hbY,hbW,hbH));

        // === ATTACK (Bee_Attack.png) ===
        // assuming 4x4 grid; we use the first 3 frames (0..2) for a quick jab
        map.put("ATTACK_UP",     frames(attackSheet, ROW_UP,    0, 2, 6, hbX,hbY,hbW,hbH));
        map.put("ATTACK_LEFT",   frames(attackSheet, ROW_LEFT,  0, 2, 6, hbX,hbY,hbW,hbH));
        map.put("ATTACK_RIGHT",  frames(attackSheet, ROW_RIGHT, 0, 2, 6, hbX,hbY,hbW,hbH));
        map.put("ATTACK_DOWN",   frames(attackSheet, ROW_DOWN,  0, 2, 6, hbX,hbY,hbW,hbH));

        return map;
    }

    // helper: build frames from a single row [colStart..colEnd]
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
