package Effects;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SnowParticleSystemSnowLevel {
    private List<SnowParticleSnowLevel> particles;
    private final int screenWidth, screenHeight;
    private final int maxParticles;

    public SnowParticleSystemSnowLevel(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.maxParticles = 500;
        this.particles = new ArrayList<>();
    }

    public void update() {
        // adds new particles if needed
        if (particles.size() < maxParticles) {
            particles.add(new SnowParticleSnowLevel(screenWidth));
        }

        // Updates all the particles and removes the ones that are off-screen
        Iterator<SnowParticleSnowLevel> iterator = particles.iterator();
        while (iterator.hasNext()) {
            SnowParticleSnowLevel p = iterator.next();
            p.update();
            if (p.isOffScreen(screenHeight)) {
                iterator.remove();
            }
        }
    }

    public void draw(Graphics2D g) {
        for (SnowParticleSnowLevel p : new ArrayList<>(particles)) {
            p.draw(g);
        }
    }
    
    public void clear() {
        particles.clear();
    }
}