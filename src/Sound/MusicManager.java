package Sound;

import java.util.HashMap;

public class MusicManager {
    private static HashMap<Integer, Music> tracks = new HashMap<Integer, Music>();

    public static void playLoop(int track) {
        if (tracks.get(track) == null) {
            System.out.println("Song not found!");
        } else if(!tracks.get(track).isPlaying()) {
            tracks.get(track).loop();
        }
    }

    public static void playJingle(int track) {
        if (tracks.get(track) == null) {
            System.out.println("Song not found!");
        } else if(!tracks.get(track).isPlaying()) {
            tracks.get(track).play();
        }
    }

    public static void stopAll() {
        if(!tracks.isEmpty()) {
            for(Music m : tracks.values()) {
                m.stop();
            }
        }
    }

    public static void init() {
        if(tracks.isEmpty()) {
            tracks.put(Music.MENU, new Music("Resources/audio/menu.wav", 90, 9));
            tracks.put(Music.GRASS, new Music("Resources/audio/grass.wav", 128.6, 13));
            tracks.put(Music.SIEGE, new Music("Resources/audio/siege.wav", 128.6, 13));
            tracks.put(Music.HIVE, new Music("Resources/audio/hive.wav", 90, 9));
            tracks.put(Music.VOLCANO, new Music("Resources/audio/volcano.wav", 81.8, 9));
            tracks.put(Music.DUNGEON, new Music("Resources/audio/dungeon.wav", 100, 0));
            tracks.put(Music.SNOW, new Music("Resources/audio/snow.wav", 90, 5));
            tracks.put(Music.MAZE, new Music("Resources/audio/maze.wav", 90, 9));
            tracks.put(Music.BOSS, new Music("Resources/audio/boss.wav", 90, 9));
            tracks.put(Music.CREDITS, new Music("Resources/audio/night.wav", 90, 9));
        }
    }
}