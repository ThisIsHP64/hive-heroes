package Effects;

import Engine.GraphicsHandler;
import java.awt.Color;
import java.awt.Font;

public class FloatingText {
    private float x, y;
    private String text;
    private Color color;
    private long startTime;
    private static final long DURATION_MS = 800;
    private static final float FLOAT_SPEED = 1.2f;
    private float xOffset; // random horizontal offset
    
    public FloatingText(float x, float y, String text, Color color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.startTime = System.currentTimeMillis();
        
        // random horizontal spread to prevent stacking
        this.xOffset = (float) (Math.random() * 40 - 20); // -20 to +20 pixels
    }
    
    public void update() {
        y -= FLOAT_SPEED;
    }
    
    public boolean isDead() {
        return (System.currentTimeMillis() - startTime) >= DURATION_MS;
    }
    
    public void draw(GraphicsHandler graphicsHandler, float cameraX, float cameraY) {
        long elapsed = System.currentTimeMillis() - startTime;
        float progress = (float) elapsed / DURATION_MS;
        
        int alpha = (int) ((1.0f - progress) * 255);
        if (alpha < 0) alpha = 0;
        if (alpha > 255) alpha = 255;
        
        Color fadeColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        
        // apply random horizontal offset
        int screenX = (int) (x + xOffset - cameraX);
        int screenY = (int) (y - cameraY);
        
        graphicsHandler.drawStringWithOutline(
            text,
            screenX,
            screenY,
            new Font("Arial", Font.BOLD, 24),
            fadeColor,
            new Color(0, 0, 0, alpha),
            2
        );
    }
}