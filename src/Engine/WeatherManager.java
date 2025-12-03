package Engine;

public class WeatherManager {
    private boolean timedMode = true;
    private boolean overrideMode = false;
    private boolean raining = false;
    private boolean wind = false;
    private boolean redRain = false;
    private boolean snow = false;

    public static final WeatherManager GLOBAL = new WeatherManager();

    public boolean isTimedMode() {
        return timedMode;
    }

    public void setTimedMode(boolean timedMode) {
        this.timedMode = timedMode;
    }

    public void enableOverrideMode() {
        overrideMode = true;
        timedMode = false;
    }

    public void disableOverrideMode() {
        overrideMode = false;
        timedMode = true;
    }

    public boolean isOverrideMode() {
        return overrideMode;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
        System.out.println("It has began to rain. Your wings are slowed.");
    }

    public boolean isWind() {
        return wind;
    }

    public void setWind(boolean wind) {
        this.wind = wind;
        System.out.println("The galeful wind stifles your movements.");
    }

    public boolean isRedRain() {
        return redRain;
    }

    public void setRedRain(boolean redRain) {
        this.redRain = redRain;
        System.out.println("Take cover! The lava rain burns through your skin.");
    }

    public boolean isSnow() {
        return snow;
    }

    public void setSnow(boolean snow) {
        this.snow = snow;
        System.out.println("You begin to feel a wistful chill in the air, and freeze up.");
    }
}
