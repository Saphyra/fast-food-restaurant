package restaurant.client;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import restaurant.meals.Meal;
import restaurant.util.Logger;
import restaurant.util.PropLoader;
import restaurant.util.Random;

//Storing Client data
public class Client implements Runnable {
    private static final String MIN_MORAL = "client.minmoral";
    private static final String MAX_MORAL = "client.maxmoral";

    private final String name;
    private final String groupid;
    private final int groupClientNum;
    private final List<Meal> mealList;

    private double moral;
    private AtomicBoolean readyWithFood = new AtomicBoolean(false);

    public Client(int id, String groupid, int groupClientNum) {
        name = "Client" + id;
        mealList = Meal.createRandomMealList();

        double minMoral = PropLoader.getDoubleProperty(MIN_MORAL);
        double maxMoral = PropLoader.getDoubleProperty(MAX_MORAL);

        moral = Random.randDouble(minMoral, maxMoral);
        this.groupid = groupid;
        this.groupClientNum = groupClientNum;
    }

    @Override
    public void run() {
        double oldMoral = getMoral();
        eating();
        Logger.logToErr(this + " has eaten his meal. Moral raised from " + oldMoral + " to " + getMoral());
    }

    // Eating food
    private void eating() {
        int counter = 0;
        for (Meal meal : mealList) {
            meal.eat();
            moral = meal.moralIncrement(moral);
            Logger.logToConsole(toString() + " has eaten one of his meals. - " + ++counter + "/" + mealList.size());
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

    public List<Meal> getMealList() {
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
        return name + " [" + groupid + " - " + groupClientNum + "]";
    }
}
