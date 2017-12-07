package restaurant.client;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import restaurant.meals.MainCourse;
import restaurant.util.Constants;
import restaurant.util.Logger;
import restaurant.util.Random;

//Storing Client data
public class Client implements Runnable {
    private static final double MIN_MORAL = 0.1;
    private static final int MAX_MORAL = 10;

    private final String name;
    private final String groupid;
    private final Vector<MainCourse> mealList;

    private double moral;
    private AtomicBoolean readyWithFood = new AtomicBoolean(false);

    public Client(int id, String groupid) {
        name = "Client" + id;
        mealList = MainCourse.mealListFactory(this);
        moral = Random.randDouble(MIN_MORAL, MAX_MORAL);
        this.groupid = groupid;
        Logger.logToConsole("New Client: " + toString());
    }

    @Override
    public void run() {
        double oldMoral = getMoral();
        eating();
        Logger.logToErr(this + " has eaten his meal. Moral raised from " + oldMoral + " to " + getMoral());
    }

    // Eating food
    private void eating() {
        for (MainCourse meal : mealList) {
            try {
                long sleepTime = Random.randLong(Constants.MIN_MEAL_EATING_TIME, Constants.MAX_MEAL_EATING_TIME);
                Thread.sleep(sleepTime);
                meal.eat();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setReadyWidthFood(true);
    }

    public String getName() {
        return name;
    }

    public double getMoral() {
        return moral;
    }

    public void setMoral(double moral) {
        this.moral = moral;
    }

    public Vector<MainCourse> getMealList() {
        return mealList;
    }

    public boolean isReadyWithFood() {
        return readyWithFood.get();
    }

    private void setReadyWidthFood(boolean value) {
        readyWithFood.set(value);
    }

    @Override
    public String toString() {
        return name + " [" + groupid + "]";
    }
}
