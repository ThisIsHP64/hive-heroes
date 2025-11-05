package Effects;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RedRainParticleSystemVolcanoLevel {
    private List<RedRainParticleVolcanoLevel> particles;
    private final int screenWidth, screenHeight;
    private final int maxParticles;

    public RedRainParticleSystemVolcanoLevel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.maxParticles = 500;
        this.particles = new ArrayList<>();
    }

    public void update() {
        // adds new particles if needed
        if (particles.size() < maxParticles) {
            particles.add(new RedRainParticleVolcanoLevel(screenWidth));
        }

        // Updates all the particles and removes the ones that are off-screen
        Iterator<RedRainParticleVolcanoLevel> iterator = particles.iterator();
        while (iterator.hasNext()) {
            RedRainParticleVolcanoLevel p = iterator.next();
            p.update();
            if (p.isOffScreen(screenHeight)) {
                iterator.remove();
            }
        }
    }

    public void draw(Graphics2D g) {
        for (RedRainParticleVolcanoLevel p : new ArrayList<>(particles)) {
            p.draw(g);
        }
    }
    
    public void clear() {
        particles.clear();
    }
}