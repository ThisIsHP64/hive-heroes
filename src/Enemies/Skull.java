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
import Players.Bee;
import Utils.Direction;
import Utils.Point;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Skull extends NPC {

    private static final int TILE_W = 64; 
    private static final int TILE_H = 64;
    private static final int SCALE = 2;     

    // Movement speeds - BALANCED for challenging but fair gameplay
    private static final float PATROL_SPEED = 0.6f;   // Slower patrol (was 0.8f)
    private static final float CHASE_SPEED = 1.2f;    // Reduced chase speed (was 1.5f)
    
    // Detection distances - Slightly tighter range
    private static final float CHASE_RANGE = 140f;    // Reduced detection (was 150f)
    private static final float GIVE_UP_RANGE = 220f;  // Gives up sooner (was 250f)
    private static final float ATTACK_RANGE = 35f;    // Need to get closer (was 40f)
    
    // Combat settings - BALANCED damage and cooldown
    private static final int DAMAGE = 8;                      // Less damage (was 10)
    private static final long ATTACK_COOLDOWN_MS = 1200;      // Slower attacks (was 1000ms)
    private static final long DEATH_LINGER_MS = 1000;
    
    // Spawn timing
    private final long spawnTime = System.currentTimeMillis();
    private static final long STARTUP_FREEZE_MS = 1000;
    private static final long CHASE_ENABLE_MS = 1500;
    private boolean hasMovedAwayFromSpawn = false;

    // Patrol zone boundaries
    private float patrolLeftX;
    private float patrolRightX;
    
    // Patrol direction tracker
    private int direction = 1;
    
    private Direction facing = Direction.RIGHT;

    private int health = 35;  // Reduced health (was 40) - dies faster
    private boolean isDead = false;
    private long deathTime = 0;
    private long lastAttackTime = 0;

    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();
    
    // State machine for skull behavior
    private enum State {
        PATROL, CHASE, ATTACK, DEAD
    }
    
    private State currentState = State.PATROL;

    public Skull(Point location) {
        super(0, location.x, location.y,
                new SpriteSheet(ImageLoader.load("Bones_SingleSkull_Fly.png"), TILE_W, TILE_H, 0),
                "FLY_RIGHT");

        float patrolRange = 96f;
        this.patrolLeftX = location.x - patrolRange;
        this.patrolRightX = location.x + patrolRange;
        
        System.out.println("Skull spawned at: " + location.x + ", " + location.y);
        System.out.println("Patrol range: " + patrolLeftX + " to " + patrolRightX);
    }

    // damage
    public void takeDamage(int amount) {
        if (isDead) return;
        health -= amount;
        
        float textX = getX() + (TILE_W * SCALE) / 2f;
        float textY = getY();
        floatingTexts.add(new FloatingText(textX, textY, "-" + amount, Color.RED));
        
        System.out.println("Skull took " + amount + " damage! HP: " + health);
        
        if (health <= 0) die();
    }

    private void die() {
        if (isDead) return;
        isDead = true;
        deathTime = System.currentTimeMillis();
        currentState = State.DEAD;
        System.out.println("Skull destroyed!");
    }
    
    public boolean isDead() {
        return isDead;
    }

    public boolean shouldRemove() {
        return isDead && (System.currentTimeMillis() - deathTime) > DEATH_LINGER_MS;
    }

    @Override
    public void update(Player player) {
        // Update floating damage numbers
        floatingTexts.removeIf(text -> {
            text.update();
            return text.isDead();
        });

        if (isDead) {
            super.update(player);
            return;
        }
        
        // Startup freeze period
        long timeSinceSpawn = System.currentTimeMillis() - spawnTime;
        if (timeSinceSpawn < STARTUP_FREEZE_MS) {
            currentAnimationName = "FLY_RIGHT";
            if (animations.get(currentAnimationName) != null) {
                currentFrame = animations.get(currentAnimationName)[0];
            }
            return;
        }

        super.update(player);
    }
    
    @Override
    public void performAction(Player player) {
        if (isDead) return;
        
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
                
                if (timeSinceSpawn >= CHASE_ENABLE_MS &&
                    hasMovedAwayFromSpawn &&
                    distanceToBee < CHASE_RANGE &&
                    beeInTerritory) {
                    currentState = State.CHASE;
                    System.out.println("Skull spotted bee! Starting chase...");
                }
                break;
                
            case CHASE:
                chase(player);
                
                // Check if close enough to attack
                if (distanceToBee < ATTACK_RANGE) {
                    tryAttack(player, currentTime);
                }
                
                // Give up if bee gets too far or leaves territory
                if (distanceToBee > GIVE_UP_RANGE || !beeInTerritory) {
                    currentState = State.PATROL;
                    System.out.println("Bee escaped! Returning to patrol...");
                }
                break;
                
            case ATTACK:
                // Continue chasing while attacking
                chase(player);
                tryAttack(player, currentTime);
                
                // Return to chase if out of attack range
                if (distanceToBee > ATTACK_RANGE) {
                    currentState = State.CHASE;
                }
                
                // Give up if bee gets too far
                if (distanceToBee > GIVE_UP_RANGE || !beeInTerritory) {
                    currentState = State.PATROL;
                    System.out.println("Bee escaped! Returning to patrol...");
                }
                break;
                
            case DEAD:
                break;
        }
    }
    
    // Calculate straight line distance to player
    private float getDistanceToBee(Player player) {
        float dx = player.getX() - getX();
        float dy = player.getY() - getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
    
    // Move back and forth in patrol zone
    private void patrol() {
        float moveAmount = direction * PATROL_SPEED;
        float actualMove = moveXHandleCollision(moveAmount);
        
        facing = (direction > 0) ? Direction.RIGHT : Direction.LEFT;
        currentAnimationName = (facing == Direction.RIGHT) ? "FLY_RIGHT" : "FLY_LEFT";
        
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
    
    // Move toward player
    private void chase(Player player) {
        float beeX = player.getX();
        float beeY = player.getY();
        float skullX = getX();
        float skullY = getY();
        
        float dx = beeX - skullX;
        float dy = beeY - skullY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0.1f) {
            float moveX = (dx / distance) * CHASE_SPEED;
            float moveY = (dy / distance) * CHASE_SPEED;
            
            moveXHandleCollision(moveX);
            moveYHandleCollision(moveY);
            
            if (Math.abs(dx) > 10) {
                Direction newFacing = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
                if (newFacing != facing) {
                    facing = newFacing;
                }
            }
        }
        
        currentAnimationName = (facing == Direction.RIGHT) ? "FLY_RIGHT" : "FLY_LEFT";
    }

    // attack
    private void tryAttack(Player player, long currentTime) {
        if (currentTime - lastAttackTime < ATTACK_COOLDOWN_MS) return;

        if (player instanceof Bee bee) {
            float dx = bee.getX() - getX();
            float dy = bee.getY() - getY();
            float dist = (float)Math.sqrt(dx * dx + dy * dy);

            if (dist < ATTACK_RANGE) {
                bee.applyDamage(DAMAGE);
                lastAttackTime = currentTime;
                currentState = State.ATTACK;
                System.out.println("Skull hit Bee for " + DAMAGE + " damage!");
            }
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> map = new HashMap<>();

        // Collision bounds - much smaller to fit just the skull head
        int hitboxX = 23;  // offset from left
        int hitboxY = 23;  // offset from top
        int hitboxW = 18;  // width of hitbox
        int hitboxH = 18;  // height of hitbox

        // Sprite is a 3x3 grid (192x192 sheet, 64x64 per frame)
        Frame[] flyRight = new Frame[9];
        Frame[] flyLeft = new Frame[9];

        int frameIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Frame f = new FrameBuilder(spriteSheet.getSprite(row, col), 8)
                        .withScale(SCALE)
                        .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                        .build();
                flyRight[frameIndex] = f;

                Frame fFlip = new FrameBuilder(spriteSheet.getSprite(row, col), 8)
                        .withScale(SCALE)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(hitboxX, hitboxY, hitboxW, hitboxH)
                        .build();
                flyLeft[frameIndex] = fFlip;

                frameIndex++;
            }
        }

        map.put("FLY_RIGHT", flyRight);
        map.put("FLY_LEFT", flyLeft);
        return map;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (isDead) {
            long elapsed = System.currentTimeMillis() - deathTime;
            float alpha = Math.max(0f, 1f - elapsed / (float)DEATH_LINGER_MS);
            Graphics2D g2d = graphicsHandler.getGraphics();
            Composite old = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.draw(graphicsHandler);
            g2d.setComposite(old);
        } else {
            super.draw(graphicsHandler);
        }

        if (map != null && map.getCamera() != null) {
            float camX = map.getCamera().getX();
            float camY = map.getCamera().getY();
            for (FloatingText t : floatingTexts)
                t.draw(graphicsHandler, camX, camY);
        }
    }
    
    // Collision box for bee attacks
    public java.awt.Rectangle getHitbox() {
        int w = 18 * SCALE;      // 36 pixels wide
        int h = 18 * SCALE;      // 36 pixels tall
        int offsetX = 23 * SCALE; // 46 pixel offset to center
        int offsetY = 23 * SCALE; // 46 pixel offset to center
        return new java.awt.Rectangle(
                (int) getX() + offsetX,
                (int) getY() + offsetY,
                w, h);
    }
}