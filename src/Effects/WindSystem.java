package Effects;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WindSystem {
    private List<Wind> particles;
    private final int screenWidth, screenHeight;
    private final int maxParticles;

    public WindSystem(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.maxParticles = 150;
        this.particles = new ArrayList<>();
    }

    public void update() {
        // Add new wind particles gradually
        if (particles.size() < maxParticles) {
            particles.add(new Wind(screenWidth, screenHeight));
        }

        // Update all particles
        Iterator<Wind> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Wind p = iterator.next();
            p.update();
            if (p.isOffScreen(screenWidth, screenHeight)) {
                iterator.remove();
            }
        }
    }

    public void draw(Graphics2D g) {
        for (Wind p : particles) {
            p.draw(g);
        }
    }

    public void clear() {
        particles.clear();
    }
}