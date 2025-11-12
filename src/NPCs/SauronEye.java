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
 * SPRITE SHEET: 67x128 px → tile size = 33x64 (2x2 grid; bottom-right empty)
 * Mapping:
 *   CENTER -> (0,0)  // top-left
 *   RIGHT  -> (0,1)  // top-right
 *   LEFT   -> (1,0)  // bottom-left
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

    public SauronEye(int id, Point location) {
        super(
            id,
            location.x,
            location.y,
            new SpriteSheet(ImageLoader.load("sauroneye.png"), 33, 64, 0),
            "CENTER"
        );
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> animations = new HashMap<>();

        // CENTER = (row 0, col 0)
        animations.put("CENTER", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(0, 0))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        // RIGHT = (row 0, col 1)
        animations.put("RIGHT", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(1, 0))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        // LEFT = (row 1, col 0)
        animations.put("LEFT", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(0, 1))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        return animations;
    }

    @Override
    public void update(Player player) {
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
        super.update(player);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
