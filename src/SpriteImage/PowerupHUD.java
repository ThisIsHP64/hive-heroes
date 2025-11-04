package SpriteImage;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PowerupHUD {

    // each icon has its own image, identifier (path), and duration timer
    private static class IconEntry {
        BufferedImage image;
        String path; // used to identify which icon to remove
        long startTime;
        int durationMs;

        IconEntry(BufferedImage image, String path, int durationMs) {
            this.image = image;
            this.path = path;
            this.durationMs = durationMs;
            this.startTime = System.currentTimeMillis();
        }

        boolean isExpired() {
            return (System.currentTimeMillis() - startTime) > durationMs;
        }
    }

    private final List<IconEntry> activeIcons = new ArrayList<>();

    // layout for bottom-left
    private static final int BASE_X = 10;
    private static final int BASE_Y = 520;
    private static final int ICON_SIZE = 48;
    private static final int ICON_SPACING = 8;

    // add icon by image path
    public void show(String path, int durationMs) {
        BufferedImage img = ImageLoader.load(path);
        activeIcons.add(new IconEntry(img, path, durationMs));

        System.out.println("[HUD] Added icon: " + path);
    }

    // add icon from a loaded image
    public void showImage(BufferedImage img, int durationMs) {
        activeIcons.add(new IconEntry(img, "direct", durationMs));
    }

    public void removeIcon(String path) {
    if (path == null) return;

    String normalizedTarget = path.toLowerCase().trim();

    Iterator<IconEntry> it = activeIcons.iterator();
    while (it.hasNext()) {
        IconEntry entry = it.next();
        if (entry.path != null && entry.path.toLowerCase().trim().equals(normalizedTarget)) {
            it.remove();
            System.out.println("[HUD] Removed icon: " + entry.path);
            return;
        } else {
            System.out.println("[HUD] Checked icon: " + entry.path);
        }
    }

    System.out.println("[HUD] Could not find icon to remove: " + path);
}


    // draw all icons side-by-side
    public void draw(GraphicsHandler graphicsHandler) {
        // remove expired ones
        Iterator<IconEntry> iterator = activeIcons.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isExpired()) {
                iterator.remove();
            }
        }

        int x = BASE_X;
        for (IconEntry entry : activeIcons) {
            graphicsHandler.drawImage(entry.image, x, BASE_Y, ICON_SIZE, ICON_SIZE);
            x += ICON_SIZE + ICON_SPACING;
        }
    }

    public void clear() {
        activeIcons.clear();
    }
}
