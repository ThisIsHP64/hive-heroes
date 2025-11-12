package Effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;

/**
 * ScreenFX – global post-process effects (DARKEN / INVERT / RED_TINT).
 *
 * Start:  ScreenFX.start(ScreenFX.Effect.DARKEN, 1200, 0.65f);
 * Apply (no backbuffer): ScreenFX.apply(g2d, panelWidth, panelHeight);
 * Apply (with backbuffer): ScreenFX.apply(g2d, backBufferImage);
 */
public final class ScreenFX {

    // ADDED: RED_TINT and RED_VIGNETTE_PULSE effects for ring corruption
    public enum Effect { NONE, DARKEN, INVERT, RED_TINT, RED_VIGNETTE_PULSE }

    private static volatile Effect current = Effect.NONE;
    private static volatile long endAtMs = 0L;
    private static volatile float strength = 0f; // 0..1 for DARKEN and RED_TINT

    private ScreenFX() {}

    /** Start a screen effect. For DARKEN and RED_TINT, use strength in [0..1]. INVERT ignores strength. */
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
            case RED_TINT -> {
                // Red overlay for evil/corruption effect
                var old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.SrcOver.derive(strength));
                g2d.setColor(new Color(180, 0, 0)); // Dark red tint
                g2d.fillRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());
                g2d.setComposite(old);
            }
            case RED_VIGNETTE_PULSE -> {
                // ADDED: Pulsing red vignette - darkness breathes from edges
                int width = backBuffer.getWidth();
                int height = backBuffer.getHeight();
                
                float pulseSpeed = 0.002f;
                float minStrength = strength * 0.6f;
                float maxStrength = strength * 1.4f;
                float pulse = minStrength + (maxStrength - minStrength) * 
                    (0.5f + 0.5f * (float)Math.sin(now * pulseSpeed));
                
                float centerX = width / 2f;
                float centerY = height / 2f;
                float maxRadius = (float)Math.sqrt(centerX * centerX + centerY * centerY);
                
                java.awt.RadialGradientPaint gradient = new java.awt.RadialGradientPaint(
                    centerX, centerY, maxRadius,
                    new float[] {0.0f, 0.5f, 1.0f},
                    new Color[] {
                        new Color(120, 0, 0, 0),
                        new Color(150, 0, 0, (int)(pulse * 180)),
                        new Color(80, 0, 0, (int)(pulse * 255))
                    }
                );
                
                var old = g2d.getComposite();
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
                g2d.setComposite(old);
            }
            default -> {}
        }
    }

    /**
     * Apply directly to the live Graphics2D with panel size.
     * DARKEN uses alpha overlay; INVERT uses XOR fill (fast, no backbuffer needed); RED_TINT uses red overlay.
     * RED_VIGNETTE_PULSE creates a pulsing red darkness from the edges.
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
            case RED_TINT -> {
                // Red corruption effect - like fires of Mount Doom / Sauron's influence
                var old = g2d.getComposite();
                g2d.setComposite(AlphaComposite.SrcOver.derive(strength));
                g2d.setColor(new Color(180, 0, 0)); // Dark red tint (adjust RGB if needed)
                g2d.fillRect(0, 0, width, height);
                g2d.setComposite(old);
            }
            case RED_VIGNETTE_PULSE -> {
                // ADDED: Pulsing red vignette - darkness breathes from edges like evil consuming the world
                // Calculate pulse (breathing effect) - oscillates between base and peak strength
                float pulseSpeed = 0.002f; // Speed of the pulse
                float minStrength = strength * 0.6f; // Minimum intensity
                float maxStrength = strength * 1.4f; // Maximum intensity
                float pulse = minStrength + (maxStrength - minStrength) * 
                    (0.5f + 0.5f * (float)Math.sin(now * pulseSpeed));
                
                // Draw radial gradient vignette (darker/redder at edges, lighter in center)
                float centerX = width / 2f;
                float centerY = height / 2f;
                float maxRadius = (float)Math.sqrt(centerX * centerX + centerY * centerY);
                
                java.awt.RadialGradientPaint gradient = new java.awt.RadialGradientPaint(
                    centerX, centerY, maxRadius,
                    new float[] {0.0f, 0.5f, 1.0f}, // gradient stops
                    new Color[] {
                        new Color(120, 0, 0, 0),           // Center: transparent dark red
                        new Color(150, 0, 0, (int)(pulse * 180)), // Middle: pulsing red
                        new Color(80, 0, 0, (int)(pulse * 255))   // Edges: intense dark red
                    }
                );
                
                var old = g2d.getComposite();
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
                g2d.setComposite(old);
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