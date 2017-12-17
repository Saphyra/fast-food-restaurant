package restaurant;

import restaurant.util.Logger;
import restaurant.util.PropLoader;

public class Entrance {
    public static final long START_TIME = System.currentTimeMillis();

    public static void main(String[] args) {
        PropLoader.init();
        startLogger();
        Restaurant restaurant = new Restaurant();
        restaurant.start();
    }

    private static void startLogger() {
        Thread loggerThread = new Thread(new Logger());
        loggerThread.setName("Logger");
        loggerThread.start();
        Logger.logToConsole("Logger started");
    }
}
