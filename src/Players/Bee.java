package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.Player;
import SpriteImage.ResourceHUD;
import Utils.Direction;
import java.util.HashMap;

public class Bee extends Player {

    private static final int TILE = 64;
    private static final float SCALE = 2.5f;

    private static final int ROW_UP = 0;
    private static final int ROW_LEFT = 1;
    private static final int ROW_RIGHT = 2;
    private static final int ROW_DOWN = 3;

    private static final int ATTACK_ACTIVE_MS = 120;
    private static final int ATTACK_COOLDOWN_MS = 10;

    private boolean attacking = false;
    private long attackStart = 0L;
    private long lastAttackEnd = 0L;

    private boolean prevSpaceDown = false;

    private boolean isDead = false;
    private boolean deathAnimationComplete = false;

    // slash effect when taking damage
    private boolean showSlash = false;
    private long slashStartTime = 0;
    private static final long SLASH_DISPLAY_MS = 400;
    private static final long SLASH_SWITCH_MS = 200;
    private SpriteSheet slashSheet;
    
    private SpriteSheet attackFxSheet;

    protected static boolean isRaining = false;

    public Bee(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Bee_Walk.png"), TILE, TILE, 0),
                x, y,
                "STAND_DOWN");

        MOVE_LEFT_KEY = Key.A;
        MOVE_RIGHT_KEY = Key.D;
        MOVE_UP_KEY = Key.W;
        MOVE_DOWN_KEY = Key.S;

        walkSpeed = 10f;
        setHealth(100);
        setStamina(25);
        setNectar(10);
        setExperience(5);
        resourceBars = new ResourceHUD(this);

        try {
            slashSheet = new SpriteSheet(ImageLoader.load("spider_slash.png"), 32, 32);
            System.out.println("Bee: Slash sprite loaded!");
        } catch (Exception e) {
            System.out.println("Bee: ERROR loading slash sprite: " + e.getMessage());
            slashSheet = null;
        }
        
        try {
            attackFxSheet = new SpriteSheet(ImageLoader.load("bee_attack1.png"), 32, 32);
            System.out.println("Bee: Attack FX sprite loaded!");
        } catch (Exception e) {
            System.out.println("Bee: ERROR loading attack FX sprite: " + e.getMessage());
            attackFxSheet = null;
        }
    }

    // called by spider to damage bee
    public void applyDamage(int amount) {
        if (isDead) return;
        
        int currentHealth = getHealth();
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
        setHealth(currentHealth);
        
        System.out.println("Bee took " + amount + " damage! HP now: " + currentHealth);
        
        if (amount > 0) {
            showSlash = true;
            slashStartTime = System.currentTimeMillis();
            
            // trigger screen shake
            if (map != null && map.getCamera() != null) {
                map.getCamera().shake();
            }
        }
        
        if (currentHealth <= 0 && !isDead) {
            isDead = true;
            walkSpeed = 0f;
            System.out.println("Bee died! Playing death animation...");
        }
    }

    @Override
    public void update() {
        super.update();

        handleAttackInput();

        resourceBars.update(this);
        System.out.println(String.format("Health: %d  Stamina: %d  Nectar: %d  Experience: %d  Speed: %f",
                this.getHealth(), this.getStamina(), this.getNectar(), this.getExperience(), this.getWalkSpeed()));

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

    public java.awt.Rectangle getAttackHitbox() {
        if (!isAttacking()) {
            return new java.awt.Rectangle(0, 0, 0, 0);
        }

        int beeW = Math.round(TILE * SCALE);
        int beeH = Math.round(TILE * SCALE);
        int beeCenterX = (int) getX() + beeW / 2;
        int beeCenterY = (int) getY() + beeH / 2;

        final int ATTACK_SIZE = 35;
        final int REACH = 15;

        int x = beeCenterX - ATTACK_SIZE / 2;
        int y = beeCenterY - ATTACK_SIZE / 2;

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
        }

        return new java.awt.Rectangle(x, y, ATTACK_SIZE, ATTACK_SIZE);
    }

    @Override
    protected void handlePlayerAnimation() {
        if (isDead) {
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

        // bigger slash effect when hit
        if (showSlash && slashSheet != null) {
            long currentTime = System.currentTimeMillis();
            long slashElapsed = currentTime - slashStartTime;

            if (slashElapsed < SLASH_DISPLAY_MS) {
                java.awt.image.BufferedImage slashImage = slashSheet.getSprite(0, 0);

                float cameraX = map.getCamera().getX();
                float cameraY = map.getCamera().getY();
                
                // bigger slash - 80 pixels instead of 40
                int slashSize = 80;
                // bee is 160 pixels, center the bigger slash: (160-80)/2 = 40
                int slashX = Math.round(this.x - cameraX + 40);
                int slashY = Math.round(this.y - cameraY + 40);

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
        SpriteSheet deathSheet = new SpriteSheet(ImageLoader.load("Bee_Death.png"), TILE, TILE, 0);

        int hbX = Math.round(10 * SCALE), hbY = Math.round(8 * SCALE);
        int hbW = Math.round(5 * SCALE), hbH = Math.round(5 * SCALE);

        HashMap<String, Frame[]> map = new HashMap<>();

        map.put("STAND_UP", frames(idleSheet, ROW_UP, 0, 3, 7, hbX, hbY, hbW, hbH));
        map.put("STAND_LEFT", frames(idleSheet, ROW_LEFT, 0, 3, 7, hbX, hbY, hbW, hbH));
        map.put("STAND_RIGHT", frames(idleSheet, ROW_RIGHT, 0, 3, 7, hbX, hbY, hbW, hbH));
        map.put("STAND_DOWN", frames(idleSheet, ROW_DOWN, 0, 3, 7, hbX, hbY, hbW, hbH));

        map.put("WALK_UP", frames(walkSheet, ROW_UP, 0, 3, 14, hbX, hbY, hbW, hbH));
        map.put("WALK_LEFT", frames(walkSheet, ROW_LEFT, 0, 3, 14, hbX, hbY, hbW, hbH));
        map.put("WALK_RIGHT", frames(walkSheet, ROW_RIGHT, 0, 3, 14, hbX, hbY, hbW, hbH));
        map.put("WALK_DOWN", frames(walkSheet, ROW_DOWN, 0, 3, 14, hbX, hbY, hbW, hbH));

        map.put("ATTACK_UP", frames(attackSheet, ROW_UP, 0, 2, 6, hbX, hbY, hbW, hbH));
        map.put("ATTACK_LEFT", frames(attackSheet, ROW_LEFT, 0, 2, 6, hbX, hbY, hbW, hbH));
        map.put("ATTACK_RIGHT", frames(attackSheet, ROW_RIGHT, 0, 2, 6, hbX, hbY, hbW, hbH));
        map.put("ATTACK_DOWN", frames(attackSheet, ROW_DOWN, 0, 2, 6, hbX, hbY, hbW, hbH));

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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getNectar() {
        return nectar;
    }

    public void setNectar(int nectar) {
        this.nectar = nectar;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public boolean isDead() {
        return isDead;
    }
    
    public boolean isDeathAnimationComplete() {
        if (!isDead) return false;
        
        Frame[] deathAnim = animations.get("DEATH");
        if (deathAnim == null) return true;
        
        return currentFrame == deathAnim[deathAnim.length - 1];
    }

}