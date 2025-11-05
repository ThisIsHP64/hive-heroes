package StaticClasses;

public class BeeStats {

    // health variables
    private static int health = 100;
    private static int maxHealth = 100;

    // stamina variables
    private static int stamina = 100;
    private static int maxStamina = 100;

    // nectar variables
    private static int nectar = 0;
    private static int maxNectar = 150;

    // experience variables
    private static int experience = 0;
    private static int[] experienceThresholds = {100, 250, 450, 700};

    // walk speed variables
    private static float walkSpeed = 8f;
    private static float maxWalkSpeed = 10f;

    // is Bee dead?
    private static boolean isDead = false;

    // bee starts at level 1
    private static int currentLevel = 1;

    // the max level the bee can get to
    private static final int maxLevel = 4;

    private static int attackDamage = 5;

    // tunic variable (red)
    private static boolean hasTunic = false;
    private static boolean tunicActive = false;

    // ring variable (OneRing)
    private static boolean hasRing = false;

    // tunic variable (blue)
    private static boolean hasBlueTunic = false;

    private static boolean blueTunicActive = false;

    public static void manageStamina() {
        if (stamina > 0) {
            stamina--;
        } else {
            stamina = 0;
        }
    }

    public static int getAttackDamage() {
        return attackDamage;
    }

    public static void setAttackDamage(int attackDamage) {
        BeeStats.attackDamage = attackDamage;
    }

    // related to bee death and respawn
    public static void respawn() {
        health = maxHealth;
        nectar = 0;
    }

    public static void takeDamage(int damage) {
        
        if (!isDead) {
            health -= damage;
        } else {
            isDead = true;
        }
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
        if (newNectar < 0)
            newNectar = 0;
        if (newNectar > maxNectar)
            newNectar = maxNectar; // hard cap
        nectar = newNectar;
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

    // if the player's current experience is greater than or equal to the current 
    // threshold, subtract the experience and level them up.
    public static void checkLevelUp() {
        if (experience >= experienceThresholds[currentLevel - 1]) {
            experience -= experienceThresholds[currentLevel - 1];
            currentLevel += 1;
            processLevelUp();
        }
    }

    public static void processLevelUp() {
        setMaxHealth(maxHealth + 25);
        setMaxStamina(maxStamina + 25);
        setWalkSpeed(walkSpeed + 1.5f);
        setMaxNectar(maxNectar + 10);
        setAttackDamage(attackDamage + 1);

        restoreAllStats();
    }

    public static void restoreAllStats() {
        setHealth(maxHealth);
        setStamina(maxStamina);
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

    public static int getCurrentLevel() {
        return currentLevel;
    }

    public static void setCurrentLevel(int currentLevel) {
        BeeStats.currentLevel = currentLevel;
    }

    public static boolean hasTunic() {
        return hasTunic;
    }

    public static void setHasTunic(boolean value) {
    hasTunic = value;
    }

    public static boolean isTunicActive() {
        return tunicActive;
    }

    public static void setTunicActive(boolean value) {
        tunicActive = value;
    }

    public static boolean hasRing() {
        return hasRing;
    }

    public static void setHasRing(boolean value) {
        hasRing = value;
    }    
    
    public static boolean hasBlueTunic() {
        return hasBlueTunic;
    }

    public static void setHasBlueTunic(boolean value) {
        hasBlueTunic = value;
    }

    public static boolean isBlueTunicActive() {
        return blueTunicActive;
    }

    public static void setBlueTunicActive(boolean value) {
        blueTunicActive = value;
    }
}