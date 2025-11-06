package StaticClasses;


import Utils.Point;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import Flowers.*;
import Level.Map;
import Level.MapTile;
import Level.Player;
import Level.Tileset;
import Sound.Music;

public class FlowerManager {

    private static int lastMilestone = 0;

    private static ArrayList<Flower> flowers = new ArrayList<>();


    public static void initializeFlowers() {
        Point point = new Point(200, 40);

        RareSunflowerwithFlowers rareSunflower = new RareSunflowerwithFlowers(4, point);

        flowers.add(rareSunflower);
    }

    public static Flower randomFlower() {
        if (!flowers.isEmpty()) {
            return flowers.get(0);
        }
        return flowers.getFirst();
    }

    public static void update(Player player, Map map, Flower flower) {
        int distance = player.totalDistanceTraveled();

        if (distance - lastMilestone >= 5000) {
            System.out.println(String.format("Added a flower at: " + randomFlower().getLocation().x + ", " + randomFlower().getLocation().y));
            addFlowerToMap(randomFlower(), map);

            lastMilestone += 5000;
        }
    }

    public static void addFlowerToMap(Flower flower, Map map) {
        map.addNPC(flower);
    }

    public static void addFlowerToList(Flower flower) {
        flowers.add(flower);
    }

    public static int flowersInArrayList() {
        return flowers.size();
    }

    public static int countFlowers(Map map) {
        int c = 0;
        for (var npc : map.getNPCs()) {
            if (npc instanceof Flower) {
                c++;
            }
        }
        return c;
    }

    public MapTile getMapTile(int x, int y) {
        if (isInBounds(x, y)) {
            return mapTiles[getConvertedIndex(x, y)];
        } else {
            return null;
        }
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    private int getConvertedIndex(int x, int y) {
        return x + width * y;
    }
}
