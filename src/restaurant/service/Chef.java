package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.meals.Cookable;
import restaurant.util.Logger;

public class Chef implements Runnable {
    private BlockingQueue<Cookable> mealQueue;
    private static int mealNum = 0;

    public Chef(BlockingQueue<Cookable> mealQueue) {
        this.mealQueue = mealQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (mealQueue.size() > 18) {
                    Logger.logToConsole("MealQueue is overloaded: " + mealQueue.size());
                }
                Cookable food = mealQueue.take();
                food.cook();
                Logger.logToConsole(food.toString() + " is cooked.");
                incMealNum();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getMealNum() {
        return mealNum;
    }

    private synchronized static void incMealNum() {
        mealNum++;
    }
}
