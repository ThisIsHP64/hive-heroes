package Projectiles;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Utils.Direction;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class BeeProjectile {

    private static final int TILE = 62;      // make sure your Bee_Projectile.png uses 20px cells
    private static final float SCALE = 2f;
    private static final int ANIM_TICKS = 6;

    private float x, y;          // world coords
    private float dx, dy;        // velocity
    private float speed = 8f;

    private Direction direction; // initial intent (we'll still store it)
    private boolean isActive = true;

    private int animIndex = 0;
    private int animCounter = 0;

    private static BufferedImage sheet;
    private static BufferedImage[][] frames; // [row][col]

    public BeeProjectile(float x, float y, Direction dir) {
        this.x = x;
        this.y = y;
        this.direction = dir;

        ensureSheet();

        switch (dir) {
            case LEFT:  dx = -speed; dy = 0;      break;
            case RIGHT: dx =  speed; dy = 0;      break;
            case UP:    dx = 0;      dy = -speed; break;
            case DOWN:  dx = 0;      dy =  speed; break;
            default:    dx = speed;  dy = 0;      break;
        }

        System.out.println("[Proj] spawn dir=" + dir + " dx=" + dx + " dy=" + dy);
    }

    public void update() {
        if (!isActive) return;

        x += dx;
        y += dy;

        if (x < -64 || y < -64 || x > 10000 || y > 10000) {
            isActive = false;
        }

        if (++animCounter >= ANIM_TICKS) {
            animCounter = 0;
            animIndex ^= 1;
        }
    }

    // Backward-compatible
    public void draw(GraphicsHandler g) { draw(g, 0f, 0f); }

    public void draw(GraphicsHandler g, float camX, float camY) {
        if (!isActive) return;

        // CHANGED: derive the render-facing from velocity, not from the constructor direction
        Direction renderDir = deriveDirectionFromVelocity();

        BufferedImage sprite = getFrame(renderDir, animIndex);
        int drawW = Math.round(TILE * SCALE);
        int drawH = Math.round(TILE * SCALE);

        int sx = Math.round(x - camX);
        int sy = Math.round(y - camY);

        g.drawImage(sprite, sx, sy, drawW, drawH);
    }

    private Direction deriveDirectionFromVelocity() {
        // If horizontal movement dominates or dy==0, choose LEFT/RIGHT
        if (Math.abs(dx) >= Math.abs(dy)) {
            return (dx < 0) ? Direction.LEFT : Direction.RIGHT;
        }
        // Otherwise UP/DOWN
        return (dy < 0) ? Direction.UP : Direction.DOWN;
    }

    public boolean isActive() { return isActive; }
    public void deactivate() { isActive = false; }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; } // kept if other code still calls these
    public void setY(float y) { this.y = y; }

    public Rectangle getHitbox() {
        int w = Math.round(TILE * SCALE);
        int h = Math.round(TILE * SCALE);
        return new Rectangle(Math.round(x), Math.round(y), w, h);
    }

    private static void ensureSheet() {
        if (sheet != null) return;
        sheet = ImageLoader.load("Bee_Projectile.png");
        int rows = sheet.getHeight() / TILE; // expect 3
        int cols = sheet.getWidth()  / TILE; // expect 3
        frames = new BufferedImage[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                frames[r][c] = sheet.getSubimage(c * TILE, r * TILE, TILE, TILE);
            }
        }
        System.out.println("[Proj] sheet loaded: " + cols + "x" + rows + " tiles @ " + TILE + "px");
    }

    // Your 3x3 layout:
    // Row0: (0,0) DOWN1, (1,0) DOWN2, (2,0) RIGHT1
    // Row1: (0,1) RIGHT2, (1,1) LEFT1, (2,1) LEFT2
    // Row2: (0,2) UP1,    (1,2) UP2,   (2,2) empty
    private BufferedImage getFrame(Direction d, int idx) {
        switch (d) {
            case DOWN:  return (idx == 0) ? frames[0][0] : frames[0][1];
            case RIGHT: return (idx == 0) ? frames[0][2] : frames[1][0];
            case LEFT:  return (idx == 0) ? frames[1][1] : frames[1][2];
            case UP:    return (idx == 0) ? frames[2][0] : frames[2][1];
            default:    return frames[0][0];
        }
    }
}
