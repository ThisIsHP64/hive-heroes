package Effects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class RedRainParticleBossFight {
    private int x, y;
    private int speed;
    private static final Random random = new Random();

    public RedRainParticleBossFight(int screenWidth) {
        // Starts the raindrop at a random x-position above the screen
        this.x = random.nextInt(screenWidth);
        this.y = -random.nextInt(300); // Start off-screen
        this.speed = 5 + random.nextInt(10);
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(148, 1, 1, 180)); // Dark red
        g.drawLine(x, y, x + 10, y); // Draw a line for the raindrop
    }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }
}