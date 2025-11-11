package Effects;

import java.awt.Point;

/**
 * CameraShake – lightweight static utility to add screen shake.
 * Call start(intensity, durationMs) when something explodes.
 * Each frame, call getOffset() to get the current x/y jitter.
 */
public final class CameraShake {

    private static boolean active = false;
    private static long startTime;
    private static long duration;
    private static float intensity;
    private static final Point offset = new Point(0, 0);

    private CameraShake() {}

    /** Start a camera shake effect. */
    public static void start(float intensity, int durationMs) {
        CameraShake.intensity = intensity;
        CameraShake.duration = durationMs;
        CameraShake.startTime = System.currentTimeMillis();
        CameraShake.active = true;
    }

    /** Update the offset based on elapsed time; call once per frame. */
    public static void update() {
        if (!active) return;
        long now = System.currentTimeMillis();
        if (now - startTime > duration) {
            active = false;
            offset.x = 0;
            offset.y = 0;
            return;
        }

        // random small offset that fades out
        float progress = (now - startTime) / (float) duration;
        float falloff = 1.0f - progress;
        offset.x = (int) ((Math.random() - 0.5) * 2 * intensity * falloff);
        offset.y = (int) ((Math.random() - 0.5) * 2 * intensity * falloff);
    }

    /** Returns the current shake offset (x,y). */
    public static Point getOffset() {
        return new Point(offset);
    }

    /** Whether the shake is still running. */
    public static boolean isActive() {
        return active;
    }
}
