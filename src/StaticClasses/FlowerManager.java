package StaticClasses;


import Flowers.Flower;
import Level.Map;
import Level.Player;

public class FlowerManager {

    private static int lastMilestone = 0;

    public static void update(Player player) {
        int distance = player.totalDistanceTraveled();

        if (distance - lastMilestone >= 5000) {
            System.out.println("Adding a flower!");
            lastMilestone += 5000;
        }
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
