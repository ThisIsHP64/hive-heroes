package SpriteImage;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Players.Bee;
import StaticClasses.BeeStats;

import java.awt.Color;
import java.util.ArrayList;

public class ResourceHUD extends ImageLoader {
    SpriteImage healthBar, staminaBar, nectarBar, experienceBar, shieldBar;
    ArrayList<SpriteImage> resourceBars;

    protected int x;
	protected int y;

    protected Bee bee;

    public ResourceHUD(Bee bee) {

        healthBar = new SpriteImage("heart_icon.png", 10, 15);
        staminaBar = new SpriteImage("stamina_icon.png", 10, 45);
        nectarBar = new SpriteImage("honeypot_icon.png", 10, 75);
        experienceBar = new SpriteImage("experience_icon.png", 10, 105);
        shieldBar = new SpriteImage("shield_icon.png", 10,135);

        // declares new arraylist containing the resource bars
        resourceBars = new ArrayList<>();
        
        resourceBars.add(healthBar);
        resourceBars.add(staminaBar);
        resourceBars.add(nectarBar);
        resourceBars.add(experienceBar);
    
        this.bee = bee;
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
        
    }

    public void draw(GraphicsHandler graphicsHandler) {

        // i need to add the borders for the maximum values for each stat
        graphicsHandler.drawImage(healthBar.getSpriteImage(), healthBar.getX(), healthBar.getY());
        graphicsHandler.drawFilledRectangleWithBorder(healthBar.getX() + 35, healthBar.getY() + 3, BeeStats.getHealth(), 24, Color.RED, Color.BLACK, 2);

        graphicsHandler.drawImage(staminaBar.getSpriteImage(), staminaBar.getX(), staminaBar.getY());
        graphicsHandler.drawFilledRectangleWithBorder(staminaBar.getX() + 35, staminaBar.getY() + 3, BeeStats.getStamina() / 100, 24, Color.GREEN, Color.BLACK, 2);

        graphicsHandler.drawImage(nectarBar.getSpriteImage(), nectarBar.getX(), nectarBar.getY());
        graphicsHandler.drawFilledRectangleWithBorder(nectarBar.getX() + 35, nectarBar.getY() + 3, BeeStats.getNectar(), 24, Color.YELLOW, Color.BLACK, 2);

        graphicsHandler.drawImage(experienceBar.getSpriteImage(), experienceBar.getX(), experienceBar.getY());
        graphicsHandler.drawFilledRectangleWithBorder(experienceBar.getX() + 35, experienceBar.getY() + 3, BeeStats.getExperience() / 2, 24, Color.YELLOW, Color.BLACK, 2);

        // for (SpriteImage s : resourceBars) {
        //     graphicsHandler.drawImage(s.getSpriteImage(), s.getX(), s.getY());
        //     graphicsHandler.drawFilledRectangleWithBorder(s.getX() + 35, s.getY() + 3, 100, 24, Color.RED, Color.GREEN, 2);
        // }

        // shield bar (this will be only visible when the bee has shield)
        if (bee.hasShield()) {
            graphicsHandler.drawImage(shieldBar.getSpriteImage(), shieldBar.getX(), shieldBar.getY());
            graphicsHandler.drawFilledRectangleWithBorder(
                shieldBar.getX() + 35,
                shieldBar.getY() + 3,
                bee.getShieldHealth(),  // width = current shield HP
                24,
                new Color(0, 255, 255, 180),  // semi-transparent cyan
                Color.BLACK,
                2
            );
        }
    }
}