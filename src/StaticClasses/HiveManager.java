package StaticClasses;

public class HiveManager {
    private static int nectar = 0;

    public static int getNectar() {
        return nectar;
    }

    public static void depositNectar(Level.Map map) {
        nectar++;
        HordeManager.onDeposit(map);
    }
}