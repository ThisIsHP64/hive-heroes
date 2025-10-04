package SpriteImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import Engine.Config;
import Engine.GraphicsHandler;
import Engine.ImageLoader;

public class SpriteImage extends ImageLoader {
    protected int x;
	protected int y;
    BufferedImage bi;
    protected String imageString;

        // loads an image and sets its transparent color to the one defined in the Config class
    public SpriteImage(String imageString, int x, int y) {
        this.imageString = imageString;
        this.x = x;
        this.y = y;
        bi = ImageLoader.load(imageString, Config.TRANSPARENT_COLOR);
    }

    public BufferedImage getSpriteImage() {
        return bi;
    }
    // public BufferedImage convertFromImageIcon(ImageIcon image) {
    //     this.image = image;
    //     BufferedImage bi = new BufferedImage(
    //         image.getIconWidth(),
    //         image.getIconHeight(),
    //         BufferedImage.TYPE_INT_RGB);
    //         Graphics2D g = bi.createGraphics();
    //         // paint the Icon to the BufferedImage.
    //         image.paintIcon(null, g, 0,0);
    //         g.dispose();

    //     return bi;
    // }

    public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

    // it should check if the bee was hurt, and then update the sprites
    public void update() {


    }


    public void draw(GraphicsHandler graphicsHandler) {
        // draws the heart at a specific x, y coordinate
        graphicsHandler.drawImage(bi, x, y);
        
        graphicsHandler.drawFilledRectangleWithBorder(45, 15, 100, 24, Color.RED, Color.GREEN, 2);
	}
}
