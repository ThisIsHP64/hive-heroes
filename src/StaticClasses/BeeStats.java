package StaticClasses;

import Sound.SFX;
import Sound.SFXManager;

public class BeeStats {

    // ------------------------
    // Health
    // ------------------------
    private static int health = 100;
    private static int maxHealth = 100;

    // ------------------------
    // Stamina (used for projectiles)
    // ------------------------
    // testing values removed — balanced version
    private static int stamina = 2000;
    private static int maxStamina = 10000;

    // Nectar
    private static int nectar = 0;
    private static int maxNectar = 60;

    // Experience
    private static int experience = 0;
    private static int[] experienceThresholds = {30, 50, 70, 100};

    // Movement speed
    private static float walkSpeed = 6f;
    private static float maxWalkSpeed = 10f;

    // Death flag
    private static boolean isDead = false;

    // Level (starts at 1)
    private static int currentLevel = 1;

    // Base melee damage (Bee.java also uses MELEE_DAMAGE = 15)
    private static int attackDamage = 5;

    // ------------------------
    // Tunics / Ring
    // ------------------------
    private static boolean hasTunic = false;
    private static boolean tunicActive = false;

    private static boolean hasRing = false;

    private static boolean hasBlueTunic = false;
    private static boolean blueTunicActive = false;

    private static boolean hasGreenEmerald = false;
    private static boolean hasRedEmerald = false;
    private static boolean hasBlueEmerald = false;
    private static boolean bossActive = false;


    // ------------------------
    // Projectile power (persistent!)
    // ------------------------
    private static boolean hasProjectilePower = false;

    public static boolean hasProjectilePower() {
        return hasProjectilePower;
    }

    public static void setHasProjectilePower(boolean value) {
        hasProjectilePower = value;
    }

    // ------------------------
    // Stamina Management
    // ------------------------
    public static void consumeStamina(int amount) {
        if (stamina > 0) {
            stamina -= amount;
        } else {
            stamina = 0;
        }
    }

    public static void regenerateStamina(int amount) {
        if (stamina < maxStamina) {
            stamina += amount;
            if (stamina > maxStamina) {
                stamina = maxStamina;
            }
        }
    }

    // projectile cost check (150 stamina)
    public static boolean canShootProjectile() {
        return stamina >= 150;
    }

    public static void useProjectileStamina() {
        if (stamina >= 150) {
            stamina -= 150;
            System.out.println("[BeeStats] Used 150 stamina for projectile. Remaining: " + stamina);
        }
    }

    // ------------------------
    // Attack Damage
    // ------------------------
    public static int getAttackDamage() {
        return attackDamage;
    }

        public static void setAttackDamage(int attackDamage) {
            BeeStats.attackDamage = attackDamage;
        }

    // ------------------------
    // Death + Respawn
    // ------------------------
    public static void respawn() {
        health = maxHealth;
        nectar = 0;
        // DO NOT clear projectile power — persistent unlock
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

    // ------------------------
    // Health
    // ------------------------
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

    // ------------------------
    // Stamina getters
    // ------------------------
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

    // ------------------------
    // Nectar
    // ------------------------
    public static int getNectar() {
        return nectar;
    }

    public static void setNectar(int newNectar) {
        if (newNectar < 0)
            newNectar = 0;
        if (newNectar > maxNectar)
            newNectar = maxNectar;
        nectar = newNectar;
    }

    public static int getMaxNectar() {
        return maxNectar;
    }

    public static void setMaxNectar(int newMaxNectar) {
        maxNectar = newMaxNectar;
    }

    // ------------------------
    // Experience / Leveling
    // ------------------------
    public static int getExperience() {
        return experience;
    }

    public static void setExperience(int newExperience) {
        experience = newExperience;
    }

    public static void checkLevelUp() {
        if (currentLevel - 1 < experienceThresholds.length &&
                experience >= experienceThresholds[currentLevel - 1]) {

            experience -= experienceThresholds[currentLevel - 1];
            currentLevel += 1;

            SFXManager.playSFX(SFX.LEVEL);
            processLevelUp();
        }
    }

    public static void processLevelUp() {
        // Level-up buffs
        setMaxHealth(maxHealth + 25);
        setMaxStamina(maxStamina + 25);
        setWalkSpeed(walkSpeed + 1f);
        setMaxNectar(maxNectar + 10);
        setAttackDamage(attackDamage + 1);

        // Completely heal after leveling
        restoreAllStats();
    }

    public static void restoreAllStats() {
        setHealth(maxHealth);
        setStamina(maxStamina);
    }

    public static void resetToDefault() {
        setMaxHealth(100);
        setMaxStamina(100);
        setWalkSpeed(6.0f);
        setMaxNectar(50);
        setAttackDamage(5);
        setCurrentLevel(1);
        restoreAllStats();
        setDead(false);
        setHasTunic(false);
        setTunicActive(false);
        setHasRing(false);
        setHasBlueTunic(false);
        setBlueTunicActive(false);
    }

    // ------------------------
    // Speed
    // ------------------------
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

    // ------------------------
    // Level Getter / Setter
    // ------------------------
    public static int getCurrentLevel() {
        return currentLevel;
    }

    public static void setCurrentLevel(int currentLevel) {
        BeeStats.currentLevel = currentLevel;
    }

    // ------------------------
    // Tunics / Ring
    // ------------------------
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

    public static boolean hasGreenEmerald() {
        return hasGreenEmerald;
    }

    public static void setHasGreenEmerald(boolean value) {
        hasGreenEmerald = value;
    }

    public static boolean hasBlueEmerald() {
        return hasBlueEmerald;
    }

    public static void setHasBlueEmerald(boolean value) {
        hasBlueEmerald = value;
    }

    public static boolean hasRedEmerald() {
        return hasRedEmerald;
    }

    public static void setHasRedEmerald(boolean value) {
        hasRedEmerald = value;
    }

    public static boolean isBossActive() {
        return bossActive;
    }

    public static void setBossActive(boolean value) {
        bossActive = value;
    }
}
