package restaurant.meals;

import restaurant.util.Random;

public class Food<T> implements ComparableFood {
    private final FoodType foodType;
    private boolean eaten = false;

    public Food(FoodType foodType) {
        this.foodType = foodType;
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

    @Override
    public FoodType getFoodType() {
        return foodType;
    }

    @Override
    public String toString() {
        return foodType.getName();
    }

    @Override
    public int compareTo(ComparableFood arg) {
        int result = 0;
        if (foodType.getBaseMoralMultiply() < arg.getFoodType().getBaseMoralMultiply()) {
            result = -1;
        }
        return result;
    }

}
