package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;

import java.util.HashMap;

public class Bee extends Player {

    private static final String PREFIX = "";

    private static final int TILE = 64;        // tiles are 64x64
    private static final float SCALE = 2.5f;   // tweak 2.0–3.0 to taste

    // row in the sprite sheet for right-facing bee
    private static final int RIGHT_ROW = 2;

    public Bee(float x, float y) {
        // gutter=0 because sheets have no spacing between frames
        super(
            new SpriteSheet(ImageLoader.loadPreserveAlpha(PREFIX + "Bee_Walk.png"), TILE, TILE, 0),
            x, y,
            "STAND_RIGHT"
        );

        // WASD
        MOVE_LEFT_KEY  = Key.A;
        MOVE_RIGHT_KEY = Key.D;
        MOVE_UP_KEY    = Key.W;
        MOVE_DOWN_KEY  = Key.S;

        walkSpeed = 10f; // tweak if need bee ;)
    }

    @Override public void update() { super.update(); }
    @Override public void draw(GraphicsHandler g) { super.draw(g); }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet walkSheet) {
        SpriteSheet idleSheet =
            new SpriteSheet(ImageLoader.loadPreserveAlpha(PREFIX + "Bee_Idle.png"), TILE, TILE, 0);


        // bounds in raw 64x64 sprite pixels (no SCALE here)
        int hbX = 22, hbY = 42;
        int hbW = 20, hbH = 14;


        HashMap<String, Frame[]> map = new HashMap<>();

        // IDLE (first column of the right-facing row)
        map.put("STAND_RIGHT", new Frame[] {
            new FrameBuilder(idleSheet.getSprite(RIGHT_ROW, 0))
                .withScale(SCALE).withBounds(hbX, hbY, hbW, hbH).build()
        });
        map.put("STAND_LEFT", new Frame[] {
            new FrameBuilder(idleSheet.getSprite(RIGHT_ROW, 0))
                .withScale(SCALE).withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                .withBounds(hbX, hbY, hbW, hbH).build()
        });

        // WALK RIGHT — cols 0..3
        map.put("WALK_RIGHT", new Frame[] {
            new FrameBuilder(walkSheet.getSprite(RIGHT_ROW, 0), 14).withScale(SCALE).withBounds(hbX, hbY, hbW, hbH).build(),
            new FrameBuilder(walkSheet.getSprite(RIGHT_ROW, 1), 14).withScale(SCALE).withBounds(hbX, hbY, hbW, hbH).build(),
            new FrameBuilder(walkSheet.getSprite(RIGHT_ROW, 2), 14).withScale(SCALE).withBounds(hbX, hbY, hbW, hbH).build(),
            new FrameBuilder(walkSheet.getSprite(RIGHT_ROW, 3), 14).withScale(SCALE).withBounds(hbX, hbY, hbW, hbH).build()
        });

        // WALK LEFT — flip
        map.put("WALK_LEFT", new Frame[] {
            new FrameBuilder(walkSheet.getSprite(RIGHT_ROW, 0), 14).withScale(SCALE).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(hbX, hbY, hbW, hbH).build(),
            new FrameBuilder(walkSheet.getSprite(RIGHT_ROW, 1), 14).withScale(SCALE).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(hbX, hbY, hbW, hbH).build(),
            new FrameBuilder(walkSheet.getSprite(RIGHT_ROW, 2), 14).withScale(SCALE).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(hbX, hbY, hbW, hbH).build(),
            new FrameBuilder(walkSheet.getSprite(RIGHT_ROW, 3), 14).withScale(SCALE).withImageEffect(ImageEffect.FLIP_HORIZONTAL).withBounds(hbX, hbY, hbW, hbH).build()
        });

        return map;
    }
}
