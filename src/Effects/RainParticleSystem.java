package Effects;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RainParticleSystem {
    private List<RainParticle> particles;
    private final int screenWidth, screenHeight;
    private final int maxParticles;

    public RainParticleSystem(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.maxParticles = 500;
        this.particles = new ArrayList<>();
    }

    public void update() {
        // Add new particles if needed
        if (particles.size() < maxParticles) {
            particles.add(new RainParticle(screenWidth));
        }

        // Update all particles and remove off-screen ones
        Iterator<RainParticle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            RainParticle p = iterator.next();
            p.update();
            if (p.isOffScreen(screenHeight)) {
                iterator.remove();
            }
        }
    }

    public void draw(Graphics2D g) {
        for (RainParticle p : particles) {
            p.draw(g);
        }
    }
    
    public void clear() {
        particles.clear();
    }
}