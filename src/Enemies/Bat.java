package Enemies;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.HashMap;

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

public class Bat extends NPC {

    // sprite dimensions and scaling
    private static final int TILE_W = 64;
    private static final int TILE_H = 64;
    private static final int SCALE = 2;

    // how fast bat moves
    private static final float PATROL_SPEED = 1.0f;
    private static final float CHASE_SPEED = 2.0f;

    // detection and attack ranges
    private static final float CHASE_RANGE = 200f;
    private static final float GIVE_UP_RANGE = 350f;
    private static final float ATTACK_RANGE = 60f;

    // attack settings
    private static final int ATTACK_DAMAGE = 10;
    private static final long ATTACK_COOLDOWN_MS = 1000;
    private static final long ATTACK_DURATION_MS = 400;
    private static final float HIT_DISTANCE = 50f;

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

    // hit flash effect
    private boolean showAttackFx = false;
    private long attackFxStartTime = 0;
    private static final long ATTACK_FX_DURATION = 450;

    public Bat(Point location) {
        super(
                0, // bats don't need unique IDs
                location.x,
                location.y,
                new SpriteSheet(ImageLoader.load("Bat-Run.png"), TILE_W, TILE_H, 0),
                "FLY_RIGHT");

        // set patrol center
        this.patrolCenterX = location.x;
        this.patrolCenterY = location.y;
        setRandomPatrolTarget();
    }

    // bee calls this when it attacks
    public void takeDamage(int amount) {
        if (isDead)
            return;

        health -= amount;
        System.out.println("Bat took " + amount + " damage! HP: " + health);

        // show hit flash
        triggerHitFx();

        if (health <= 0) {
            die();
        }
    }

    // handle death
    private void die() {
        if (isDead)
            return;

        isDead = true;
        deathTime = System.currentTimeMillis();
        currentState = State.DEAD;
        currentAnimationName = "DIE";
        System.out.println("Bat died!");
    }

    public boolean isDead() {
        return isDead;
    }

    // check if bat should be removed from game
    public boolean shouldRemove() {
        if (!isDead)
            return false;
        return (System.currentTimeMillis() - deathTime) >= DEATH_LINGER_MS;
    }

    @Override
    public void update(Player player) {
        // just play death animation if dead
        if (isDead) {
            super.update(player);
            return;
        }

        // clear hit flash after duration
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

                // check if player is in range
                if (distanceToBee < CHASE_RANGE) {
                    currentState = State.CHASE;
                    System.out.println("Bat spotted bee! Starting chase...");
                }
                break;

            case CHASE:
                boolean canAttack = (currentTime - lastAttackTime) > ATTACK_COOLDOWN_MS;

                // try to attack if close enough
                if (distanceToBee < ATTACK_RANGE && canAttack) {
                    startAttack(player, currentTime);
                } else {
                    chase(player);
                }

                // give up chase if player escapes
                if (distanceToBee > GIVE_UP_RANGE) {
                    currentState = State.PATROL;
                    setRandomPatrolTarget();
                    System.out.println("Bee escaped! Returning to patrol...");
                }
                break;

            case ATTACK:
                updateAttack(player, currentTime);
                break;

            case DEAD:
                break;
        }
    }

    // begin attack sequence
    private void startAttack(Player player, long currentTime) {
        currentState = State.ATTACK;
        isAttacking = true;
        attackStartTime = currentTime;
        hasDealtDamageThisAttack = false;

        float beeX = player.getX();
        float batX = getX();
        facing = (beeX > batX) ? Direction.RIGHT : Direction.LEFT;

        currentAnimationName = (facing == Direction.RIGHT) ? "ATTACK_RIGHT" : "ATTACK_LEFT";

        System.out.println("Bat attacking toward " + facing);
    }

    // handle attack animation and damage
    private void updateAttack(Player player, long currentTime) {
        long attackElapsed = currentTime - attackStartTime;

        // check for damage during middle of attack
        if (!hasDealtDamageThisAttack && attackElapsed >= ATTACK_DURATION_MS / 2) {
            checkAttackDamage(player);
        }

        // end attack after duration
        if (attackElapsed >= ATTACK_DURATION_MS) {
            isAttacking = false;
            lastAttackTime = currentTime;
            currentState = State.CHASE;
            System.out.println("Attack finished, resuming chase");
        }
    }

    // check if attack hits player
    private void checkAttackDamage(Player player) {
        float batCenterX = getX() + (TILE_W * SCALE) / 2f;
        float batCenterY = getY() + (TILE_H * SCALE) / 2f;

        float beeCenterX = player.getX() + 32f;
        float beeCenterY = player.getY() + 32f;

        float dx = beeCenterX - batCenterX;
        float dy = beeCenterY - batCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // only hit if player is in front of bat
        boolean beeIsInFacingDirection = false;
        if (facing == Direction.RIGHT && dx > 0) {
            beeIsInFacingDirection = true;
        } else if (facing == Direction.LEFT && dx < 0) {
            beeIsInFacingDirection = true;
        }

        // apply damage if close enough and in front
        if (distance < HIT_DISTANCE && beeIsInFacingDirection) {
            if (player instanceof Players.Bee) {
                Players.Bee bee = (Players.Bee) player;
                bee.applyDamage(ATTACK_DAMAGE);
                hasDealtDamageThisAttack = true;
                System.out.println("Bat hit bee for " + ATTACK_DAMAGE + " damage!");
            }
        }
    }

    // calculate distance to player
    private float getDistanceToBee(Player player) {
        float dx = player.getX() - getX();
        float dy = player.getY() - getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // move toward player
    private void chase(Player player) {
        float beeX = player.getX();
        float beeY = player.getY();
        float batX = getX();
        float batY = getY();

        float dx = beeX - batX;
        float dy = beeY - batY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0.1f) {
            float moveX = (dx / distance) * CHASE_SPEED;
            float moveY = (dy / distance) * CHASE_SPEED;

            moveXHandleCollision(moveX);
            moveYHandleCollision(moveY);

            // update facing direction
            if (Math.abs(dx) > 10) {
                facing = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
            }
        }

        currentAnimationName = (facing == Direction.RIGHT) ? "FLY_RIGHT" : "FLY_LEFT";
    }

    // fly around patrol zone
    private void patrol() {
        float dx = patrolTargetX - getX();
        float dy = patrolTargetY - getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // pick new target when reached current one
        if (distance < 20f) {
            setRandomPatrolTarget();
        }

        if (distance > 0.1f) {
            float moveX = (dx / distance) * PATROL_SPEED;
            float moveY = (dy / distance) * PATROL_SPEED;

            moveXHandleCollision(moveX);
            moveYHandleCollision(moveY);

            facing = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        }

        currentAnimationName = (facing == Direction.RIGHT) ? "FLY_RIGHT" : "FLY_LEFT";
    }

    // pick random point in patrol radius
    private void setRandomPatrolTarget() {
        double angle = Math.random() * 2 * Math.PI;
        float distance = (float) (Math.random() * PATROL_RADIUS);

        patrolTargetX = patrolCenterX + (float) (Math.cos(angle) * distance);
        patrolTargetY = patrolCenterY + (float) (Math.sin(angle) * distance);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> animations = new HashMap<>();

        // load run/fly sprites - 8 frames in one row, NO GUTTER
        SpriteSheet runSheet = new SpriteSheet(ImageLoader.load("Bat-Run.png"), TILE_W, TILE_H, 0);
        Frame[] flyFrames = new Frame[8];
        for (int i = 0; i < 8; i++) {
            flyFrames[i] = new FrameBuilder(runSheet.getSprite(0, i), 6)
                    .withScale(SCALE)
                    .withBounds(16, 16, 32, 32)
                    .build();
        }

        Frame[] flyFramesFlipped = new Frame[8];
        for (int i = 0; i < 8; i++) {
            flyFramesFlipped[i] = new FrameBuilder(runSheet.getSprite(0, i), 6)
                    .withScale(SCALE)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(16, 16, 32, 32)
                    .build();
        }

        animations.put("FLY_RIGHT", flyFrames);
        animations.put("FLY_LEFT", flyFramesFlipped);

        // load attack sprites - 8 frames in one row, NO GUTTER
        SpriteSheet attackSheet = new SpriteSheet(ImageLoader.load("Bat-Attack1.png"), TILE_W, TILE_H, 0);
        Frame[] attackFrames = new Frame[8];
        for (int i = 0; i < 8; i++) {
            attackFrames[i] = new FrameBuilder(attackSheet.getSprite(0, i), 4)
                    .withScale(SCALE)
                    .withBounds(16, 16, 32, 32)
                    .build();
        }

        Frame[] attackFramesFlipped = new Frame[8];
        for (int i = 0; i < 8; i++) {
            attackFramesFlipped[i] = new FrameBuilder(attackSheet.getSprite(0, i), 4)
                    .withScale(SCALE)
                    .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                    .withBounds(16, 16, 32, 32)
                    .build();
        }

        animations.put("ATTACK_RIGHT", attackFrames);
        animations.put("ATTACK_LEFT", attackFramesFlipped);

        // load hurt sprites - 5 frames in one row, NO GUTTER
        SpriteSheet hurtSheet = new SpriteSheet(ImageLoader.load("Bat-Hurt.png"), TILE_W, TILE_H, 0);
        Frame[] hurtFrames = new Frame[5];
        for (int i = 0; i < 5; i++) {
            hurtFrames[i] = new FrameBuilder(hurtSheet.getSprite(0, i), 5)
                    .withScale(SCALE)
                    .withBounds(16, 16, 32, 32)
                    .build();
        }
        animations.put("HURT", hurtFrames);

        // load death sprites - 8 frames in one row, NO GUTTER
        SpriteSheet dieSheet = new SpriteSheet(ImageLoader.load("Bat-Die.png"), TILE_W, TILE_H, 0);
        Frame[] dieFrames = new Frame[8];
        for (int i = 0; i < 8; i++) {
            dieFrames[i] = new FrameBuilder(dieSheet.getSprite(0, i), 8)
                    .withScale(SCALE)
                    .withBounds(16, 16, 32, 32)
                    .build();
        }
        animations.put("DIE", dieFrames);

        return animations;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // fade out corpse over time
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

    // collision box for bee attacks
    public java.awt.Rectangle getHitbox() {
        int w = 32;
        int h = 32;
        int offsetX = 16;
        int offsetY = 16;
        return new java.awt.Rectangle(
                (int) getX() + offsetX,
                (int) getY() + offsetY,
                w, h);
    }

    // trigger hit flash effect
    private void triggerHitFx() {
        showAttackFx = true;
        attackFxStartTime = System.currentTimeMillis();
    }

    // check if hit flash should be displayed
    public boolean isShowingAttackFx() {
        return !isDead
                && showAttackFx
                && (System.currentTimeMillis() - attackFxStartTime) < ATTACK_FX_DURATION;
    }
}