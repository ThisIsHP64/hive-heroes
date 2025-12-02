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
            tracks.put(Music.MENU, new Music("audio/menu.wav", 90, 9));
            tracks.put(Music.GRASS, new Music("audio/grass.wav", 128.6, 13));
            tracks.put(Music.SIEGE, new Music("audio/siege.wav", 128.6, 13));
            tracks.put(Music.HIVE, new Music("audio/hive.wav", 90, 9));
            tracks.put(Music.VOLCANO, new Music("audio/volcano.wav", 81.8, 9));
            tracks.put(Music.DUNGEON, new Music("audio/dungeon.wav", 100, 0));
            tracks.put(Music.SNOW, new Music("audio/snow.wav", 90, 5));
            tracks.put(Music.MAZE, new Music("audio/maze.wav", 90, 9));
            tracks.put(Music.BOSS, new Music("audio/boss.wav", 90, 9));
            tracks.put(Music.CREDITS, new Music("audio/night.wav", 90, 9));
        }
    }
}