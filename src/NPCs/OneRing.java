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
    private float fadeAlpha = 1.0f; // 1.0 = fully visible, 0.0 = invisible
    private int fadeTimer = 0;
    private static final int FADE_DURATION = 60; // frames (about 1 second at 60fps)

    public OneRing(int id, Point location) {
        super(id, location.x, location.y, new SpriteSheet(ImageLoader.load("onering.png"), 64, 64), "IDLE");
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {{
            put("IDLE", new Frame[] {
                new FrameBuilder(spriteSheet.getSprite(0, 0))
                        .withScale(0.75f)
                        .build()
            });
        }};
    }

    @Override
    public void update(Player player) {
        super.update(player);
        
        // Handle fade-out animation when collected
        if (collected && fadeTimer < FADE_DURATION) {
            fadeTimer++;
            fadeAlpha = 1.0f - ((float) fadeTimer / FADE_DURATION);
            
            // Add a slight upward float effect
            this.y -= 0.5f;
        }
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
        System.out.println("Random Ring. Looks shiny, I'll grab it.");
    }
}