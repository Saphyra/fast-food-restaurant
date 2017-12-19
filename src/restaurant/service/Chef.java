package restaurant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import restaurant.meals.Extra;
import restaurant.meals.Food;
import restaurant.meals.FoodType;
import restaurant.meals.MainCourse;
import restaurant.meals.Meal;
import restaurant.meals.MealOrder;
import restaurant.meals.MealPackage;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

public class Chef implements Runnable {

    private static int chefCount;
    private static volatile int mealNum = 0;

    private final BlockingQueue<MealOrder> mealQueue;
    private final BlockingQueue<MealPackage> mealPackageQueue;
    private final String name;

    private Chef(BlockingQueue<MealOrder> mealQueue, BlockingQueue<MealPackage> mealPackageQueue, String name) {
        this.mealQueue = mealQueue;
        this.mealPackageQueue = mealPackageQueue;
        this.name = name;
    }

    public static Chef chefFactory(BlockingQueue<MealOrder> mealQueue, BlockingQueue<MealPackage> mealPackageQueue) {
        chefCount++;
        String chefName = "Chef " + chefCount;
        Chef chef = new Chef(mealQueue, mealPackageQueue, chefName);
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

    private void logOverloaded() {
        if (mealQueue.size() > PropLoader.getIntegerProperty("entrance.mealqueuesize") - 2) {
            Logger.logToConsole("MealQueue is overloaded: " + mealQueue.size());
        }
    }

    private void cookFood() throws InterruptedException {
        MealOrder order = mealQueue.take();
        Meal cookedMeal = cookMeal(order);
        MealPackage mealPackage = new MealPackage(order.getDeskName(), order.getClientName(), cookedMeal);

        mealPackageQueue.put(mealPackage);

        Logger.logToConsole(cookedMeal.toString() + " is cooked.");
        incMealNum();
    }

    private Meal cookMeal(MealOrder order) throws InterruptedException {
        String mainCourseName = order.getMainCourseName();
        List<String> extraNames = order.getExtraNames();

        Food<MainCourse> mainCourse = cookMainCourse(mainCourseName);
        List<Food<Extra>> extras = cookExtras(extraNames);

        Meal meal = new Meal(mainCourse, extras);

        return meal;
    }

    private Food<MainCourse> cookMainCourse(String mainCourseType) throws InterruptedException {
        FoodType foodType = PropLoader.getMainCourse(mainCourseType);

        Thread.sleep(Random.randLong(foodType.getMinCookTime(), foodType.getMaxCookTime()));

        Food<MainCourse> mainCourse = new Food<>(foodType);
        return mainCourse;
    }

    private List<Food<Extra>> cookExtras(List<String> extraNames) throws InterruptedException {
        List<Food<Extra>> extras = new ArrayList<>(extraNames.size());

        for (String extraName : extraNames) {
            Food<Extra> extra = cookExtra(extraName);
            extras.add(extra);
        }

        return extras;
    }

    private Food<Extra> cookExtra(String extraName) throws InterruptedException {
        FoodType foodType = PropLoader.getExtra(extraName);

        Thread.sleep(Random.randLong(foodType.getMinCookTime(), foodType.getMaxCookTime()));

        Food<Extra> extra = new Food<>(foodType);
        return extra;
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
