package Effects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class RainParticleBossFight {
    private int x, y;
    private int speed;
    private static final Random random = new Random();

    public RainParticleBossFight(int screenWidth) {
        // Starts the raindrop at a random x-position above the screen
        this.x = random.nextInt(screenWidth);
        this.y = -random.nextInt(300); // Start off-screen
        this.speed = 5 + random.nextInt(10);
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(150, 200, 255, 180)); // Semi-transparent blue
        g.drawLine(x, y, x, y + 10); // Draw a line for the raindrop
    }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }
}