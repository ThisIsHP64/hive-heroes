package Effects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class WindGrassLevel {
    private static final Random rand = new Random();
    private float x, y, speedX, speedY;
    private int length;

    public WindGrassLevel(int screenWidth, int screenHeight) {
        x = rand.nextInt(screenWidth);
        y = rand.nextInt(screenHeight);
        // wind blows mostly horizontally
        speedX = 2 + rand.nextFloat() * 2;  // wind speed to the right
        speedY = -0.5f + rand.nextFloat();  // slight vertical drift
        length = 10 + rand.nextInt(10);
    }

    public void update() {
        x += speedX;
        y += speedY;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 255, 255, 100)); 
        g.drawLine((int)x, (int)y, (int)(x - length), (int)(y));
    }

    public boolean isOffScreen(int screenWidth, int screenHeight) {
        return (x > screenWidth || y < 0 || y > screenHeight);
    }
}
