package SpriteImage;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import java.awt.image.BufferedImage;

public class ProjectileHUD {
    
    private BufferedImage projectileIcon;
    private boolean hasProjectile = false;
    
    // layout for bottom-right
    private static final int SCREEN_WIDTH = 800;  // keep as is
    private static final int BASE_Y = 480;  // moved UP from 520 (was getting cut off at bottom)
    private static final int ICON_SIZE = 100;  // keep at 80
    private static final int RIGHT_MARGIN = 30;  // increased from 10 (move away from right edge)
    
    public void showProjectile(String iconPath) {
        projectileIcon = ImageLoader.load(iconPath);
        hasProjectile = true;
        System.out.println("[ProjectileHUD] Projectile icon shown!");
    }
    
    public void hideProjectile() {
        hasProjectile = false;
        projectileIcon = null;
        System.out.println("[ProjectileHUD] Projectile icon hidden!");
    }
    
    public boolean hasProjectile() {
        return hasProjectile;
    }
    
    public void draw(GraphicsHandler graphicsHandler) {
        if (hasProjectile && projectileIcon != null) {
            int x = SCREEN_WIDTH - RIGHT_MARGIN - ICON_SIZE;
            graphicsHandler.drawImage(projectileIcon, x, BASE_Y, ICON_SIZE, ICON_SIZE);
        }
    }
    
    public void clear() {
        hasProjectile = false;
        projectileIcon = null;
    }
}