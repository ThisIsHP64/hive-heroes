package SpriteImage;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.ScreenManager;
import java.awt.image.BufferedImage;

// Draws a temporary power-up icon at the bottom-left of the screen. 
public class PowerupHUD {
    private BufferedImage icon;
    private long showUntilMs = 0;

    // Show an icon for a given duration (ms). 
    public void show(String spritePath, int durationMs) {
        try { this.icon = ImageLoader.load(spritePath); } catch (Exception e) { this.icon = null; }
        this.showUntilMs = System.currentTimeMillis() + Math.max(0, durationMs);
    }

    // Clear any active icon display immediately. 
    public void clear() {
        this.icon = null;
        this.showUntilMs = 0;
    }

    // Draw icon if still active.
    public void draw(GraphicsHandler g) {
        if (icon == null) return;
        if (System.currentTimeMillis() > showUntilMs) { clear(); return; }
        int padding = 16, size = 48;
        int x = padding;
        int y = ScreenManager.getScreenHeight() - size - padding;
        g.drawImage(icon, x, y, size, size);
    }
}