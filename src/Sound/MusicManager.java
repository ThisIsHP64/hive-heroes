package Sound;

public class MusicManager {
    private static Music menuLoop, gpLoop;

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
}
