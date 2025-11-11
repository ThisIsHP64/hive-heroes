package PowerUps;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.MapEntityStatus;
import Level.NPC;
import Level.Player;
import Players.Bee;
import Utils.Point;
import java.util.HashMap;

public class StingerPowerUp extends NPC {

    private boolean used = false;
    private boolean animationPlaying = false;
    private long animationStartTime = 0L;

    private static final int ACTIVATED_ANIM_DURATION_MS = 800;

    public StingerPowerUp(int id, Point location) {
        super(id, location.x, location.y,
              new SpriteSheet(ImageLoader.load("projectile_powerup1.png"), 32, 32, 0),
              "IDLE");
        this.isUncollidable = true;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            // IDLE - just the first frame (whole icon)
            put("IDLE", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                    .withScale(2f)
                    .withBounds(0, 0, 32, 32)
                    .build(),
            });

            // ACTIVATED - 3 frames showing the crack and shatter animation
            put("ACTIVATED", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                    .withScale(2f)
                    .withBounds(0, 0, 32, 32)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(0, 1), 10)
                    .withScale(2f)
                    .withBounds(0, 0, 32, 32)
                    .build(),
                new FrameBuilder(spriteSheet.getSprite(1, 0), 10)
                    .withScale(2f)
                    .withBounds(0, 0, 32, 32)
                    .build()
            });
        }};
    }

    @Override
    public void update(Player player) {
        super.update(player);

        // Pick up projectile powerup with E
        if (!used && this.intersects(player) && Keyboard.isKeyDown(Key.E)) {
            if (player instanceof Bee bee) {
                System.out.println("Stinger Power-up collected!");

                // Play pickup animation
                this.currentAnimationName = "ACTIVATED";
                animationPlaying = true;
                animationStartTime = System.currentTimeMillis();

                // Give the bee the projectile powerup
                bee.collectProjectilePowerup("Bee_Projectile_HUD.png");

                used = true;
            }
        }

        // Remove from map after animation completes
        if (animationPlaying && System.currentTimeMillis() - animationStartTime > ACTIVATED_ANIM_DURATION_MS) {
            animationPlaying = false;
            setMapEntityStatus(MapEntityStatus.REMOVED);
            System.out.println("Stinger power-up removed after animation.");
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (getMapEntityStatus() != MapEntityStatus.REMOVED) {
            super.draw(graphicsHandler);
        }
    }
}