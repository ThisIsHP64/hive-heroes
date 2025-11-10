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
 * SauronEye – static eye that looks toward the player.
 *
 * SPRITE SHEET: 67x128 px → tile size = 33x64
 * [0,0] = look LEFT
 * [1,0] = look RIGHT
 * [0,1] = look CENTER
 * [1,1] = empty
 */
public class SauronEye extends NPC {

    private static final int HYSTERESIS = 8;

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

        animations.put("LEFT", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(0, 0))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        animations.put("RIGHT", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(1, 0))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        animations.put("CENTER", new Frame[] {
            new FrameBuilder(spriteSheet.getSprite(0, 1))
                .withScale(3)
                .withImageEffect(ImageEffect.NONE)
                .build()
        });

        return animations;
    }

    @Override
    public void update(Player player) {
        float eyeCenterX = getX() + getWidth() / 2f;
        float playerCenterX = player.getX() + player.getWidth() / 2f;
        float dx = playerCenterX - eyeCenterX;

        Dir dir;
        if (dx < -HYSTERESIS)      dir = Dir.LEFT;
        else if (dx > HYSTERESIS)  dir = Dir.RIGHT;
        else                       dir = Dir.CENTER;

        if (dir != lastDir) {
            switch (dir) {
                case LEFT:   setCurrentAnimationName("LEFT");   break;
                case RIGHT:  setCurrentAnimationName("RIGHT");  break;
                default:     setCurrentAnimationName("CENTER"); break;
            }
            lastDir = dir;
        }

        super.update(player);
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }
}
