package restaurant.util;

public final class Random {

    private Random() {

    }

    public static int randInt(int min, int max) {
        Double rand = Math.floor(Math.random() * (max - min + 1) + min);
        return rand.intValue();
    }

    public static int randInt(double min, double max) {
        Double rand = Math.floor(Math.random() * (max - min + 1) + min);
        return rand.intValue();
    }

    public static double randDouble(int min, int max) {
        return Math.random() * (max - min + 1) + min;
    }

    public static double randDouble(double min, double max) {
        return Math.random() * (max - min + 1) + min;
    }

    public static long randLong(long min, long max) {
        Double rand = Math.floor(Math.random() * (max - min + 1) + min);
        return rand.longValue();
    }

    public static long randLong(int min, int max) {
        Double rand = Math.floor(Math.random() * (max - min + 1) + min);
        return rand.longValue();
    }

    public static long randLong(double min, double max) {
        Double rand = Math.floor(Math.random() * (max - min + 1) + min);
        return rand.longValue();
    }

    public static boolean randBoolean() {
        return randInt(0, 1) == 1 ? true : false;
    }
}
