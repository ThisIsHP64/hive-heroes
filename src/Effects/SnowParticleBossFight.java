package Effects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class SnowParticleBossFight {
    private int x, y;
    private int speed;

    private static final Random random = new Random();

    public SnowParticleBossFight(int screenWidth) {
        // Starts the raindrop at a random x-position above the screen
        this.x = random.nextInt(screenWidth);
        this.y = -random.nextInt(300); // Start off-screen
        this.speed = 5 + random.nextInt(10);
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 255, 255, 180)); // white
        g.fillOval(x, y, 5,5); // Draw a circle for the raindrop
    }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }
}