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

    private static final float MOVE_SPEED = 0.8f;
    private static final float MOVE_RANGE = 80f;
    private static final int DAMAGE = 10;
    private static final long ATTACK_COOLDOWN_MS = 1000;
    private static final long DEATH_LINGER_MS = 1000;

    private Direction facing = Direction.RIGHT;
    private float startX;
    private boolean movingRight = true;

    private int health = 40;
    private boolean isDead = false;
    private long deathTime = 0;
    private long lastAttackTime = 0;

    private ArrayList<FloatingText> floatingTexts = new ArrayList<>();

    public Skull(Point location) {
        super(0, location.x, location.y,
                new SpriteSheet(ImageLoader.load("Bones_SingleSkull_Fly.png"), TILE_W, TILE_H, 0),
                "FLY_RIGHT");

        this.startX = location.x;
    }

    // damage
    public void takeDamage(int amount) {
        if (isDead) return;
        health -= amount;
        floatingTexts.add(new FloatingText(getX() + 16, getY(), "-" + amount, Color.RED));
        if (health <= 0) die();
    }

    private void die() {
        if (isDead) return;
        isDead = true;
        deathTime = System.currentTimeMillis();
        System.out.println("💀 Skull destroyed");
    }

    public boolean shouldRemove() {
        return isDead && (System.currentTimeMillis() - deathTime) > DEATH_LINGER_MS;
    }

    @Override
    public void update(Player player) {
        floatingTexts.removeIf(t -> { t.update(); return t.isDead(); });

        if (isDead) {
            super.update(player);
            return;
        }

        movePattern();
        tryAttack(player);

        super.update(player);
    }

    // movement 
    private void movePattern() {
        if (movingRight) {
            moveXHandleCollision(MOVE_SPEED);
            if (getX() > startX + MOVE_RANGE) movingRight = false;
        } else {
            moveXHandleCollision(-MOVE_SPEED);
            if (getX() < startX - MOVE_RANGE) movingRight = true;
        }

        facing = movingRight ? Direction.RIGHT : Direction.LEFT;
        currentAnimationName = (facing == Direction.RIGHT) ? "FLY_RIGHT" : "FLY_LEFT";
    }

    // attack
    private void tryAttack(Player player) {
        long now = System.currentTimeMillis();
        if (now - lastAttackTime < ATTACK_COOLDOWN_MS) return;

        if (player instanceof Bee bee) {
            float dx = bee.getX() - getX();
            float dy = bee.getY() - getY();
            float dist = (float)Math.sqrt(dx * dx + dy * dy);

            if (dist < 40f) {
                bee.applyDamage(DAMAGE);
                lastAttackTime = now;
                System.out.println("☠️ Skull hit Bee for " + DAMAGE);
            }
        }
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> map = new HashMap<>();

        // Sprite is a 3x3 grid (192x192 sheet, 64x64 per frame)
        Frame[] flyRight = new Frame[9];
        Frame[] flyLeft = new Frame[9];

        int frameIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Frame f = new FrameBuilder(spriteSheet.getSprite(row, col), 8)
                        .withScale(SCALE)
                        .build();
                flyRight[frameIndex] = f;

                Frame fFlip = new FrameBuilder(spriteSheet.getSprite(row, col), 8)
                        .withScale(SCALE)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
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
}
