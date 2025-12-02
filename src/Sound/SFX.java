package Sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Objects;

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
            InputStream raw = Objects.requireNonNull(
                    Music.class.getResourceAsStream("/" + audioPath)
            );
            BufferedInputStream buffered = new BufferedInputStream(raw);
            AudioInputStream input = AudioSystem.getAudioInputStream(buffered);
            clip = AudioSystem.getClip();
            clip.open(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // play the audio once
    public void play() {
        if (clip != null) {
            if (clip.isRunning()) {
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
