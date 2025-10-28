package StaticClasses;

public class BeeStats {

    private static int health = 50;
    private static int maxHealth = 50;

    private static int stamina = 25;
    private static int maxStamina = 25; 
    
    private static int nectar = 5;
    private static int maxNectar = 50;

    private static int experience = 1;
    private static int maxExperience = 100;

    private static float walkSpeed = 6f;
    private static float maxWalkSpeed = 10f;

    private static boolean isDead = false;


    // related to bee death and respawn
    public static void respawn() {
        health = maxHealth;
        nectar = 5;
        
    }

    public static boolean isDead() {
        return isDead;
    }

    public static void setDead(boolean deadUpdate) {
        isDead = deadUpdate;
    }

    // health getters and setters
    public static int getHealth() {
        return health;
    }

    public static void setHealth(int newHealth) {
        health = newHealth;
    }

    public static int getMaxHealth() {
        return maxHealth;
    }

    public static void setMaxHealth(int newMaxHealth) {
        maxHealth = newMaxHealth;
    }
    
    // stamina getters and setters
    public static int getStamina() {
        return stamina;
    }

    public static void setStamina(int newStamina) {
        stamina = newStamina;
    }

    public static int getMaxStamina() {
        return maxStamina;
    }

    public static void setMaxStamina(int newMaxStamina) {
        maxStamina = newMaxStamina;
    }

    // nectar getters and setters
    public static int getNectar() {
        return nectar;
    }

    public static void setNectar(int newNectar) {
        if(newNectar < maxNectar) {
            nectar = newNectar;
        }

    }

    public static int getMaxNectar() {
        return maxNectar;
    }

    public static void setMaxNectar(int newMaxNectar) {
        maxNectar = newMaxNectar;
    }


    // experience setters and getters
    public static int getExperience() {
        return experience;
    }

    public static void setExperience(int newExperience) {
        experience = newExperience;
    }

    public static int getMaxExperience() {
        return maxExperience;
    }

    public static void setMaxExperience(int newMaxExperience) {
        maxExperience = newMaxExperience;
    }

    // walk speed getters and setters
    public static float getWalkSpeed() {
        return walkSpeed;
    }

    public static void setWalkSpeed(float newWalkSpeed) {
        walkSpeed = newWalkSpeed;
    }

    public static float getMaxWalkSpeed() {
        return maxWalkSpeed;
    }

    public static void setMaxWalkSpeed(float newMaxWalkSpeed) {
        maxWalkSpeed = newMaxWalkSpeed;
    }
}
