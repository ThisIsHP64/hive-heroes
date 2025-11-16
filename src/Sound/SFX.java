package Sound;

import javax.sound.sampled.*;
import java.io.File;

public class SFX {
    // songs
    public static final int NECTAR = 0;
    public static final int MOVE = 1;
    public static final int SHOOT = 2;
    public static final int STING = 3;
    public static final int HURT = 4;
    public static final int LEVEL = 5;

    // actual audio data
    private Clip clip;

    // constructor
    public SFX(String audioPath) {
        try {
            File file = new File(audioPath);
            AudioInputStream input = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // play the audio once
    public void play() {
        if(clip != null) {
            if(clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // check if the audio is playing
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}
