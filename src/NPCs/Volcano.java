package NPCs;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;

import java.util.HashMap;

// Volcano decoration for volcanic region
public class Volcano extends NPC {

    // --- Tiny local shaker so only this sprite jitters ---
    private static final class Shaker {
        private float intensityPx = 0f;
        private long endAtMs = 0L;

        void start(float intensityPx, long durationMs) {
            this.intensityPx = intensityPx;
            this.endAtMs = System.currentTimeMillis() + durationMs;
        }

        boolean active() {
            return System.currentTimeMillis() < endAtMs;
        }

        int dx() {
            return active() ? (int) Math.round((Math.random() * 2 - 1) * intensityPx) : 0;
        }

        int dy() {
            return active() ? (int) Math.round((Math.random() * 2 - 1) * intensityPx) : 0;
        }
    }

    private final Shaker shaker = new Shaker();

    public Volcano(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("volcano1.png"), 32, 32),
            "IDLE"
        );
        System.out.println("Volcano spawned at: " + location.x + ", " + location.y);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> animations = new HashMap<>();

        Frame idle = new FrameBuilder(spriteSheet.getSprite(0, 0))
                .withScale(8) // keep big so it’s obvious
                .build();

        animations.put("IDLE", new Frame[] { idle });
        return animations;
    }

    // Call this when the ring is destroyed (e.g., on E interaction success)
    public void startRingDestroyShake() {
        shaker.start(3.5f, 1200); // ~1.2s rumble at ~3–4px jitter
        // Optional: if you have global camera shake, trigger it where you handle the interaction:
        // Camera.shake(6, 600);
    }

    @Override
    public void draw(GraphicsHandler g) {
        int ox = shaker.dx();
        int oy = shaker.dy();
        g.getGraphics().translate(ox, oy);
        super.draw(g);
        g.getGraphics().translate(-ox, -oy);
    }
}
