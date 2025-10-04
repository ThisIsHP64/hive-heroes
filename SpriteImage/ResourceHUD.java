package SpriteImage;

import java.awt.Color;
import java.util.ArrayList;

import Engine.GraphicsHandler;
import Engine.ImageLoader;

public class ResourceHUD extends ImageLoader {
    SpriteImage health, stamina, nectar;
    ArrayList<SpriteImage> resourceBars;

    protected int x;
	protected int y;

    public ResourceHUD() {
        
        health = new SpriteImage("heart_icon.png", 10, 10);
        stamina = new SpriteImage("stamina_icon.png", 20, 20);
        nectar = new SpriteImage("honeypot_icon.png", 30, 10);

        // declares new arraylist containing the resource bars
        resourceBars = new ArrayList<>();
        
        resourceBars.add(health);
        resourceBars.add(stamina);
        resourceBars.add(nectar);

    }

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

    public void update() {
        System.out.println("Updated.");

    }

    public void draw(GraphicsHandler graphicsHandler) {
      
        health.setLocation(10, 15);
        stamina.setLocation(10, 45);
        nectar.setLocation(10, 75);

        graphicsHandler.drawImage(health.getSpriteImage(), health.getX(), health.getY());
        graphicsHandler.drawFilledRectangleWithBorder(health.getX() + 35, health.getY() + 3, 100, 24, Color.RED, Color.RED, 2);

        graphicsHandler.drawImage(stamina.getSpriteImage(), stamina.getX(), stamina.getY());
        graphicsHandler.drawFilledRectangleWithBorder(stamina.getX() + 35, stamina.getY() + 3, 100, 24, Color.GREEN, Color.GREEN, 2);

        graphicsHandler.drawImage(nectar.getSpriteImage(), nectar.getX(), nectar.getY());
        graphicsHandler.drawFilledRectangleWithBorder(nectar.getX() + 35, nectar.getY() + 3, 100, 24, Color.YELLOW, Color.YELLOW, 2);

        // for (SpriteImage s : resourceBars) {
        //     graphicsHandler.drawImage(s.getSpriteImage(), s.getX(), s.getY());
        //     graphicsHandler.drawFilledRectangleWithBorder(s.getX() + 35, s.getY() + 3, 100, 24, Color.RED, Color.GREEN, 2);
        // }
    }
}