package StaticClasses;


import java.util.ArrayList;

import Flowers.*;
import Level.Map;
import Level.Player;
import Sound.Music;

public class FlowerManager {

    private static int lastMilestone = 0;

    private static ArrayList<Flower> flowers = new ArrayList<>();

    public static void initialize() {
        // if(flowers.isEmpty()) {
        //     flowers.add();
        // }
    }

    public static void initializeFlowers() {
    }

    public static Flower randomFlower() {
        return flowers.get(1);
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
}
