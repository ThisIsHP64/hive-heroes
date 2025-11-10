package NPCs;
import Builders.FrameBuilder;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Utils.Point;
import java.util.HashMap;
// Volcano decoration for volcanic region
public class Volcano extends NPC {
   
 public Volcano(int id, Point location) {
    super(id, location.x, location.y,
          new SpriteSheet(ImageLoader.load("volcano1.png"), 32, 32),
          "IDLE");
    System.out.println("Volcano spawned at: " + location.x + ", " + location.y);
}
    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        HashMap<String, Frame[]> animations = new HashMap<>();
       
        // Scale it bigger so we can definitely see it
        Frame idle = new FrameBuilder(spriteSheet.getSprite(0, 0))
                .withScale(4)  // Made it even bigger
                .build();
       
        animations.put("IDLE", new Frame[] { idle });
        return animations;
    }
}