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

public class PowerUp extends NPC {

    private boolean used = false;             // picked up already?
    private boolean animationPlaying = false; // animation running?
    private long animationStartTime = 0L;     // when animation started

    private static final int ACTIVATED_ANIM_DURATION_MS = 800; // 0.8s

    public PowerUp(int id, Point location) {
        super(id, location.x, location.y,
              new SpriteSheet(ImageLoader.load("powerup_speed.png"), 32, 32, 0),
              "IDLE");
        this.isUncollidable = true;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("IDLE", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0), 10)
                    .withScale(2f)
                    .withBounds(0, 0, 32, 32)
                    .build(),
            });

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

        // Step 1: pickup with E
        if (!used && this.intersects(player) && Keyboard.isKeyDown(Key.E)) {
            if (player instanceof Bee bee) {
                System.out.println("Power-up collected! (Press 1 to activate)");

                // play pickup animation once
                this.currentAnimationName = "ACTIVATED";
                animationPlaying = true;
                animationStartTime = System.currentTimeMillis();

                // give bee the power-up
                bee.collectPowerup("speed_icon.png");

                // mark as used so it can’t be picked up again
                used = true;
            }
        }

        // once animation finishes, remove from map
        if (animationPlaying && System.currentTimeMillis() - animationStartTime > ACTIVATED_ANIM_DURATION_MS) {
            animationPlaying = false;
            setMapEntityStatus(MapEntityStatus.REMOVED);
            System.out.println("Entity removed after animation.");
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // draw only if not removed
        if (getMapEntityStatus() != MapEntityStatus.REMOVED) {
            super.draw(graphicsHandler);
        }
    }
}
