package GameObject;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private final BufferedImage image;

    // public for engine compatibility (Tileset, etc.)
    public final int spriteWidth;
    public final int spriteHeight;

    // pixels of spacing between tiles; legacy/common tiles = 1, Bee = 0
    private final int gutter;

    // Backward-compatible constructor (keeps old behavior = 1px gutter)
    public SpriteSheet(BufferedImage image, int spriteWidth, int spriteHeight) {
        this(image, spriteWidth, spriteHeight, 1);
    }

    // New: explicitly choose gutter
    public SpriteSheet(BufferedImage image, int spriteWidth, int spriteHeight, int gutter) {
        this.image = image;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.gutter = Math.max(0, gutter);
    }

    // Preferred API
    public BufferedImage getSprite(int row, int col) {
        int x = col * (spriteWidth  + gutter);
        int y = row * (spriteHeight + gutter);

        if (x + spriteWidth > image.getWidth() || y + spriteHeight > image.getHeight()) {
            throw new IllegalArgumentException(
                "Sprite out of bounds: row=" + row + ", col=" + col +
                ", tile=" + spriteWidth + "x" + spriteHeight +
                ", gutter=" + gutter +
                ", sheet=" + image.getWidth() + "x" + image.getHeight()
            );
        }
        return image.getSubimage(x, y, spriteWidth, spriteHeight);
    }

    // Compatibility alias (some code calls getSubImage)
    public BufferedImage getSubImage(int row, int col) {
        return getSprite(row, col);
    }

    // Helpers
    public int getTileWidth()   { return spriteWidth; }
    public int getTileHeight()  { return spriteHeight; }
    public int getGutter()      { return gutter; }
    public int getSheetWidth()  { return image.getWidth(); }
    public int getSheetHeight() { return image.getHeight(); }
}
