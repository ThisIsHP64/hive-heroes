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

// This class is for the powerup
public class PowerUp extends NPC {

    private boolean used = false;        // to see if the powerup has been picked
    private long startTimeMs = 0L;       // when boost started
    private float originalSpeed = 0f;    // bee’s speed before boost
    private boolean visible = true;      // simple visibility flag

    private static final int BOOST_DURATION_MS = 10_000;  // 10 seconds
    private static final float BOOST_MULTIPLIER = 2.0f;   // 2x faster

    public PowerUp(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("PowerUp.png"), 24, 24), "IDLE");
        this.isUncollidable = true;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("IDLE", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(0, 0))
                            .withScale(3f)
                            .withBounds(0, 0, 24, 24)
                            .build()
            });
        }};
    }

    

     @Override
    public void update(Player player) {
        super.update(player);

        if (!used && this.intersects(player) && Keyboard.isKeyDown(Key.E)) {
            if (player instanceof Bee bee) {
                System.out.println("Picked up PowerUp! Speed boosted!");

                // save and apply boost
                originalSpeed = bee.getWalkSpeed();
                bee.setWalkSpeed(originalSpeed * BOOST_MULTIPLIER);

                // start timer and mark used
                startTimeMs = System.currentTimeMillis();
                used = true;

                // hide/remove powerup from map
                setMapEntityStatus(MapEntityStatus.REMOVED);
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);

    // Draw the hitbox in red so you can see where it is
    drawBounds(graphicsHandler, new java.awt.Color(255, 0, 0, 100));
    }
}
