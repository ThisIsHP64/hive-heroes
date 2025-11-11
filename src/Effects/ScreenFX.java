package Effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;

/**
 * ScreenFX – global post-process effects (DARKEN / INVERT).
 *
 * Start:  ScreenFX.start(ScreenFX.Effect.DARKEN, 1200, 0.65f);
 * Apply (no backbuffer): ScreenFX.apply(g2d, panelWidth, panelHeight);
 * Apply (with backbuffer): ScreenFX.apply(g2d, backBufferImage);
 */
public final class ScreenFX {

    public enum Effect { NONE, DARKEN, INVERT }

    private static volatile Effect current = Effect.NONE;
    private static volatile long endAtMs = 0L;
    private static volatile float strength = 0f; // 0..1 for DARKEN

    private ScreenFX() {}

    /** Start a screen effect. For DARKEN, use strength in [0..1]. INVERT ignores strength. */
    public static void start(Effect effect, long durationMs, float strength01) {
        if (effect == null || durationMs <= 0) {
            current = Effect.NONE;
            endAtMs = 0L;
            strength = 0f;
            return;
        }
        current = effect;
        endAtMs = System.currentTimeMillis() + durationMs;
        strength = clamp01(strength01);
    }

    /** Apply using a back buffer image (keeps your older pipeline working). */
    public static void apply(Graphics2D g2d, BufferedImage backBuffer) {
        if (current == Effect.NONE || g2d == null || backBuffer == null) return;
        long now = System.currentTimeMillis();
        if (now >= endAtMs) { current = Effect.NONE; return; }

        switch (current) {
            case DARKEN -> {
                var old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.SrcOver.derive(strength));
                g2d.setColor(Color.black);
                g2d.fillRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());
                g2d.setComposite(old);
            }
            case INVERT -> {
                final byte[] invert = new byte[256];
                for (int i = 0; i < 256; i++) invert[i] = (byte)(255 - i);
                new LookupOp(new ByteLookupTable(0, invert), null).filter(backBuffer, backBuffer);
            }
            default -> {}
        }
    }

    /**
     * Apply directly to the live Graphics2D with panel size.
     * DARKEN uses alpha overlay; INVERT uses XOR fill (fast, no backbuffer needed).
     */
    public static void apply(Graphics2D g2d, int width, int height) {
        if (current == Effect.NONE || g2d == null) return;
        long now = System.currentTimeMillis();
        if (now >= endAtMs) { current = Effect.NONE; return; }

        switch (current) {
            case DARKEN -> {
                var old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.SrcOver.derive(strength));
                g2d.setColor(Color.black);
                g2d.fillRect(0, 0, width, height);
                g2d.setComposite(old);
            }
            case INVERT -> {
                // XOR with white gives a negative-like flash without image buffers
                g2d.setXORMode(Color.white);
                g2d.fillRect(0, 0, width, height);
                g2d.setPaintMode();
            }
            default -> {}
        }
    }

    public static boolean isActive() {
        return current != Effect.NONE && System.currentTimeMillis() < endAtMs;
    }

    private static float clamp01(float v) {
        return Math.max(0f, Math.min(1f, v));
    }
}
