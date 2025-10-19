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

public class PowerUp2 extends NPC {

    private boolean used = false;          // picked up already?
    private boolean boostActive = false;   // track boost state
    private boolean animationPlaying = false; // track if activated animation is running
    private boolean visible = true;        // whether the sprite is drawn
    private long startTimeMs = 0L;         // when boost started
    private float originalSpeed = 0f;      // bee’s speed before boost

    private static final int BOOST_DURATION_MS = 10_000;  // 10 seconds
    private static final float BOOST_MULTIPLIER = 2.0f;   // 2x faster

    // animation timing
    private static final int ACTIVATED_ANIM_DURATION_MS = 800; // show activated animation for 0.8s
    private long animationStartTime = 0L;

    public PowerUp2(int id, Point location) {
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

        // pickup and start animation
        if (!used && this.intersects(player) && Keyboard.isKeyDown(Key.E)) {
            if (player instanceof Bee bee) {
                System.out.println("Picked up PowerUp! Speed boosted!");

                // save and apply boost
                originalSpeed = bee.getWalkSpeed();
                bee.setWalkSpeed(originalSpeed * BOOST_MULTIPLIER);

                // play activated animation
                this.currentAnimationName = "ACTIVATED";
                animationPlaying = true;
                animationStartTime = System.currentTimeMillis();

                // mark used
                startTimeMs = System.currentTimeMillis();
                used = true;
                boostActive = true;
            }
        }

        // hide after animation completes
        if (animationPlaying && System.currentTimeMillis() - animationStartTime > ACTIVATED_ANIM_DURATION_MS) {
            animationPlaying = false;
            visible = false; // hides the icon
            this.isUncollidable = true;
            this.currentAnimationName = "IDLE";
        }

        // revert speed after 10s
        if (boostActive && player instanceof Bee bee) {
            long elapsed = System.currentTimeMillis() - startTimeMs;
            if (elapsed > BOOST_DURATION_MS) {
                bee.setWalkSpeed(originalSpeed);
                boostActive = false;
                System.out.println("Speed boost ended!");

                // remove the icon
                setMapEntityStatus(MapEntityStatus.REMOVED);
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (visible || animationPlaying) {
            super.draw(graphicsHandler);
        }
    }
}
