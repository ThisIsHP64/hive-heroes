package SpriteImage;

import java.awt.Color;
import java.util.ArrayList;

import Engine.GraphicsHandler;
import Engine.ImageLoader;


public class ResourceHUD extends ImageLoader {
    SpriteImage healthBar, staminaBar, nectarBar;
    ArrayList<SpriteImage> resourceBars;

    protected int x;
	protected int y;

    protected int health = 250;
    protected int stamina = 100;
    protected int nectar = 50;


    public ResourceHUD() {

        healthBar = new SpriteImage("heart_icon.png", 10, 10);
        staminaBar = new SpriteImage("stamina_icon.png", 20, 20);
        nectarBar = new SpriteImage("honeypot_icon.png", 30, 10);

        // declares new arraylist containing the resource bars
        resourceBars = new ArrayList<>();
        
        resourceBars.add(healthBar);
        resourceBars.add(staminaBar);
        resourceBars.add(nectarBar);
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
        // System.out.println(String.format("Health: %d  Stamina: %d  Nectar: %d", player.getHealth(), player.getStamina(), player.getNectar()));
    }

    public void draw(GraphicsHandler graphicsHandler) {
      
        healthBar.setLocation(10, 15);
        staminaBar.setLocation(10, 45);
        nectarBar.setLocation(10, 75);

        graphicsHandler.drawImage(healthBar.getSpriteImage(), healthBar.getX(), healthBar.getY());
        graphicsHandler.drawFilledRectangleWithBorder(healthBar.getX() + 35, healthBar.getY() + 3, health, 24, Color.RED, Color.RED, 2);

        graphicsHandler.drawImage(staminaBar.getSpriteImage(), staminaBar.getX(), staminaBar.getY());
        graphicsHandler.drawFilledRectangleWithBorder(staminaBar.getX() + 35, staminaBar.getY() + 3, stamina, 24, Color.GREEN, Color.GREEN, 2);

        graphicsHandler.drawImage(nectarBar.getSpriteImage(), nectarBar.getX(), nectarBar.getY());
        graphicsHandler.drawFilledRectangleWithBorder(nectarBar.getX() + 35, nectarBar.getY() + 3, nectar, 24, Color.YELLOW, Color.YELLOW, 2);

        // for (SpriteImage s : resourceBars) {
        //     graphicsHandler.drawImage(s.getSpriteImage(), s.getX(), s.getY());
        //     graphicsHandler.drawFilledRectangleWithBorder(s.getX() + 35, s.getY() + 3, 100, 24, Color.RED, Color.GREEN, 2);
        // }
    }
}