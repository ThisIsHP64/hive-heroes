package Effects;

import Engine.GraphicsHandler;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;

public class SmokeParticle {
    private float x, y;
    private float vx, vy;
    private float size;
    private float alpha;
    private float fadeRate;
    private long spawnTime;
    
    public SmokeParticle(float x, float y) {
        this.x = x;
        this.y = y;
        // random velocity for spread effect
        this.vx = (float)(Math.random() * 2 - 1) * 1.5f;
        this.vy = (float)(Math.random() * 2 - 1) * 1.5f - 2f; // bias upward
        this.size = (float)(Math.random() * 20 + 15);
        this.alpha = 0.7f;
        this.fadeRate = 0.02f;
        this.spawnTime = System.currentTimeMillis();
    }
    
    public void update() {
        x += vx;
        y += vy;
        alpha -= fadeRate;
        size += 0.5f; // grow over time
        vy -= 0.05f; // slow down vertical movement
    }
    
    public boolean isDead() {
        return alpha <= 0 || System.currentTimeMillis() - spawnTime > 1000;
    }
    
    public void draw(GraphicsHandler graphicsHandler, float cameraX, float cameraY) {
        Graphics2D g = graphicsHandler.getGraphics();
        
        int screenX = (int)(x - cameraX);
        int screenY = (int)(y - cameraY);
        
        var oldComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, alpha)));
        
        // dark purple/black smoke
        g.setColor(new Color(20, 0, 30));
        g.fillOval(screenX - (int)size/2, screenY - (int)size/2, (int)size, (int)size);
        
        g.setComposite(oldComposite);
    }
}