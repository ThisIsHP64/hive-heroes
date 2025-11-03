package Flowers;

import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.NPC;

public class Flower extends NPC {

    public Flower(int id, float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(id, x, y, spriteSheet, startingAnimation);
    }
    
    public java.awt.Rectangle getHitbox() {

        Rectangle bounds = getBounds();
        
        int w = bounds.getWidth();
        int h = bounds.getHeight();

        int x = (int) getX();
        int y = (int) getY();
        
        return new java.awt.Rectangle(x, y, w, h);
    }

}
