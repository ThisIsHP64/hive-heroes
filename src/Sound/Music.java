package Sound;

import javax.sound.sampled.*;
import java.io.File;

public class Music {
    // songs
    public static final int MENU = 0;
    public static final int SIEGE = 1;
    public static final int HIVE = 2;
    public static final int GRASS = 3;
    public static final int SNOW = 4;
    public static final int VOLCANO = 5;
    public static final int MAZE = 6;
    public static final int DUNGEON = 7;
    public static final int BOSS = 8;
    public static final int CREDITS = 9;

    // actual audio data
    private Clip clip;
    private double bpm;
    private int loopBar;

    // constructor
    public Music(String audioPath) {
        try {
            File file = new File(audioPath);
            AudioInputStream input = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Music(String audioPath, double bpm, int loopBar) {
        try {
            File file = new File(audioPath);
            AudioInputStream input = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(input);
            this.bpm = bpm;
            this.loopBar = loopBar;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // play the audio once
    public void play() {
        if(clip != null) {
            clip.start();
        }
    }

    // play the audio and loop at a certain bar
    public void loop() {
        // loop from beginning
        if(loopBar <= 0) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            return;
        }

        // calculate loop point (in seconds)
        double secondsPerBar = (double) 4 / (bpm / 60.0);
        double startSeconds = (loopBar - 1) * secondsPerBar;

        // get loop point frame
        int sampleRate = (int) clip.getFormat().getSampleRate();
        int startFrame = (int) (startSeconds * sampleRate) - 1800;

        // start from beginning and set loop point
        clip.setLoopPoints(startFrame, -1);
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // stop the audio
    public void stop() {
        if(clip != null) {
            clip.stop();
        }
    }

    // check if the audio is playing
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}
