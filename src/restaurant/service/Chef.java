package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.meals.Cookable;
import restaurant.util.Logger;
import restaurant.util.PropLoader;

public class Chef implements Runnable {

    private static int chefCount;
    private static volatile int mealNum = 0;

    private BlockingQueue<Cookable> mealQueue;
    private final String name;

    private Chef(BlockingQueue<Cookable> mealQueue, String name) {
        this.mealQueue = mealQueue;
        this.name = name;
    }

    public static Chef chefFactory(BlockingQueue<Cookable> mealQueue) {
        chefCount++;
        String chefName = "Chef " + chefCount;
        Chef chef = new Chef(mealQueue, chefName);
        return chef;
    }

    @Override
    public void run() {
        while (true) {
            try {
                logOverloaded();
                cookFood();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void cookFood() throws InterruptedException {
        Cookable food = mealQueue.take();
        food.cook();
        Logger.logToConsole(food.toString() + " is cooked.");
        incMealNum();
    }

    private void logOverloaded() {
        if (mealQueue.size() > PropLoader.getIntegerProperty("entrance.mealqueuesize") - 2) {
            Logger.logToConsole("MealQueue is overloaded: " + mealQueue.size());
        }
    }

    public static int getMealNum() {
        return mealNum;
    }

    private synchronized static void incMealNum() {
        mealNum++;
    }

    public String getName() {
        return name;
    }
}
