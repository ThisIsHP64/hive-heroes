package Enemies;

import Builders.FrameBuilder;
import Effects.FloatingText;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Direction;
import Utils.Point;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

public class Goblin extends NPC {

    // sprite dimensions and scaling
    private static final int TILE_W = 32;
    private static final int TILE_H = 16;
    private static final int SCALE = 2;

    // how fast goblin moves
    private static final float PATROL_SPEED = 1.0f;
    private static final float CHASE_SPEED = 2.0f;

    // --- Horde mode ---
    private boolean hordeMode = false;
    private float hordeSpeedMult = 1.0f;

    private float currentChaseSpeed() {
        return hordeMode ? CHASE_SPEED * hordeSpeedMult : CHASE_SPEED;
    }

    private float currentPatrolSpeed() {
        return hordeMode ? PATROL_SPEED * hordeSpeedMult : PATROL_SPEED;
    }

    public void setHordeAggression(float speedMult, boolean on) {
        this.hordeMode = on;
        this.hordeSpeedMult = (speedMult <= 0f) ? 1.0f : speedMult;
    }

    // detection and attack ranges
    private static final float CHASE_RANGE = 200f;
    private static final float GIVE_UP_RANGE = 350f;
    private static final float ATTACK_RANGE = 60f;

    // attack settings
    private static final int ATTACK_DAMAGE = 10;
    private static final long ATTACK_COOLDOWN_MS = 1000;
    private static final long ATTACK_DURATION_MS = 400;

    // hammer swing hitbox (a bit wider since it’s a smasher)
    private static final int ATTACK_HITBOX_W = 56;
    private static final int ATTACK_HITBOX_H = 32;
    private static final int ATTACK_HITBOX_Y_OFFSET = 4;

    // state tracking
    private enum State {
        PATROL, CHASE, ATTACK, DEAD
    }

    private State currentState = State.PATROL;

    // patrol zone
    private float patrolCenterX;
    private float patrolCenterY;
    private float patrolTargetX;
    private float patrolTargetY;
    private static final float PATROL_RADIUS = 100f;

    // facing direction
    private Direction facing = Direction.RIGHT;

    // health tracking
    private int health = 50;
    private boolean isDead = false;
    private long deathTime = 0;
    private static final long DEATH_LINGER_MS = 2000;

    // attack tracking
    private boolean isAttacking = false;
    private long attackStartTime = 0;
    private long lastAttackTime = -10000;
    private boolean hasDealtDamageThisAttack = false;
    private Direction attackFacingDirection = null;

    // hit flash effect
    private boolean showAttackFx = false;
    private long attackFxStartTime = 0;
    private static final long ATTACK_FX_DURATION = 450;

    // floating damage numbers
    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();

    public Goblin(Point location) {
        super(
                0,
                location.x,
                location.y,
                new SpriteSheet(ImageLoader.load("goblinsmasher_idle2.png"), TILE_W, TILE_H, 0),
                "IDLE_RIGHT");

        this.patrolCenterX = location.x;
        this.patrolCenterY = location.y;
        setRandomPatrolTarget();
    }

    public void takeDamage(int amount) {
        if (isDead)
            return;

        health -= amount;
        System.out.println("Goblin took " + amount + " damage! HP: " + health);

        triggerHitFx();

        // spawn red damage number above goblin
        float textX = getX() + (TILE_W * SCALE) / 2f;
        float textY = getY();
        floatingTexts.add(new FloatingText(textX, textY, "-" + amount, Color.RED));

        if (health <= 0) {
            die();
        }
    }

    private void die() {
        if (isDead)
            return;

        isDead = true;
        deathTime = System.currentTimeMillis();
        currentState = State.DEAD;
        currentAnimationName = "DIE";
        System.out.println("Goblin died!");
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean shouldRemove() {
        if (!isDead)
            return false;
        return (System.currentTimeMillis() - deathTime) >= DEATH_LINGER_MS;
    }

    // used by horde / cleanup code that expects canBeRemoved()
    public boolean canBeRemoved() {
        return shouldRemove();
    }

    @Override
    public void update(Player player) {
        // update floating damage numbers
        floatingTexts.removeIf(text -> {
            text.update();
            return text.isDead();
        });

        if (isDead) {
            super.update(player);
            return;
        }

        if (showAttackFx && (System.currentTimeMillis() - attackFxStartTime) >= ATTACK_FX_DURATION) {
            showAttackFx = false;
        }

        super.update(player);
    }

    @Override
    public void performAction(Player player) {
        if (isDead)
            return;

        long currentTime = System.currentTimeMillis();
        float distanceToBee = getDistanceToBee(player);

        switch (currentState) {
            case PATROL:
                patrol();

                if (distanceToBee < CHASE_RANGE) {
                    currentState = State.CHASE;
                    System.out.println("Goblin spotted bee! Starting chase...");
                }
                break;

            case CHASE:
                boolean canAttack = (currentTime - lastAttackTime) > ATTACK_COOLDOWN_MS;

                if (distanceToBee < ATTACK_RANGE && canAttack) {
                    startAttack(player, currentTime);
                } else {
                    chase(player);
                }

                if (!hordeMode && distanceToBee > GIVE_UP_RANGE) {
                    currentState = State.PATROL;
                    setRandomPatrolTarget();
                    System.out.println("Goblin escaped! Returning to patrol...");
                }
                break;

            case ATTACK:
                updateAttack(player, currentTime);
                break;

            case DEAD:
                break;
        }
    }

    private void startAttack(Player player, long currentTime) {
        currentState = State.ATTACK;
        isAttacking = true;
        attackStartTime = currentTime;
        hasDealtDamageThisAttack = false;

        float beeX = player.getX();
        float goblinX = getX();
        facing = (beeX > goblinX) ? Direction.RIGHT : Direction.LEFT;
        attackFacingDirection = facing;

        currentAnimationName = (facing == Direction.RIGHT) ? "ATTACK_RIGHT" : "ATTACK_LEFT";

        System.out.println("Goblin attacking toward " + facing + " at Goblin pos: " + goblinX + ", player pos: " + beeX);
    }

    private void updateAttack(Player player, long currentTime) {
        long attackElapsed = currentTime - attackStartTime;
        
        // lock facing during attack
        facing = attackFacingDirection;
        currentAnimationName = (facing == Direction.RIGHT) ? "ATTACK_RIGHT" : "ATTACK_LEFT";

        // hammer connects mid-swing
        if (!hasDealtDamageThisAttack && attackElapsed >= ATTACK_DURATION_MS / 2) {
            checkAttackDamage(player);
        }

        if (attackElapsed >= ATTACK_DURATION_MS) {
            isAttacking = false;
            lastAttackTime = currentTime;
            currentState = State.CHASE;
            attackFacingDirection = null;
            System.out.println("Goblin attack finished, resuming chase");
        }
    }

    // hammer swing hitbox vs bee rectangle
    private void checkAttackDamage(Player player) {
        Rectangle swing = getAttackHitbox();
        Rectangle beeBounds = new Rectangle(
                (int) player.getX(),
                (int) player.getY(),
                player.getWidth(),
                player.getHeight()
        );

        if (swing.intersects(beeBounds)) {
            if (player instanceof Players.Bee) {
                Players.Bee bee = (Players.Bee) player;
                bee.applyDamage(ATTACK_DAMAGE);
                hasDealtDamageThisAttack = true;
                System.out.println("Goblin hit bee for " + ATTACK_DAMAGE + " damage!");
            }
        }
    }

    // hammer swing rectangle based on goblin center + facing
    private Rectangle getAttackHitbox() {
        float goblinCenterX = getX() + (TILE_W * SCALE) / 2f;
        float goblinCenterY = getY() + (TILE_H * SCALE) / 2f + ATTACK_HITBOX_Y_OFFSET;

        int w = ATTACK_HITBOX_W;
        int h = ATTACK_HITBOX_H;

        int x;
        if (facing == Direction.RIGHT) {
            x = (int) (goblinCenterX);
        } else {
            x = (int) (goblinCenterX - w);
        }
        int y = (int) (goblinCenterY - h / 2f);

        return new Rectangle(x, y, w, h);
    }

    private float getDistanceToBee(Player player) {
        float dx = player.getX() - getX();
        float dy = player.getY() - getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void chase(Player player) {
        float beeX = player.getX();
        float beeY = player.getY();
        float goblinX = getX();
        float goblinY = getY();

        float dx = beeX - goblinX;
        float dy = beeY - goblinY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0.1f) {
            float moveX = (dx / distance) * currentChaseSpeed();
            float moveY = (dy / distance) * currentPatrolSpeed(); // slight variation if you want; can use chase speed

            moveXHandleCollision(moveX);
            moveYHandleCollision(moveY);

            if (Math.abs(dx) > 10) {
                facing = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
            }
        }

        currentAnimationName = (facing == Direction.RIGHT) ? "IDLE_RIGHT" : "IDLE_LEFT";
    }

    private void patrol() {
        float dx = patrolTargetX - getX();
        float dy = patrolTargetY - getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance < 20f) {
            setRandomPatrolTarget();
        }

        if (distance > 0.1f) {
            float moveX = (dx / distance) * currentPatrolSpeed();
            float moveY = (dy / distance) * currentPatrolSpeed();

            moveXHandleCollision(moveX);
            moveYHandleCollision(moveY);

            facing = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        }

        currentAnimationName = (facing == Direction.RIGHT) ? "IDLE_RIGHT" : "IDLE_LEFT";
    }

    private void setRandomPatrolTarget() {
        double angle = Math.random() * 2 * Math.PI;
        float distance = (float) (Math.random() * PATROL_RADIUS);

        patrolTargetX = patrolCenterX + (float) (Math.cos(angle) * distance);
        patrolTargetY = patrolCenterY + (float) (Math.sin(angle) * distance);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> animations = new HashMap<>();

        // --- IDLE ---
        SpriteSheet runSheet = new SpriteSheet(ImageLoader.load("goblinsmasher_idle2.png"), TILE_W, TILE_H, 0);
        Frame[] idleFrames = new Frame[4];
        for (int i = 0; i < 4; i++) {
            idleFrames[i] = new FrameBuilder(runSheet.getSprite(0, i), 6)
                    .withScale(SCALE)
                    .withBounds(9, 0, 18, 16)  // RIGHT-facing bounds (center +2px)
                    .build();
        }

        Frame[] idleFramesFlipped = new Frame[4];
        for (int i = 0; i < 4; i++) {
            idleFramesFlipped[i] = new FrameBuilder(runSheet.getSprite(0, i), 6)
                    .withScale(SCALE)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(5, 0, 18, 16)  // LEFT-facing bounds (center -2px)
                    .build();
        }

        animations.put("IDLE_RIGHT", idleFrames);
        animations.put("IDLE_LEFT", idleFramesFlipped);

        // --- ATTACK ---
        SpriteSheet attackSheet = new SpriteSheet(ImageLoader.load("goblinsmasher_attack2.png"), TILE_W, TILE_H, 0);
        Frame[] attackFrames = new Frame[8];
        for (int i = 0; i < 8; i++) {
            attackFrames[i] = new FrameBuilder(attackSheet.getSprite(0, i), 4)
                    .withScale(SCALE)
                    .withBounds(9, 0, 18, 16)  // RIGHT
                    .build();
        }

        Frame[] attackFramesFlipped = new Frame[8];
        for (int i = 0; i < 8; i++) {
            attackFramesFlipped[i] = new FrameBuilder(attackSheet.getSprite(0, i), 4)
                    .withScale(SCALE)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(5, 0, 18, 16)  // LEFT
                    .build();
        }

        // NOTE: your map of names flips left/right; keeping your original mapping
        animations.put("ATTACK_RIGHT", attackFramesFlipped);
        animations.put("ATTACK_LEFT", attackFrames);

        // --- HURT ---
        SpriteSheet hurtSheet = new SpriteSheet(ImageLoader.load("goblinsmasher_hurt2.png"), TILE_W, TILE_H, 0);
        Frame[] hurtFrames = new Frame[1];
        for (int i = 0; i < 1; i++) {
            hurtFrames[i] = new FrameBuilder(hurtSheet.getSprite(0, i), 5)
                    .withScale(SCALE)
                    .withBounds(9, 0, 18, 16)  // RIGHT default
                    .build();
        }
        animations.put("HURT", hurtFrames);

        // --- DIE ---
        SpriteSheet dieSheet = new SpriteSheet(ImageLoader.load("goblinsmasher_death2.png"), TILE_W, TILE_H, 0);
        Frame[] dieFrames = new Frame[6];
        for (int i = 0; i < 6; i++) {
            dieFrames[i] = new FrameBuilder(dieSheet.getSprite(0, i), 8)
                    .withScale(SCALE)
                    .withBounds(9, 0, 18, 16)  // RIGHT default
                    .build();
        }
        animations.put("DIE", dieFrames);

        return animations;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (isDead) {
            long timeSinceDeath = System.currentTimeMillis() - deathTime;
            float fadeProgress = (float) timeSinceDeath / DEATH_LINGER_MS;

            float alpha = 1.0f - fadeProgress;
            if (alpha < 0)
                alpha = 0;
            if (alpha > 1)
                alpha = 1;

            Graphics2D g2d = graphicsHandler.getGraphics();
            Composite originalComposite = g2d.getComposite();

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.draw(graphicsHandler);
            g2d.setComposite(originalComposite);
        } else {
            super.draw(graphicsHandler);
        }
    }

    // draw floating damage numbers
    public void drawFloatingTexts(GraphicsHandler graphicsHandler, float cameraX, float cameraY) {
        for (FloatingText text : new ArrayList<>(floatingTexts)) {
            text.draw(graphicsHandler, cameraX, cameraY);
        }
    }

    public java.awt.Rectangle getHitbox() {
        // Drawn sprite size
        int drawW = TILE_W * SCALE; // 64
        int drawH = TILE_H * SCALE; // 32

        // Hitbox size matches bounds
        int w = 18 * SCALE; // 36
        int h = 16 * SCALE; // 32

        // Base centered offset (7 px in source units)
        int baseOffsetX = 7 * SCALE;

        // Art compensation: +2 px for RIGHT, -2 px for LEFT (source units)
        int artOffsetX = (facing == Direction.RIGHT ? 2 : -2) * SCALE;

        int offsetX = baseOffsetX + artOffsetX;
        int offsetY = (drawH - h) / 2; // remains 0 here

        return new java.awt.Rectangle(
                (int) getX() + offsetX,
                (int) getY() + offsetY,
                w, h);
    }

    private void triggerHitFx() {
        showAttackFx = true;
        attackFxStartTime = System.currentTimeMillis();
    }

    public boolean isShowingAttackFx() {
        return !isDead
                && showAttackFx
                && (System.currentTimeMillis() - attackFxStartTime) < ATTACK_FX_DURATION;
    }

    // getter for facing direction (needed for attack FX reflection)
    public Direction getFacing() {
        return facing;
    }
}
