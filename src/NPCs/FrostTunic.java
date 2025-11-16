package NPCs;

import Builders.FrameBuilder;
import Engine.ImageLoader;
import Engine.Key;
import Engine.Keyboard;
import GameObject.Frame;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Players.Bee;
import Utils.Point;

import java.util.HashMap;

public class FrostTunic extends NPC {
    
    private boolean collected = false;
    private float fadeAlpha = 1.0f; // 1.0 = fully visible, 0.0 = invisible
    private int fadeTimer = 0;
    private static final int FADE_DURATION = 60; // frames (about 1 second at 60fps)

    public FrostTunic(int id, Point location) {
        super(id, location.x, location.y - 20, new SpriteSheet(ImageLoader.load("frost_tunic.png"), 64, 64), "IDLE");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("IDLE", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                        .withScale(1.0f)
                        .build()
            });
        }};
    }

    @Override
    public void update(Player player) {
        super.update(player);
        
       // Debug: Check if player is near
    //    if (!collected && this.intersects(player)) {
    //        System.out.println("FrostTunic: Player nearby! Press E to collect");
    //    }

    //    // Collect frost tunic with E key (like other powerups)
    //    if (!collected && this.intersects(player) && Keyboard.isKeyDown(Key.E)) {
    //        if (player instanceof Bee bee) {
    //            System.out.println("Frost Tunic collected!");
    //            bee.obtainBlueTunic();

    //            // Show collection message in textbox
    //            if (map != null && map.getTextbox() != null) {
    //                map.getTextbox().addText("Frost Tunic acquired");
    //                map.getTextbox().setIsActive(true);
    //            }

    //            collect();
    //        }
    //    }

       // Handle fade-out animation when collected
        // if (fadeTimer < FADE_DURATION) {
        //     fadeTimer++;
        //     fadeAlpha = 1.0f - ((float) fadeTimer / FADE_DURATION);
            
        //     // Add a slight upward float effect
        //     this.y -= 0.5f;
        // }

    }
    
    @Override
    public void draw(Engine.GraphicsHandler graphicsHandler) {
        if (fadeAlpha <= 0.0f) {
            return; // Completely faded, don't draw
        }
        
        if (collected && fadeAlpha < 1.0f) {
            // Apply fade effect with alpha transparency
            java.awt.Graphics2D g2d = (java.awt.Graphics2D) graphicsHandler.getGraphics();
            java.awt.Composite originalComposite = g2d.getComposite();
            
            // Set alpha composite for transparency
            g2d.setComposite(java.awt.AlphaComposite.getInstance(
                java.awt.AlphaComposite.SRC_OVER, fadeAlpha));
            
            super.draw(graphicsHandler);
            
            // Restore original composite
            g2d.setComposite(originalComposite);
        } else {
            // Not collected or not fading yet, draw normally
            super.draw(graphicsHandler);
        }
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public boolean hasFadedOut() {
        return collected && fadeAlpha <= 0.0f;
    }
    
    public void collect() {
        this.collected = true;
        System.out.println("Frost Tunic Acquired");
    }
}