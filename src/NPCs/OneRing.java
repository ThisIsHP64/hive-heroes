package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Point;

import java.util.HashMap;

public class OneRing extends NPC {
    
    private boolean collected = false;

    public OneRing(int id, Point location) {
        super(id, location.x - 96, location.y - 20, new SpriteSheet(ImageLoader.load("onering.png"), 64, 64), "IDLE");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("IDLE", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                        .withScale(.75f)
                        .build()
            });
        }};
    }

    @Override
    public void update(Player player) {
        super.update(player);
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public void collect() {
        this.collected = true;
        System.out.println("Picked up some random ring...");
    }
}