package restaurant.service;

import java.util.concurrent.BlockingQueue;

import restaurant.meals.MainCourse;
import restaurant.util.Constants;
import restaurant.util.Random;

public class Chef implements Runnable {
    private static final double MAX_MULTIPLICATOR = 1.5;
    private static final double MIN_MULTIPLICATOR = 0.5;
    private BlockingQueue<MainCourse> mealQueue;

    public Chef(BlockingQueue<MainCourse> mealQueue) {
        this.mealQueue = mealQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
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
        meal.setCooked(true);
    }

    private void addingExtras(MainCourse meal) throws InterruptedException {
        if (meal.isWithKetchup()) {
            Thread.sleep((long) (Constants.KETCHUP_COOK_TIME * Random.randDouble(MIN_MULTIPLICATOR, MAX_MULTIPLICATOR)));
        }
        if (meal.isWithKetchup()) {
            Thread.sleep((long) (Constants.MUSTARD_COOK_TIME * Random.randDouble(MIN_MULTIPLICATOR, MAX_MULTIPLICATOR)));
        }
    }
}
