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
import java.util.ArrayList;
import java.util.HashMap;

public class Bat extends NPC {

    // sprite dimensions and scaling
    private static final int TILE_W = 64;
    private static final int TILE_H = 64;
    private static final int SCALE = 2;

    // how fast bat moves
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

    // --- BALANCE KNOBS ---
    // health & damage – tweak these if bat feels too weak/strong
    private static final int MAX_HEALTH = 40;       // was 50 – now dies in ~3 melee or 2 projectiles
    private static final int ATTACK_DAMAGE = 8;     // was 10 – softer chip damage

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
    private int health = MAX_HEALTH;
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

    public Bat(Point location) {
        super(
                0,
                location.x,
                location.y,
                new SpriteSheet(ImageLoader.load("Bat-Run.png"), TILE_W, TILE_H, 0),
                "FLY_RIGHT");

        this.patrolCenterX = location.x;
        this.patrolCenterY = location.y;
        setRandomPatrolTarget();
    }

    public void takeDamage(int amount) {
        if (isDead)
            return;

        health -= amount;
        System.out.println("Bat took " + amount + " damage! HP: " + health + "/" + MAX_HEALTH);

        triggerHitFx();

        // spawn red damage number above bat
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
        System.out.println("Bat died!");
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean shouldRemove() {
        if (!isDead)
            return false;
        return (System.currentTimeMillis() - deathTime) >= DEATH_LINGER_MS;
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
                    System.out.println("Bat spotted bee! Starting chase...");
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

    private void startAttack(Player player, long currentTime) {
        currentState = State.ATTACK;
        isAttacking = true;
        attackStartTime = currentTime;
        hasDealtDamageThisAttack = false;

        float beeX = player.getX();
        float batX = getX();
        facing = (beeX > batX) ? Direction.RIGHT : Direction.LEFT;
        attackFacingDirection = facing;

        currentAnimationName = (facing == Direction.RIGHT) ? "ATTACK_RIGHT" : "ATTACK_LEFT";

        System.out.println("Bat attacking toward " + facing + " at bat pos: " + batX + ", player pos: " + beeX);
    }

    private void updateAttack(Player player, long currentTime) {
        long attackElapsed = currentTime - attackStartTime;
        
        facing = attackFacingDirection;
        currentAnimationName = (facing == Direction.RIGHT) ? "ATTACK_RIGHT" : "ATTACK_LEFT";

        if (!hasDealtDamageThisAttack && attackElapsed >= ATTACK_DURATION_MS / 2) {
            checkAttackDamage(player);
        }

        if (attackElapsed >= ATTACK_DURATION_MS) {
            isAttacking = false;
            lastAttackTime = currentTime;
            currentState = State.CHASE;
            attackFacingDirection = null;
            System.out.println("Attack finished, resuming chase");
        }
    }

    private void checkAttackDamage(Player player) {
        float batCenterX = getX() + (TILE_W * SCALE) / 2f;
        float batCenterY = getY() + (TILE_H * SCALE) / 2f;

        float beeCenterX = player.getX() + 32f;
        float beeCenterY = player.getY() + 32f;

        float dx = beeCenterX - batCenterX;
        float dy = beeCenterY - batCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        boolean beeIsInFacingDirection = false;
        if (facing == Direction.RIGHT && dx > 0) {
            beeIsInFacingDirection = true;
        } else if (facing == Direction.LEFT && dx < 0) {
            beeIsInFacingDirection = true;
        }

        if (distance < HIT_DISTANCE && beeIsInFacingDirection) {
            if (player instanceof Players.Bee) {
                Players.Bee bee = (Players.Bee) player;
                bee.applyDamage(ATTACK_DAMAGE);
                hasDealtDamageThisAttack = true;
                System.out.println("Bat hit bee for " + ATTACK_DAMAGE + " damage!");
            }
        }
    }

    private float getDistanceToBee(Player player) {
        float dx = player.getX() - getX();
        float dy = player.getY() - getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void chase(Player player) {
        float beeX = player.getX();
        float beeY = player.getY();
        float batX = getX();
        float batY = getY();

        float dx = beeX - batX;
        float dy = beeY - batY;
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

        currentAnimationName = (facing == Direction.RIGHT) ? "FLY_RIGHT" : "FLY_LEFT";
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

        currentAnimationName = (facing == Direction.RIGHT) ? "FLY_RIGHT" : "FLY_LEFT";
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

        animations.put("ATTACK_RIGHT", attackFramesFlipped);
        animations.put("ATTACK_LEFT", attackFrames);

        SpriteSheet hurtSheet = new SpriteSheet(ImageLoader.load("Bat-Hurt.png"), TILE_W, TILE_H, 0);
        Frame[] hurtFrames = new Frame[5];
        for (int i = 0; i < 5; i++) {
            hurtFrames[i] = new FrameBuilder(hurtSheet.getSprite(0, i), 5)
                    .withScale(SCALE)
                    .withBounds(16, 16, 32, 32)
                    .build();
        }
        animations.put("HURT", hurtFrames);

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
        int w = 32;
        int h = 32;
        int offsetX = 16;
        int offsetY = 16;
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
}
