package Sound;

import java.util.HashMap;

public class SFXManager {
    private static HashMap<Integer, SFX> effects = new HashMap<Integer, SFX>();

    public static void playSFX(int effect) {
        if (effects.get(effect) == null) {
            System.out.println("Sound not found!");
        } else if(!effects.get(effect).isPlaying()){
            effects.get(effect).play();
            // System.out.println("played fx");
        }
    }

    public static void init() {
        if(effects.isEmpty()) {
            effects.put(SFX.NECTAR, new SFX("Resources/audio/nectar.wav"));
            effects.put(SFX.MOVE, new SFX("Resources/audio/move.wav"));
            effects.put(SFX.SHOOT, new SFX("Resources/audio/shoot.wav"));
            effects.put(SFX.STING, new SFX("Resources/audio/sting.wav"));
            effects.put(SFX.HURT, new SFX("Resources/audio/hurt.wav"));
            effects.put(SFX.LEVEL, new SFX("Resources/audio/level.wav"));
        }
    }
}
