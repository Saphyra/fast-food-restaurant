package restaurant.meals;

import restaurant.util.Random;

public class Food implements Eatable, Cookable {
    private final FoodType foodType;
    private boolean cooked = false;
    private boolean eaten = false;

    public Food(FoodType foodType) {
        this.foodType = foodType;
    }

    @Override
    public void cook() {
        try {
            Long minCookTime = foodType.getMinCookTime();
            Long maxCookTime = foodType.getMaxCookTime();
            long sleepTime = Random.randLong(minCookTime, maxCookTime);
            Thread.sleep(sleepTime);
            setCooked(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCooked(boolean cooked) {
        this.cooked = cooked;
    }

    @Override
    public boolean isCooked() {
        return cooked;
    }

    @Override
    public void eat() {
        try {
            long sleepTime = Random.randLong(foodType.getMinEatTime(), foodType.getMaxEatTime());
            Thread.sleep(sleepTime);
            setEaten(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isEaten() {
        return eaten;
    }

    @Override
    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    @Override
    public String toString() {
        return foodType.getName();
    }
}
