package Sound;

import javax.sound.sampled.*;
import java.io.File;

public class Music {
    // actual audio data
    private Clip clip;

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

    // play the audio once
    public void play() {
        if(clip != null) {
            clip.start();
        }
    }

    // play the audio in a loop from start to finish
    public void loop() {
        if(clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // play the audio and loop at a certain bar
    public void loopAtBar(double bpm, int loopBar) {
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
