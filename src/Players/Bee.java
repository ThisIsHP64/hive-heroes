package Players;

import Builders.FrameBuilder;
import Effects.FloatingText;
import Enemies.Bat; // ADDED: For clearing screen effects on death
import Enemies.Crab;
import Enemies.FrostDragon;
import Enemies.Goblin;
import Enemies.Spider;
import Engine.*;
import Flowers.*;
import Game.GameState;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.MapTile;
import Level.Player;
import Level.TileType;
import Projectiles.BeeProjectile;
import Sound.SFX;
import Sound.SFXManager;
import SpriteImage.PowerupHUD;
import SpriteImage.ProjectileHUD;
import SpriteImage.ResourceHUD;
import StaticClasses.BeeStats;
import StaticClasses.HiveManager;
import StaticClasses.TeleportManager;
import Utils.Direction;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Bee extends Player {

    private static final int TILE = 64;
    private static final float SCALE = 2.5f;

    // row mapping in BOTH sheets (0-based)
    private static final int ROW_UP = 0;
    private static final int ROW_LEFT = 1;
    private static final int ROW_RIGHT = 2;
    private static final int ROW_DOWN = 3;

    // attack timing
    private static final int ATTACK_ACTIVE_MS = 120;
    private static final int ATTACK_COOLDOWN_MS = 10;

    // ADDED: melee damage amount + one-hit latch per swing
    private static final int MELEE_DAMAGE = 15;
    private boolean meleeDealtThisSwing = false;

    private boolean attacking = false;
    private long attackStart = 0L;
    private long lastAttackEnd = 0L;

    private boolean prevSpaceDown = false;

    private boolean deathAnimationComplete = false;

    // Slash effect when taking damage
    private boolean showSlash = false;
    private long slashStartTime = 0;
    private static final long SLASH_DISPLAY_MS = 400;
    private static final long SLASH_SWITCH_MS = 200;
    private SpriteSheet slashSheet;

    private SpriteSheet attackFxSheet;

    protected static boolean isRaining = false;

    // Necessary for the boost logic
    private boolean hasPowerup = false;
    private boolean boostActive = false;
    private long boostStartTime = 0L;
    private float originalSpeed = 0f;
    private String powerupIconPath;
    private static final int BOOST_DURATION_MS = 20_000;
    private static final float BOOST_MULTIPLIER = 2.0f;

    // Shield variables
    private boolean hasShield = false;
    private int shieldHealth = 0;
    private static final int MAX_SHIELD_HEALTH = 100;
    
    // Projectile powerup variable (session flag; persistent lives in BeeStats)
    private boolean hasProjectile = false; // we keep this for local convenience, but real truth is in BeeStats

    // --- projectile tuning / HUD ---
    private static final int PROJECTILE_DAMAGE = 25; // tweak to taste
    private static final String PROJECTILE_HUD_ICON = "Bee_Projectile_HUD.png"; // your HUD icon

    // tunic variable
    private boolean useRedSprites = false;

    // tunic blue variable
    private boolean useBlueSprites = false;

    // --- Fix for stuck attack animation ---
    private boolean waitingForSpaceRelease = false; // prevents attack looping when holding SPACE
    private static final long ATTACK_DURATION_MS = 250; // attack lasts ~¼ second before reset

    private PowerupHUD powerupHUD;
    private ProjectileHUD projectileHUD;
    
    // Projectile list
    private ArrayList<BeeProjectile> activeProjectiles = new ArrayList<>();
    private long lastShotTime = 0;
    
    protected MapTile[] mapTiles;

    // floating damage numbers when bee takes damage
    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();


    int lastMilestone = 0;

    public Bee(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Bee_Walk.png"), TILE, TILE, 0),
                x, y, "STAND_DOWN");

        MOVE_LEFT_KEY = Key.A;
        MOVE_RIGHT_KEY = Key.D;
        MOVE_UP_KEY = Key.W;
        MOVE_DOWN_KEY = Key.S;

        BeeStats.setWalkSpeed(6f);

        resourceBars = new ResourceHUD(this);

        powerupHUD = new PowerupHUD();
        projectileHUD = new ProjectileHUD();

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

        if (BeeStats.hasTunic()) {
            powerupHUD.show("RedTunic_hud.png", Integer.MAX_VALUE);
            if (BeeStats.isTunicActive()) {
                useRedSprites = true;
                swapToRedBeeSprites();
            }
        }

        if (BeeStats.hasBlueTunic()) {
            powerupHUD.show("BlueTunic_Hud.PNG", Integer.MAX_VALUE);

            if (BeeStats.isBlueTunicActive()) {
                useBlueSprites = true;
                swapToBlueBeeSprites();
            }
        }


        if (BeeStats.hasRing()) {
            powerupHUD.show("onering.png", Integer.MAX_VALUE);
        }

        // --- PERSISTENT PROJECTILE: if you ever unlocked it, keep it active + HUD on new maps ---
        if (BeeStats.hasProjectilePower()) {
            hasProjectile = true;
            if (projectileHUD != null) {
                projectileHUD.showProjectile(PROJECTILE_HUD_ICON);
            }
            System.out.println("[Bee] Projectile already unlocked from previous map. Restoring HUD + ability.");
        }
    }


    public int getNectar() {
        return BeeStats.getNectar();
    }

    public int getNectarCap() {
        return BeeStats.getMaxNectar();
    }

    public boolean isNectarFull() {
        return BeeStats.getNectar() >= BeeStats.getMaxNectar();
    }

    public int tryAddNectar(int amount) {
        if (amount <= 0)
            return 0;
        int before = BeeStats.getNectar();
        int cap = BeeStats.getMaxNectar();
        int after = Math.min(cap, before + amount);
        int added = after - before;
        if (added > 0) {
            BeeStats.setNectar(after);

            if (after >= cap) {
                StaticClasses.UnleashMayhem.fire(this.map, this);
            }
        }
        return added;
    }

    public int drainAllNectar() {
        int carried = BeeStats.getNectar();
        if (carried > 0)
            BeeStats.setNectar(0);
        return carried;
    }

    public void applyDamage(int amount) {
        if (BeeStats.isDead())
            return;

        SFXManager.playSFX(SFX.HURT);

        if (hasShield && shieldHealth > 0) {
            shieldHealth -= amount;
            System.out.println("Shield absorbed " + amount + " damage! Shield left: " + shieldHealth);

            // spawn blue damage number when shield absorbs damage
            float textX = getX() + (TILE * SCALE) / 2f;
            float textY = getY();
            floatingTexts.add(new FloatingText(textX, textY, "-" + amount, new Color(100, 200, 255)));

            if (shieldHealth <= 0) {
                hasShield = false;
                shieldHealth = 0;
                System.out.println("Shield depleted!");

                if (powerupHUD != null) {
                    powerupHUD.removeIcon("shield_icon.png");
                }
            }
            return;
        }

        int currentHealth = BeeStats.getHealth();
        currentHealth -= amount;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
        BeeStats.setHealth(currentHealth);

        System.out.println("Bee took " + amount + " damage! HP now: " + currentHealth);

        // spawn red damage number when health is damaged
        float textX = getX() + (TILE * SCALE) / 2f;
        float textY = getY();
        floatingTexts.add(new FloatingText(textX, textY, "-" + amount, Color.RED));

        if (amount > 0) {
            showSlash = true;
            slashStartTime = System.currentTimeMillis();

            if (map != null && map.getCamera() != null) {
                map.getCamera().shake();
            }
        }

        if (currentHealth <= 0) {
            System.out.println(BeeStats.isDead());
            BeeStats.setDead(true);
            BeeStats.setWalkSpeed(0);
            System.out.println("Bee died! Playing death animation...");
            
            // ADDED: Clear any screen effects when bee dies (darkness from ring horde)
            try {
                Effects.ScreenFX.start(Effects.ScreenFX.Effect.NONE, 0, 0f);
            } catch (Exception e) {
                // Fail silently if ScreenFX not available
            }
        }
    }

    public void collectPowerup(String iconPath) {
        hasPowerup = true;
        powerupIconPath = iconPath;
        showPowerupIcon(iconPath, 999999);
        System.out.println("Bee collected power-up! (Press 1 to activate)");
    }

    public void obtainTunic() {
        if (BeeStats.hasTunic()) return; // it will prevent icon duplication

        BeeStats.setHasTunic(true);
        if (powerupHUD != null) {
            powerupHUD.show("RedTunic_hud.png", Integer.MAX_VALUE);
        }

        if (BeeStats.isTunicActive()) {
            useRedSprites = true;
            swapToRedBeeSprites();
        }
        System.out.println("You received the Red Tunic");
    }

    public void showRingIcon() {
        if (BeeStats.hasRing() && powerupHUD != null) {
            powerupHUD.show("onering.png", Integer.MAX_VALUE);
            System.out.println("Ring icon shown in HUD!");
        }
    }

    public void obtainBlueTunic() {
        if (BeeStats.hasBlueTunic()) return; // prevent duplicate the icons

        BeeStats.setHasBlueTunic(true);

        if (powerupHUD != null) {
            powerupHUD.show("BlueTunic_Hud.PNG", Integer.MAX_VALUE);
        }

        System.out.println("You received the Blue Tunic! You can now transform into your frost form.");
    }


    public void spawnFlower() {

        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();

        int randomX = ThreadLocalRandom.current().nextInt(1, mapWidth - 1);
        int randomY = ThreadLocalRandom.current().nextInt(1, mapHeight - 1);

        int flowerNumber = ThreadLocalRandom.current().nextInt(1, 5);

        int distance = totalDistanceTraveled();
        

        if (TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL) {
        
        switch (flowerNumber) {
            case 1:
                RareSunflowerwithFlowers rareSunflower = new RareSunflowerwithFlowers(4, this.map.getMapTile(randomX, randomY).getLocation());

                if (distance - lastMilestone >= 750 && this.map.getMapTile(randomX, randomY).getTileType() == TileType.PASSABLE) {
                    this.map.addNPC(rareSunflower);
                    lastMilestone += 750;
                }

                break;

            case 2:
                BlueBorah blueBorah = new BlueBorah(4, this.map.getMapTile(randomX, randomY).getLocation());
                if (distance - lastMilestone >= 100 && this.map.getMapTile(randomX, randomY).getTileType() == TileType.PASSABLE) {
                    this.map.addNPC(blueBorah);
                    lastMilestone += 100;
                }
                break;

            case 3:
                Cosmo cosmo = new Cosmo(4, this.map.getMapTile(randomX, randomY).getLocation());
                if (distance - lastMilestone >= 100 && this.map.getMapTile(randomX, randomY).getTileType() == TileType.PASSABLE) {
                    this.map.addNPC(cosmo);
                    lastMilestone += 100;
                }
                break;

            case 4:
                Daffodil daffodil = new Daffodil(4, this.map.getMapTile(randomX, randomY).getLocation());
                if (distance - lastMilestone >= 100 && this.map.getMapTile(randomX, randomY).getTileType() == TileType.PASSABLE) {
                    this.map.addNPC(daffodil);
                    lastMilestone += 100;
                }
                break;

            case 5:
                Daisy daisy = new Daisy(4, this.map.getMapTile(randomX, randomY).getLocation());
                if (distance - lastMilestone >= 100 && this.map.getMapTile(randomX, randomY).getTileType() == TileType.PASSABLE) {
                    this.map.addNPC(daisy);
                    lastMilestone += 100;
                }
                break;
            }
        }
    }


    public int countFlowers() {
        int c = 0;
        for (var npc : this.map.getNPCs()) {
            if (npc instanceof Flower) {
                c++;
            }
        }
        return c;
    }

    @Override
    public void update() {
        super.update();

        BeeStats.checkLevelUp();

        // update floating damage numbers
        floatingTexts.removeIf(text -> {
            text.update();
            return text.isDead();
        });

        if (TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL) {
            spawnFlower();
        }

        // System.out.println("Current number of flowers on the map: " + countFlowers());
        // System.out.println(BeeStats.getStamina());

        System.out.println("Hive Nectar: " + HiveManager.getNectar());

        handleAttackInput();
        handleTunicInput();

        // ADDED: while the swing is active and we haven't hit yet, check for melee hits once
        if (attacking && !meleeDealtThisSwing) {
            long now = System.currentTimeMillis();
            long elapsed = now - attackStart;
            // sweet spot roughly mid-swing (don’t be too strict)
            if (elapsed >= 60 && elapsed <= ATTACK_ACTIVE_MS) {
                tryMeleeDamage();
            }
        }

        // Volcano fire damage logic
        if (TeleportManager.getCurrentGameState() == GameState.VOLCANOLEVEL
                && WeatherManager.GLOBAL.isRedRain()
                && !BeeStats.isTunicActive()) {

            applyDamage(1);
        }

        // frost slow-fast speed logic
        // if (TeleportManager.getCurrentGameState() == GameState.SNOWLEVEL) {

        //     // If Blue Tunic is NOT active then slow down the bee in the frost
        //     if (!BeeStats.isBlueTunicActive()) {
        //         BeeStats.setWalkSpeed(3f);   // slow and painful
        //     } 
        //     // If Blue Tunic IS active fasst
        //     else if (BeeStats.isBlueTunicActive()) {
        //         BeeStats.setWalkSpeed(10f);   // boosted speed in frost
        //     }

        //     else {
        //          BeeStats.setWalkSpeed(BeeStats.getWalkSpeed());
        //      }
        // } 

        resourceBars.update();
        
        if (attacking && System.currentTimeMillis() - attackStart > ATTACK_ACTIVE_MS) {
            attacking = false;
            lastAttackEnd = System.currentTimeMillis();
        }

        handlePowerupInput();
        
        // Update projectiles (now includes collision + damage)
        updateProjectiles();
        
        // // Regenerate stamina (only if haven't shot recently)
        // long timeSinceLastShot = System.currentTimeMillis() - lastShotTime;
        // if (timeSinceLastShot > 1000) {  // 1 second delay after shooting
        //     BeeStats.regenerateStamina(1);
        // }

    }

    public void showPowerupIcon(String spritePath, int durationMs) {
        if (powerupHUD != null) {
            powerupHUD.show(spritePath, durationMs);
        }
    }

    // Check if player is near any NPC (for prioritizing dialogue over shooting)
    private boolean isNearNPC() {
        if (map == null) return false;
        
        // Check all NPCs to see if any are close enough to interact with
        for (var npc : map.getNPCs()) {
            if (this.intersects(npc)) {
                return true;
            }
        }
        return false;
    }

    private void handleAttackInput() {
        long now = System.currentTimeMillis();

        // 1. End attack automatically after its duration passes
        if (attacking && (now - attackStart) >= ATTACK_DURATION_MS) {
            attacking = false;
            currentAnimationName = "STAND_" + facingDirection.name();
        }

        // 2. Read SPACE key state
        boolean spaceDown = Keyboard.isKeyDown(Key.SPACE);

        // 3. If waiting for SPACE to release, do nothing until key is up
        if (waitingForSpaceRelease) {
            if (!spaceDown) {
                waitingForSpaceRelease = false; // key released, ready next tap
            }
            prevSpaceDown = spaceDown; // keep this in sync
            return;
        }

        // 4. Edge trigger: only fire once per press
        boolean justPressed = spaceDown && !prevSpaceDown;

        // 5. Start attack if just pressed and not already attacking
        if (justPressed && !attacking) {
            attacking = true;
            attackStart = now;
            meleeDealtThisSwing = false; // ADDED: reset swing-latch
            currentAnimationName = "ATTACK_" + facingDirection.name();

            // Only shoot projectile if we have it AND we're not near an NPC
            // (NPCs take priority for SPACE interaction)
            // --- PERSISTENT PROJECTILE FIX: check BeeStats, not just local flag ---
            if (BeeStats.hasProjectilePower() && !isNearNPC()) {
                SFXManager.playSFX(SFX.SHOOT);
                shootProjectile();
            }

            waitingForSpaceRelease = true; // latch until key is fully released
        }

        prevSpaceDown = spaceDown; // update edge detector
    }

    // ADDED: melee hit check against enemies we actually have types for
    private void tryMeleeDamage() {
        if (map == null) return;

        java.awt.Rectangle atk = getAttackHitbox();
        if (atk.width <= 0 || atk.height <= 0) return;

        boolean hitSomething = false;

        for (var npc : new ArrayList<>(map.getNPCs())) {
            // Goblin
            if (npc instanceof Goblin) {
                Goblin g = (Goblin) npc;
                if (g.getHitbox().intersects(atk)) {
                    g.takeDamage(MELEE_DAMAGE);
                    hitSomething = true;
                }
            }
            // Frost Dragon
            else if (npc instanceof FrostDragon) {
                FrostDragon d = (FrostDragon) npc;
                // FrostDragon has getHitbox() in the version I gave you
                if (d.getHitbox().intersects(atk)) {
                    d.takeDamage(MELELE_DAMAGE_FIX()); // see helper below to avoid typo
                    hitSomething = true;
                }
            }
            // Spider
            else if (npc instanceof Spider) {
                Spider s = (Spider) npc;
                try {
                    if (s.getHitbox().intersects(atk)) {
                        s.takeDamage(MELEE_DAMAGE);
                        hitSomething = true;
                    }
                } catch (Throwable ignored) {}
            }
            // Bat
            else if (npc instanceof Bat) {
                Bat b = (Bat) npc;
                try {
                    if (b.getHitbox().intersects(atk)) {
                        b.takeDamage(MELEE_DAMAGE);
                        hitSomething = true;
                    }
                } catch (Throwable ignored) {}
            }
            // Crab
            else if (npc instanceof Enemies.Crab) {
                Crab c = (Crab) npc;
                try {
                    if (c.getHitbox().intersects(atk)) {
                        c.takeDamage(MELEE_DAMAGE);
                        hitSomething = true;
                    }
                } catch (Throwable ignored) {}
            }
        }

        if (hitSomething) {
            meleeDealtThisSwing = true;
            SFXManager.playSFX(SFX.STING);

            // little feedback
            if (map != null && map.getCamera() != null) {
                map.getCamera().shake();
            }
        }
    }

    // tiny shim to keep the code compiling if you paste quickly (spotted a typo hazard)
    private int MELELE_DAMAGE_FIX() { return MELEE_DAMAGE; }

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

        final int ATTACK_SIZE = 45;
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
            case NONE:
                break;
        }

        return new java.awt.Rectangle(x, y, ATTACK_SIZE, ATTACK_SIZE);
    }

    @Override
    protected void handlePlayerAnimation() {
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

        if (powerupHUD != null)
            powerupHUD.draw(graphicsHandler);
        
        if (projectileHUD != null)
            projectileHUD.draw(graphicsHandler);
        
        // Draw projectiles
        drawProjectiles(graphicsHandler);

        // draw floating damage numbers on bee
        if (map != null && map.getCamera() != null) {
            float cameraX = map.getCamera().getX();
            float cameraY = map.getCamera().getY();
            for (FloatingText text : new ArrayList<>(floatingTexts)) {
                text.draw(graphicsHandler, cameraX, cameraY);
            }
        }

        if (showSlash && slashSheet != null) {
            long currentTime = System.currentTimeMillis();
            long slashElapsed = currentTime - slashStartTime;

            if (slashElapsed < SLASH_DISPLAY_MS) {
                java.awt.image.BufferedImage slashImage = slashSheet.getSprite(0, 0);

                float cameraX = map.getCamera().getX();
                float cameraY = map.getCamera().getY();

                int slashSize = 40;
                int slashX = Math.round(this.x - cameraX + 60);
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
        boolean isRed = useRedSprites || BeeStats.isTunicActive();
        boolean isBlue = useBlueSprites || BeeStats.isBlueTunicActive();

        SpriteSheet idleSheet = new SpriteSheet(ImageLoader.load(
        isRed ? "Bee_Idle_Red.png" :
        isBlue ? "Bee_Idle_Blue.png" :
        "Bee_Idle.png"
        ), TILE, TILE, 0);

        SpriteSheet attackSheet = new SpriteSheet(ImageLoader.load(
        isRed ? "Bee_Attack_Red.png" :
        isBlue ? "Bee_Attack_Blue.png" :
        "Bee_Attack.png"
        ), TILE, TILE, 0);

        SpriteSheet deathSheet = new SpriteSheet(ImageLoader.load(
        isRed ? "Bee_Death_Red.png" :
        isBlue ? "Bee_Death_Blue.png" :
        "Bee_Death.png"
        ), TILE, TILE, 0);

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

    public boolean isDead() {
        return BeeStats.isDead();
    }

    public boolean isDeathAnimationComplete() {
        if (!BeeStats.isDead())
            return false;

        Frame[] deathAnim = animations.get("DEATH");
        if (deathAnim == null)
            return true;

        return currentFrame == deathAnim[deathAnim.length - 1];
    }

    public void handlePowerupInput() {
        if (hasPowerup && Keyboard.isKeyDown(Key.ONE)) {
            System.out.println("Bee activated power-up! Speed boost for 10s!");
            hasPowerup = false;

            if (powerupHUD != null) {
                powerupHUD.removeIcon("speed_icon.png");
            }

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

    public void activateShield(String iconPath) {
        hasShield = true;
        shieldHealth = MAX_SHIELD_HEALTH;
        showPowerupIcon(iconPath, 999999);
        System.out.println("Shield activated! (" + shieldHealth + " HP)");
    }

    public boolean hasShield() {
        return hasShield;
    }

    public int getShieldHealth() {
        return shieldHealth;
    }

    public int getMaxShieldHealth() {
        return MAX_SHIELD_HEALTH;
    }
    
    // Projectile powerup methods
    public void collectProjectilePowerup(String iconPath) {
        hasProjectile = true;
        BeeStats.setHasProjectilePower(true); // persist across screens/sessions

        String hudIcon = (iconPath != null && !iconPath.isEmpty()) ? iconPath : PROJECTILE_HUD_ICON;
        if (projectileHUD != null) {
            projectileHUD.showProjectile(hudIcon); // Show in bottom-right corner
        }
        System.out.println("Projectile power-up collected! Press SPACE to shoot!");
    }
    
    public boolean hasProjectile() {
        // tie this to the global flag so any other code that calls this
        // respects the persistent unlock
        return BeeStats.hasProjectilePower();
    }
    
    private void shootProjectile() {
        // Check if bee has enough stamina
        if (!BeeStats.canShootProjectile()) {
            System.out.println("[Bee] Not enough stamina to shoot! Need 150, have: " + BeeStats.getStamina());
            return;
        }
        
        // Spawn projectile at bee's position in the direction bee is facing
        float projectileX = this.x + 32; // center-ish of bee
        float projectileY = this.y + 32;
        
        System.out.println("[Bee] Shooting projectile!");
        System.out.println("[Bee] Facing direction: " + facingDirection);
        
        BeeProjectile projectile = new BeeProjectile(projectileX, projectileY, facingDirection);
        activeProjectiles.add(projectile);
        
        // Use stamina and record shot time
        BeeStats.useProjectileStamina();
        lastShotTime = System.currentTimeMillis();
        
        System.out.println("[Bee] Projectile created. Total projectiles: " + activeProjectiles.size());
    }
    
    public void updateProjectiles() {
        // Update & collide
        for (BeeProjectile projectile : new ArrayList<>(activeProjectiles)) {
            projectile.update();
            if (!projectile.isActive()) continue;

            java.awt.Rectangle pBox = projectile.getHitbox();

            // iterate a copy to avoid concurrent modification
            for (var npc : new ArrayList<>(map.getNPCs())) {
                // Spider
                if (npc instanceof Spider) {
                    Spider s = (Spider) npc;
                    if (s.getHitbox().intersects(pBox)) {
                        s.takeDamage(PROJECTILE_DAMAGE);
                        projectile.deactivate();
                        break;
                    }
                }
                // Goblin
                else if (npc instanceof Goblin) {
                    Goblin g = (Goblin) npc;
                    try {
                        if (g.getHitbox().intersects(pBox)) {
                            g.takeDamage(PROJECTILE_DAMAGE);
                            projectile.deactivate();
                            break;
                        }
                    } catch (Throwable ignored) {}
                }
                // Bat
                else if (npc instanceof Bat) {
                    Bat b = (Bat) npc;
                    try {
                        if (b.getHitbox().intersects(pBox)) {
                            b.takeDamage(PROJECTILE_DAMAGE);
                            projectile.deactivate();
                            break;
                        }
                    } catch (Throwable ignored) {}
                }
                // Crab
                else if (npc instanceof Crab) {
                    Crab c = (Crab) npc;
                    try {
                        if (c.getHitbox().intersects(pBox)) {
                            c.takeDamage(PROJECTILE_DAMAGE);
                            projectile.deactivate();
                            break;
                        }
                    } catch (Throwable ignored) {}
                }
                // FrostDragon
                else if (npc instanceof FrostDragon) {
                    FrostDragon d = (FrostDragon) npc;
                    try {
                        if (d.getHitbox().intersects(pBox)) {
                            d.takeDamage(PROJECTILE_DAMAGE);
                            projectile.deactivate();
                            break;
                        }
                    } catch (Throwable ignored) {}
                }
            }
        }

        // Cull inactive
        activeProjectiles.removeIf(p -> !p.isActive());
    }
    
    public void drawProjectiles(GraphicsHandler graphicsHandler) {
        // Camera-aware draw; no temp position hack
        if (map != null && map.getCamera() != null) {
            float camX = map.getCamera().getX();
            float camY = map.getCamera().getY();
            for (BeeProjectile projectile : activeProjectiles) {
                projectile.draw(graphicsHandler, camX, camY);
            }
        }
    }
    
    public ArrayList<BeeProjectile> getActiveProjectiles() {
        return activeProjectiles;
    }

    // ADDED: Getter method for powerupHUD so VolcanoLevelScreen can remove ring icon
    public PowerupHUD getPowerupHUD() {
        return powerupHUD;
    }

    // handle tunic activation (press 3 for Red, 4 for Blue)
    private void handleTunicInput() {
        if (BeeStats.hasTunic() && Keyboard.isKeyDown(Key.THREE)) {
            BeeStats.setBlueTunicActive(false);
            useBlueSprites = false;
            BeeStats.setTunicActive(!BeeStats.isTunicActive());

            if (BeeStats.isTunicActive()) {
                System.out.println("Red Tunic activated! You are now fireproof!");
                swapToRedBeeSprites();
            } else {
                System.out.println("Red Tunic deactivated!");
                revertToNormalBeeSprites();
            }

            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }

        // Blue Tunic toggle (press 4)
        if (BeeStats.hasBlueTunic() && Keyboard.isKeyDown(Key.FOUR)) {
            BeeStats.setTunicActive(false);
            useRedSprites = false;
            BeeStats.setBlueTunicActive(!BeeStats.isBlueTunicActive());

            if (BeeStats.isBlueTunicActive()) {
                System.out.println("Blue Tunic activated! Frost can no longer harm you!");
                swapToBlueBeeSprites();
            } else {
                System.out.println("Blue Tunic deactivated!");
                revertToNormalBeeSprites();
            }

            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }
    }

    // Change sprite sets
    private void swapToRedBeeSprites() {
        useRedSprites = true;
        useBlueSprites = false;
        SpriteSheet redWalk = new SpriteSheet(ImageLoader.load("Bee_Walk_Red.png"), 64, 64, 0);
        this.animations = loadAnimations(redWalk);
        this.currentAnimationName = "STAND_DOWN"; // reset to a safe state
    }

    private void swapToBlueBeeSprites() {
        useBlueSprites = true;
        useRedSprites = false;
        SpriteSheet blueWalk = new SpriteSheet(ImageLoader.load("Bee_Walk_Blue.png"), 64, 64, 0);
        this.animations = loadAnimations(blueWalk);
        this.currentAnimationName = "STAND_DOWN";
    }

    private void revertToNormalBeeSprites() {
        useRedSprites = false;
        useBlueSprites = false;
        SpriteSheet yellowWalk = new SpriteSheet(ImageLoader.load("Bee_Walk.png"), 64, 64, 0);
        this.animations = loadAnimations(yellowWalk);
        this.currentAnimationName = "STAND_DOWN";
    }
}
