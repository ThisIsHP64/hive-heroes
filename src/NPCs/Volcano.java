package NPCs;

import Builders.FrameBuilder;
import Effects.CameraShake;
import Effects.ScreenFX;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

/**
 * Volcano NPC
 * - Uses your project's NPC constructor: (int id, int x, int y, SpriteSheet, String startAnim)
 * - Provides triggerRingDestroyFX() you can call from your interaction logic
 * - Shakes locally (sprite jitters), optional CameraShake, and full-screen ScreenFX
 */
public class Volcano extends NPC {

    // --- tiny local shaker so only this sprite jitters ---
    private static final class Shaker {
        private float intensityPx = 0f;
        private long endAtMs = 0L;
        void start(float intensityPx, long durationMs) {
            this.intensityPx = intensityPx;
            this.endAtMs = System.currentTimeMillis() + durationMs;
        }
        boolean active() { return System.currentTimeMillis() < endAtMs; }
        int dx() { return active() ? (int)Math.round((Math.random()*2 - 1) * intensityPx) : 0; }
        int dy() { return active() ? (int)Math.round((Math.random()*2 - 1) * intensityPx) : 0; }
    }

    private final Shaker shaker = new Shaker();

    // Adjust sprite path/tile size if your asset differs
    public Volcano(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("volcano1.png"), 32, 32),
            "IDLE"
        );
        // scale is applied in loadAnimations via FrameBuilder.withScale(...)
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> animations = new HashMap<>();

        Frame idle = new FrameBuilder(spriteSheet.getSprite(0, 0))
                .withScale(8)               // make the volcano big; tweak if needed
                .build();

        animations.put("IDLE", new Frame[] { idle });
        return animations;
    }

    /**
     * Call this from your ring-destruction logic (e.g., when the player presses E at the altar).
     * Triggers sprite shake + camera shake (screen will clear after timer in VolcanoLevelScreen).
     */
    public void triggerRingDestroyFX() {
        // MODIFIED: Local sprite rumble - BIGGER shake for dramatic effect (was 5.0f, now 12.0f)
        shaker.start(12.0f, 1400); // intensity px, duration ms

        // Global camera shake
        try {
            CameraShake.start(6.0f, 900); // intensity, duration ms
        } catch (Throwable ignored) {}
        
        // NOTE: Screen invert clears in VolcanoLevelScreen after 3 second timer
    }

    @Override
    public void draw(GraphicsHandler g) {
        // Jitter only this volcano sprite
        int ox = shaker.dx();
        int oy = shaker.dy();
        g.getGraphics().translate(ox, oy);
        super.draw(g);
        g.getGraphics().translate(-ox, -oy);
    }
}