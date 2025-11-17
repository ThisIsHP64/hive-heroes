package NPCs;

import java.util.HashMap;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;

/**
 * SauronEye – static eye that tracks the player's position.
 *
 * SPRITE SHEET: 99x128 px → tile size = 33x64 (3x2 grid)
 *
 * Layout (row, col):
 *   row 0: (0,0) CENTER  | (0,1) RIGHT | (0,2) LEFT
 *   row 1: (1,0) DESTROYED | (1,1) empty | (1,2) empty
 */
public class SauronEye extends NPC {

    // Make CENTER easier to hit when you are "in front"
    private static final int HYSTERESIS = 8;         // pixels of dead zone
    private static final int CHECK_INTERVAL_MS = 80; // ~12.5x/sec

    // If your world X feels reversed, keep this true
    private static final boolean MIRROR_X = true;

    private long lastCheckTime = 0;

    private enum Dir { LEFT, RIGHT, CENTER }
    private Dir lastDir = Dir.CENTER;

    // destroyed flag
    private boolean destroyed = false;

    public SauronEye(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("sauroneye1.png"), 33, 64, 0),
            "CENTER"
        );
    }

    // called when the ring is destroyed
    public void destroyEye() {
        destroyed = true;
        setCurrentAnimationName("DESTROYED");
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> animations = new HashMap<>();

        // CENTER = row 0, col 0
        animations.put("CENTER", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(0, 0))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        // RIGHT = row 0, col 1
        animations.put("RIGHT", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(0, 2))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        // LEFT = row 0, col 2
        animations.put("LEFT", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(0, 1))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        // DESTROYED = row 1, col 0
        animations.put("DESTROYED", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(1, 0))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        return animations;
    }

    @Override
    public void update(Player player) {
        // Once destroyed, freeze on destroyed frame, no tracking
        if (!destroyed) {
            long now = System.currentTimeMillis();
            if (now - lastCheckTime >= CHECK_INTERVAL_MS) {
                lastCheckTime = now;

                float eyeCenterX = getX() + getWidth() / 2f;
                float playerCenterX = player.getX() + player.getWidth() / 2f;

                // Flip axis if needed so "player on the right" truly means RIGHT frame
                float dx = MIRROR_X ? (eyeCenterX - playerCenterX)
                                    : (playerCenterX - eyeCenterX);

                Dir dir;
                if (dx >= HYSTERESIS)        dir = Dir.RIGHT;   // player is to the right
                else if (dx <= -HYSTERESIS)  dir = Dir.LEFT;    // player is to the left
                else                         dir = Dir.CENTER;  // near aligned

                if (dir != lastDir) {
                    switch (dir) {
                        case LEFT:   setCurrentAnimationName("LEFT");   break;
                        case RIGHT:  setCurrentAnimationName("RIGHT");  break;
                        default:     setCurrentAnimationName("CENTER"); break;
                    }
                    lastDir = dir;
                }
            }
        }

        super.update(player);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
