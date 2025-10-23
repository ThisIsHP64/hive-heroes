package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
import Game.GameState;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.Map;
import Level.Player;
import SpriteImage.PowerupHUD;
import SpriteImage.ResourceHUD;
import StaticClasses.BeeStats;
import StaticClasses.HiveManager;
import StaticClasses.TeleportManager;
import Utils.Direction;
import java.util.HashMap;

public class Bee extends Player {

    private static final int TILE = 64; // frames are 64x64
    private static final float SCALE = 2.5f; // resize bee (2.0–3.0)

    // row mapping in BOTH sheets (0-based)
    private static final int ROW_UP = 0;
    private static final int ROW_LEFT = 1;
    private static final int ROW_RIGHT = 2;
    private static final int ROW_DOWN = 3;

    // attack timing
    private static final int ATTACK_ACTIVE_MS = 120; // sting window
    private static final int ATTACK_COOLDOWN_MS = 10; // delay before next attack

    private boolean attacking = false;
    private long attackStart = 0L;
    private long lastAttackEnd = 0L;

    private boolean prevSpaceDown = false; // edge detection

    // Death state - tracks if bee is dead and playing death animation
    private boolean deathAnimationComplete = false;

    // Slash effect when taking damage
    private boolean showSlash = false;
    private long slashStartTime = 0;
    private static final long SLASH_DISPLAY_MS = 400; // total duration
    private static final long SLASH_SWITCH_MS = 200;  // when to flip direction
    private SpriteSheet slashSheet;
    
    // Attack FX when hitting enemies
    private SpriteSheet attackFxSheet;

    protected static boolean isRaining = false;


    // Necessary for the boost logic
    private boolean hasPowerup = false;
    private boolean boostActive = false;
    private long boostStartTime = 0L;
    private float originalSpeed = 0f;
    private String powerupIconPath;
    private static final int BOOST_DURATION_MS = 10_000;
    private static final float BOOST_MULTIPLIER = 2.0f;

    // Shield variables
    private boolean hasShield = false;
    private int shieldHealth = 0;
    private static final int MAX_SHIELD_HEALTH = 100;

    private PowerupHUD powerupHUD;

    public Bee(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Bee_Walk.png"), TILE, TILE, 0),
                x, y, "STAND_DOWN");

        // Controls: WASD
        MOVE_LEFT_KEY = Key.A;
        MOVE_RIGHT_KEY = Key.D;
        MOVE_UP_KEY = Key.W;
        MOVE_DOWN_KEY = Key.S;

        walkSpeed = 10f;

        resourceBars = new ResourceHUD(this);

        powerupHUD = new PowerupHUD();

        // Load slash sprite for when bee takes damage
        try {
            slashSheet = new SpriteSheet(ImageLoader.load("spider_slash.png"), 32, 32);
            System.out.println("Bee: Slash sprite loaded!");
        } catch (Exception e) {
            System.out.println("Bee: ERROR loading slash sprite: " + e.getMessage());
            slashSheet = null;
        }
        
        // Load attack FX sprite for when bee hits enemies
        try {
            attackFxSheet = new SpriteSheet(ImageLoader.load("bee_attack1.png"), 32, 32);
            System.out.println("Bee: Attack FX sprite loaded!");
        } catch (Exception e) {
            System.out.println("Bee: ERROR loading attack FX sprite: " + e.getMessage());
            attackFxSheet = null;
        }
    }

    // Called by enemies to damage the bee
    public void applyDamage(int amount) {
        if (BeeStats.isDead()) return; // already dead, can't take more damage
        
        // Logic to check shield first when the powerup is picked up
        // Check if shield can absorb damage first
        if (hasShield && shieldHealth > 0) {
            shieldHealth -= amount;
            System.out.println("Shield absorbed " + amount + " damage! Shield left: " + shieldHealth);

        if (shieldHealth <= 0) {
            hasShield = false;
            shieldHealth = 0;
            System.out.println("Shield depleted!");

            // remove shield icon when it's gone
            if (powerupHUD != null) {
                powerupHUD.removeIcon("shield_icon.png");
            }
        }
        return; // stop here so health isn't reduced
    }


        int currentHealth = BeeStats.getHealth();
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
        BeeStats.setHealth(currentHealth);
        
        System.out.println("Bee took " + amount + " damage! HP now: " + currentHealth);
        
        // Trigger slash effect when damaged
        if (amount > 0) {
            showSlash = true;
            slashStartTime = System.currentTimeMillis();

            // trigger screen shake
            if (map != null && map.getCamera() != null) {
                map.getCamera().shake();
            }
        }
        
        // Check if this killed the bee
        if (currentHealth <= 0) {
            System.out.println(BeeStats.isDead());
            BeeStats.setDead(true);
            BeeStats.setWalkSpeed(0); // freeze movement
            System.out.println("Bee died! Playing death animation...");
        }
    }

    // Powerup activation logic
    public void collectPowerup(String iconPath) {
        hasPowerup = true;
        powerupIconPath = iconPath;
        showPowerupIcon(iconPath, 999999);
        System.out.println("Bee collected power-up! (Press 1 to activate)");
    }

    @Override
    public void update() {
        super.update();

        // System.out.println(this.getWidth());
        // System.out.println(this.getHeight());
        

        handleAttackInput();

        // both the Bee instance/class and ResourceHUD class have access to the get
        // methods for the resources.
        resourceBars.update();
        int tileX = (int)(getX() / TILE);
        int tileY = (int)(getY() / TILE);

        // for positioning triggers
        // System.out.println(getX() + ", " + getY());

        // if((tileX == 49 || tileX == 50) && tileY == 36 && keyLocker.isKeyLocked(Key.SPACE) && BeeStats.getNectar() > 0) {
        //     if (TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL) {
        //         BeeStats.setNectar(BeeStats.getNectar() - 1);
        //         HiveManager.depositNectar();
        //     }
        //     TeleportManager.setCurrentScreen(GameState.HIVELEVEL);
        // }


        System.out.println(String.format(
                "Health: %d  Stamina: %d  Nectar: %d  Experience: %d  Speed: %f  Hive Nectar: %d  X: %d  Y: %d",
                BeeStats.getHealth(), BeeStats.getStamina(), BeeStats.getNectar(), BeeStats.getExperience(), BeeStats.getWalkSpeed(), HiveManager.getNectar(), tileX, tileY
        ));

        if (attacking && System.currentTimeMillis() - attackStart > ATTACK_ACTIVE_MS) {
            attacking = false;
            lastAttackEnd = System.currentTimeMillis();
        }

        // needs to be checked every frame (powerup)
        handlePowerupInput();
    }


    public void showPowerupIcon(String spritePath, int durationMs) {
        if (powerupHUD != null) {
            powerupHUD.show(spritePath, durationMs);
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

    // smaller, more accurate attack hitbox - bee needs to be close
    public java.awt.Rectangle getAttackHitbox() {
        if (!isAttacking()) {
            return new java.awt.Rectangle(0, 0, 0, 0);
        }

        // bee renders at roughly 160x160 with SCALE=2.5, center is around 80,80
        int beeW = Math.round(TILE * SCALE);
        int beeH = Math.round(TILE * SCALE);
        int beeCenterX = (int) getX() + beeW / 2;
        int beeCenterY = (int) getY() + beeH / 2;

        // smaller attack box - bee needs to be closer
        final int ATTACK_SIZE = 35;
        final int REACH = 15; // closer range

        int x = beeCenterX - ATTACK_SIZE / 2;
        int y = beeCenterY - ATTACK_SIZE / 2;

        // push the box forward based on facing direction
        switch (getFacingDirection()) {
            case RIGHT:
                x += REACH;
                break;
            case LEFT:
                x -= REACH;
                break;
            case UP:
                y -= REACH;
                break;
            case DOWN:
                y += REACH;
                break;
            case NONE:
                break;
        }

        return new java.awt.Rectangle(x, y, ATTACK_SIZE, ATTACK_SIZE);
    }

    @Override
    protected void handlePlayerAnimation() {
        // Death animation overrides everything else
        if (BeeStats.isDead()) {
            currentAnimationName = "DEATH";
            return;
        }
        
        if (attacking) {
            currentAnimationName = "ATTACK_" + facingDirection.name();
            return;
        }

        boolean walking = (currentWalkingYDirection != Direction.NONE) ||
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
        resourceBars.draw(graphicsHandler);

        if (powerupHUD != null) powerupHUD.draw(graphicsHandler);


        // slash shows when we get hit
        // bigger slash effect when hit
        if (showSlash && slashSheet != null) {
            long currentTime = System.currentTimeMillis();
            long slashElapsed = currentTime - slashStartTime;

            if (slashElapsed < SLASH_DISPLAY_MS) {
                java.awt.image.BufferedImage slashImage = slashSheet.getSprite(0, 0);

                // get camera-adjusted position
                float cameraX = map.getCamera().getX();
                float cameraY = map.getCamera().getY();
                
                // center slash on bee's body - bee is 64*2.5 = 160 pixels, slash is 40 pixels
                // offset horizontally by (160-40)/2 = 60, vertically shifted up by 20
                int slashSize = 40;
                int slashX = Math.round(this.x - cameraX + 60);
                int slashY = Math.round(this.y - cameraY + 40);

                // flip slash halfway through for double-slash effect
                boolean firstSlash = slashElapsed < SLASH_SWITCH_MS;

                if (firstSlash) {
                    graphicsHandler.drawImage(slashImage, slashX, slashY, slashSize, slashSize);
                } else {
                    graphicsHandler.drawImage(slashImage, slashX + slashSize, slashY, -slashSize, slashSize);
                }
            } else {
                showSlash = false;
            }
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet walkSheet) {
        SpriteSheet idleSheet = new SpriteSheet(ImageLoader.load("Bee_Idle.png"), TILE, TILE, 0);
        SpriteSheet attackSheet = new SpriteSheet(ImageLoader.load("Bee_Attack.png"), TILE, TILE, 0);
        SpriteSheet deathSheet = new SpriteSheet(ImageLoader.load("Bee_Death.png"), TILE, TILE, 0); // death sprite

        int hbX = Math.round(10 * SCALE), hbY = Math.round(8 * SCALE);
        int hbW = Math.round(5 * SCALE), hbH = Math.round(5 * SCALE);

        HashMap<String, Frame[]> map = new HashMap<>();

        // IDLE hover animations
        map.put("STAND_UP", frames(idleSheet, ROW_UP, 0, 3, 7, hbX, hbY, hbW, hbH));
        map.put("STAND_LEFT", frames(idleSheet, ROW_LEFT, 0, 3, 7, hbX, hbY, hbW, hbH));
        map.put("STAND_RIGHT", frames(idleSheet, ROW_RIGHT, 0, 3, 7, hbX, hbY, hbW, hbH));
        map.put("STAND_DOWN", frames(idleSheet, ROW_DOWN, 0, 3, 7, hbX, hbY, hbW, hbH));

        // WALK animations
        map.put("WALK_UP", frames(walkSheet, ROW_UP, 0, 3, 14, hbX, hbY, hbW, hbH));
        map.put("WALK_LEFT", frames(walkSheet, ROW_LEFT, 0, 3, 14, hbX, hbY, hbW, hbH));
        map.put("WALK_RIGHT", frames(walkSheet, ROW_RIGHT, 0, 3, 14, hbX, hbY, hbW, hbH));
        map.put("WALK_DOWN", frames(walkSheet, ROW_DOWN, 0, 3, 14, hbX, hbY, hbW, hbH));

        // ATTACK animations
        map.put("ATTACK_UP", frames(attackSheet, ROW_UP, 0, 2, 6, hbX, hbY, hbW, hbH));
        map.put("ATTACK_LEFT", frames(attackSheet, ROW_LEFT, 0, 2, 6, hbX, hbY, hbW, hbH));
        map.put("ATTACK_RIGHT", frames(attackSheet, ROW_RIGHT, 0, 2, 6, hbX, hbY, hbW, hbH));
        map.put("ATTACK_DOWN", frames(attackSheet, ROW_DOWN, 0, 2, 6, hbX, hbY, hbW, hbH));

        // DEATH - 4x4 grid (16 total frames)
        // reads left to right, top to bottom
        Frame[] deathFrames = new Frame[16];
        int frameIdx = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                deathFrames[frameIdx] = new FrameBuilder(deathSheet.getSprite(row, col), 10)
                        .withScale(SCALE)
                        .withBounds(hbX, hbY, hbW, hbH)
                        .build();
                frameIdx++;
            }
        }
        map.put("DEATH", deathFrames);

        return map;
    }

    // Helper to create frame arrays from sprite sheet rows
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


    // Getter for death state - useful for game over checks
    public boolean isDead() {
        return BeeStats.isDead();
    }
    
    // check if death animation finished playing (16 frames at 10 delay each = 160 ticks)
    public boolean isDeathAnimationComplete() {
        if (!BeeStats.isDead()) return false;
        
        // death animation has 16 frames at 10 delay each
        // after animation completes, we're ready for game over
        Frame[] deathAnim = animations.get("DEATH");
        if (deathAnim == null) return true;
        
        // if we're on the last frame of death animation, it's complete
        return currentFrame == deathAnim[deathAnim.length - 1];
    }

    // Helper method for the PowerUp
    public void handlePowerupInput() {
        if (hasPowerup && Keyboard.isKeyDown(Key.ONE)) {
            System.out.println("Bee activated power-up! Speed boost for 10s!");
            hasPowerup = false;

            // remove the speed icon
            if (powerupHUD != null) {
                powerupHUD.removeIcon("speed_icon.png");
            }

            // apply speed boost
            originalSpeed = BeeStats.getWalkSpeed();
            BeeStats.setWalkSpeed(originalSpeed * BOOST_MULTIPLIER);
            boostStartTime = System.currentTimeMillis();
            boostActive = true;
        }

    if (boostActive) {
        long elapsed = System.currentTimeMillis() - boostStartTime;
        if (elapsed > BOOST_DURATION_MS) {
            BeeStats.setWalkSpeed(originalSpeed);
            boostActive = false;
            System.out.println("Speed boost ended!");
            }
        }
    }

    // Activate Shield Powerup logic
    public void activateShield(String iconPath) {
        hasShield = true;
        shieldHealth = MAX_SHIELD_HEALTH;
        showPowerupIcon(iconPath, 999999); // show icon indefinitely
        System.out.println("Shield activated! (" + shieldHealth + " HP)");
    }

    // Helper and Getters for the shield powerup
    public boolean hasShield() {
        return hasShield;
    }

    public int getShieldHealth() {
        return shieldHealth;
    }

    public int getMaxShieldHealth() {
        return MAX_SHIELD_HEALTH;
    }

}