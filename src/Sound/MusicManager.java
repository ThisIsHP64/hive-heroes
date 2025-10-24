package Sound;

public class MusicManager {
    private static Music menuLoop, gpLoop, siegeLoop;

    public static void playMenuLoop() {
        if (menuLoop == null) {
            menuLoop = new Music("Resources/audio/title.wav");
            menuLoop.loopAtBar(90, 9);
        } else if(!menuLoop.isPlaying()) {
            menuLoop.loopAtBar(90, 9);
        }
    }

    public static void stopMenuLoop() {
        if(menuLoop != null) {
            menuLoop.stop();
        }
    }

    public static void playGpLoop() {
        if (gpLoop == null) {
            gpLoop = new Music("Resources/audio/gp.wav");
            gpLoop.loopAtBar(128.6, 13);
        } else if(!gpLoop.isPlaying()) {
            gpLoop.loopAtBar(128.6, 13);
        }
    }

    public static void stopGpLoop() {
        if(gpLoop != null) {
            gpLoop.stop();
        }
    }

    public static void playSiegeLoop() {
        if (siegeLoop == null) {
            siegeLoop = new Music("Resources/audio/siege.wav");
            siegeLoop.loopAtBar(120, 8); // adjust BPM and bar count as needed
        } else if(!siegeLoop.isPlaying()) {
            siegeLoop.loopAtBar(120, 8);
        }
    }

    public static void stopSiegeLoop() {
        if(siegeLoop != null) {
            siegeLoop.stop();
        }
    }
}