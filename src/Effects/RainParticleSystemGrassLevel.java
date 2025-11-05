package Effects;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RainParticleSystemGrassLevel {
    private List<RainParticleGrassLevel> particles;
    private final int screenWidth, screenHeight;
    private final int maxParticles;

    public RainParticleSystemGrassLevel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.maxParticles = 500;
        this.particles = new ArrayList<>();
    }

    public void update() {
        // adds new particles if needed
        if (particles.size() < maxParticles) {
            particles.add(new RainParticleGrassLevel(screenWidth));
        }

        // Updates all the particles and removes the ones that are off-screen
        Iterator<RainParticleGrassLevel> iterator = particles.iterator();
        while (iterator.hasNext()) {
            RainParticleGrassLevel p = iterator.next();
            p.update();
            if (p.isOffScreen(screenHeight)) {
                iterator.remove();
            }
        }
    }

    public void draw(Graphics2D g) {
        for (RainParticleGrassLevel p : new ArrayList<>(particles)) {
            p.draw(g);
        }
    }
    
    public void clear() {
        particles.clear();
    }
}