package Sound;

public class MusicManager {
    private static Music menuLoop;

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
}
