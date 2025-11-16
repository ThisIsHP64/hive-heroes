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

public class Crab extends NPC {

    // sprite dimensions and scaling
    private static final int TILE_W = 64;
    private static final int TILE_H = 64;
    private static final int SCALE = 2;

    // how fast crab moves
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

    // called by horde/volcano logic
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

    // swing hitbox (claw swipe) – we’ll use a rectangle instead of pure distance
    private static final int ATTACK_HITBOX_W = 50;
    private static final int ATTACK_HITBOX_H = 40;
    private static final int ATTACK_HITBOX_Y_OFFSET = 10;

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

    public Crab(Point location) {
        super(
                0,
                location.x,
                location.y,
                new SpriteSheet(ImageLoader.load("crabidle2.png"), TILE_W, TILE_H, 0),
                "IDLE_RIGHT");

        this.patrolCenterX = location.x;
        this.patrolCenterY = location.y;
        setRandomPatrolTarget();
    }

    public void takeDamage(int amount) {
        if (isDead)
            return;

        health -= amount;
        System.out.println("Crab took " + amount + " damage! HP: " + health);

        triggerHitFx();

        // spawn red damage number above crab
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
        System.out.println("Crab died!");
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
                    System.out.println("Crab spotted bee! Starting chase...");
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
                    System.out.println("Crab gave up! Returning to patrol...");
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

        // figure out which way to attack
        float beeX = player.getX();
        float crabX = getX();
        facing = (beeX > crabX) ? Direction.RIGHT : Direction.LEFT;
        attackFacingDirection = facing;

        currentAnimationName = (facing == Direction.RIGHT) ? "ATTACK_RIGHT" : "ATTACK_LEFT";

        System.out.println("Crab attacking toward " + facing + " at Crab pos: " + crabX + ", player pos: " + beeX);
    }

    private void updateAttack(Player player, long currentTime) {
        long attackElapsed = currentTime - attackStartTime;

        // lock facing during attack
        facing = attackFacingDirection;
        currentAnimationName = (facing == Direction.RIGHT) ? "ATTACK_RIGHT" : "ATTACK_LEFT";

        // actually swing around halfway through
        if (!hasDealtDamageThisAttack && attackElapsed >= ATTACK_DURATION_MS / 2) {
            checkAttackDamage(player);
        }

        // once the swing is done, go back to chase
        if (attackElapsed >= ATTACK_DURATION_MS) {
            isAttacking = false;
            lastAttackTime = currentTime;
            currentState = State.CHASE;
            attackFacingDirection = null;
            System.out.println("Crab attack finished, resuming chase");
        }
    }

    // build a claw swing rectangle in front of the crab and check vs bee bounds
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
                System.out.println("Crab hit bee for " + ATTACK_DAMAGE + " damage!");
            }
        }
    }

    // rectangle for the claw swipe, based on crab center + facing
    private Rectangle getAttackHitbox() {
        float crabCenterX = getX() + (TILE_W * SCALE) / 2f;
        float crabCenterY = getY() + (TILE_H * SCALE) / 2f + ATTACK_HITBOX_Y_OFFSET;

        int w = ATTACK_HITBOX_W;
        int h = ATTACK_HITBOX_H;

        int x;
        if (facing == Direction.RIGHT) {
            x = (int) (crabCenterX);
        } else {
            x = (int) (crabCenterX - w);
        }
        int y = (int) (crabCenterY - h / 2f);

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
        float crabX = getX();
        float crabY = getY();

        float dx = beeX - crabX;
        float dy = beeY - crabY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0.1f) {
            float moveX = (dx / distance) * currentChaseSpeed();
            float moveY = (dy / distance) * currentChaseSpeed();

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

        // reached target, pick a new random spot
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

        // hitbox positioning for the crab body (tight around body like bee)
        int hitboxX = 21;
        int hitboxY = 30;
        int hitboxW = 22;
        int hitboxH = 18;

        // IDLE animation - 4 frames arranged in a 2x2 grid
        SpriteSheet runSheet = new SpriteSheet(ImageLoader.load("crabidle2.png"), TILE_W, TILE_H, 0);
        Frame[] idleFrames = new Frame[4];
        idleFrames[0] = new FrameBuilder(runSheet.getSprite(0, 0), 6)  // top-left
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        idleFrames[1] = new FrameBuilder(runSheet.getSprite(0, 1), 6)  // top-right
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        idleFrames[2] = new FrameBuilder(runSheet.getSprite(1, 0), 6)  // bottom-left
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        idleFrames[3] = new FrameBuilder(runSheet.getSprite(1, 1), 6)  // bottom-right
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();

        // same thing but flipped for left-facing
        Frame[] idleFramesFlipped = new Frame[4];
        idleFramesFlipped[0] = new FrameBuilder(runSheet.getSprite(0, 0), 6)
                .withScale(SCALE)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        idleFramesFlipped[1] = new FrameBuilder(runSheet.getSprite(0, 1), 6)
                .withScale(SCALE)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        idleFramesFlipped[2] = new FrameBuilder(runSheet.getSprite(1, 0), 6)
                .withScale(SCALE)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        idleFramesFlipped[3] = new FrameBuilder(runSheet.getSprite(1, 1), 6)
                .withScale(SCALE)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();

        animations.put("IDLE_RIGHT", idleFrames);
        animations.put("IDLE_LEFT", idleFramesFlipped);

        // ATTACK animation
        SpriteSheet attackSheet = new SpriteSheet(ImageLoader.load("crabhit2.png"), TILE_W, TILE_H, 0);
        Frame[] attackFrames = new Frame[3];
        attackFrames[0] = new FrameBuilder(attackSheet.getSprite(0, 0), 4)
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        attackFrames[1] = new FrameBuilder(attackSheet.getSprite(0, 1), 4)
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        attackFrames[2] = new FrameBuilder(attackSheet.getSprite(1, 0), 4)
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();

        Frame[] attackFramesFlipped = new Frame[3];
        attackFramesFlipped[0] = new FrameBuilder(attackSheet.getSprite(0, 0), 4)
                .withScale(SCALE)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        attackFramesFlipped[1] = new FrameBuilder(attackSheet.getSprite(0, 1), 4)
                .withScale(SCALE)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        attackFramesFlipped[2] = new FrameBuilder(attackSheet.getSprite(1, 0), 4)
                .withScale(SCALE)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();

        animations.put("ATTACK_RIGHT", attackFramesFlipped);
        animations.put("ATTACK_LEFT", attackFrames);

        // DEATH animation
        SpriteSheet dieSheet = new SpriteSheet(ImageLoader.load("crabdeath2.png"), TILE_W, TILE_H, 0);
        Frame[] dieFrames = new Frame[4];
        dieFrames[0] = new FrameBuilder(dieSheet.getSprite(0, 0), 8)
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        dieFrames[1] = new FrameBuilder(dieSheet.getSprite(0, 1), 8)
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        dieFrames[2] = new FrameBuilder(dieSheet.getSprite(1, 0), 8)
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        dieFrames[3] = new FrameBuilder(dieSheet.getSprite(1, 1), 8)
                .withScale(SCALE)
                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                .build();
        
        animations.put("DIE", dieFrames);

        return animations;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (isDead) {
            // fade out gradually after death
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
        int w = 22 * SCALE;
        int h = 18 * SCALE;
        int offsetX = 21 * SCALE;
        int offsetY = 30 * SCALE;
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
