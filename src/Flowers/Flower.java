package Flowers;

import GameObject.SpriteSheet;
import Level.NPC;

public class Flower extends NPC {

    public Flower(int id, float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(id, x, y, spriteSheet, startingAnimation);
        // isUncollidable = true;
    }
    
}
