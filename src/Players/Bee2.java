package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;

import java.util.HashMap;

// This is the class for the Cat player character
// basically just sets some values for physics and then defines animations
public class Bee2 extends Player {

        private static final String PREFIX = "";
    private static final float SCALE = 3.0f;   // tweak 2.0–3.0 to taste
    private static final int RIGHT_ROW = 2;

    public Bee2(float x, float y) {
        super(new SpriteSheet(ImageLoader.loadPreserveAlpha(PREFIX + "Bee_Walk.png"), 24, 24), x, y, "STAND_RIGHT");
        walkSpeed = 15.0f;
    }

    public void update() {
        super.update();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        SpriteSheet idleSheet =
            new SpriteSheet(ImageLoader.loadPreserveAlpha(PREFIX + "Bee_Idle.png"), 64, 64, 0);
        
        return new HashMap<String, Frame[]>() {{
                
            // IDLE (first column of the right-facing row)
        put("STAND_RIGHT", new Frame[] {
            new FrameBuilder(idleSheet.getSprite(2, 0))
                .withScale(SCALE)
                .withBounds(6, 12, 12, 7)
                .build()
        });
        
        put("STAND_LEFT", new Frame[] {
            new FrameBuilder(idleSheet.getSprite(2, 0))
                .withScale(SCALE)
                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(6, 12, 12, 7)
                .build()
        });

            put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(idleSheet.getSprite(2, 0), 14)
                            .withScale(3)
                            .withBounds(6, 12, 12, 7)
                            .build(),
                    new FrameBuilder(idleSheet.getSprite(2, 1), 14)
                            .withScale(3)
                            .withBounds(6, 12, 12, 7)
                            .build(),
                    new FrameBuilder(idleSheet.getSprite(2, 2), 14)
                            .withScale(3)
                            .withBounds(6, 12, 12, 7)
                            .build(),
                    new FrameBuilder(idleSheet.getSprite(2, 3), 14)
                            .withScale(3)
                            .withBounds(6, 12, 12, 7)
                            .build()
            });

            put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(idleSheet.getSprite(2, 0), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 12, 12, 7)
                            .build(),
                    new FrameBuilder(idleSheet.getSprite(2, 1), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 12, 12, 7)
                            .build(),
                    new FrameBuilder(idleSheet.getSprite(2, 2), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 12, 12, 7)
                            .build(),
                    new FrameBuilder(idleSheet.getSprite(2, 3), 14)
                            .withScale(3)
                            .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                            .withBounds(6, 12, 12, 7)
                            .build()
            });
        }};
    }
}
