package Enemies;

import java.util.HashMap;
import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

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
import Effects.FloatingText;

public class Spider extends NPC {

    // sprite dimensions and scaling
    private static final int TILE_W = 32;
    private static final int TILE_H = 32;
    private static final int SCALE = 2;

    // how fast spider moves
    private static final float PATROL_SPEED = 1.0f;
    private static final float CHASE_SPEED = 1.8f;

    // --- Horde mode ---
    private boolean hordeMode = false;
    private float hordeSpeedMult = 1.0f;

    private float currentChaseSpeed() {
        return hordeMode ? CHASE_SPEED * hordeSpeedMult : CHASE_SPEED;
    }

    private float currentPatrolSpeed() {
        return hordeMode ? PATROL_SPEED * hordeSpeedMult : PATROL_SPEED;
    }

    /** Called by HordeManager to flip aggression on/off. */
    public void setHordeAggression(float speedMult, boolean on) {
        this.hordeMode = on;
        this.hordeSpeedMult = (speedMult <= 0f) ? 1.0f : speedMult;
    }

    // detection distances
    private static final float CHASE_RANGE = 150f;
    private static final float GIVE_UP_RANGE = 250f;
    private static final float TOO_CLOSE_RANGE = 25f;
    private static final float JUMP_RANGE = 60f;

    // --- BALANCE KNOBS ---
    private static final int SPIDER_MAX_HEALTH = 60;    // was 50 – tankier than bat
    private static final int JUMP_DAMAGE = 20;          // was 25 – still scary, not ridiculous

    // jump attack settings
    private static final long JUMP_COOLDOWN_MS = 800;
    private static final long JUMP_WINDUP_MS = 150;
    private static final long JUMP_DURATION_MS = 500;
    private static final long JUMP_RECOVERY_MS = 300;
    private static final long DAMAGE_WINDOW_START = 200;
    private static final long DAMAGE_WINDOW_END = 400;
    private static final float HIT_DISTANCE = 40f;

    // jump speed boost
    private static final float JUMP_SPEED_MULTIPLIER = 2.2f;

    // jump state tracking
    private boolean isJumping = false;
    private boolean isWindingUp = false;
    private boolean isRecovering = false;
    private long jumpStartTime = 0;
    private long windupStartTime = 0;
    private long recoveryStartTime = 0;
    private long lastJumpTime = -10000;
    private boolean hasDealtDamageThisJump = false;
    private Direction lockedJumpDirection = null;

    // spawn timing
    private final long spawnTime = System.currentTimeMillis();
    private static final long STARTUP_FREEZE_MS = 1000;
    private static final long CHASE_ENABLE_MS = 1500;
    private boolean hasMovedAwayFromSpawn = false;

    // patrol zone boundaries
    private float patrolLeftX;
    private float patrolRightX;

    // patrol direction tracker
    private int direction = 1;

    // which way spider is facing for animations
    private Direction facing = Direction.RIGHT;

    // health tracking
    private int health = SPIDER_MAX_HEALTH;
    private boolean isDead = false;
    private long deathTime = 0;
    private static final long DEATH_LINGER_MS = 2000;

    // hit flash effect when taking damage
    private boolean showAttackFx = false;
    private long attackFxStartTime = 0;
    private static final long ATTACK_FX_DURATION = 450;

    // floating damage numbers
    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();

    // state machine for spider behavior
    private enum State {
        PATROL, CHASE, JUMP, RECOVERY, DEAD
    }

    private State currentState = State.PATROL;

    public Spider(int id, Point location) {
        super(
                id,
                location.x,
                location.y,
                new SpriteSheet(ImageLoader.load("spider05.png"), TILE_W, TILE_H),
                "WALK_RIGHT");

        float patrolRange = 96f;
        this.patrolLeftX = location.x - patrolRange;
        this.patrolRightX = location.x + patrolRange;

        System.out.println("Spider spawned at: " + location.x + ", " + location.y);
        System.out.println("Patrol range: " + patrolLeftX + " to " + patrolRightX);
    }

    // bee calls this when it attacks
    public void takeDamage(int amount) {
        if (isDead)
            return;

        health -= amount;
        System.out.println("Spider took " + amount + " damage! HP: " + health + "/" + SPIDER_MAX_HEALTH);

        // show hit flash
        triggerHitFx();

        // spawn red damage number above spider
        float textX = getX() + (TILE_W * SCALE) / 2f;
        float textY = getY();
        floatingTexts.add(new FloatingText(textX, textY, "-" + amount, Color.RED));

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
        currentAnimationName = "DEATH";
        System.out.println("Spider died!");
    }

    public boolean isDead() {
        return isDead;
    }

    // check if spider should be removed from game
    public boolean canBeRemoved() {
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

        // just play death animation if dead
        if (isDead) {
            super.update(player);
            return;
        }

        // clear hit flash after duration
        if (showAttackFx && (System.currentTimeMillis() - attackFxStartTime) >= ATTACK_FX_DURATION) {
            showAttackFx = false;
        }

        long timeSinceSpawn = System.currentTimeMillis() - spawnTime;
        if (timeSinceSpawn < STARTUP_FREEZE_MS) {
            currentAnimationName = "WALK_RIGHT";
            if (animations.get(currentAnimationName) != null) {
                currentFrame = animations.get(currentAnimationName)[0];
            }
            return;
        }

        super.update(player);
    }

    @Override
    public void performAction(Player player) {
        if (isDead)
            return;

        long timeSinceSpawn = System.currentTimeMillis() - spawnTime;
        if (timeSinceSpawn < STARTUP_FREEZE_MS) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        float distanceToBee = getDistanceToBee(player);

        float beeX = player.getX();
        float territoryRange = 200f;
        boolean beeInTerritory = (beeX >= patrolLeftX - territoryRange) &&
                (beeX <= patrolRightX + territoryRange);

        switch (currentState) {
            case PATROL:
                patrol();

                if (!hasMovedAwayFromSpawn && timeSinceSpawn > STARTUP_FREEZE_MS + 500) {
                    hasMovedAwayFromSpawn = true;
                }

                if (hordeMode || (timeSinceSpawn >= CHASE_ENABLE_MS &&
                        hasMovedAwayFromSpawn &&
                        distanceToBee < CHASE_RANGE &&
                        distanceToBee > TOO_CLOSE_RANGE &&
                        beeInTerritory)) {
                    currentState = State.CHASE;
                    System.out.println("Spider spotted bee! Starting chase...");
                }
                break;

            case CHASE:
                boolean canJump = (currentTime - lastJumpTime) > JUMP_COOLDOWN_MS;

                if (distanceToBee < JUMP_RANGE && canJump) {
                    startJumpAttack(player, currentTime);
                } else {
                    chase(player);
                }

                if (!hordeMode && (distanceToBee > GIVE_UP_RANGE || !beeInTerritory)) {
                    currentState = State.PATROL;
                    System.out.println("Bee escaped! Returning to patrol...");
                }
                break;

            case JUMP:
                updateJumpAttack(player, currentTime);
                break;

            case RECOVERY:
                updateRecovery(currentTime);
                break;

            case DEAD:
                break;
        }
    }

    // begin jump attack sequence
    private void startJumpAttack(Player player, long currentTime) {
        currentState = State.JUMP;
        isWindingUp = true;
        windupStartTime = currentTime;
        hasDealtDamageThisJump = false;

        float beeX = player.getX();
        float spiderX = getX();
        facing = (beeX > spiderX) ? Direction.RIGHT : Direction.LEFT;

        currentAnimationName = (facing == Direction.RIGHT) ? "JUMP_RIGHT" : "JUMP_LEFT";

        System.out.println("Spider winding up jump toward " + facing);
    }

    // handle jump attack movement and damage
    private void updateJumpAttack(Player player, long currentTime) {
        if (isWindingUp) {
            long windupElapsed = currentTime - windupStartTime;

            float beeX = player.getX();
            float spiderX = getX();
            facing = (beeX > spiderX) ? Direction.RIGHT : Direction.LEFT;

            currentAnimationName = (facing == Direction.RIGHT) ? "JUMP_RIGHT" : "JUMP_LEFT";

            if (windupElapsed >= JUMP_WINDUP_MS) {
                isWindingUp = false;
                isJumping = true;
                jumpStartTime = currentTime;
                lockedJumpDirection = facing;
                System.out.println("Spider jumping toward " + facing + "! Direction LOCKED");
            }
            return;
        }

        long jumpElapsed = currentTime - jumpStartTime;

        if (jumpElapsed < JUMP_DURATION_MS) {
            float beeX = player.getX();
            float beeY = player.getY();
            float spiderX = getX();
            float spiderY = getY();

            float dx = beeX - spiderX;
            float dy = beeY - spiderY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            facing = lockedJumpDirection;
            currentAnimationName = (facing == Direction.RIGHT) ? "JUMP_RIGHT" : "JUMP_LEFT";

            if (distance > 0.1f) {
                float jumpSpeed = currentChaseSpeed() * JUMP_SPEED_MULTIPLIER;
                float moveX = (dx / distance) * jumpSpeed;
                float moveY = (dy / distance) * jumpSpeed;

                moveXHandleCollision(moveX);
                moveYHandleCollision(moveY);
            }

            if (!hasDealtDamageThisJump &&
                    jumpElapsed >= DAMAGE_WINDOW_START &&
                    jumpElapsed <= DAMAGE_WINDOW_END) {
                checkJumpDamage(player, currentTime);
            }
        } else {
            endJumpAttack(currentTime);
        }
    }

    // brief pause after jump before resuming chase
    private void updateRecovery(long currentTime) {
        long recoveryElapsed = currentTime - recoveryStartTime;

        if (lockedJumpDirection != null) {
            facing = lockedJumpDirection;
        }

        currentAnimationName = (facing == Direction.RIGHT) ? "WALK_RIGHT" : "WALK_LEFT";

        if (recoveryElapsed >= JUMP_RECOVERY_MS) {
            isRecovering = false;
            lockedJumpDirection = null;
            currentState = State.CHASE;
            System.out.println("Recovery complete, unlocking facing, resuming chase");
        }
    }

    // check if jump hits player
    private void checkJumpDamage(Player player, long currentTime) {
        float spiderCenterX = getX() + (TILE_W * SCALE) / 2f;
        float spiderCenterY = getY() + (TILE_H * SCALE) / 2f;

        float beeCenterX = player.getX() + 32f;
        float beeCenterY = player.getY() + 32f;

        float dx = beeCenterX - spiderCenterX;
        float dy = beeCenterY - spiderCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        boolean beeIsInFacingDirection = false;
        if (lockedJumpDirection == Direction.RIGHT && dx > 0) {
            beeIsInFacingDirection = true;
        } else if (lockedJumpDirection == Direction.LEFT && dx < 0) {
            beeIsInFacingDirection = true;
        }

        if (distance < HIT_DISTANCE && beeIsInFacingDirection) {
            int attackBoxSize = 25;
            int attackX = (int) spiderCenterX - attackBoxSize / 2;
            int attackY = (int) spiderCenterY - attackBoxSize / 2;

            java.awt.Rectangle spiderAttack = new java.awt.Rectangle(
                    attackX, attackY, attackBoxSize, attackBoxSize);

            int beeHitSize = 40;
            java.awt.Rectangle beeBox = new java.awt.Rectangle(
                    (int) player.getX() + 12,
                    (int) player.getY() + 12,
                    beeHitSize,
                    beeHitSize);

            if (spiderAttack.intersects(beeBox)) {
                if (player instanceof Players.Bee) {
                    Players.Bee bee = (Players.Bee) player;
                    bee.applyDamage(JUMP_DAMAGE);
                    hasDealtDamageThisJump = true;

                    System.out.println("Spider hit bee for " + JUMP_DAMAGE + " damage!");
                }
            }
        }
    }

    // finish jump and enter recovery
    private void endJumpAttack(long currentTime) {
        isJumping = false;
        isWindingUp = false;
        lastJumpTime = currentTime;

        currentState = State.RECOVERY;
        isRecovering = true;
        recoveryStartTime = currentTime;

        currentAnimationName = (facing == Direction.RIGHT) ? "WALK_RIGHT" : "WALK_LEFT";

        System.out.println("Jump finished, entering recovery...");
    }

    // calculate straight line distance to player
    private float getDistanceToBee(Player player) {
        float dx = player.getX() - getX();
        float dy = player.getY() - getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // move toward player
    private void chase(Player player) {
        float beeX = player.getX();
        float beeY = player.getY();
        float spiderX = getX();
        float spiderY = getY();

        float dx = beeX - spiderX;
        float dy = beeY - spiderY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0.1f) {
            float moveX = (dx / distance) * currentChaseSpeed();
            float moveY = (dy / distance) * currentChaseSpeed();

            moveXHandleCollision(moveX);
            moveYHandleCollision(moveY);

            if (lockedJumpDirection == null && Math.abs(dx) > 10) {
                Direction newFacing = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
                if (newFacing != facing) {
                    facing = newFacing;
                }
            }
        }

        currentAnimationName = (facing == Direction.RIGHT) ? "WALK_RIGHT" : "WALK_LEFT";
    }

    // move back and forth in patrol zone
    private void patrol() {
        float moveAmount = direction * currentPatrolSpeed();
        float actualMove = moveXHandleCollision(moveAmount);

        facing = (direction > 0) ? Direction.RIGHT : Direction.LEFT;
        currentAnimationName = (facing == Direction.RIGHT) ? "WALK_RIGHT" : "WALK_LEFT";

        float currentX = getX();
        boolean hitLeftBoundary = currentX <= patrolLeftX;
        boolean hitRightBoundary = currentX >= patrolRightX;
        boolean gotBlocked = Math.abs(actualMove) < 0.1f;

        if ((hitLeftBoundary && direction < 0) ||
                (hitRightBoundary && direction > 0) ||
                gotBlocked) {
            direction *= -1;
            facing = (direction > 0) ? Direction.RIGHT : Direction.LEFT;
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        int hitboxX = 5;
        int hitboxY = 20;
        int hitboxW = 20;
        int hitboxH = 14;

        int walkFrameDelay = 10;

        return new HashMap<String, Frame[]>() {
            {
                put("WALK_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 3), walkFrameDelay)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 4), walkFrameDelay)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 5), walkFrameDelay)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 6), walkFrameDelay)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build()
                });

                put("WALK_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 3), walkFrameDelay)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 4), walkFrameDelay)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 5), walkFrameDelay)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 6), walkFrameDelay)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build()
                });

                put("JUMP_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(1, 2), 9)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 3), 9)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 4), 9)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 5), 9)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 6), 9)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build()
                });

                put("JUMP_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(1, 2), 9)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 3), 9)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 4), 9)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 5), 9)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 6), 9)
                                .withScale(SCALE)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build()
                });

                put("DEATH", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(3, 0), 10)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(3, 1), 10)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(3, 2), 10)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(3, 3), 10)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(3, 4), 10)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(3, 5), 10)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(3, 6), 10)
                                .withScale(SCALE)
                                .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                                .build()
                });
            }
        };
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

    // draw floating damage numbers
    public void drawFloatingTexts(GraphicsHandler graphicsHandler, float cameraX, float cameraY) {
        for (FloatingText text : new ArrayList<>(floatingTexts)) {
            text.draw(graphicsHandler, cameraX, cameraY);
        }
    }

    // collision box for bee attacks
    public java.awt.Rectangle getHitbox() {
        int w = 20 * SCALE;
        int h = 14 * SCALE;
        int offsetX = 6 * SCALE;
        int offsetY = 10 * SCALE;
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

    // get current frame of hit flash animation
    public int getAttackFxFrame(int frames) {
        if (!isShowingAttackFx() || frames <= 1)
            return 0;
        long elapsed = System.currentTimeMillis() - attackFxStartTime;
        if (elapsed < 0)
            elapsed = 0;
        int idx = (int) ((elapsed * frames) / ATTACK_FX_DURATION);
        if (idx >= frames)
            idx = frames - 1;
        return idx;
    }
}
