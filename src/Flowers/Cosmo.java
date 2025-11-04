package Flowers;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
import GameObject.Frame;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.MapEntityStatus;
import Level.Player;
import Players.Bee;
import Utils.Point;
import java.util.HashMap;

public class Cosmo extends Flower {

    public Cosmo(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("CosmoOUTLINED.png"), 
                32, 32), "STAND_LEFT");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                put("STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0)) // frame 1 for 200ms
                                .withScale(1.5f)
                                .withBounds(0, 0, 32, 32)
                                .build(),
                });
            }
        };
    }

    public java.awt.Rectangle getHitbox() {

        Rectangle bounds = getBounds();
        
        int w = bounds.getWidth();
        int h = bounds.getHeight();

        int x = (int) getX();
        int y = (int) getY();
        
        return new java.awt.Rectangle(x, y, w, h);
    }


    // @Override
    // public void update(Player player) {
    //     super.update(player);

    //     if (player instanceof Bee bee) {
    //         java.awt.Rectangle sting = bee.getAttackHitbox();
            
    //         if (sting.intersects(this.getHitbox())) {
    //             nectarAmount -= 1;

    //             if (nectarAmount <= 0) {
    //                 setMapEntityStatus(MapEntityStatus.REMOVED);
    //             }
    //         }
    //     }
    // }

    // @Override
    // public void draw(GraphicsHandler graphicsHandler) {
    //     // draw only if not removed
    //     if (getMapEntityStatus() != MapEntityStatus.REMOVED) {
    //         super.draw(graphicsHandler);
    //     }
    // }

    // @Override
    // public void draw(GraphicsHandler graphicsHandler) {
    //     super.draw(graphicsHandler);
    // }
    
}
