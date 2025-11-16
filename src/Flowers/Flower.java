package Flowers;

import Engine.GraphicsHandler;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.MapEntityStatus;
import Level.NPC;
import Level.Player;
import Players.Bee;
import Sound.SFX;
import Sound.SFXManager;

public class Flower extends NPC {

    protected int nectarAmount = 15;

    public Flower(int id, float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(id, x, y, spriteSheet, startingAnimation);
        isUncollidable = true;
    }
    
    public java.awt.Rectangle getHitbox() {

        Rectangle bounds = getBounds();
        
        int w = bounds.getWidth();
        int h = bounds.getHeight();

        int x = (int) getX();
        int y = (int) getY();
        
        return new java.awt.Rectangle(x, y, w, h);
    }

    @Override
    public void update(Player player) {
        super.update(player);

        if (player instanceof Bee bee) {
            java.awt.Rectangle sting = bee.getAttackHitbox();
            
            if (sting.intersects(this.getHitbox())) {
                SFXManager.playSFX(SFX.NECTAR);
                nectarAmount -= 1;

                if (nectarAmount <= 0) {
                    setMapEntityStatus(MapEntityStatus.REMOVED);
                }
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // draw only if not removed
        if (getMapEntityStatus() != MapEntityStatus.REMOVED) {
            super.draw(graphicsHandler);
        }
    }

}
