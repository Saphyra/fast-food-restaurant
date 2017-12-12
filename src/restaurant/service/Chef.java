package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.Entrance;
import restaurant.meals.MainCourse;
import restaurant.util.Logger;
import restaurant.util.Random;

public class Chef implements Runnable {
    private static final double MAX_MULTIPLICATOR = 1.5;
    private static final double MIN_MULTIPLICATOR = 0.5;
    private BlockingQueue<MainCourse> mealQueue;
    private static int mealNum = 0;

    public Chef(BlockingQueue<MainCourse> mealQueue) {
        this.mealQueue = mealQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (mealQueue.size() > 18) {
                    Logger.logToConsole("MealQueue is overloaded: " + mealQueue.size());
                }
                MainCourse meal = mealQueue.take();
                cookMeal(meal);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void cookMeal(MainCourse meal) throws InterruptedException {
        Thread.sleep((long) (meal.getCookTime() * Random.randDouble(MIN_MULTIPLICATOR, MAX_MULTIPLICATOR)));
        addingExtras(meal);

        Logger.logToConsole(meal.getClient().toString() + "'s meal: " + meal.toString() + " is cooked.");
        incMealNum();
        meal.setCooked(true);
    }

    private void addingExtras(MainCourse meal) throws InterruptedException {
        if (meal.isWithKetchup()) {
            long cookTime = Long.valueOf((String) Entrance.EXTRAS.get("ketchup.cooktime"));
            Thread.sleep((long) (cookTime * Random.randDouble(MIN_MULTIPLICATOR, MAX_MULTIPLICATOR)));
        }
        if (meal.isWithKetchup()) {
            long cookTime = Long.valueOf((String) Entrance.EXTRAS.get("mustard.cooktime"));
            Thread.sleep((long) (cookTime * Random.randDouble(MIN_MULTIPLICATOR, MAX_MULTIPLICATOR)));
        }
    }

    public static int getMealNum() {
        return mealNum;
    }

    private synchronized static void incMealNum() {
        mealNum++;
    }
}
